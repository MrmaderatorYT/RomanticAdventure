package com.ccs.romanticadventure.data;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceConfig {
    public static final String REFERENCE = "reference";
    public static final String IP = "ip";

    public static void registerPref(Context context, SharedPreferences.OnSharedPreferenceChangeListener listener) {
        SharedPreferences pref = context.getSharedPreferences(REFERENCE, Context.MODE_PRIVATE);
        pref.registerOnSharedPreferenceChangeListener(listener);
    }
    public static void saveIP(Context context, String ip) {
        SharedPreferences pref = context.getSharedPreferences(REFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(IP, ip);
        editor.apply();
    }

    public static String getIP(Context context) {
        SharedPreferences pref = context.getSharedPreferences(REFERENCE, Context.MODE_PRIVATE);
        return pref.getString(IP, "");
    }
}
