package com.app.dusmile.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class FirebasePreference {

    public static final String PREF_NAME = "FirebasePrefs";
    public static final int MODE = Context.MODE_PRIVATE;
    static String TAG="FirebasePrefs";

    public static  final String TOKEN_ID="token_id";




    public static void writeBoolean(Context context, String key, boolean value) {
        getEditor(context).putBoolean(key, value).commit();
    }

    public static boolean readBoolean(Context context, String key,
                                      boolean defValue) {
        return getPreferences(context).getBoolean(key, defValue);
    }

    public static void writeInteger(Context context, String key, int value) {
        getEditor(context).putInt(key, value).commit();
    }

    public static int readInteger(Context context, String key, int defValue) {
        return getPreferences(context).getInt(key, defValue);
    }

    public static void writeString(Context context, String key, String value) {
        getEditor(context).putString(key, value).commit();
    }

    public static String readString(Context context, String key, String defValue) {
        return getPreferences(context).getString(key, defValue);
    }

    public static void writeFloat(Context context, String key, float value) {
        getEditor(context).putFloat(key, value).commit();
    }

    public static float readFloat(Context context, String key, float defValue) {
        return getPreferences(context).getFloat(key, defValue);
    }

    public static void writeLong(Context context, String key, long value) {
        getEditor(context).putLong(key, value).commit();
    }

    public static long readLong(Context context, String key, long defValue) {
        return getPreferences(context).getLong(key, defValue);
    }

    public static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREF_NAME, MODE);
    }

    public static SharedPreferences.Editor getEditor(Context context) {
        return getPreferences(context).edit();
    }

    //------------------ CLEAR ALL PREFERENCES -----------------------------------------
    public static void clearAllPreference(Context context) {
        getEditor(context).clear().commit();
    }
    //----------------------------------------------------------------------------------



    public static void saveTokenID(Context mContext, String token) {
        writeString(mContext, TOKEN_ID,token);
        Log.v(TAG, "SAVED FIREBASE IN PREFERENCE - " + token);

    }
    public static String getTokenID(Context mContext) {
        String token=readString(mContext, TOKEN_ID,null);
      return token;
    }


}
