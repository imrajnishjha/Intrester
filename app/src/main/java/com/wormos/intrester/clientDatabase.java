package com.wormos.intrester;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class clientDatabase extends AppCompatActivity {

    ImageView backBtn;
    EditText clientSearchBar;
    RecyclerView cdRV;
    clientDatabaseAdapter  clientDatabaseAdapter;
    FirebaseRecyclerOptions<clientDatabaseModel> options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_database);

        backBtn = (ImageView) findViewById(R.id.client_database_back);

        backBtn.setOnClickListener(view -> finish());

        cdRV = findViewById(R.id.client_database_RV);

        cdRV.setLayoutManager(new LinearLayoutManager(this));

        clientSearchBar = findViewById(R.id.client_database_search);
        clientSearchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                setClientSearchBar(charSequence.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        fetchFirebase();
    }

    private void fetchFirebase() {
        try{
            options = new FirebaseRecyclerOptions.Builder<clientDatabaseModel>()
                    .setQuery(FirebaseDatabase.getInstance().getReference("MemberInfo"), clientDatabaseModel.class)
                    .build();

            clientDatabaseAdapter = new clientDatabaseAdapter(options);
            cdRV.setAdapter(clientDatabaseAdapter);
            clientDatabaseAdapter.startListening();

        }catch (Exception e){
            Toast.makeText(this, "Error" +e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    void setClientSearchBar(String text){
        try{
            options = new FirebaseRecyclerOptions.Builder<clientDatabaseModel>()
                    .setQuery(FirebaseDatabase.getInstance().getReference("MemberInfo")
                            .orderByChild("memberNameLowercase")
                            .startAt(text)
                            .endAt(text+"\uf8ff"), clientDatabaseModel.class)
                    .build();

            clientDatabaseAdapter = new clientDatabaseAdapter(options);
            cdRV.setAdapter(clientDatabaseAdapter);
            clientDatabaseAdapter.startListening();

        }catch (Exception e){
            Toast.makeText(this, "Error" +e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }
}