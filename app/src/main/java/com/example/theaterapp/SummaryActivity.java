package com.example.theaterapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class SummaryActivity extends AppCompatActivity {
    private static final String PREF_KEY = MainActivity.class.getPackage().toString();

    TextView seatNumberTextViewSummary;
    TextView playTitleTextViewSummary;
    TextView playPriceTextViewSummary;
    String seatNumber;
    String playTitle;
    String playPrice;
    String playDate;
    private SharedPreferences preferences;
    private NotificationHandler mNotificationHandler;
    private CollectionReference mCollectionTicket;
    private FirebaseFirestore mFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        seatNumberTextViewSummary = findViewById(R.id.numberOfSeatInput);
        playTitleTextViewSummary = findViewById(R.id.titleOfPlayInput);
        playPriceTextViewSummary = findViewById(R.id.priceOfPlayInput);
        SetSummaryFieldBySharedPref();
        mNotificationHandler = new NotificationHandler(this);

        SharedPreferences.Editor editor = preferences.edit();
        //TODO Itt befejezniazt h preferencesbe mentse le a pending tickets számát, madj app indulaskor is betoltese, meg valszleg torleskor is jo lenne menteni az akkori pending ticket számot

        editor.apply();


        mFirestore = FirebaseFirestore.getInstance();
        mCollectionTicket = mFirestore.collection("Tickets");

    }

    private void SetSummaryFieldBySharedPref() {
        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);
        seatNumber = preferences.getString("seatNumber", "");
        playTitle = preferences.getString("playTitle", "");
        playPrice = preferences.getString("playPrice", "");
        playDate = preferences.getString("date", "");
        seatNumberTextViewSummary.setText(seatNumber);
        playTitleTextViewSummary.setText(playTitle);
        playPriceTextViewSummary.setText(playPrice);
    }

    public void bookMyTicket(View view) {
        DramaListActivity.pendingTickets += 1;
        initializeTicketData();
        mNotificationHandler.send("Az előadás címe: " + playTitle + ". " + playDate + ".");

        Intent intent = new Intent(this, FinalActivity.class);
        startActivity(intent);
    }

    public void backToSeatSelection(View view) {
        finish();
    }

    public void initializeTicketData(){

        String ticketTitle = playTitle;
        String ticketSeatNum = seatNumber;
        String ticketPrice = playPrice;



        mCollectionTicket.add(new Ticket(ticketTitle, ticketSeatNum, ticketPrice));


    }

}