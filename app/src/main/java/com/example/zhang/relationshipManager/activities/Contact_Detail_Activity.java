package com.example.zhang.relationshipManager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.zhang.relationshipManager.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Contact_Detail_Activity extends AppCompatActivity {

    @BindView(R.id.contact_name)
    AppCompatEditText contactName;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.textView_contactID)
    AppCompatTextView textViewContactID;
    private boolean isEditing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        contactName.setText(intent.getStringExtra("name"));
        textViewContactID.setText(intent.getStringExtra("id"));
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
                    int id = Integer.parseInt(textViewContactID.getText().toString());
                    String name = newContactName.getText().toString();
                    BaseActivity.getPersonManager().updatePerson(id, name);
                    sendBroadcast(new Intent("DataChanged"));
                } else{
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
