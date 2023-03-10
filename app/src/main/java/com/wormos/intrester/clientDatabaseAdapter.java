package com.wormos.intrester;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class clientDatabaseAdapter extends FirebaseRecyclerAdapter<clientDatabaseModel, clientDatabaseAdapter.placeViewHolder> {


    public clientDatabaseAdapter(@NonNull FirebaseRecyclerOptions<clientDatabaseModel> options) {
        super(options);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull clientDatabaseAdapter.placeViewHolder holder, int position, @NonNull clientDatabaseModel model) {

        Log.d("ucl", "onBindViewHolder: ");
        holder.name.setText(model.getMemberName());
        holder.premiumAmount.setVisibility(View.GONE);
        holder.cdDOM.setText(model.getDateOfMaturity());
        String firstLetter = String.valueOf(model.getMemberName().charAt(0));

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.getContext().startActivity(new Intent(v.getContext(),UserData.class).putExtra("id",getRef(position).getKey()));
            }
        });
//        holder.icon.setText(firstLetter.toUpperCase());



    }

    @NonNull
    @Override
    public clientDatabaseAdapter.placeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_user_data,parent,false);
        return new placeViewHolder(view);
    }

    static class placeViewHolder extends RecyclerView.ViewHolder {
        TextView name, premiumAmount, icon;
        Button  cdDOM;
        View view;
        public placeViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.userDataName);
            premiumAmount = itemView.findViewById(R.id.userDataPmAmount);
            cdDOM = itemView.findViewById(R.id.userAmountRemaining);
            view = itemView;
//            icon = itemView.findViewById(R.id.userIcon);

        }
    }
}
