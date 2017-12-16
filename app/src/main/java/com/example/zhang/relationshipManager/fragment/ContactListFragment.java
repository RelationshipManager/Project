package com.example.zhang.relationshipManager.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zhang.relationshipManager.R;
import com.example.zhang.relationshipManager.models.ContactAdapter;
import com.example.zhang.relationshipManager.models.ContactManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by 29110 on 2017/12/7.
 */

public class ContactListFragment extends Fragment {
    @BindView(R.id.contactRecyclerView)
    RecyclerView contactRecyclerView;
    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contact_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        // @todo Show recyclerView and setAdapter
        contactRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        contactRecyclerView.setAdapter(new ContactAdapter(ContactManager.getInstance(getContext()).getAllContacts()));
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
