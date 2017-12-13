package com.example.zhang.relationshipManager.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zhang.relationshipManager.R;

/**
 * Created by 29110 on 2017/12/13.
 */

public class ContactRsFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_contactrs,container);
        return view;
    }
}
