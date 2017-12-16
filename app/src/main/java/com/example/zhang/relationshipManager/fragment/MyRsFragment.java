package com.example.zhang.relationshipManager.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.example.zhang.relationshipManager.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class MyRsFragment extends BaseFragment {

    //Fragment列表
    private ArrayList<Fragment> mFragments;
    //ViewPager
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    //标签栏
    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragments = new ArrayList<>();
        mFragments.add(MyRsByTypeFragment.getInstance(1));
        mFragments.add(MyRsByTypeFragment.getInstance(2));

    }

    @Override
    protected int getResourceId() {
        return R.layout.fragment_my_rs;
    }

    @Override
    protected void initViews() {
        mViewPager.setAdapter(new FragmentsAdapter(getFragmentManager(), mFragments));
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private class FragmentsAdapter extends FragmentPagerAdapter {

        List<Fragment> mFragments;

        FragmentsAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            mFragments = fragments;

        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            CharSequence title = "";
            switch (position) {
                case 0:
                    title = getText(R.string.friend_rs);
                    break;
                case 1:
                    title = getText(R.string.colleague_rs);
                    break;
            }
            return title;
        }
    }
}
