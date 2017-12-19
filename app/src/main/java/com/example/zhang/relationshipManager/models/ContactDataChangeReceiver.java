package com.example.zhang.relationshipManager.models;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Created by zhang on 2017-12-19.
 */

public class ContactDataChangeReceiver extends BroadcastReceiver {
    private Refreshable mRefresher;
    private Context mContext;

    public final static String INTENTFILTER = "DataChanged";

    public ContactDataChangeReceiver(Context context, Refreshable refresher){
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
