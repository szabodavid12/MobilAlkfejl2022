package com.example.theaterapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getName();
    private static final String PREF_KEY = MainActivity.class.getPackage().toString();
    private static final int SECRET_KEY = 99;
    private static final int RC_SIGN_IN = 123;

    EditText userNameEditText;
    EditText passwordEditText;
    CheckBox checkBox;

    private SharedPreferences preferences;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();


        userNameEditText = findViewById(R.id.userNameEditTextXml);
        passwordEditText = findViewById(R.id.passwordEditTextXml);
        checkBox = findViewById(R.id.rememberMeCheckBoxActXml);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        rememberMeSetFieldBySharedPref();


        Log.i(LOG_TAG, "onCreate");
    }

    public void login(View view) {
        String userName = userNameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

       // rememberMeUploadSharedPref();


     //   Log.i(LOG_TAG, "Bejelentkezett: " + userName + " Jelszava: " + password + ".");

        mAuth.signInWithEmailAndPassword(userName, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d(LOG_TAG, "Login done!");
                    selectDrama();
                } else {
                    Toast.makeText(MainActivity.this, "Invalid email or password!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void register(View view) {
        Intent intent = new Intent(this, RegistrationActivity.class);
        intent.putExtra("SECRET_KEY", SECRET_KEY);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(LOG_TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(LOG_TAG, "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(LOG_TAG, "signInWithCredential:success");
                            selectDrama();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(LOG_TAG, "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(LOG_TAG, "onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(LOG_TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(LOG_TAG, "onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(LOG_TAG, "onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        rememberMeSetFieldBySharedPref();
        Log.i(LOG_TAG, "onResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(LOG_TAG, "onRestart");
    }

    private void rememberMeUploadSharedPref() {
        boolean checkboxIsChecked = checkBox.isChecked();
        SharedPreferences.Editor editor = preferences.edit();
        if (checkboxIsChecked) {
            editor.putString("userName", userNameEditText.getText().toString());
            editor.putString("password", passwordEditText.getText().toString());
            editor.putBoolean("checkBox", checkBox.isChecked());
            editor.apply();
        } else {
            editor.remove("userName");
            editor.remove("password");
            editor.remove("checkBox");
            editor.apply();
        }
        Log.i(LOG_TAG, "igaz-e most: " + checkboxIsChecked);
    }

    private void rememberMeSetFieldBySharedPref() {
        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);
        String userName = preferences.getString("userName", "");
        String password = preferences.getString("password", "");
        boolean checkbox = preferences.getBoolean("checkBox", false);
        userNameEditText.setText(userName);
        passwordEditText.setText(password);
        checkBox.setChecked(checkbox);
    }

    public void onCheckboxClicked(View view) {
        if (!checkBox.isChecked()) {
            rememberMeUploadSharedPref();
        }
    }

    private void selectDrama(/*registered user data*/){
        Intent intent = new Intent(this, DramaListActivity.class);
        intent.putExtra("SECRET_KEY", SECRET_KEY);
        startActivity(intent);
    }

    public void loginWithGoogle(View view) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void loginAsGuest(View view) {
        mAuth.signInAnonymously().addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d(LOG_TAG, "Login as guest done!");
                    selectDrama();
                } else {
                    Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //TODO először a seat picker cml t megcsinalni, utana jegyfoglalas utan szekvalasztasra iranyitas, ott
    // kivalasztja a széket, és utána véglegesíti a jegyvásárlást egy új xml-en, ahol összegezve lesznek a vasarlas adatai.
    //ha megvolt a vasarlas, akkor pedig a jegyeim szekcioban egyszerűen ott lesz hogy jegyeim: 1. Rákattinthat h lemond, és akk meg csökken és kész.
}