package com.example.zhang.relationshipManager.fragment;

import android.annotation.SuppressLint;
import android.content.AbstractThreadedSyncAdapter;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;


public class SearchP2PFragment extends BaseFragment {

    @BindView(R.id.spinner_contact_from)
    Spinner spinnerContactFrom;
    @BindView(R.id.spinner_contact_to)
    Spinner spinnerContactTo;
    @BindView(R.id.searchP2P_button)
    Button searchButton;

    ArrayAdapter<String> contactToAdapter, contactFromAdapter;
    ContactDataChangeReceiver contactDataChangeReceiver;

    ArrayList<Contact> contactArrayList;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        contactDataChangeReceiver.unRegister();
    }

    Contact contactFrom = new Contact(), contactTo = new Contact();

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
        spinnerContactTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                contactTo = contactArrayList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        contactFromAdapter = new ArrayAdapter<String>(this.getContext(), R.layout.spinner_contact_item, R.id.spinner_contact_item);
        contactFromAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_contact);
        spinnerContactFrom.setAdapter(contactFromAdapter);
        spinnerContactFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                contactFrom = contactArrayList.get(position);
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
                if (contactTo.getId() == Contact.DEFAULT_ID || contactFrom.getId() == Contact.DEFAULT_ID) {
                    ToastHelper.show(getContext(), "请选择联系人！");
                    return;
                }
                // @todo Search, need data as part of url
                @SuppressLint("StaticFieldLeak") AsyncTask task = new AsyncTask() {
                    @Override
                    protected Object doInBackground(Object[] objects) {
                        String result = null;
                        try {
                            result = Neo4jManager.getInstance(getContext()).searchRsP2P(contactTo, contactFrom);
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

    private void setAdapterData() {
        contactArrayList = ContactManager.getInstance(null).getAllContacts();
        // spinnerCantactTo's adapter
        contactToAdapter.clear();
        contactToAdapter.addAll(parseAllContact(contactArrayList));

        // spinnerCantactFrom's adapter
        contactFromAdapter.clear();
        contactFromAdapter.addAll(parseAllContact(contactArrayList));
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
