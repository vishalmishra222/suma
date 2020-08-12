package com.app.dusmile.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.app.dusmile.service.SubmitOfflineJobHttpService;

public class CheckConnectivityReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if(isInternetAvailable(context))
        {
            // Toast.makeText(context,"Fired!!!!!!",Toast.LENGTH_LONG).show();
            Intent serviceIntent = new Intent(context, SubmitOfflineJobHttpService.class);
            context.startService(serviceIntent);
        }

    }

    private boolean isInternetAvailable(Context context)
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isNetworkAvailable = false;
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
                isNetworkAvailable = true;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to the mobile provider's data plan
                isNetworkAvailable = true;
            }
        }
        return isNetworkAvailable;
    }
}