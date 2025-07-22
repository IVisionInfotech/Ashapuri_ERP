package com.madhavsteel.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Session {

    private SharedPreferences prefs;

    public Session(Context cntx) {
        prefs = PreferenceManager.getDefaultSharedPreferences(cntx);
    }

    public Boolean getLoginStatus() {
        return prefs.getBoolean("loginStatus", false);
    }

    public void setLoginStatus(Boolean status) {
        prefs.edit().putBoolean("loginStatus", status).apply();
    }

    public String getStartActivity() {
        return prefs.getString("startActivity", "");
    }

    public void setStartActivity(String startActivity) {
        prefs.edit().putString("startActivity", startActivity).apply();
    }
}
