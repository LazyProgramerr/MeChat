package com.sai.mechat.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.sai.mechat.constants.SharedPreferenceManagerConstants;

import com.sai.mechat.models.dateAndTime;

public class SharedPreferenceManager {
    public static dateAndTime getLastLoginTime(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPreferenceManagerConstants.SHARED_PREFERENCE_LAST_LOGIN_TIME, Context.MODE_PRIVATE);
        long dateAndTime = sharedPreferences.getLong("lastLoginTime",0);
        return new dateAndTime(dateAndTime);
    }
    public static void saveLastLoginTime(Context context,long time){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPreferenceManagerConstants.SHARED_PREFERENCE_LAST_LOGIN_TIME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("lastLoginTime",time);
        editor.apply();
    }

}
