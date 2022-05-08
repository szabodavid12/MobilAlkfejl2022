package com.example.theaterapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class SeatPickingActivity extends AppCompatActivity {
    private static final String PREF_KEY = MainActivity.class.getPackage().toString();

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_picking);
        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);
    }

    public void seatFree(View view) {

        saveSeatNumberUploadSharedPref(view.getContentDescription().toString());

        Intent intent = new Intent(this, SummaryActivity.class);
        startActivity(intent);
    }

    public void seatReserved(View view){
        Toast.makeText(SeatPickingActivity.this, "Ez a hely már foglalt!", Toast.LENGTH_LONG).show();
    }

    private void saveSeatNumberUploadSharedPref(String seatNumber) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("seatNumber", seatNumber);
        editor.apply();
    }

    public void backToMovieSelection(View view) {
        finish();
    }
}