package com.example.zhang.relationshipManager.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zhang.relationshipManager.R;

import java.util.ArrayList;
import java.util.List;

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
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new SearchP2PFragment());
        fragmentList.add(new SearchTypeFragment());
        viewPager.setAdapter(new MyFragmentStatePagerAdapter(getFragmentManager(),fragmentList));
        tabLayout = (TabLayout)view.findViewById(R.id.search_tabLayout);
        // Use setupWithViewPager make the origin TabLayout reset tab with ViewPager pageTitle
        // So set the pageTitle in PagerAdapter
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }

    public class MyFragmentStatePagerAdapter extends FragmentStatePagerAdapter{
        private List<Fragment> fragmentList;

        public MyFragmentStatePagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
            super(fm);
            this.fragmentList = fragmentList;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            CharSequence title = "";
            switch (position){
                case 0:
                    title = getText(R.string.RsSearch_p2p);
                    break;
                case 1:
                    title = getText(R.string.RsSearch_rstype);
                    break;
            }
            return title;
        }
    }
}
