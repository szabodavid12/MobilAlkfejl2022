package com.example.theaterapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class PlayAdapter extends RecyclerView.Adapter<PlayAdapter.ViewHolder> implements Filterable {
    private static final String PREF_KEY = MainActivity.class.getPackage().toString();
    private ArrayList<Play> mPlayData;
    private ArrayList<Play> mPlayDataAll;
    private Context mContext;
    private int lastPosition = -1;
    private SharedPreferences preferences;

    private String playTitle;
    private String playPrice;
    private String playDate;




    public PlayAdapter(Context context, ArrayList<Play> playData) {
        this.mPlayData = playData;
        this.mPlayDataAll = playData;
        this.mContext = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.list_play, parent, false));
    }

    @Override
    public void onBindViewHolder(PlayAdapter.ViewHolder holder, int position) {
        Play currentPlay = mPlayData.get(position);

        holder.bindTo(currentPlay);

        if (holder.getAdapterPosition() > lastPosition){
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_row);
            holder.itemView.startAnimation(animation);
            lastPosition = holder.getAdapterPosition();
        }
    }

    @Override
    public int getItemCount() {
        return mPlayData.size();
    }

    @Override
    public Filter getFilter() {
        return playFilter;
    }

    private Filter playFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<Play> filteredList = new ArrayList<>();
            FilterResults results = new FilterResults();

            if (charSequence == null || charSequence.length() == 0) {
                results.count = mPlayDataAll.size();
                results.values = mPlayDataAll;
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (Play play: mPlayDataAll){
                    if (play.getName().toLowerCase().contains(filterPattern)){
                        filteredList.add(play);
                    }
                }

                results.count = filteredList.size();
                results.values = filteredList;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mPlayData = (ArrayList) filterResults.values;
            notifyDataSetChanged();
        }
    };

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTitleText;
        private TextView mInfoText;
        private TextView mPriceText;
        private TextView mDateText;
        private ImageView mItemImage;
        private RatingBar mRatingBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mTitleText = itemView.findViewById(R.id.playTitle);
            mInfoText = itemView.findViewById(R.id.playInfo);
            mPriceText = itemView.findViewById(R.id.price);
            mDateText = itemView.findViewById(R.id.date);
            mItemImage = itemView.findViewById(R.id.playImage);
            mRatingBar = itemView.findViewById(R.id.ratingBar);

            itemView.findViewById(R.id.bookATicket).setOnClickListener( new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    Log.d("Activity", "Reserve a ticket " + mTitleText.getText());

                    ((DramaListActivity)mContext).updateAlertIcon();

                    playTitle = mTitleText.getText().toString();
                    playPrice = mPriceText.getText().toString();
                    playDate = mDateText.getText().toString();
                    savePlayNamePriceDateUploadSharedPref(playTitle, playPrice, playDate);

                    Intent intent = new Intent(mContext, SeatPickingActivity.class);
                    mContext.startActivity(intent);

                }
            });

        }

        public void bindTo(Play currentPlay) {
            mTitleText.setText(currentPlay.getName());
            mInfoText.setText(currentPlay.getInfo());
            mPriceText.setText(currentPlay.getPrice());
            mDateText.setText(currentPlay.getDate());
            mRatingBar.setRating(currentPlay.getRatedInfo());

            Glide.with(mContext).load(currentPlay.getImageResource()).into(mItemImage);

        }
    }

    private void savePlayNamePriceDateUploadSharedPref(String playTitle, String playPrice, String date) {
        preferences = mContext.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("playTitle", playTitle);
        editor.putString("playPrice", playPrice);
        editor.putString("date", date);
        editor.apply();
    }

}



