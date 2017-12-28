package com.example.zhang.relationshipManager.Helper;

import android.content.Context;
import android.widget.Toast;

public class ToastHelper {
    private static Toast toast = null;

    public static void show(Context context, CharSequence text){
        if (toast == null){
            toast = Toast.makeText(context,text,Toast.LENGTH_SHORT);
        }else{
            toast.setText(text);
        }
        toast.show();
    }

    public static void cancel() {
        if (toast != null)
            toast.cancel();
    }
}
