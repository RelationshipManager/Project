package com.example.zhang.relationshipManager.fragment;

import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.zhang.relationshipManager.R;
import com.example.zhang.relationshipManager.models.Contact;
import com.example.zhang.relationshipManager.models.ContactManager;

import java.util.ArrayList;

import butterknife.BindView;


public class SearchP2PFragment extends BaseFragment {

    @BindView(R.id.spinner_contact_from)
    Spinner spinnerContactFrom;
    @BindView(R.id.spinner_contact_to)
    Spinner spinnerContactTo;

    public SearchP2PFragment() {
    }

    @Override
    protected int getResourceId() {
        return R.layout.search_fragment_p2p;
    }

    @Override
    protected void initViews() {
        setAdapter();
    }

    private void setAdapter() {
        // spinnerCantactTo's adapter
        ArrayAdapter<String> contactToAdapter = new ArrayAdapter<String>(this.getContext(), R.layout.spinner_contact_item, R.id.spinner_contact_item);
        contactToAdapter.addAll(parseAllContact(ContactManager.getInstance(getContext()).getAllContacts()));
        contactToAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_contact);
        spinnerContactTo.setAdapter(contactToAdapter);

        // spinnerCantactFrom's adapter
        ArrayAdapter<String> contactFromAdapter = new ArrayAdapter<String>(this.getContext(), R.layout.spinner_contact_item, R.id.spinner_contact_item);
        contactFromAdapter.addAll(parseAllContact(ContactManager.getInstance(getContext()).getAllContacts()));
        contactFromAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_contact);
        spinnerContactFrom.setAdapter(contactFromAdapter);
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
