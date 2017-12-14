package com.example.zhang.relationshipManager.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.zhang.relationshipManager.R;

import butterknife.BindView;

/**
 * Created by 29110 on 2017/12/13.
 */

public class ContactInfoFragment extends Fragment {
    @BindView(R.id.contact_name)
    EditText mEtName;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contactinfo, container, false);
        return view;
    }
}
