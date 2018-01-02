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
import com.example.zhang.relationshipManager.models.Neo4jManager;
import com.example.zhang.relationshipManager.models.Relationship;
import com.example.zhang.relationshipManager.models.RsType;

import java.util.ArrayList;

import butterknife.BindView;


public class SearchTypeFragment extends BaseFragment {
    @BindView(R.id.spinner_contact)
    Spinner spinnerContact;
    @BindView(R.id.spinner_rsType)
    Spinner spinnerRsType;
    @BindView(R.id.searchType_button)
    Button searchButton;

    ArrayList<Contact> contactArrayList;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        contactDataChangeReceiver.unRegister();
    }

    ContactDataChangeReceiver contactDataChangeReceiver;

    ArrayAdapter<String> contactAdapter, rsTypeAdapter;


    int rsType = -1;
    Contact contact = new Contact();

    public SearchTypeFragment() {
    }

    private void setAdapterData() {
        contactArrayList = ContactManager.getInstance(null).getAllContacts();
        contactAdapter.clear();
        contactAdapter.addAll(parseAllContact(contactArrayList));
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
                contact = contactArrayList.get(position);
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
                switch (rsTypeAdapter.getItem(position)){
                    case "朋友":
                        rsType = RsType.FRIENDS;
                        break;
                    case "同事":
                        rsType = RsType.COLLEAGUES;
                        break;
                }
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
                if (contact.getId() == Contact.DEFAULT_ID){
                    ToastHelper.show(getContext(),"请选择联系人！");
                    return;
                }else if (rsType == -1){
                    ToastHelper.show(getContext(),"请选择关系类型！");
                    return;
                }
                // @todo Search, need data as part of url
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String result = null;
                        try {
                            result = Neo4jManager.getInstance(getContext()).searchRsByType(contact, rsType);
                        }catch (Exception e){
                            e.printStackTrace();
                            ToastHelper.show(getContext(),"暂时无法查询");
                        }
                        String url = "http://192.168.1.118/my-app/neo4j.html?info=" + result;
                        ShowRsInSVGActivity.startActivity(getContext(), url);
                    }
                }).start();
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
