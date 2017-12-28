package com.example.zhang.relationshipManager.fragment;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.zhang.relationshipManager.Helper.ToastHelper;
import com.example.zhang.relationshipManager.R;
import com.example.zhang.relationshipManager.activities.ShowRsInSVGActivity;
import com.example.zhang.relationshipManager.models.Contact;
import com.example.zhang.relationshipManager.models.ContactDataChangeReceiver;
import com.example.zhang.relationshipManager.models.ContactManager;

import java.util.ArrayList;

import butterknife.BindView;


public class SearchTypeFragment extends BaseFragment {
    @BindView(R.id.spinner_contact)
    Spinner spinnerContact;
    @BindView(R.id.spinner_rsType)
    Spinner spinnerRsType;
    @BindView(R.id.searchType_button)
    Button searchButton;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        contactDataChangeReceiver.unRegister();
    }

    ContactDataChangeReceiver contactDataChangeReceiver;

    ArrayAdapter<String> contactAdapter, rsTypeAdapter;


    String contact = null, rsType = null;

    public SearchTypeFragment() {
    }

    private void setAdapterData() {
        contactAdapter.clear();
        contactAdapter.addAll(parseAllContact(ContactManager.getInstance(getContext()).getAllContacts()));
    }

    @Override
    protected int getResourceId() {
        return R.layout.search_fragment_type;
    }

    @Override
    protected void initViews() {
        // spinnerContact's adapter
        contactAdapter = new ArrayAdapter<String>(this.getContext(), R.layout.spinner_contact_item, R.id.spinner_contact_item);
        contactAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_contact);
        spinnerContact.setAdapter(contactAdapter);
        spinnerContact.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                contact = contactAdapter.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // spinnerRsType's adapter
        rsTypeAdapter = new ArrayAdapter<String>(this.getContext(), R.layout.spinner_contact_item, R.id.spinner_contact_item);
        rsTypeAdapter.addAll("朋友", "同事");
        rsTypeAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_contact);
        spinnerRsType.setAdapter(rsTypeAdapter);
        spinnerRsType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                rsType = rsTypeAdapter.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        setAdapterData();
        contactDataChangeReceiver = new ContactDataChangeReceiver(getContext(), new ContactDataChangeReceiver.Refreshable() {
            @Override
            public void refresh() {
                setAdapterData();
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (contact == null){
                    ToastHelper.show(getContext(),"请选择联系人！");
                    return;
                }else if (rsType == null){
                    ToastHelper.show(getContext(),"请选择关系类型！");
                    return;
                }
                // @todo Search, need data as part of url

                String url = "www.baidu.com";
                ShowRsInSVGActivity.startActivity(getContext(), url);
            }
        });
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
