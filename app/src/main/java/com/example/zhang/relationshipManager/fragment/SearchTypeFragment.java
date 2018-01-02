package com.example.zhang.relationshipManager.fragment;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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
                @SuppressLint("StaticFieldLeak") AsyncTask task = new AsyncTask() {
                    @Override
                    protected Object doInBackground(Object[] objects) {
                        String result = null;
                        try {
                            result = Neo4jManager.getInstance(getContext()).searchRsByType(contact, rsType);
                            JSONObject check = (JSONObject) new JSONObject(result).getJSONArray("results").get(0);
                            if (check.getJSONArray("data").length() == 0)
                                return 0;
                        } catch (IOException e) {
                            e.printStackTrace();
                            return 1;
                        } catch (JSONException e) {
                            e.printStackTrace();
                            return 0;
                        }
                        return 1;
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        int result = (Integer) o;
                        switch (result) {
                            case 0:
                                ToastHelper.show(getContext(), "没有数据需要显示！");
                                break;
                            case 1:
                                ToastHelper.show(getContext(), "暂时无法查询");
                                break;
                            case 2:
                                String url = "http://10.0.2.2/my-app/neo4j.html?info=" + result;
                                ShowRsInSVGActivity.startActivity(getContext(), url);
                                break;
                        }
                    }
                };
                task.execute();
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
