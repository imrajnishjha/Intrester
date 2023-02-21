package com.wormos.intrester;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    CardView entryLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        entryLog = findViewById(R.id.dashboard_entry_logbook_navigation);
        entryLog.setOnClickListener(view-> startActivity(new Intent(this,CreatePlace.class)));
    }
}