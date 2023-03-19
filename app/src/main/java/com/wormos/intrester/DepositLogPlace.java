package com.wormos.intrester;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class DepositLogPlace extends AppCompatActivity {

    RecyclerView duePlaceRv;
    CreatePlaceAdapter depositPlaceAdapter;
    ImageView depositPlaceBackBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposite_log_place);

        depositPlaceBackBtn = findViewById(R.id.deposit_log_place_back_arrow);
        depositPlaceBackBtn.setOnClickListener(view -> finish());


        FirebaseRecyclerOptions<CreatePlaceModel> options = new FirebaseRecyclerOptions.Builder<CreatePlaceModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference("Place"), CreatePlaceModel.class)
                .build();;

        duePlaceRv = findViewById(R.id.deposit_log_place_Rv);
        duePlaceRv.setLayoutManager(new LinearLayoutManager(this));
        depositPlaceAdapter = new CreatePlaceAdapter(options,"due");
        duePlaceRv.setAdapter(depositPlaceAdapter);
        depositPlaceAdapter.startListening();
    }
}