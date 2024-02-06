package com.ccs.romanticadventure.data;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceConfig {
    public static final String REFERENCE = "reference";

    public static final String FIRST_CHOOSE = "first_choose";
    public static final String VOLUME_LEVEL = "volume_level";

    public static final String IVAN = "ivan";
    public static final String KATYA = "katya";
    public static final String ANTONYA = "antonya";
    public static final String EVGENIY_ANATOLIEVICH = "evgeniy_anatolievich";
    public static final String IGOR = "igor";
    public static final String VADYM = "vadym";
    public static final String SONYA = "sonya";

    public static void registerPref(Context context, SharedPreferences.OnSharedPreferenceChangeListener listener) {
        SharedPreferences pref = context.getSharedPreferences(REFERENCE, Context.MODE_PRIVATE);
        pref.registerOnSharedPreferenceChangeListener(listener);
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
    public static void setVolumeLevel(Context context, float value) {
        SharedPreferences pref = context.getSharedPreferences(REFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putFloat(VOLUME_LEVEL, value);
        editor.apply();
    }

    public static float getVolumeLevel(Context context) {
        SharedPreferences pref = context.getSharedPreferences(REFERENCE, Context.MODE_PRIVATE);
        return pref.getFloat(VOLUME_LEVEL, 100.0f);
    }

    public static void setIvanValue(Context context, int value) {
        SharedPreferences pref = context.getSharedPreferences(REFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(IVAN, value);
        editor.apply();
    }

    public static int getIvanValue(Context context) {
        SharedPreferences pref = context.getSharedPreferences(REFERENCE, Context.MODE_PRIVATE);
        return pref.getInt(IVAN, 0);
    }
    public static void setKatyaValue(Context context, int value) {
        SharedPreferences pref = context.getSharedPreferences(REFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(KATYA, value);
        editor.apply();
    }

    public static int getKatyaValue(Context context) {
        SharedPreferences pref = context.getSharedPreferences(REFERENCE, Context.MODE_PRIVATE);
        return pref.getInt(KATYA, 0);
    }
    public static void setAntonyaValue(Context context, int value) {
        SharedPreferences pref = context.getSharedPreferences(REFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(ANTONYA, value);
        editor.apply();
    }

    public static int getAntonyaValue(Context context) {
        SharedPreferences pref = context.getSharedPreferences(REFERENCE, Context.MODE_PRIVATE);
        return pref.getInt(ANTONYA, 0);
    }
    public static void setEvgeniyAnatolievichValue(Context context, int value) {
        SharedPreferences pref = context.getSharedPreferences(REFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(EVGENIY_ANATOLIEVICH, value);
        editor.apply();
    }

    public static int getEvgeniyAnatolievichValue(Context context) {
        SharedPreferences pref = context.getSharedPreferences(REFERENCE, Context.MODE_PRIVATE);
        return pref.getInt(EVGENIY_ANATOLIEVICH, 0);
    }
    public static void setIgorValue(Context context, int value) {
        SharedPreferences pref = context.getSharedPreferences(REFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(IGOR, value);
        editor.apply();
    }

    public static int getIgorValue(Context context) {
        SharedPreferences pref = context.getSharedPreferences(REFERENCE, Context.MODE_PRIVATE);
        return pref.getInt(IGOR, 0);
    }
    public static void setVadymValue(Context context, int value) {
        SharedPreferences pref = context.getSharedPreferences(REFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(SONYA, value);
        editor.apply();
    }

    public static int getVadymValue(Context context) {
        SharedPreferences pref = context.getSharedPreferences(REFERENCE, Context.MODE_PRIVATE);
        return pref.getInt(SONYA, 0);
    }

}
