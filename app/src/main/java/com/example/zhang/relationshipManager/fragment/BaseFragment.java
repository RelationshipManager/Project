package com.example.zhang.relationshipManager.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zhang.relationshipManager.models.ContactDataChangeReceiver;

import butterknife.ButterKnife;
import butterknife.Unbinder;


public abstract class BaseFragment extends Fragment {
    //ButterKnife 解绑
    Unbinder mUnbinder;
    //获取资源id
    protected abstract int getResourceId();
    //初始化控件
    protected abstract void initViews();

    protected View mView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(getResourceId(),container, false);
        mUnbinder = ButterKnife.bind(this, mView);
        initViews();
        return mView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
