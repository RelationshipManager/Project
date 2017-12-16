package com.example.zhang.relationshipManager.fragment;

import android.widget.EditText;

import com.example.zhang.relationshipManager.R;

import butterknife.BindView;


public class ContactInfoFragment extends BaseFragment {
    @BindView(R.id.contact_name)
    EditText mEtName;

    @Override
    protected int getResourceId() {
        return R.layout.fragment_contactinfo;
    }

    @Override
    protected void initViews() {

    }
}
