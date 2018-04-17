package com.example.nhomcuong.applockv3.model;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class DataUse {
    private final SharedPreferences setting;

    public DataUse(Context context) {
        setting=context.getSharedPreferences("CUONG",0);
    }

    public String getPass() {
        synchronized(setting){
            String pass=setting.getString("password","");
            return  pass;
        }
    }
    public  String getPassPattern() {
        synchronized (setting) {
            String pass=setting.getString("passwordpattern","");
            return  pass;


        }
    }
    public  String getPassInt() {
        synchronized (setting) {
            String pass=setting.getString("passwordint","");
            return  pass;


        }
    }
    public void setPass(String pass) {
        synchronized(setting){
            SharedPreferences.Editor editor=setting.edit();
            editor.putString("password",pass);
            editor.apply();
        }
    }
    public void setPassPattern(String pass) {
        synchronized(setting){
            SharedPreferences.Editor editor=setting.edit();
            editor.putString("passwordpattern",pass);
            editor.apply();
        }
    }
    public void setPassInt(String pass) {
        synchronized(setting){
            SharedPreferences.Editor editor=setting.edit();
            editor.putString("passwordint",pass);
            editor.apply();
        }
    }
    public void setLockMode(String mode) {
        synchronized(setting){
            SharedPreferences.Editor editor=setting.edit();
            editor.putString("lockmode",mode);
            editor.apply();
        }
    }
    public String getLockMode(){
        synchronized (setting){
            return setting.getString("lockmode","");
        }
    }


    public  String getChoPhep() {
        synchronized (setting) {
            return setting.getString("chophep","");

        }
    }
    public void setChoPhep(String packet){

        synchronized (setting) {
            SharedPreferences.Editor editor=setting.edit();
            editor.putString("chophep",packet);
            editor.apply();
        }
    }


    // finger
    public void setPassFinger(String pass) {
        synchronized(setting){
            SharedPreferences.Editor editor=setting.edit();
            editor.putString("passwordfinger",pass);
            editor.apply();
        }
    }
    public  String getPassFinger() {
        synchronized (setting) {
            String pass=setting.getString("passwordfinger","");
            return  pass;


        }
    }


// them ung dung vao danh sach app khoa
    public void addPacketLock(String name) {
        synchronized(setting){
            Set<String> set = new HashSet<String>();
            set = setting.getStringSet("applock", null);

            //convert hashset to Arraylist
            ArrayList<String> danhSachAppLock = new ArrayList<String>(set);
            danhSachAppLock.add(name);
            SharedPreferences.Editor editor2 = setting.edit();
            Set<String> set2 = new HashSet<String>();
            set2.addAll(danhSachAppLock);
            editor2.putStringSet("applock", set2);
            editor2.commit();

        }
    }
    // lay ra danh sach app khoa
    public ArrayList<String> getdsApplock(){
        synchronized (setting) {
            Set<String> set = new HashSet<String>();
            set = setting.getStringSet("applock", null);

            //convert hashset to Arraylist
            ArrayList<String> danhSachAppLock = new ArrayList<String>(set);
            return danhSachAppLock;
        }
    }
// xoa di app khoa
    public void removeAppLock(int chiso) {
        synchronized(setting){
            ArrayList<String> arr=getdsApplock();
            arr.remove(chiso);

            SharedPreferences.Editor editor2 = setting.edit();
            Set<String> set2 = new HashSet<String>();
            set2.addAll(arr);
            editor2.putStringSet("applock", set2);
            editor2.commit();

        }
    }

    public void setIsAppLock(String bool){
        synchronized (setting) {
            SharedPreferences.Editor editor = setting.edit();
            editor.putString("isapplock", bool);
            editor.apply();
        }

    }
    public String getisAppLock(){
        synchronized (setting){
            return setting.getString("isapplock","");}
    }



}
