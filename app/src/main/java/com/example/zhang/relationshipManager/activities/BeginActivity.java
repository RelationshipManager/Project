package com.example.zhang.relationshipManager.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.zhang.relationshipManager.models.ContactManager;
import com.example.zhang.relationshipManager.models.Neo4jManager;
import com.example.zhang.relationshipManager.models.RelationshipManager;
import com.example.zhang.relationshipManager.models.User;


public class BeginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ContactManager.getInstance(this);
        RelationshipManager.getInstance(this);
        Neo4jManager.getInstance(this);
        User.getInstance(this);
        LoginActivity.startActivity(this);
//        MainActivity.startActivity(this);
        finish();
    }
}
