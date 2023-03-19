package com.wormos.intrester;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class AccountSummaryAdapter extends FirebaseRecyclerAdapter<CreatePlaceModel,AccountSummaryAdapter.placeStatViewHolder> {

    String month,year,date;
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Place Collection");

    public AccountSummaryAdapter(@NonNull FirebaseRecyclerOptions<CreatePlaceModel> options, String month, String year, String date) {
        super(options);
        this.month = month;
        this.year = year;
        this.date = date;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull placeStatViewHolder holder, int position, @NonNull CreatePlaceModel model) {
        dbRef.child(Objects.requireNonNull(getRef(holder.getAbsoluteAdapterPosition()).getKey()))
                .child(year).child(month).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            String totalAmount ="0";
                            String dailyAmount ="0";
                            if(snapshot.child("Monthly Amount").exists())
                                totalAmount = Objects.requireNonNull(snapshot.child("Monthly Amount").getValue()).toString();
                            if(snapshot.child(date).exists())
                                dailyAmount = Objects.requireNonNull(snapshot.child(date).getValue()).toString();
                            holder.dailyAmount.setText("Today: "+dailyAmount);
                            holder.monthlyAmount.setText("Total: "+totalAmount);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        holder.placeName.setText(model.getPlace());
        String firstLetter = String.valueOf(model.getPlace().charAt(0));
        holder.icon.setText(firstLetter.toUpperCase());
        holder.placeId.setText("id: "+getRef(holder.getAbsoluteAdapterPosition()).getKey());
    }

    @NonNull
    @Override
    public placeStatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_stats_item,parent,false);
        return new placeStatViewHolder(view);
    }

    static class placeStatViewHolder extends RecyclerView.ViewHolder{

        TextView icon,placeName,placeId,monthlyAmount,dailyAmount;
        public placeStatViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.placeIcon);
            placeName = itemView.findViewById(R.id.placeDataName);
            placeId = itemView.findViewById(R.id.placeDataId);
            monthlyAmount = itemView.findViewById(R.id.placeDataTotalAmount);
            dailyAmount = itemView.findViewById(R.id.placeDataDailyAmount);
        }
    }
}
