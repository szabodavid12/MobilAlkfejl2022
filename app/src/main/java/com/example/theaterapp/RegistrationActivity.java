package com.example.theaterapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity {
    private static final String LOG_TAG = RegistrationActivity.class.getName();
    private static final String PREF_KEY = MainActivity.class.getPackage().toString();
    private static final int SECRET_KEY = 99;

    EditText userNameEditText;
    EditText userEmailEditText;
    EditText passwordEditText;
    EditText passwordAgainEditText;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        int secret_key = getIntent().getIntExtra("SECRET_KEY", 0);

        if (secret_key != SECRET_KEY) {
            finish();
        }

        userNameEditText = findViewById(R.id.userNameEditTextRegXml);
        userEmailEditText = findViewById(R.id.emailEditTextRegXml);
        passwordEditText = findViewById(R.id.passwordEditTextRegXml);
        passwordAgainEditText = findViewById(R.id.passwordAgainEditTextRegXml);

        mAuth = FirebaseAuth.getInstance();


        Log.i(LOG_TAG, "onCreate");
    }

    public void registerRegAct(View view) {
        String userName = userNameEditText.getText().toString();
        String userEmail = userEmailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String passwordAgain = passwordAgainEditText.getText().toString();

        if (!password.equals(passwordAgain)){
            Log.e(LOG_TAG, "Nem egyenlő a két jelszó");
            return;
        }

        Log.i(LOG_TAG, "Regisztrált: " + userName + ", e-mail: " + userEmail);
        // TODO: A regisztrációs funkcionalitást meg kellene valósítani egyszer.
        //TODO Jelszo hosszusagot allitani akar

        mAuth.createUserWithEmailAndPassword(userEmail, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Log.d(LOG_TAG, "User created successfully");
                selectDrama();
                } else {
                    Log.d(LOG_TAG, "Sikertelen user krealas");
                    Toast.makeText(RegistrationActivity.this, "User was't created successfully:", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void logInRegAct(View view) {
        finish();
    }

    private void selectDrama(/*registered user data*/){
        Intent intent = new Intent(this, DramaListActivity.class);
        intent.putExtra("SECRET_KEY", SECRET_KEY);
        startActivity(intent);
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
        Log.i(LOG_TAG, "onResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(LOG_TAG, "onRestart");
    }

}