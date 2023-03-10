package com.wormos.intrester;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class addMemberViewHolder extends FirebaseRecyclerAdapter<addMemberModal, addMemberViewHolder.placeViewHolder> {


    public addMemberViewHolder(@NonNull FirebaseRecyclerOptions<addMemberModal> options) {
        super(options);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull addMemberViewHolder.placeViewHolder holder, int position, @NonNull addMemberModal model) {

        Log.d("ucl", "onBindViewHolder: ");
        holder.name.setText(model.getMemberName());
        holder.premiumAmount.setText(model.getPremiumPlan() +"/" + model.getDuration());
        holder.amountRemaining.setText(model.getAmountRemaining());
        String firstLetter = String.valueOf(model.getMemberName().charAt(0));
        holder.icon.setText(firstLetter.toUpperCase());


    }

    @NonNull
    @Override
    public addMemberViewHolder.placeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_user_data,parent,false);
        return new placeViewHolder(view);
    }

    static class placeViewHolder extends RecyclerView.ViewHolder {
        TextView name, premiumAmount, icon;
        Button  amountRemaining;
        public placeViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.userDataName);
            premiumAmount = itemView.findViewById(R.id.userDataPmAmount);
            amountRemaining = itemView.findViewById(R.id.userAmountRemaining);
            icon = itemView.findViewById(R.id.userIcon);


        }
    }
}
