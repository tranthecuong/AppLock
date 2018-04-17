package com.example.nhomcuong.applockv3.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.nhomcuong.applockv3.services.MyServices;


public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Intent monitor = new Intent(context, MyServices.class);
                context.startService(monitor);
            }
        }).start();


    }
}
