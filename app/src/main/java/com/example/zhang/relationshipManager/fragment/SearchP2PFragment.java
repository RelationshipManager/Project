package com.example.zhang.relationshipManager.fragment;

import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.zhang.relationshipManager.R;
import com.example.zhang.relationshipManager.models.Contact;
import com.example.zhang.relationshipManager.models.ContactDataChangeReceiver;
import com.example.zhang.relationshipManager.models.ContactManager;

import java.util.ArrayList;

import butterknife.BindView;


public class SearchP2PFragment extends BaseFragment {

    @BindView(R.id.spinner_contact_from)
    Spinner spinnerContactFrom;
    @BindView(R.id.spinner_contact_to)
    Spinner spinnerContactTo;

    ArrayAdapter<String> contactToAdapter, contactFromAdapter;
    ContactDataChangeReceiver contactDataChangeReceiver;

    public SearchP2PFragment() {
    }

    @Override
    protected int getResourceId() {
        return R.layout.search_fragment_p2p;
    }

    @Override
    protected void initViews() {
        contactToAdapter = new ArrayAdapter<String>(this.getContext(), R.layout.spinner_contact_item, R.id.spinner_contact_item);
        contactToAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_contact);
        spinnerContactTo.setAdapter(contactToAdapter);

        contactFromAdapter = new ArrayAdapter<String>(this.getContext(), R.layout.spinner_contact_item, R.id.spinner_contact_item);
        contactFromAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_contact);
        spinnerContactFrom.setAdapter(contactFromAdapter);

        setAdapterData();
        contactDataChangeReceiver = new ContactDataChangeReceiver(getContext(), new ContactDataChangeReceiver.Refreshable() {
            @Override
            public void refresh() {
                setAdapterData();
            }
        });
    }

    private void setAdapterData() {
        // spinnerCantactTo's adapter
        contactFromAdapter.clear();
        contactToAdapter.addAll(parseAllContact(ContactManager.getInstance(getContext()).getAllContacts()));

        // spinnerCantactFrom's adapter
        contactFromAdapter.clear();
        contactFromAdapter.addAll(parseAllContact(ContactManager.getInstance(getContext()).getAllContacts()));
    }

    private String[] parseAllContact(ArrayList<Contact> contacts){
        int size = contacts.size();
        String[] result = new String[size];
        for (int i = 0; i < size; i++) {
            result[i] = contacts.get(i).getName();
        }
        return result;
    }
}
