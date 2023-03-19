package com.wormos.intrester;

import static com.wormos.intrester.DepositLogBook.todayDateFormatter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class AccountSummary extends AppCompatActivity {

    TextView dateTv;
    ImageView forward_date_arrow,backward_date_arrow;
    String selectedDate;
    int year,month,day;
    RecyclerView placeStatRv;
    FirebaseRecyclerOptions<CreatePlaceModel> options;
    AccountSummaryAdapter accountSummaryAdapter;


    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_summary);


        forward_date_arrow = findViewById(R.id.accounts_date_forward_arrow);
        backward_date_arrow = findViewById(R.id.accounts_date_back_arrow);
        placeStatRv = findViewById(R.id.account_placeRv);
        placeStatRv.setLayoutManager(new LinearLayoutManager(this));



        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);



        dateTv = findViewById(R.id.accounts_selected_date);
        selectedDate = todayDateFormatter("MMM dd, yyyy");
        dateTv.setText(selectedDate);
        dateTv.setOnClickListener(view -> {
            chooseDate();
        });



        forward_date_arrow.setOnClickListener(view -> {
            if(!todayDateFormatter("dd-MM-yyyy").equals(changeDateFormat(selectedDate,"MMM dd, yyyy","dd-MM-yyyy"))){
                dayIncrementer(1);
                pieChartMaker(changeDateFormat(selectedDate,"MMM dd, yyyy","dd-MM-yyyy"));
                fetchFirebase(changeDateFormat(selectedDate,"MMM dd, yyyy","dd-MM-yyyy"));
            }

        });
        backward_date_arrow.setOnClickListener(view -> {
            dayIncrementer(-1);
            pieChartMaker(changeDateFormat(selectedDate,"MMM dd, yyyy","dd-MM-yyyy"));
            fetchFirebase(changeDateFormat(selectedDate,"MMM dd, yyyy","dd-MM-yyyy"));

        });



        pieChartMaker(todayDateFormatter("dd-MM-yyyy"));
        fetchFirebase("dd-MM-yyyy");



    }

    void dayIncrementer(int no){
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(Objects.requireNonNull(sdf.parse(selectedDate)));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        c.add(Calendar.DATE, no);
        selectedDate = sdf.format(c.getTime());
        dateTv.setText(selectedDate);
    }

    private String changeDateFormat(String inputDate,String originalDateFormat,String targetDateFormat){
        SimpleDateFormat originalFormat = new SimpleDateFormat(originalDateFormat, Locale.getDefault());
        SimpleDateFormat targetFormat = new SimpleDateFormat(targetDateFormat,Locale.getDefault());
        Date date = null;
        try {
            date = originalFormat.parse(inputDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return targetFormat.format(date);
    }

    private void chooseDate() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(

                AccountSummary.this,android.R.style.Theme_Material_Dialog_Alert,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int years,
                                          int monthOfYear, int dayOfMonth) {
                        StringBuilder infoDate = new StringBuilder();
                        if((monthOfYear + 1)<10){
                            if(dayOfMonth<10) infoDate.append("0"+dayOfMonth + "-" +"0"+(monthOfYear + 1) + "-" + years);
                            else infoDate.append(dayOfMonth + "-" + "0"+(monthOfYear + 1) + "-" + years);
                        } else {
                            if(dayOfMonth<10) infoDate.append("0"+dayOfMonth + "-" +(monthOfYear + 1) + "-" + years);
                            else infoDate.append(dayOfMonth + "-" +(monthOfYear + 1) + "-" + years);
                        }
                        year= years;
                        month=monthOfYear;
                        day= dayOfMonth;
                        selectedDate = changeDateFormat(infoDate.toString(),"dd-MM-yyyy","MMM dd, yyyy");
                        dateTv.setText(selectedDate);
                        pieChartMaker(infoDate.toString());
                        fetchFirebase(infoDate.toString());

                    }
                },
                year, month, day);

        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();

    }

    void pieChartMaker(String chosenDate){

        PieChart pieChart = (PieChart) findViewById(R.id.pie_chart_data);
        String chosenMonth = chosenDate.substring(3,5);
        String chosenYear = chosenDate.substring(6);
        ArrayList<PieEntry> records = new ArrayList<>();
        dbRef.child("Place Collection").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    String id = dataSnapshot.getKey();
                    dbRef.child("Place Collection").child(id).child(chosenYear).child(chosenMonth)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot2) {
                                    long placeMonthlyAmount =0;
                                    if(snapshot2.child("Monthly Amount").exists()){
                                        placeMonthlyAmount = Long.parseLong(Objects.requireNonNull(snapshot2.child("Monthly Amount").getValue()).toString());

                                    }
                                    records.add(new PieEntry(placeMonthlyAmount,id));
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        new Handler().postDelayed(() -> {
            Log.d("kok", "pieChartMaker: "+records.toString());
            PieDataSet pieDataSet = new PieDataSet(records,"Monthly Revenue by Place Id");
            pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
            pieDataSet.setValueTextColor(Color.BLACK);
            pieDataSet.setValueTextSize(14f);
            PieData pieData = new PieData(pieDataSet);
            pieDataSet.notifyDataSetChanged();
            pieChart.setData(pieData);
            pieData.notifyDataChanged();
            pieChart.notifyDataSetChanged();
            pieChart.getDescription().setEnabled(true);
            pieChart.setCenterText("Click to Reload");
            pieChart.animate();

        },500);
    }

    @Override
    protected void onResume() {
        super.onResume();
        pieChartMaker(todayDateFormatter("dd-MM-yyyy"));
    }

    private void fetchFirebase(String date) {
        try{
            options = new FirebaseRecyclerOptions.Builder<CreatePlaceModel>()
                    .setQuery(FirebaseDatabase.getInstance().getReference("Place"), CreatePlaceModel.class)
                    .build();

            accountSummaryAdapter = new AccountSummaryAdapter(options,date.substring(3,5),date.substring(6),date);
            placeStatRv.setAdapter(accountSummaryAdapter);
            accountSummaryAdapter.startListening();

        }catch (Exception e){
            Toast.makeText(this, "Error" +e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }
}



