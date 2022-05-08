package com.example.theaterapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class FinalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);
    }

    public void backToPlaySelect(View view) {
        Intent intent = new Intent(this, DramaListActivity.class);
        startActivity(intent);
    }
}