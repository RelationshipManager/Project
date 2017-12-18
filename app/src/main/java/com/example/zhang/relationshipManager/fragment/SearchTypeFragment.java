package com.example.zhang.relationshipManager.fragment;

import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.zhang.relationshipManager.R;
import com.example.zhang.relationshipManager.models.Contact;
import com.example.zhang.relationshipManager.models.ContactManager;

import java.util.ArrayList;

import butterknife.BindView;


public class SearchTypeFragment extends BaseFragment {
    @BindView(R.id.spinner_contact)
    Spinner spinnerContact;
    @BindView(R.id.spinner_rsType)
    Spinner spinnerRsType;

    public SearchTypeFragment() {
    }

    private void setAdapter() {
        // spinnerContact's adapter
        ArrayAdapter<String> contactAdapter = new ArrayAdapter<String>(this.getContext(), R.layout.spinner_contact_item, R.id.spinner_contact_item);
        contactAdapter.addAll(parseAllContact(ContactManager.getInstance(getContext()).getAllContacts()));
        contactAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_contact);
        spinnerContact.setAdapter(contactAdapter);

        // spinnerRsType's adapter
        ArrayAdapter<String> rsTypeAdapter = new ArrayAdapter<String>(this.getContext(), R.layout.spinner_contact_item, R.id.spinner_contact_item);
        rsTypeAdapter.addAll("朋友", "同事");
        rsTypeAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_contact);
        spinnerRsType.setAdapter(rsTypeAdapter);
    }

    @Override
    protected int getResourceId() {
        return R.layout.search_fragment_type;
    }

    @Override
    protected void initViews() {
        setAdapter();
    }

    private String[] parseAllContact(ArrayList<Contact> contacts) {
        int size = contacts.size();
        String[] result = new String[size];
        for (int i = 0; i < size; i++) {
            result[i] = contacts.get(i).getName();
        }
        return result;
    }
}
