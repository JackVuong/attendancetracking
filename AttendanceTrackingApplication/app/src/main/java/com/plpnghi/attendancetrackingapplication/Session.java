package com.plpnghi.attendancetrackingapplication;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Phuong Nghi Phan Ly on 9/25/2016.
 */
public class Session {
    SharedPreferences sharedPrefs;
    SharedPreferences.Editor editor;
    Context context;

    public Session(Context context){
        this.context = context;
        sharedPrefs = context.getSharedPreferences("myapp", Context.MODE_PRIVATE);
        editor = sharedPrefs.edit();
    }

    public void setLoggedin(boolean logggedin){
        editor.putBoolean("loggedInMode", logggedin);
        editor.commit();
    }

    public boolean loggedin(){
        return sharedPrefs.getBoolean("loggedInMode", false);
    }
}
