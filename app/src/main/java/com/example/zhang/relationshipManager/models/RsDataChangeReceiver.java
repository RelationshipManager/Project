package com.example.zhang.relationshipManager.models;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;


public class RsDataChangeReceiver extends BroadcastReceiver {
    private RsDataChangeReceiver.Refreshable mRefresher;
    private Context mContext;

    public final static String INTENTFILTER = "RsDataChanged";

    public RsDataChangeReceiver(Context context, RsDataChangeReceiver.Refreshable refresher){
        mRefresher=refresher;
        IntentFilter intentFilter=new IntentFilter(INTENTFILTER);
        mContext=context;
        context.registerReceiver(this,intentFilter);
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        if(mRefresher!=null)
            mRefresher.refresh();
    }

    public interface Refreshable{
        void refresh();
    }

    public void unRegister(){
        mContext.unregisterReceiver(this);
    }
}
