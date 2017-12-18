package com.example.zhang.relationshipManager.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.example.zhang.relationshipManager.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class RsSearchFragment extends BaseFragment {

    @BindView(R.id.search_viewPager)
    ViewPager mViewPager;
    @BindView(R.id.search_tabLayout)
    TabLayout mTabLayout;

    public static RsSearchFragment newInstance() {
        return new RsSearchFragment();
    }

    public RsSearchFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getResourceId() {
        return R.layout.fragment_search;
    }

    @Override
    protected void initViews() {
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new SearchP2PFragment());
        fragmentList.add(new SearchTypeFragment());
        mViewPager.setAdapter(new MyFragmentStatePagerAdapter(getFragmentManager(), fragmentList));
        // Use setupWithViewPager make the origin TabLayout reset tab with ViewPager pageTitle
        // So set the pageTitle in PagerAdapter
        mTabLayout.setupWithViewPager(mViewPager);
    }

    public class MyFragmentStatePagerAdapter extends FragmentStatePagerAdapter {
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
            switch (position) {
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
