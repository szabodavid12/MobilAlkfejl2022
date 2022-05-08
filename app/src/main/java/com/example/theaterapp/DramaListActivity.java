package com.example.theaterapp;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DramaListActivity extends AppCompatActivity {
    private static final String LOG_TAG = DramaListActivity.class.getName();
    private int gridNumber = 1;
    public static int pendingTickets = 0;
    private boolean viewRow = true;
    private FirebaseUser user;

    private RecyclerView mRecyclerView;
    private ArrayList<Play> mPlayList;
    private PlayAdapter mAdapter;

    private FirebaseFirestore mFirestore;
    private CollectionReference mCollectionPlay;

    private FrameLayout redCircle;
    private TextView contentTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drama_list);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Log.d(LOG_TAG, "Authenticated user!");
        } else {
            Log.d(LOG_TAG, "Unauthenticated user!");
            finish();
        }

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, gridNumber));
        mPlayList = new ArrayList<>();

        mAdapter = new PlayAdapter(this, mPlayList);

        mRecyclerView.setAdapter(mAdapter);

        mFirestore = FirebaseFirestore.getInstance();
        mCollectionPlay = mFirestore.collection("Plays");

        queryData();
    }

    private void queryData() {
        mPlayList.clear(); //törli az eddigi adatokat h hne legyen duplikacio

        mCollectionPlay.orderBy("name").limit(10).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                Play play = document.toObject(Play.class);
                mPlayList.add(play);
            }
            if (mPlayList.size() == 0){
                initializeData();
                queryData();
            }
            mAdapter.notifyDataSetChanged(); //alkalmazza a valtoztatasokat imo
        });


    }

    private void initializeData() {
        String[] playsList = getResources().getStringArray(R.array.play_names);
        String[] playsInfo = getResources().getStringArray(R.array.play_desc);
        String[] playsDate = getResources().getStringArray(R.array.play_date);
        String[] playsPrice = getResources().getStringArray(R.array.play_price);
        TypedArray playsImageResource = getResources().obtainTypedArray(R.array.play_images);
        TypedArray playsRate = getResources().obtainTypedArray(R.array.play_rates);

/*        for (int i = 0; i < playsList.length; i++) {
            mPlayList.add(new Play(playsList[i], playsInfo[i], playsPrice[i], playsRate.getFloat(i, 0), playsImageResource.getResourceId(i, 0)));
        }*/

        for (int i = 0; i < playsList.length; i++) {
            mCollectionPlay.add(new Play(playsList[i], playsInfo[i], playsPrice[i], playsRate.getFloat(i, 0), playsImageResource.getResourceId(i, 0), playsDate[i]));
        }

        playsImageResource.recycle();


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.play_list_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_bar);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Log.d(LOG_TAG, s);
                mAdapter.getFilter().filter(s);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.log_out_button:
                Log.d(LOG_TAG, "Logout clicked!");
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                return true;
       /*     case R.id.setting_button:
                Log.d(LOG_TAG, "Setting clicked!");
                FirebaseAuth.getInstance().signOut();
                finish();
                return true;*/
            case R.id.pending_tickets:
                showMyTickets();
                Log.d(LOG_TAG, "Cart clicked!");
                return true;
            case R.id.view_selector:
                if (viewRow) {
                    changeSpanCount(item, R.drawable.ic_vie_grid, 1);
                } else {
                    changeSpanCount(item, R.drawable.ic_view_row, 2);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void changeSpanCount(MenuItem item, int drawableId, int spanCount) {
        viewRow = !viewRow;
        item.setIcon(drawableId);
        GridLayoutManager layoutManager = (GridLayoutManager) mRecyclerView.getLayoutManager();
        layoutManager.setSpanCount(spanCount);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        final MenuItem alertMenuItem = menu.findItem(R.id.pending_tickets);
        FrameLayout rootView = (FrameLayout) alertMenuItem.getActionView();

        redCircle = (FrameLayout) rootView.findViewById(R.id.view_alert_red_circle);
        contentTextView = (TextView) rootView.findViewById(R.id.view_alert_count_textview);

        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(alertMenuItem);
            }
        });
        updateAlertIcon();
        return super.onPrepareOptionsMenu(menu);
    }

    public void updateAlertIcon() {
        pendingTickets = (pendingTickets + 0);
        if (0 < pendingTickets) {
            contentTextView.setText(String.valueOf(pendingTickets));
        } else {
            contentTextView.setText("");
        }

        redCircle.setVisibility((pendingTickets > 0) ? VISIBLE : GONE);
    }

    public void showMyTickets() {
        Intent intent = new Intent(this, MyTicketsActivity.class);
        startActivity(intent);
    }


}