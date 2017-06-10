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
import android.view.MenuItem;

import com.example.zhang.relationshipManager.TestActivities.TestMainActivity;
import com.example.zhang.relationshipManager.fragment.*;

import java.util.ArrayList;
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

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemID;
                switch (String.valueOf(item.getTitle())) {
                    case "联系人":
                        itemID = 0;
                        break;
                    case "关系图谱":
                        itemID = 1;
                        break;
                    case "设置":
                        itemID = 2;
                        break;
                    default:
                        break;
                }
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
