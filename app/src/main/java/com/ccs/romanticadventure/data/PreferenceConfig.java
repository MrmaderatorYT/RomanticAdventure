package com.ccs.romanticadventure.data;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceConfig {
    public static final String REFERENCE = "reference";
    public static final String IP = "ip";

    public static final String FIRST_CHOOSE = "first_choose";

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
    public static void setFirstChoose(Context context, int value) {
        SharedPreferences pref = context.getSharedPreferences(REFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(FIRST_CHOOSE, value);
        editor.apply();
    }

    public static int getFirstChoose(Context context) {
        SharedPreferences pref = context.getSharedPreferences(REFERENCE, Context.MODE_PRIVATE);
        return pref.getInt(FIRST_CHOOSE, 0);
    }

}
