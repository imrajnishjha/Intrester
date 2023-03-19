package com.wormos.intrester;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DepositLogBook extends AppCompatActivity {

    RecyclerView depositDataRv;
    FirebaseRecyclerOptions<addMemberModal> options;
    DepositLogBookAdapter depositLogBookAdapter;
    String placeId,infoDate;
    ImageView CalenderIcon;
    EditText depositSearchBar;
    int year,month,day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposite_log_book);

        placeId = getIntent().getExtras().getString("id");
        infoDate = todayDateFormatter("dd-MM-YYYY");

        depositSearchBar = findViewById(R.id.depositLogBookSearchBar);
        depositSearchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = charSequence.toString().toLowerCase();
                setDepositSearchBar(text,infoDate,placeId);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



        final Calendar c = Calendar.getInstance();

         year = c.get(Calendar.YEAR);
         month = c.get(Calendar.MONTH);
         day = c.get(Calendar.DAY_OF_MONTH);





        depositDataRv = findViewById(R.id.depositLogBook_RV);
        depositDataRv.setLayoutManager(new LinearLayoutManager(this));
        options = new FirebaseRecyclerOptions.Builder<addMemberModal>()
                .setQuery(FirebaseDatabase.getInstance().getReference("MemberByPlace").child(placeId), addMemberModal.class)
                .build();


        depositLogBookAdapter = new DepositLogBookAdapter(options,placeId,infoDate);
        depositDataRv.setAdapter(depositLogBookAdapter);
        depositLogBookAdapter.startListening();




        CalenderIcon = findViewById(R.id.depositLogBookCalender);
        CalenderIcon.setOnClickListener(view -> {
            chooseDate();

        });
    }

    public static String todayDateFormatter(String format){
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat(format, Locale.getDefault());
        return df.format(date);
    }

    private void chooseDate() {


        DatePickerDialog datePickerDialog = new DatePickerDialog(

                DepositLogBook.this,android.R.style.Theme_Material_Dialog_Alert,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int years,
                                          int monthOfYear, int dayOfMonth) {
                        StringBuilder selectedDate = new StringBuilder();
                        if((monthOfYear + 1)<10){
                            if(dayOfMonth<10) selectedDate.append("0"+dayOfMonth + "-" +"0"+(monthOfYear + 1) + "-" + years);
                            else selectedDate.append(dayOfMonth + "-" + "0"+(monthOfYear + 1) + "-" + years);
                        } else {
                            if(dayOfMonth<10) selectedDate.append("0"+dayOfMonth + "-" +(monthOfYear + 1) + "-" + years);
                            else selectedDate.append(dayOfMonth + "-" +(monthOfYear + 1) + "-" + years);
                        }
                        year= years;
                        month=monthOfYear;
                        day= dayOfMonth;
                        infoDate = selectedDate.toString();
                        depositLogBookAdapter = new DepositLogBookAdapter(options,placeId,infoDate);
                        depositDataRv.setAdapter(depositLogBookAdapter);
                        depositLogBookAdapter.startListening();

                    }
                },
                year, month, day);

        datePickerDialog.show();
    }

    void setDepositSearchBar(String text,String infoDate,String placeId){
        try{
            options = new FirebaseRecyclerOptions.Builder<addMemberModal>()
                    .setQuery(FirebaseDatabase.getInstance().getReference("MemberByPlace")
                            .child(placeId).orderByChild("memberNameLowercase")
                            .startAt(text)
                            .endAt(text+"\uf8ff"), addMemberModal.class)
                    .build();

            depositLogBookAdapter = new DepositLogBookAdapter(options,placeId,infoDate);
            depositDataRv.setAdapter(depositLogBookAdapter);
            depositLogBookAdapter.startListening();

        }catch (Exception e){
            Toast.makeText(DepositLogBook.this, "Error" +e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }
}