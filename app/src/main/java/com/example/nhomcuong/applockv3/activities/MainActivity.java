package com.example.nhomcuong.applockv3.activities;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.nhomcuong.applockv3.R;
import com.example.nhomcuong.applockv3.adapter.ItemAdapter;
import com.example.nhomcuong.applockv3.model.DataUse;
import com.example.nhomcuong.applockv3.model.Item;
import com.example.nhomcuong.applockv3.model.TransferData;
import com.example.nhomcuong.applockv3.services.MyServices;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, TransferData {
    private static final int REQ_USAGE_STATUS = 11;
   // private static LinearLayout noResults;
    ListView lv;
    ItemAdapter adapter;
    DataUse dataUse;
    private ArrayList<Item> apps;
// Lay ve ngay hoac dem
    public static int getDayOrNight() {
        int actualHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

        if (actualHour >= 8 && actualHour < 19) {
            return 1;
        } else {
            return 0;
        }
    }

// check 1 service co dang chay khong
    public static boolean isMyServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        dataUse = new DataUse(getApplicationContext());
        //Log.e("CUONG", "onCREAT MAIN");
       // Log.e("CUONG", "Co Phai la appLock khong" + dataUse.getisAppLock());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
// kiem tra ngay dem roi set cho header
        int ivHeader;
        if (getDayOrNight() == 1) {
            ivHeader = R.drawable.header_day;
        } else {
            ivHeader = R.drawable.header_night;
        }
        header.setBackgroundResource(ivHeader);

        //code app V1


        lv = (ListView) findViewById(R.id.lv);
        // loadapp();

        prepareArrayLists();

        adapter = new ItemAdapter(MainActivity.this, R.layout.item_layout, apps, this);
        lv.setAdapter(adapter);


        //start service

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            requestUsageStatsPermission();

        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Intent monitor = new Intent(MainActivity.this, MyServices.class);
                    startService(monitor);
                }
            }).start();

        }
    }

    private void requestUsageStatsPermission() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                && !hasUsageStatsPermission(this)) {
            tryAllowAccessToStatsManager();
        } else {
            //kiem tra service con chay khong
            if (!isMyServiceRunning(MyServices.class, getApplicationContext())) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Intent monitor = new Intent(MainActivity.this, MyServices.class);
                        startService(monitor);
                    }
                }).start();
                // security();
            } else
                Log.e("CUONG", "service van chay");
        }
    }

    private void tryAllowAccessToStatsManager() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AppTheme));
        alertDialogBuilder.setTitle("AppLock");
        alertDialogBuilder
                .setMessage("Để sử dụng được ứng dụng cần cấp quyền truy cập")
                .setCancelable(false)
                .setPositiveButton("cho phép", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        //conf.setIsZakram(true);
                        dataUse.setIsAppLock("true");
                        dialog.dismiss();
                        startActivityForResult(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS), REQ_USAGE_STATUS);

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Intent monitor = new Intent(MainActivity.this, MyServices.class);
                                startService(monitor);
                            }
                        }).start();
                    }
                })
                .setNegativeButton("Để lần sau", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        MainActivity.this.finish();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    boolean hasUsageStatsPermission(Context context) {
        AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow("android:get_usage_stats",
                android.os.Process.myUid(), context.getPackageName());
        boolean granted = mode == AppOpsManager.MODE_ALLOWED;
        return granted;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sort_az) {
            Collections.sort(apps, new Comparator<Item>() {
                @Override
                public int compare(Item strA, Item strB) {
                    return strA.getLabel().compareToIgnoreCase(strB.getLabel());
                }
            });
            adapter.notifyDataSetChanged();
            // Toast.makeText(MainActivity.this,"hahaha",Toast.LENGTH_SHORT).show();
        }
        if (id == R.id.action_sort_za) {
            Collections.sort(apps, new Comparator<Item>() {
                public int compare(Item strB, Item strA) {
                    return strA.getLabel().compareToIgnoreCase(strB.getLabel());
                }
            });
            adapter.notifyDataSetChanged();
        }
        if (id == R.id.action_sort_by_lock) {
            ArrayList<String> listAppLock = dataUse.getdsApplock();
            ArrayList<Item> temp = new ArrayList<Item>();
            //lay ra cac item khoa
            for (Item i : apps) {
                for (String s : listAppLock) {
                    if (i.getPacketName().equals(s)) {
                        temp.add(i);
                    }

                }
            }

            //lay ra cac item con lai
            for (Item i : apps) {
                int count = 0;
                for (String s : listAppLock) {
                    if (i.getPacketName().equals(s))
                        count++;
                }
                if (count == 0)
                    temp.add(i);
            }
            apps = temp;
            adapter = new ItemAdapter(MainActivity.this, R.layout.item_layout, apps, this);
            lv.setAdapter(adapter);
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.drawer_item_about) {
            dataUse.setIsAppLock("true");
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
        } else if (id == R.id.drawer_item_home) {
            dataUse.setIsAppLock("true");
            //do some thing

        } else if (id == R.id.drawer_item_settings) {
            dataUse.setIsAppLock("true");
            Intent intent = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(intent);

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void prepareArrayLists() {

        ArrayList<String> listAppLock = dataUse.getdsApplock();


        apps = new ArrayList<>();

        PackageManager pm = this.getPackageManager();

        Intent i = new Intent(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_LAUNCHER);

        ArrayList<ResolveInfo> mList = (ArrayList<ResolveInfo>) pm
                .queryIntentActivities(i, PackageManager.GET_META_DATA);

        for (ResolveInfo resInfo : mList) {
            if (!resInfo.activityInfo.applicationInfo.packageName.equals("com.example.nhomcuong.applockv3")) {
                boolean check = checkWhatAppLock(resInfo.activityInfo.applicationInfo.packageName, listAppLock);
                //add application item to list
                AddObjectToList(resInfo.activityInfo.applicationInfo.loadIcon(pm),
                        resInfo.activityInfo.applicationInfo.loadLabel(pm)
                                .toString(),
                        resInfo.activityInfo.applicationInfo.packageName, check);
            }
        }
    }

    private void AddObjectToList(Drawable image, String title,
                                 String packageName, boolean ischeck) {
        Item m = new Item();
        m.setLabel(title);
        m.setPacketName(packageName);
        m.setIcon(image);
        m.setIschecked(ischeck);

        apps.add(m);
    }

    private boolean checkWhatAppLock(String app, ArrayList<String> arr) {
        int i;
        boolean lock = false;
        int size = arr.size();
        for (i = 0; i < size; i++)
            if (app.equals(arr.get(i))) {
                lock = true;
                return lock;

            }
        return lock;

    }

    @Override
    public void tinhtoan(int position, String packet, boolean ischeck, String label) {

        //Lay ve danh sach app bi khoa
        ArrayList<String> listAppLock = dataUse.getdsApplock();
        boolean daco = checkWhatAppLock(packet, listAppLock);

        if (ischeck == true) {
            if (daco == false) {
                dataUse.addPacketLock(packet);
                apps.get(position).setIschecked(true);

                final Toast toast = Toast.makeText(this, label + " đã khóa", Toast.LENGTH_SHORT);
                toast.show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toast.cancel();
                    }
                }, 400);
            }
        } else {
            if (daco == true) {
                int index = indexInList(packet, listAppLock);
                dataUse.removeAppLock(index);
                apps.get(position).setIschecked(false);
                final Toast toast = Toast.makeText(this, label + " đã mở khóa", Toast.LENGTH_SHORT);
                toast.show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toast.cancel();
                    }
                }, 400);
            }
        }
        adapter.notifyDataSetChanged();
    }

    public int indexInList(String app, ArrayList<String> arr) {
        int i;
        int lock = -1;
        int size = arr.size();
        for (i = 0; i < size; i++)
            if (app.equals(arr.get(i))) {
                lock = i;
                return i;

            }
        return i;
    }

    @Override
    protected void onResume() {

      //  Log.e("CUONG", "onResume MAIN");
        if (!isMyServiceRunning(MyServices.class, getApplicationContext())) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Intent monitor = new Intent(MainActivity.this, MyServices.class);
                    startService(monitor);
                }
            }).start();
            // security();

        }
        dataUse.setIsAppLock("false");
        super.onResume();
    }

    @Override
    protected void onPause() {
       // Log.e("CUONG", "onPAUSE MAIN");
       // Log.e("CUONG", "Co Phai la appLock khong" + dataUse.getisAppLock());
        if (dataUse.getisAppLock().equals("false")) {
          //  Log.e("CUONG", "finish o onPause ");

            finish();
        }


        super.onPause();
    }

    @Override
    protected void onStop() {
       // Log.e("CUONG", "onStop MAIN");

        if (!isMyServiceRunning(MyServices.class, getApplicationContext())) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Intent monitor = new Intent(MainActivity.this, MyServices.class);
                    startService(monitor);
                }
            }).start();
            // security();
        }
      //  Log.e("CUONG", "Co Phai la appLock khong" + dataUse.getisAppLock());
        if (dataUse.getisAppLock().equals("false")) {
           // Log.e("CUONG", "finish o onStop ");

            finish();
        }
        super.onStop();

    }

    @Override
    protected void onDestroy() {

       // Log.e("CUONG", "onDestroy");
        //Log.e("CUONG", "Co Phai la appLock khong" + dataUse.getisAppLock());
        if (dataUse.getisAppLock().equals("false")) {
           // Log.e("CUONG", "finish o onDestroy ");

            finish();
        }
        super.onDestroy();
    }
}

