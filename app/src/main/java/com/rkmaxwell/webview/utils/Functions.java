package com.rkmaxwell.webview.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.rkmaxwell.webview.interfaces.InternetCheckCallback;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class Functions {
    public static void RegisterConnectivity(Context context, final InternetCheckCallback callback) {

        if(context!=null) {
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (isConnectedToInternet(context)) {
                        callback.GetResponse("alert", "connected");
                    } else {
                        callback.GetResponse("alert", "disconnected");
                    }
                }

            };

            context.registerReceiver(broadcastReceiver, intentFilter);
        }
    }

    public static Boolean isConnectedToInternet(Context context) {
        try {

            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
         //   Log.e(Constants.tag, "Exception : "+e.getMessage());
            return false;
        }
    }
    public static BroadcastReceiver broadcastReceiver;
    public static IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");

    public static void unRegisterConnectivity(Context mContext) {
        try {
            if (broadcastReceiver != null)
                mContext.unregisterReceiver(broadcastReceiver);

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
}
