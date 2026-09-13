package com.example.mynotepad.Utils;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;


public class SpfUtils {
    private static final String SPF_NAME = "note_spf";
    public static void saveInt(Context context, String key, int value) {
        SharedPreferences spf = context.getSharedPreferences(SPF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = spf.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static int getInt(Context context, String key, int defaultValue) {
        SharedPreferences spf = context.getSharedPreferences(SPF_NAME, MODE_PRIVATE);
        int value = spf.getInt(key, defaultValue);
        return value;
    }

}
