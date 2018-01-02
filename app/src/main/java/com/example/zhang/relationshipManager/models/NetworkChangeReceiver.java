package com.example.zhang.relationshipManager.models;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;


public class NetworkChangeReceiver extends BroadcastReceiver {
    private Context mContext;

    public NetworkChangeReceiver(Context context){
        mContext=context;
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        filter.addAction("android.net.wifi.STATE_CHANGE");
        context.registerReceiver(this,filter);
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        Neo4jManager.getInstance(context).synchronize();
    }


    public void unRegister(){
        mContext.unregisterReceiver(this);
    }
}
