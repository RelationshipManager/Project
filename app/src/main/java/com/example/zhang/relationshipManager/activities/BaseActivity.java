package com.example.zhang.relationshipManager.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.zhang.relationshipManager.models.PersonManager;
import com.example.zhang.relationshipManager.models.RelationshipManager;

/**
 * Created by 10040 on 2017/6/10.
 */

public class BaseActivity extends AppCompatActivity {
    private static PersonManager sPersonManager;
    private static RelationshipManager sRelationshipManager;

    public static PersonManager getPersonManager() {
        return sPersonManager;
    }

    public static RelationshipManager getRelationshipManager() {
        return sRelationshipManager;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (sPersonManager == null || sRelationshipManager == null) {
            sPersonManager = PersonManager.getInstance(this);
            sRelationshipManager = RelationshipManager.getInstance(this);
        }
        //getSupportActionBar().hide();
    }
}
