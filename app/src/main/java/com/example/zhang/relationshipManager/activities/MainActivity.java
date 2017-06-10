package com.example.zhang.relationshipManager.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.zhang.relationshipManager.R;

public class MainActivity extends BaseActivity {

    public static void startActivity(Context context){
        Intent intent=new Intent(context,MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
