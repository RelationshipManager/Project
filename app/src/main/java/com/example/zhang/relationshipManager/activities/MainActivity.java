package com.example.zhang.relationshipManager.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.zhang.relationshipManager.TestActivities.TestMainActivity;
import com.example.zhang.relationshipManager.fragment.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.example.zhang.relationshipManager.R;

public class MainActivity extends BaseActivity {
    @BindView(R.id.navigation)
    BottomNavigationView bottomNavigationView;
    @BindView(R.id.main_pager)
    ViewPager viewPager;
    private List<Fragment> fragmentList;

    public static void startActivity(Context context){
        Intent intent=new Intent(context,MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        fragmentList = new ArrayList<>();
        fragmentList.add(new ShowContactFragment());
        fragmentList.add(new ShowRelationMapFragemnt());
        fragmentList.add(new ShowSettingsFragment());

        viewPager.setAdapter(new MyFragmentAdapter(getSupportFragmentManager(), fragmentList));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int itemID = 0;
                switch (position){
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
                bottomNavigationView.setSelectedItemId(itemID);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
                viewPager.setCurrentItem(itemID,true);
                return true;
            }
        });

    }

    public static class MyFragmentAdapter extends FragmentStatePagerAdapter {
        private List<Fragment> mFragmentList;

        public MyFragmentAdapter(FragmentManager fm, List<Fragment> fragmentList) {
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
