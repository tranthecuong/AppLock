package com.example.nhomcuong.applockv3.services;

/**
 * Created by cuong on 10/11/2017.
 */

import android.app.ActivityManager;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.nhomcuong.applockv3.model.DataUse;
import com.example.nhomcuong.applockv3.password.EnterPassActivity;
import com.example.nhomcuong.applockv3.password.EnterPassPatternActivity;
import com.example.nhomcuong.applockv3.password.FingerPassActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class MyServices extends Service {
    Context mContext;
    DataUse dataUse;
    private Thread dThread;
    private Runnable checkDataRunnable = new Runnable() {

        @Override
        public void run() {
            //dataUse.setChoPhep("");
            //String choPhep="";
            while (true) {
                try {

                    ActivityManager am = (ActivityManager) getBaseContext().getSystemService(ACTIVITY_SERVICE);
                    PackageManager pm = getBaseContext().getPackageManager();
                    String mPackageName = null;

                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        UsageStatsManager mUsageStatsManager = (UsageStatsManager) getSystemService("usagestats");
                        long time = System.currentTimeMillis();
                        // We get usage stats for the last 10 seconds
                        List<UsageStats> stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 10, time);
                        // Sort the stats by the last time used
                        if (stats != null) {
                            SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
                            for (UsageStats usageStats : stats) {
                                mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
                            }
                            if (!mySortedMap.isEmpty()) {
                                mPackageName = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
                            }
                        }
                    } else {
                        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                        List<ActivityManager.RunningTaskInfo> runningTasks = activityManager
                                .getRunningTasks(1);
                        ActivityManager.RunningTaskInfo runningTaskInfo = runningTasks.get(0);
                        ComponentName topActivity = runningTaskInfo.topActivity;
                        mPackageName = topActivity.getPackageName();
                    }

                    PackageInfo foregroundAppPackageInfo = null;
                    try {
                        if (mPackageName != null) {
                            foregroundAppPackageInfo = pm.getPackageInfo(
                                    mPackageName, 0);
                        }
                    } catch (PackageManager.NameNotFoundException e) {
                        // TODO Auto-generated catch block
                        System.out.println("Exception in run method " + e);
                        e.printStackTrace();
                    }
                    if (foregroundAppPackageInfo != null) {
                        //code CUONG HERE

                        String top = foregroundAppPackageInfo.packageName;
                        if (top != null) {
                            //Kiem tra top co phai la khoa khong
                            boolean checkIsLock = isPacketLock(top);

                            if ((checkIsLock == true) && (!top.contains("com.example.nhomcuong.applockv3"))) {
                                runLock(top);
                            } else {
                                if ((dataUse.getChoPhep() != null) && (dataUse.getChoPhep().equals(top) == false) && (!top.contains("com.example.nhomcuong.applockv3")))
                                    dataUse.setChoPhep(null);

                            }
                        }
                    } else {
                       // Log.e("CUONG", "foregroundAppPackageInfo null");
                    }
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    // good practice
                    dThread.interrupt();
                    e.printStackTrace();
                    return;
                }
            }

        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        dataUse = new DataUse(getApplicationContext());
        mContext = getApplicationContext();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        dThread = new Thread(checkDataRunnable);
        dThread.start();
        flags = Service.START_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }

    public synchronized void runLock(String packetName) {

        if ((dataUse.getChoPhep() != null) && (dataUse.getChoPhep().equals(packetName)))
            return;
        // dataUse.setChoPhep(packetName);

        //dataUse.setIsAppLock(false);


        Intent i = null;
        String lockmode = dataUse.getLockMode();

        if (lockmode.equals("enterstringpass")) {

            i = new Intent(mContext, EnterPassActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("packet", packetName);
            mContext.startActivity(i);
        } else if (lockmode.equals("enterpatternpass")) {
            //chuyen sang pattern
            i = new Intent(mContext, EnterPassPatternActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("packet", packetName);
            mContext.startActivity(i);

        }
        else
        if(lockmode.equals("enterfingerpass")){
            //chuyen sang pass int
            i = new Intent(mContext, FingerPassActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("packet", packetName);
           // Log.e("CUONG", "Ben service gui di " + packetName);
            mContext.startActivity(i);

        }
    }

    public boolean isPacketLock(String packet) {
        boolean isLock = false;
        ArrayList<String> dsLock = dataUse.getdsApplock();
        int i;
        int size = dsLock.size();
        for (i = 0; i < size; i++)
            if (packet.equals(dsLock.get(i))) {
                return true;
            }

        return isLock;
    }


}


