package com.wormos.intrester;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Objects;

public class CreatePlace extends AppCompatActivity {

    AppCompatButton createPlaceBtn;
    RecyclerView placeRv;
    CreatePlaceAdapter createPlaceAdapter;
    FirebaseRecyclerOptions<CreatePlaceModel> options;
    TextView addPlaceBtn;
    EditText placeName, placeId;
    ImageView placeIcon;
    private final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
   // static StorageReference storageRef = FirebaseStorage.getInstance().getReference("Place Picture/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_place);

        createPlaceBtn = findViewById(R.id.create_place_btn);
        placeRv = findViewById(R.id.create_place_Rv);

        //RecycleView Setup
        placeRv.setLayoutManager(new LinearLayoutManager(this));
        options = new FirebaseRecyclerOptions.Builder<CreatePlaceModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference("Place"), CreatePlaceModel.class)
                .build();

        createPlaceAdapter = new CreatePlaceAdapter(options,"create");
        placeRv.setAdapter(createPlaceAdapter);
        createPlaceAdapter.startListening();

        //Create Place functionality
        createPlaceBtn.setOnClickListener(view->{
            BottomSheetDialog bottomSheet = new BottomSheetDialog(CreatePlace.this);
            bottomSheet.setContentView(R.layout.activity_add_new_place);
            bottomSheet.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            placeIcon = bottomSheet.findViewById(R.id.create_place_image);
            placeName = bottomSheet.findViewById(R.id.create_place_name);
            placeId = bottomSheet.findViewById(R.id.create_place_image_text);
            addPlaceBtn = bottomSheet.findViewById(R.id.add_place_btn);

            //get image from gallery
//            placeIcon.setOnClickListener(v->{
//                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
//                LayoutInflater layoutInflater = getLayoutInflater();
//                View pickImgview = layoutInflater.inflate(R.layout.image_picker_item, null);
//                builder.setCancelable(true);
//                builder.setView(pickImgview);
//                AlertDialog alertDialogImg = builder.create();
//                Window window = alertDialogImg.getWindow();
//                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                WindowManager.LayoutParams wlp = window.getAttributes();
//                wlp.gravity = Gravity.BOTTOM;
//                wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
//                alertDialogImg.show();
//                window.setAttributes(wlp);
//
//                CardView cameraCardView = pickImgview.findViewById(R.id.chooseCamera);
//                CardView galleryCardView = pickImgview.findViewById(R.id.chooseGallery);
//
//                galleryCardView.setOnClickListener(view1 -> {
//                    ImagePicker.with(this)
//                            .galleryOnly()
//                            .crop(1f, 1f)
//                            .maxResultSize(720, 1080)
//                            .start(0);
//                    alertDialogImg.dismiss();
//                });
//                cameraCardView.setOnClickListener(view1 -> {
//                    ImagePicker.with(this)
//                            .cameraOnly()
//                            .crop(1f, 1f)
//                            .maxResultSize(720, 1080)
//                            .start(1);
//                    alertDialogImg.dismiss();
//                });
//            });


            //send place data to firebase
            addPlaceBtn.setOnClickListener(v->{
                if(placeName.getText().toString().isBlank()){
                    placeName.setError("Enter the name");
                    placeName.requestFocus();
                } else if(placeId.getText().toString().isBlank()){
                    placeId.setError("Enter the name");
                    placeId.requestFocus();
                } else{
                    HashMap<String,Object> placeMap = new HashMap<>();
                    placeMap.put("place",placeName.getText().toString());
                    dbRef.child("Place").child(placeId.getText().toString()).updateChildren(placeMap)
                            .addOnCompleteListener(task->{
                                if(task.isSuccessful()){
                                    bottomSheet.dismiss();
                                } else {
                                    Toast.makeText(getApplicationContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }


            });
            bottomSheet.show();


        });


    }

//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK && requestCode == 0) {
//            placeUri = data.getData();
//            placeIcon.setImageURI(placeUri);
//        } else if (resultCode == RESULT_OK && requestCode == 1) {
//            placeUri = data.getData();
//            placeIcon.setImageURI(placeUri);
//
//        }
//    }

    @Override
    protected void onStart() {
        super.onStart();
        createPlaceAdapter.startListening();
    }
}