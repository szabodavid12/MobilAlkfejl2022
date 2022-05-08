package com.example.theaterapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.ViewHolderTicket> implements Filterable {
    private static final String PREF_KEY = MainActivity.class.getPackage().toString();

    private ArrayList<Ticket> mTicketData;
    private ArrayList<Ticket> mTicketDataAll;
    private Context mContext;
    private int lastPosition = -1;

    public TicketAdapter( Context context, ArrayList<Ticket> ticketData) {
        this.mTicketData = ticketData;
        this.mTicketDataAll = ticketData;
        this.mContext = context;
    }



    @NonNull
    @Override
    public ViewHolderTicket onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderTicket(LayoutInflater.from(mContext).inflate(R.layout.list_ticket, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderTicket holder, int position) {
        Ticket currentTicket = mTicketData.get(position);

        holder.bindTo(currentTicket);

        if (holder.getAdapterPosition() > lastPosition){
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_row);
            holder.itemView.startAnimation(animation);
            lastPosition = holder.getAdapterPosition();
        }
    }

    @Override
    public int getItemCount() {
        return mTicketData.size();
    }

    @Override
    public Filter getFilter() {
        return ticketFilter;
    }

    private Filter ticketFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<Ticket> filteredList = new ArrayList<>();
            FilterResults results = new FilterResults();

            if (charSequence == null || charSequence.length() == 0) {
                results.count = mTicketDataAll.size();
                results.values = mTicketDataAll;
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (Ticket ticket: mTicketDataAll){
                    if (ticket.getName().toLowerCase().contains(filterPattern)){
                        filteredList.add(ticket);
                    }
                }

                results.count = filteredList.size();
                results.values = filteredList;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mTicketData = (ArrayList) filterResults.values;
            notifyDataSetChanged();
        }
    };

    class ViewHolderTicket extends RecyclerView.ViewHolder {
        private TextView mTicketTitle;
        private TextView mTicketSeatNumber;
        private TextView mTicketPrice;

        public ViewHolderTicket(@NonNull View itemView) {
            super(itemView);

            mTicketTitle = itemView.findViewById(R.id.playTicketTitle);
            mTicketSeatNumber = itemView.findViewById(R.id.playTicketSeatNumber);
            mTicketPrice = itemView.findViewById(R.id.playTicketPrice);

            itemView.findViewById(R.id.throwAway).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO Jegy törlés
                }
            });
        }

        public void bindTo(Ticket currentTicket) {
            mTicketTitle.setText(currentTicket.getName());
            mTicketSeatNumber.setText(currentTicket.getSeatNumber());
            mTicketPrice.setText(currentTicket.getPrice());
            itemView.findViewById(R.id.throwAway).setOnClickListener(view -> ((MyTicketsActivity)mContext).deleteTicket(currentTicket));
        }
    }
}





