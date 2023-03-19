package com.wormos.intrester;

import static com.wormos.intrester.DepositLogBook.todayDateFormatter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DueLogBook extends AppCompatActivity {

    String selectedId="-1",selectedImg ="0",typeId="1";
    MotionLayout dueLogMotionLayout;
    Spinner placeSpinner;
    ArrayList<String> spinnerList,placeIdList;
    ArrayAdapter<String> spinnerAdapter;
    DatabaseReference dbRef= FirebaseDatabase.getInstance().getReference();

    TextView allTv,paidTv,dueTv;
    RecyclerView dueLogRv;
    DueLogBookAdapter dueLogBookAdapter;
    ImageView dueLogSearchBtn;
    EditText dueLogSearchBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_due_log_book);

        allTv = findViewById(R.id.due_log_all_search);
        paidTv = findViewById(R.id.due_log_paid_search);
        dueTv = findViewById(R.id.due_log_due_search);

        dueLogMotionLayout = findViewById(R.id.dueLog_motion_layout);


        dueLogSearchBtn = findViewById(R.id.due_log_search_btn);
        dueLogSearchBar = findViewById(R.id.due_log_search_bar);


        dueLogRv = findViewById(R.id.dueLogBook_RV);
        dueLogRv.setLayoutManager(new LinearLayoutManager(this));


        placeSpinner = findViewById(R.id.due_log_search_spinner);
        spinnerList = new ArrayList<>();
        placeIdList = new ArrayList<>();
        spinnerList.add("Select a place");
        spinnerAdapter = new ArrayAdapter<String>(DueLogBook.this, R.layout.spinner_item,spinnerList);
        placeSpinner.setAdapter(spinnerAdapter);

        dbRef.child("Place").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot item:snapshot.getChildren()){
                    spinnerList.add(item.child("place").getValue().toString());
                    placeIdList.add(item.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        placeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(!placeSpinner.getSelectedItem().toString().equals("Select a place")){
                    selectedId = placeIdList.get(i-1);
                    menuSelector(allTv,dueTv,paidTv);
                    RvSelector(selectedId,"all");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //Menu Selector
        allTv.setOnClickListener(view -> {
            menuSelector(allTv,dueTv,paidTv);
            RvSelector(selectedId,"all");
        });

        dueTv.setOnClickListener(view -> {
            menuSelector(dueTv,paidTv,allTv);
            RvSelector(selectedId,"due");
        });

        paidTv.setOnClickListener(view -> {
            menuSelector(paidTv,allTv,dueTv);
            RvSelector(selectedId,"paid");
        });

        //Search Bar
        dueLogSearchBtn.setOnClickListener(view ->{
            Log.d("img", "onCreate: ");
            if(selectedImg.equals("0")){
                dueLogSearchBtn.setImageResource(R.drawable.cancel);
                selectedImg = "1";
                dueLogMotionLayout.transitionToEnd();

            } else {
                dueLogSearchBtn.setImageResource(R.drawable.search_icon);
                selectedImg = "0";
                dueLogMotionLayout.transitionToStart();
            }
        });

        dueLogSearchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                menuSelector(allTv,dueTv,paidTv);
                setDueLogSearchBtn(selectedId,"all",charSequence.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    void menuSelector(TextView tv1, TextView tv2, TextView tv3){
        tv1.setBackground(ContextCompat.getDrawable(DueLogBook.this, R.drawable.round_white));
        tv2.setBackground(ContextCompat.getDrawable(DueLogBook.this, R.drawable.round_grey));
        tv3.setBackground(ContextCompat.getDrawable(DueLogBook.this, R.drawable.round_grey));
    }

    void RvSelector(String placeId,String type){
        if(!placeId.equals("-1")){
            String infoDate = todayDateFormatter("dd-MM-YYYY");
            FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<addMemberModal>()
                    .setQuery(FirebaseDatabase.getInstance().getReference("MemberByPlace").child(placeId), addMemberModal.class)
                    .build();
            dueLogBookAdapter = new DueLogBookAdapter(options,placeId,infoDate,type);
            dueLogRv.setAdapter(dueLogBookAdapter);
            dueLogBookAdapter.startListening();
        }

    }

    void setDueLogSearchBtn(String placeId,String type,String text){
        if(!placeId.equals("-1")){
            String infoDate = todayDateFormatter("dd-MM-YYYY");
            FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<addMemberModal>()
                    .setQuery(FirebaseDatabase.getInstance().getReference("MemberByPlace").child(placeId)
                            .orderByChild("memberNameLowercase")
                            .startAt(text)
                            .endAt(text+"\uf8ff"), addMemberModal.class)
                    .build();
            dueLogBookAdapter = new DueLogBookAdapter(options,placeId,infoDate,type);
            dueLogRv.setAdapter(dueLogBookAdapter);
            dueLogBookAdapter.startListening();
        } else {
            Toast.makeText(DueLogBook.this,"Select a Place",Toast.LENGTH_SHORT).show();
        }
    }


}