package com.wormos.intrester;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomsheet.BottomSheetDialog;

public class CreatePlace extends AppCompatActivity {

    AppCompatButton createPlaceBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_place);

        createPlaceBtn = findViewById(R.id.create_place_btn);
        createPlaceBtn.setOnClickListener(view->{
            BottomSheetDialog bottomSheet = new BottomSheetDialog(CreatePlace.this);
            bottomSheet.setContentView(R.layout.activity_add_new_place);
            bottomSheet.show();
        });
    }
}