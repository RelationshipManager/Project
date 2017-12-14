package com.example.zhang.relationshipManager.fragment;

import android.support.v4.app.Fragment;


public class MyRsByTypeFragment extends Fragment {

    //类别
    private int mType;

    static public MyRsByTypeFragment getInstance(int type) {
        MyRsByTypeFragment myRsByTypeFragment = new MyRsByTypeFragment();
        myRsByTypeFragment.setType(type);
        return myRsByTypeFragment;
    }

    public MyRsByTypeFragment() {
        setType(0);
    }

    private void setType(int type) {
        mType = type;
    }
}
