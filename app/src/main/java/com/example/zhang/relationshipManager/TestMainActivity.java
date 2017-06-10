package com.example.zhang.relationshipManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.zhang.relationshipManager.activities.MainActivity;
import com.example.zhang.relationshipManager.R;

/**
 * Created by 10040 on 2017/6/10.
 */

public class TestMainActivity extends AppCompatActivity {

    public static void startActivity(Context context){
        Intent intent=new Intent(context,TestMainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        //需要测试的时候把下面的这行注释掉就好了
        MainActivity.startActivity(this);
    }
}
