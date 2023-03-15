package com.wormos.intrester;

import android.annotation.SuppressLint;
import android.graphics.Color;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DepositLogBookAdapter extends FirebaseRecyclerAdapter<addMemberModal, DepositLogBookAdapter.LogBookViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Collection By Place");
    String placeId,infoDate;
    public DepositLogBookAdapter(@NonNull FirebaseRecyclerOptions<addMemberModal> options,String placeId,String infoDate) {
        super(options);
        this.placeId= placeId;
        this.infoDate = infoDate;
    }

    @Override
    protected void onBindViewHolder(@NonNull LogBookViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull addMemberModal model) {
        holder.name.setText(model.getMemberName());
        holder.premiumAmount.setText(model.getPremiumPlan() +"/" + model.getDuration());
        String firstLetter = String.valueOf(model.getMemberName().charAt(0));
        holder.icon.setText(firstLetter.toUpperCase());
        Log.d("kik", "onBindViewHolder: "+dbRef.child(placeId).child(getRef(position).getKey()));
        dbRef.child(infoDate).child(placeId).child(getRef(position).getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Log.d("kikg", "onBindViewHolder: "+getRef(position).getKey()+placeId);
                    holder.dueStatus.setBackgroundResource(R.drawable.round_btn_green);
                    holder.dueStatus.setText("Paid");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @NonNull
    @Override
    public LogBookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.due_status_item,parent,false);
        return new LogBookViewHolder(view);
    }

    static class LogBookViewHolder extends RecyclerView.ViewHolder{
        TextView name, premiumAmount, icon;
        Button dueStatus;

        public LogBookViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.userDataName);
            premiumAmount = itemView.findViewById(R.id.userDataPmAmount);
            dueStatus = itemView.findViewById(R.id.userDueInfo);
            icon = itemView.findViewById(R.id.userIcon);
        }
    }
}
