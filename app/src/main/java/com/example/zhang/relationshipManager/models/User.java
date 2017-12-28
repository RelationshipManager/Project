package com.example.zhang.relationshipManager.models;


import android.content.Context;
import android.content.SharedPreferences;

public class User {
    private static User sUser;
    private int mUserId;
    private SharedPreferences mSharedPreferences;

    public static User getInstance(Context context){
        if (sUser == null && context!=null)
            sUser = new User(context);
        return sUser;
    }

    private User(Context context){
        mSharedPreferences = context.getSharedPreferences("user_settings", Context.MODE_PRIVATE);
    }

    public void setUserId(int id){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt("user_id", id);
        editor.commit();
        mUserId = id;
    }

    public int getUserId() {
        return mUserId;
    }
}
