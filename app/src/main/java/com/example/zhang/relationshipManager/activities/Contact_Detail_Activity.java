package com.example.zhang.relationshipManager.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;

import com.example.zhang.relationshipManager.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Contact_Detail_Activity extends Activity {

    @BindView(R.id.contact_name)
    AppCompatTextView contactName;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        contactName.setText(intent.getStringExtra("name"));

    }
}
