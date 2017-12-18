package com.example.zhang.relationshipManager.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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


public class ContactInfoActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.contact_image)
    ImageView mImageView;
    @BindView(R.id.contact_viewpager)
    ViewPager viewPager;

    @BindView(R.id.contact_tabLayout)
    TabLayout tabLayout;

    private int mContactId;
    private List<Fragment> fragmentList;

    public static void startActivity(Context context, int contactId) {
        Intent intent = new Intent(context, ContactInfoActivity.class);
        intent.putExtra("contact", contactId);
        context.startActivity(intent);
    }

    @Override
    protected int getResourceId() {
        return R.layout.activity_contact_infomation;
    }

    @Override
    protected void initViews() {
        setSupportActionBar(toolbar);
        mContactId = getIntent().getIntExtra("contact", -1);
        fragmentList = new ArrayList<>();
        fragmentList.add(new ContactInfoFragment().setmContact(mContactId));
        fragmentList.add(new ContactRsFragment().setContact(ContactManager.getInstance(getApplicationContext()).getContactById(mContactId)));
        viewPager.setAdapter(new ContactInfoActivity.MyFragmentStatePagerAdapter(getSupportFragmentManager(), fragmentList));
        tabLayout.setupWithViewPager(viewPager);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.contactinfo_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit:
                ((ContactInfoFragment)fragmentList.get(0)).startEditContact();
                break;
            case R.id.delete:
                ((ContactInfoFragment)fragmentList.get(0)).deleteContact();
                finish();
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
