package com.example.zhang.relationshipManager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.zhang.relationshipManager.R;
import com.example.zhang.relationshipManager.models.Person;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Contact_Detail_Activity extends AppCompatActivity {

    @BindView(R.id.contact_name)
    AppCompatEditText contactName;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.manage_relationship)
    AppCompatButton manageRelationship;
    private boolean isEditing = false;
    private String personID,personName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        personName = intent.getStringExtra("name");
        personID = intent.getStringExtra("id");
        contactName.setText(personName);
        manageRelationship.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelationshipActivity.startActivity(v.getContext(),new Person(Integer.parseInt(personID),personName));
            }
        });
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.contact_detail_app_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        AppCompatEditText newContactName = (AppCompatEditText) this.findViewById(R.id.contact_name);

        switch (item.getItemId()) {
            case R.id.edit:
                if (isEditing) {
                    isEditing = false;
                    newContactName.setEnabled(false);
                    item.setIcon(R.drawable.ic_edit_white_24dp);
                    int id = Integer.parseInt(personID);
                    String name = newContactName.getText().toString();
                    BaseActivity.getPersonManager().updatePerson(id, name);
                    sendBroadcast(new Intent("DataChanged"));
                } else {
                    isEditing = true;
                    newContactName.setEnabled(true);
                    item.setIcon(R.drawable.ic_done_white_36dp);
                }
                break;
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }
}
