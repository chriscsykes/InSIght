package edu.dartmouth.cs.finalproject.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Preference {

    private SharedPreferences sharedPreferences;

    private static final String LOGIN_STATUS_KEY = "login_status";


    public Preference(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    // getters and setters
    public boolean getLoginStatus() {
        return sharedPreferences.getBoolean(LOGIN_STATUS_KEY, false);
    }

    public void setLoginStatus(boolean loginStatus) {
        sharedPreferences.edit().putBoolean(LOGIN_STATUS_KEY, loginStatus).apply();
    }
}
