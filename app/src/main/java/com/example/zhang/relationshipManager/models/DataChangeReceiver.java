package com.example.zhang.relationshipManager.models;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.example.zhang.relationshipManager.R;

/**
 * Created by 10040 on 2017/6/12.
 */

public class DataChangeReceiver extends BroadcastReceiver {
    private Refreshable mRefresher;

    public DataChangeReceiver(Context context,Refreshable refresher){
        mRefresher=refresher;
        IntentFilter intentFilter=new IntentFilter("DataChanged");
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
}
