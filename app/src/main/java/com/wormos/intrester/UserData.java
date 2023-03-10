package com.wormos.intrester;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class UserData extends AppCompatActivity {
    TextView amDateOfOpen, amDateOfClose, amPlanA, amPlanB, amCreate;
    EditText amPremiumPlan, amMemberName, amAccNo, amAmountCollected, amAmountRemaining, amAddress, amPhone;
    Button btn;
    Spinner spinner;

    Boolean plan = false;
    String amPlan = "B", dur="";
    int memberCount = 0;
    String placeId;

    RecyclerView memberRv;
    addMemberViewHolder addMemberHolder;
    FirebaseRecyclerOptions<addMemberModal> options;
    Bundle extras;


    private final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_data);

        btn = (Button) findViewById(R.id.userDataAddBtn);
        extras = getIntent().getExtras();

        //RecycleView Setup
        memberRv = findViewById(R.id.member_RV);

        memberRv.setLayoutManager(new LinearLayoutManager(this));



        fetchFirebase();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               bottomUp();
            }
        });


    }

    private void fetchFirebase() {
        try{
            options = new FirebaseRecyclerOptions.Builder<addMemberModal>()
                    .setQuery(FirebaseDatabase.getInstance().getReference("MemberInfo"), addMemberModal.class)
                    .build();

            addMemberHolder = new addMemberViewHolder(options);
            memberRv.setAdapter(addMemberHolder);
            addMemberHolder.startListening();

        }catch (Exception e){
            Toast.makeText(this, "Error" +e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }

    private void bottomUp() {
        BottomSheetDialog bottomSheet = new BottomSheetDialog(UserData.this);
        bottomSheet.setContentView(R.layout.activity_add_member);
        initialization(bottomSheet);
        onClickFun(bottomSheet);
        bottomSheet.show();
    }

    private void initialization(BottomSheetDialog bottomSheet) {

        amDateOfOpen = (TextView) bottomSheet.findViewById(R.id.addMemberDOO);
        amDateOfClose = (TextView) bottomSheet.findViewById(R.id.addMemberDOM);
        amPlanA = (TextView) bottomSheet.findViewById(R.id.amPlanA);
        amPlanB = (TextView) bottomSheet.findViewById(R.id.amPlanB);
        amCreate =(TextView) bottomSheet.findViewById(R.id.addMemberButton);

        amPremiumPlan =(EditText) bottomSheet.findViewById(R.id.addMemberPP);
        amMemberName =(EditText) bottomSheet.findViewById(R.id.addMemberName);
        amAccNo =(EditText) bottomSheet.findViewById(R.id.addMemberAccount);
        amAddress =(EditText) bottomSheet.findViewById(R.id.addMemberAddress);
        amPhone =(EditText) bottomSheet.findViewById(R.id.addMemberPhone);
        amAmountCollected =(EditText) bottomSheet.findViewById(R.id.addMemberAC);
        amAmountRemaining =(EditText) bottomSheet.findViewById(R.id.addMemberAR);



        spinner = (Spinner) bottomSheet.findViewById(R.id.addMemberOption);



    }

    private void onClickFun(BottomSheetDialog bottomSheet) {
        amDateOfOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseDate(amDateOfOpen);
            }
        });

        amDateOfClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseDate(amDateOfClose);

            }
        });

        amPlanA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!plan){
                    amPlanA.setBackgroundResource(R.color.light_teal);
                    amPlanB.setBackgroundResource(R.color.grey);
                    amPlan ="A";
                    plan = true;
                }

            }
        });
        amPlanB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(plan){
                    amPlanA.setBackgroundResource(R.color.grey);
                    amPlanB.setBackgroundResource(R.color.light_teal);
                    amPlan ="B";
                    plan = false;
                }

            }
        });


        spinnerFun();

        //Create Member Working
        amCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFirebase(bottomSheet);
            }
        });


    }

    private void uploadFirebase(BottomSheetDialog bottomSheet) {

        if(amPremiumPlan.getText().toString().isBlank() || amMemberName.getText().toString().isBlank() || amAddress.getText().toString().isBlank()
        || amPhone.getText().toString().isBlank() || amAmountCollected.getText().toString().isBlank() || amAmountRemaining.getText().toString().isBlank()
        || amAccNo.getText().toString().isBlank()){
            amPremiumPlan.setError("Please Fill All Details");
            amPremiumPlan.requestFocus();
        } else{
            if (extras != null) {
                placeId = extras.getString("id");

            }else{
                placeId = "NuLL";
            }
            HashMap<String,Object> placeMap = new HashMap<>();
            placeMap.put("premiumPlan",amPremiumPlan.getText().toString());
            placeMap.put("memberName",amMemberName.getText().toString());
            placeMap.put("accountNo",amAccNo.getText().toString());
            placeMap.put("amountCollected",amAmountCollected.getText().toString());
            placeMap.put("amountRemaining",amAmountRemaining.getText().toString());
            placeMap.put("address",amAddress.getText().toString());
            placeMap.put("phone",amPhone.getText().toString());
            placeMap.put("dateOfOpen",amDateOfOpen.getText().toString());
            placeMap.put("dateOfMaturity",amDateOfClose.getText().toString());
            placeMap.put("plan",amPlan);
            placeMap.put("duration",dur);
            placeMap.put("placeId", placeId);



            dbRef.child("MemberInfo").child(amAccNo.getText().toString()).updateChildren(placeMap)
                    .addOnCompleteListener(task->{
                        if(task.isSuccessful()){
                            Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
            dbRef.child("MemberByGroup").child(amPlan).child(amAccNo.getText().toString()).updateChildren(placeMap)
                    .addOnCompleteListener(task->{
                        if(task.isSuccessful()){
                            Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

            dbRef.child("MemberByPlace").child(placeId).child(amAccNo.getText().toString()).updateChildren(placeMap)
                    .addOnCompleteListener(task->{
                        if(task.isSuccessful()){
                            Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show();
                            bottomSheet.dismiss();
                        } else {
                            Toast.makeText(getApplicationContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        }
    }

    private void spinnerFun() {
        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Daily");
        categories.add("Weekly");
        categories.add("Monthly");
        categories.add("Yearly");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dur = parent.getItemAtPosition(position).toString();
//                Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void chooseDate(TextView date) {
        final Calendar c = Calendar.getInstance();


        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(

                UserData.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                },
                year, month, day);
        datePickerDialog.show();
    }
    @Override
    protected void onStart() {
        super.onStart();
        addMemberHolder.startListening();
    }
}