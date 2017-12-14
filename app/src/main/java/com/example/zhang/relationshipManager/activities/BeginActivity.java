package com.example.zhang.relationshipManager.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


public class BeginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.startActivity(this);
        finish();
    }
}
