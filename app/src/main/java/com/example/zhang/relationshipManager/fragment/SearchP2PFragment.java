package com.example.zhang.relationshipManager.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

public class SearchP2PFragment extends Fragment {

    @BindView(R.id.spinner_contact_from)
    Spinner spinnerContactFrom;
    @BindView(R.id.spinner_contact_to)
    Spinner spinnerContactTo;
    Unbinder unbinder;

    public SearchP2PFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment_p2p, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    class ContactArrayAdapter extends ArrayAdapter<String>{

        public ContactArrayAdapter(@NonNull Context context, int resource) {
            super(context, resource);
        }
    }
}
