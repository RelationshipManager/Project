package com.example.zhang.relationshipManager.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.example.zhang.relationshipManager.R;
import com.example.zhang.relationshipManager.fragment.ContactInfoFragment;
import com.example.zhang.relationshipManager.fragment.ContactRsFragment;
import com.example.zhang.relationshipManager.models.Contact;
import com.example.zhang.relationshipManager.models.ContactManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by 29110 on 2017/12/12.
 */

public class ContactInfoActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.contact_image)
    ImageView mImageView;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Contact mContact;

    public static void startActivity(Context context, int contactId) {
        Intent intent = new Intent(context, ContactInfoActivity.class);
        intent.putExtra("contact", contactId);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_infomation);
        setSupportActionBar(toolbar);
        viewPager = (ViewPager) findViewById(R.id.contact_viewpager);
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new ContactInfoFragment());
        fragmentList.add(new ContactRsFragment());
        viewPager.setAdapter(new ContactInfoActivity.MyFragmentStatePagerAdapter(getSupportFragmentManager(), fragmentList));
        tabLayout = (TabLayout) findViewById(R.id.contact_tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        mContact = ContactManager.getInstance().getContactById(getIntent().getIntExtra("contact", -1));
    }

//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View view=inflater.inflate(R.layout.activity_contact_infomation, container, false);
//        viewPager=(ViewPager)view.findViewById(R.id.contact_viewpager);
//        List<Fragment> fragmentList = new ArrayList<>();
//        fragmentList.add(new ContactInfoFragment());
//        fragmentList.add(new ContactRsFragment());
//        viewPager.setAdapter(new ContactInfoActivity.MyFragmentStatePagerAdapter(getSupportFragmentManager(),fragmentList));
//        tabLayout=(TabLayout)view.findViewById(R.id.contact_tabLayout);
//        tabLayout.setupWithViewPager(viewPager);
//        return view;
//    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.contactinfo_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit:

                break;
            case R.id.delte:

                break;
            default:
        }
        return true;
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
                    title = getText(R.string.contact_info);
                    break;
                case 1:
                    title = getText(R.string.relationship);
                    break;
            }
            return title;
        }
    }

}
