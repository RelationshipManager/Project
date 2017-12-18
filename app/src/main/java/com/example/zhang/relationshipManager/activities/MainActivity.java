package com.example.zhang.relationshipManager.activities;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.example.zhang.relationshipManager.R;
import com.example.zhang.relationshipManager.fragment.AddContactDialog;
import com.example.zhang.relationshipManager.fragment.ContactListFragment;
import com.example.zhang.relationshipManager.fragment.MyRsFragment;
import com.example.zhang.relationshipManager.fragment.RsSearchFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.navigation)
    BottomNavigationView mBottomNavigationView;
    @BindView(R.id.main_pager)
    ViewPager mViewPager;
    private List<Fragment> mFragmentList;
    //当前页编号
    private int mNowFragmentPosition;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected int getResourceId() {
        return R.layout.activity_main;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_bar, menu);
        return true;
    }

    @Override
    protected void initViews() {
        mFragmentList = new ArrayList<>();
        mFragmentList.add(new ContactListFragment());
        mFragmentList.add(new RsSearchFragment());
        mFragmentList.add(new MyRsFragment());

        //初始化toolbar
        setSupportActionBar(mToolbar);
//        mToolbar.inflateMenu(R.menu.app_bar);
        mToolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.add:
                    switch (mNowFragmentPosition) {
                        case 0:
                            AddContactDialog.startDialog(MainActivity.this);
                            break;
                        case 1:
                            break;
                        case 2:
                            break;
                    }
                    break;
            }
            return true;
        });
        mNowFragmentPosition = 0;

        mViewPager.setAdapter(new MyFragmentAdapter(getSupportFragmentManager(), mFragmentList));
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int itemID = 0;
                switch (position) {
                    case 0:
                        itemID = R.id.contact_list;
                        break;
                    case 1:
                        itemID = R.id.relationship_map;
                        break;
                    case 2:
                        itemID = R.id.settings;
                        break;
                    default:
                        break;
                }
                mBottomNavigationView.setSelectedItemId(itemID);
                mNowFragmentPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mBottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemID = 0;
            switch (item.getItemId()) {
                case R.id.contact_list:
                    itemID = 0;
                    break;
                case R.id.relationship_map:
                    itemID = 1;
                    break;
                case R.id.settings:
                    itemID = 2;
                    break;
                default:
                    break;
            }
            // @todo 设置成 false 可以取消滑动效果
            mViewPager.setCurrentItem(itemID, true);
            mNowFragmentPosition = itemID;
            return true;
        });
    }

    private static class MyFragmentAdapter extends FragmentStatePagerAdapter {
        private List<Fragment> mFragmentList;

        MyFragmentAdapter(FragmentManager fm, List<Fragment> fragmentList) {
            super(fm);
            mFragmentList = fragmentList;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }
    }
}
