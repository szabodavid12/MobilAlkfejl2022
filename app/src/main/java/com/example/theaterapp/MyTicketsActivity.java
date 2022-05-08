package com.example.theaterapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class MyTicketsActivity extends AppCompatActivity {
    private static final String LOG_TAG = MyTicketsActivity.class.getName();
    private int gridNumber = 1;
    private FirebaseUser user;

    private RecyclerView mRecyclerView;
    private ArrayList<Ticket> mTicketList;
    private TicketAdapter mAdaper;

    private FirebaseFirestore mFirestore;
    private CollectionReference mCollectionTicket;

    private TextView contentTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tickets);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Log.d(LOG_TAG, "Authenticated user!");
        } else {
            Log.d(LOG_TAG, "Unauthenticated user!");
            finish();
        }

        mRecyclerView = findViewById(R.id.ticketRecyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,gridNumber));
        mTicketList = new ArrayList<>();

        mAdaper = new TicketAdapter(this, mTicketList);

        mRecyclerView.setAdapter(mAdaper);

        mFirestore = FirebaseFirestore.getInstance();
        mCollectionTicket = mFirestore.collection("Tickets");

        queryData();

    }

    private void queryData() {
        //mTicketList.clear();
        mCollectionTicket.orderBy("name").limit(10).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                Ticket ticket = document.toObject(Ticket.class);
                ticket.setId(document.getId());
                mTicketList.add(ticket);
            }
            mAdaper.notifyDataSetChanged();
        });
    }

    public void back(View view) {
        finish();
    }

    public void deleteTicket(Ticket ticket){
        DocumentReference reference = mCollectionTicket.document(ticket._getId());
        reference.delete().addOnSuccessListener(succes->{
            Log.d(LOG_TAG, "Item is successfully deleted: " + ticket._getId());
        }).addOnFailureListener(failure -> {
            Toast.makeText(this, "Item " + ticket._getId() + " cannot be deleted.", Toast.LENGTH_LONG).show();
        });
        queryData();
        DramaListActivity.pendingTickets -= 1;
        finish();
        startActivity(getIntent());
    }



}