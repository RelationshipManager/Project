package com.example.zhang.relationshipManager.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.zhang.relationshipManager.TestActivities.TestMainActivity;

/**
 * Created by 10040 on 2017/6/10.
 */

public class BeginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TestMainActivity.startActivity(this);
    }
}
