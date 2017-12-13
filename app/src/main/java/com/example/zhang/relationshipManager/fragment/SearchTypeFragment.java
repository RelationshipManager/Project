package com.example.zhang.relationshipManager.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.zhang.relationshipManager.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by zhang on 2017-12-04.
 */

public class SearchTypeFragment extends Fragment {
    @BindView(R.id.spinner_contact)
    Spinner spinnerContact;
    @BindView(R.id.spinner_rsType)
    Spinner spinnerRsType;
    Unbinder unbinder;

    public SearchTypeFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void setAdapter(){
        // spinnerContact's adapter
        ArrayAdapter<String> contactAdapter = new ArrayAdapter<String>(this.getContext(), R.layout.spinner_contact_item, R.id.spinner_contact_item);
        contactAdapter.addAll(new String[]{"a", "b"});
        contactAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_contact);
        spinnerContact.setAdapter(contactAdapter);

        // spinnerRsType's adapter
        ArrayAdapter<String> rsTypeAdapter = new ArrayAdapter<String>(this.getContext(), R.layout.spinner_contact_item, R.id.spinner_contact_item);
        rsTypeAdapter.addAll(new String[]{"a", "b"});
        rsTypeAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_contact);
        spinnerRsType.setAdapter(rsTypeAdapter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment_type, container, false);
        unbinder = ButterKnife.bind(this, view);

        setAdapter();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
