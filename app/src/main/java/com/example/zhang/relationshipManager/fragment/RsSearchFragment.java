package com.example.zhang.relationshipManager.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zhang.relationshipManager.R;

public class RsSearchFragment extends Fragment {

    private ViewPager viewPager;
    private TabLayout tabLayout;

    public static RsSearchFragment newInstance() {
        return new RsSearchFragment();
    }

    public RsSearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_search, container, false);
        viewPager = (ViewPager)view.findViewById(R.id.search_viewPager);
        tabLayout = (TabLayout)view.findViewById(R.id.search_tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }
}
