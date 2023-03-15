package com.wormos.intrester;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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

import java.util.HashMap;
import java.util.Objects;

public class DueLogBookAdapter extends FirebaseRecyclerAdapter<addMemberModal , DueLogBookAdapter.DueLogViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Collection By Place");
    String placeId,infoDate,type;

    public DueLogBookAdapter(@NonNull FirebaseRecyclerOptions<addMemberModal> options,String placeId,String infoDate,String type) {
        super(options);
        this.placeId= placeId;
        this.infoDate = infoDate;
        this.type = type;
    }

    @Override
    protected void onBindViewHolder(@NonNull DueLogViewHolder holder, int position, @NonNull addMemberModal model) {

        if(type.equals("all")){
            dbRef.child(infoDate).child(placeId).child(Objects.requireNonNull(getRef(holder.getAbsoluteAdapterPosition()).getKey())).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        holder.dueStatus.setBackgroundResource(R.drawable.round_btn_green);
                        holder.dueStatus.setText("Paid");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else if(type.equals("paid")){
            dbRef.child(infoDate).child(placeId).child(getRef(holder.getAbsoluteAdapterPosition()).getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        holder.dueStatus.setBackgroundResource(R.drawable.round_btn_green);
                        holder.dueStatus.setText("Paid");
                    } else {
                        ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
                        params.height = 0;
                        holder.itemView.setLayoutParams(params);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else if(type.equals("due")){
            dbRef.child(infoDate).child(placeId).child(getRef(holder.getAbsoluteAdapterPosition()).getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
                        params.height = 0;
                        holder.itemView.setLayoutParams(params);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        holder.name.setText(model.getMemberName());
        holder.premiumAmount.setText(model.getPremiumPlan() +"/" + model.getDuration());
        String firstLetter = String.valueOf(model.getMemberName().charAt(0));
        holder.icon.setText(firstLetter.toUpperCase());

        holder.dueStatus.setOnClickListener(statusView->{
            AlertDialog.Builder builder = new AlertDialog.Builder(statusView.getContext());
                View pickImgview =  LayoutInflater.from(statusView.getContext()).inflate(R.layout.confirmation_item, null);
                builder.setCancelable(true);
                builder.setView(pickImgview);
                AlertDialog alertDialogImg = builder.create();
                Window window = alertDialogImg.getWindow();
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialogImg.show();


                TextView yesView = pickImgview.findViewById(R.id.confirmation_yes);
                TextView noView = pickImgview.findViewById(R.id.confirmation_no);
                TextView textView = pickImgview.findViewById(R.id.confirmation_text_id);

                yesView.setOnClickListener(v->{
                    if(holder.dueStatus.getText().equals("Due")){
                        HashMap<String,Object> paidMap = new HashMap<>();
                        paidMap.put(getRef(holder.getAbsoluteAdapterPosition()).getKey(),"paid");
                        dbRef.child(infoDate).child(placeId).updateChildren(paidMap).addOnSuccessListener(success->{
                            holder.dueStatus.setBackgroundResource(R.drawable.round_btn_green);
                            holder.dueStatus.setText("Paid");
                            paymentCollection(model.getPremiumPlan());
                            paymentPlaceCollection(model.getPremiumPlan());
                            alertDialogImg.dismiss();
                        });
                    } else {
                        dbRef.child(infoDate).child(placeId).child(getRef(holder.getAbsoluteAdapterPosition()).getKey())
                                .removeValue().addOnSuccessListener(sucess->{
                                    holder.dueStatus.setBackgroundResource(R.drawable.round_btn);
                                    holder.dueStatus.setText("Due");
                                    deletePaymentCollection(model.getPremiumPlan());
                                    deletePaymentPlaceCollection(model.getPremiumPlan());
                                    alertDialogImg.dismiss();
                                });
                    }

                });

                noView.setOnClickListener(v->{
                    alertDialogImg.dismiss();
                });
        });
    }

    @NonNull
    @Override
    public DueLogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.due_status_item,parent,false);
        return new DueLogViewHolder(view);
    }

    static class DueLogViewHolder extends RecyclerView.ViewHolder{
        TextView name, premiumAmount, icon;
        Button dueStatus;

        public DueLogViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.userDataName);
            premiumAmount = itemView.findViewById(R.id.userDataPmAmount);
            dueStatus = itemView.findViewById(R.id.userDueInfo);
            icon = itemView.findViewById(R.id.userIcon);
        }
    }

    public void paymentCollection(String amount){
        long intAmount = Long.parseLong(amount);
        DatabaseReference collectionRef = FirebaseDatabase.getInstance().getReference().child("Collection");

        //Daily Payment
        collectionRef.child(infoDate.substring(6)).child(infoDate.substring(3,5)).child(infoDate)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        HashMap<String,Object> updatePaymentMap = new HashMap<>();
                        if(snapshot.exists()){
                            Long currentDailyAmount = Long.valueOf(Objects.requireNonNull(snapshot.getValue()).toString());
                            updatePaymentMap.put(infoDate,currentDailyAmount+intAmount);

                        } else {
                            Long currentDailyAmount= Long.valueOf(0);
                            updatePaymentMap.put(infoDate,currentDailyAmount+intAmount);
                        }
                        collectionRef.child(infoDate.substring(6)).child(infoDate.substring(3,5)).updateChildren(updatePaymentMap);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        //Monthly Payment
        collectionRef.child(infoDate.substring(6)).child(infoDate.substring(3,5)).child("Monthly Amount")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        HashMap<String,Object> updatePaymentMap = new HashMap<>();
                        Log.d("tiit", "onDataChange: ");
                        if(snapshot.exists()){
                            Long currentMonthlyAmount = Long.valueOf(Objects.requireNonNull(snapshot.getValue()).toString());
                            updatePaymentMap.put("Monthly Amount",currentMonthlyAmount+intAmount);

                        } else {
                            Long currentMonthlyAmount= Long.valueOf(0);
                            updatePaymentMap.put("Monthly Amount",currentMonthlyAmount+intAmount);
                        }
                        collectionRef.child(infoDate.substring(6)).child(infoDate.substring(3,5)).updateChildren(updatePaymentMap);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    void paymentPlaceCollection(String amount){
        long intAmount = Long.parseLong(amount);
        DatabaseReference collectionRef = FirebaseDatabase.getInstance().getReference().child("Place Collection");

        //Daily Payment for individual place
        collectionRef.child(placeId).child(infoDate.substring(6)).child(infoDate.substring(3,5)).child(infoDate)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        HashMap<String,Object> updatePaymentMap = new HashMap<>();
                        Log.d("tiit", "onDataChange: ");
                        if(snapshot.exists()){
                            Long currentDailyAmount = Long.valueOf(Objects.requireNonNull(snapshot.getValue()).toString());
                            updatePaymentMap.put(infoDate,currentDailyAmount+intAmount);

                        } else {
                            Long currentDailyAmount= Long.valueOf(0);
                            updatePaymentMap.put(infoDate,currentDailyAmount+intAmount);
                        }
                        collectionRef.child(placeId).child(infoDate.substring(6)).child(infoDate.substring(3,5)).updateChildren(updatePaymentMap);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        //Monthly Payment
        collectionRef.child(placeId).child(infoDate.substring(6)).child(infoDate.substring(3,5)).child("Monthly Amount")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        HashMap<String,Object> updatePaymentMap = new HashMap<>();
                        Log.d("tiit", "onDataChange: ");
                        if(snapshot.exists()){
                            Long currentMonthlyAmount = Long.valueOf(Objects.requireNonNull(snapshot.getValue()).toString());
                            updatePaymentMap.put("Monthly Amount",currentMonthlyAmount+intAmount);

                        } else {
                            Long currentMonthlyAmount= Long.valueOf(0);
                            updatePaymentMap.put("Monthly Amount",currentMonthlyAmount+intAmount);
                        }
                        collectionRef.child(placeId).child(infoDate.substring(6)).child(infoDate.substring(3,5)).updateChildren(updatePaymentMap);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    void deletePaymentCollection(String amount){
        long intAmount = Long.parseLong(amount);
        DatabaseReference collectionRef = FirebaseDatabase.getInstance().getReference().child("Collection");

        //Daily Payment
        collectionRef.child(infoDate.substring(6)).child(infoDate.substring(3,5)).child(infoDate)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        HashMap<String,Object> updatePaymentMap = new HashMap<>();
                        Log.d("tiit", "onDataChange: ");
                        if(snapshot.exists()){
                            Long currentDailyAmount = Long.valueOf(Objects.requireNonNull(snapshot.getValue()).toString());
                            updatePaymentMap.put(infoDate,currentDailyAmount-intAmount);
                            collectionRef.child(infoDate.substring(6)).child(infoDate.substring(3,5)).updateChildren(updatePaymentMap);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        //Monthly Payment
        collectionRef.child(infoDate.substring(6)).child(infoDate.substring(3,5)).child("Monthly Amount")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        HashMap<String,Object> updatePaymentMap = new HashMap<>();
                        Log.d("tiit", "onDataChange: ");
                        if(snapshot.exists()){
                            Long currentMonthlyAmount = Long.valueOf(Objects.requireNonNull(snapshot.getValue()).toString());
                            updatePaymentMap.put("Monthly Amount",currentMonthlyAmount-intAmount);
                            collectionRef.child(infoDate.substring(6)).child(infoDate.substring(3,5)).updateChildren(updatePaymentMap);
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    void deletePaymentPlaceCollection(String amount){
        long intAmount = Long.parseLong(amount);
        DatabaseReference collectionRef = FirebaseDatabase.getInstance().getReference().child("Place Collection");

        //Daily Payment for individual place
        collectionRef.child(placeId).child(infoDate.substring(6)).child(infoDate.substring(3,5)).child(infoDate)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        HashMap<String,Object> updatePaymentMap = new HashMap<>();
                        Log.d("tiit", "onDataChange: ");
                        if(snapshot.exists()){
                            Long currentDailyAmount = Long.valueOf(Objects.requireNonNull(snapshot.getValue()).toString());
                            updatePaymentMap.put(infoDate,currentDailyAmount-intAmount);
                            collectionRef.child(placeId).child(infoDate.substring(6)).child(infoDate.substring(3,5)).updateChildren(updatePaymentMap);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        //Monthly Payment
        collectionRef.child(placeId).child(infoDate.substring(6)).child(infoDate.substring(3,5)).child("Monthly Amount")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        HashMap<String,Object> updatePaymentMap = new HashMap<>();
                        Log.d("tiit", "onDataChange: ");
                        if(snapshot.exists()){
                            Long currentMonthlyAmount = Long.valueOf(Objects.requireNonNull(snapshot.getValue()).toString());
                            updatePaymentMap.put("Monthly Amount",currentMonthlyAmount-intAmount);
                            collectionRef.child(placeId).child(infoDate.substring(6)).child(infoDate.substring(3,5)).updateChildren(updatePaymentMap);
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}
