package com.wormos.intrester;

import static com.wormos.intrester.DepositLogBook.todayDateFormatter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    CardView entryLog,accountSummary;
    CardView clientDatabase,depositLogPlace,dueLogBook;

    TextView monthCollection,dayCollection;
    String infoDate;
    DatabaseReference collectionRef = FirebaseDatabase.getInstance().getReference().child("Collection");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        entryLog = findViewById(R.id.dashboard_entry_logbook_navigation);
        entryLog.setOnClickListener(view-> startActivity(new Intent(this,CreatePlace.class)));

        clientDatabase = findViewById(R.id.dashboard_client_database_Cv);
        clientDatabase.setOnClickListener(view-> startActivity(new Intent(this,  clientDatabase.class)));

        depositLogPlace = findViewById(R.id.dashboard_deposite_logbook_Cv);
        depositLogPlace.setOnClickListener(view -> startActivity(new Intent(this,DepositLogPlace.class)));

        dueLogBook = findViewById(R.id.dashboard_due_logbook_Cv);
        dueLogBook.setOnClickListener(view -> startActivity(new Intent(this,DueLogBook.class)));

        accountSummary= findViewById(R.id.dashboard_account_navigation);
        accountSummary.setOnClickListener(view -> startActivity(new Intent(this,AccountSummary.class)));



        monthCollection = findViewById(R.id.dashboard_month_collection);
        dayCollection = findViewById(R.id.dashboard_day_collection);
        infoDate = todayDateFormatter("dd-MM-YYYY");

        //Setting daily collection
        collectionRef.child(infoDate.substring(6)).child(infoDate.substring(3,5)).child(infoDate)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            Long currentDailyAmount = Long.valueOf(Objects.requireNonNull(snapshot.getValue()).toString());
                            dayCollection.setText(String.valueOf(currentDailyAmount));
                            Log.d("tiit", "onDataChange1: "+currentDailyAmount);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        //Setting monthly payment
        collectionRef.child(infoDate.substring(6)).child(infoDate.substring(3,5)).child("Monthly Amount")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            Long currentMonthAmount = Long.valueOf(Objects.requireNonNull(snapshot.getValue()).toString());
                            monthCollection.setText(String.valueOf(currentMonthAmount));
                            Log.d("tiit", "onDataChange: "+currentMonthAmount);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
}