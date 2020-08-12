package com.app.dusmile.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.app.dusmile.model.LoginResponse;

import java.util.List;

public class UserPreference {

    public static final String PREF_NAME = "UserPrefs";
    public static final int MODE = Context.MODE_PRIVATE;
    private static final String IMAGES = "images";
    private static final String TOKEN = "token";
    private static final String APP_LANGUAGE = "app_language";
    static String TAG="UserPrefs";

    public static  final String USER_ID="user_id";
    public static  final String USER_INT_ID="user_int_id";
    public static final String EMAIL_TO="email_to";
    public static  final String USER_NAME="user_name";
    public static  final String FIRST_NAME="fname";
    public static  final String LAST_NAME="lname";
    public static final String  ROLE_ID = "role_id";
    public static final String  BU_ID = "bu_id";
    public static final String  ENTITIY_ID = "entity_id";
    public static final String  REPORTING_TO = "reporting_to";
    public static final String  STATUS = "status";
    public static final String  OTP = "otp";
    private static final String CATEGORY_SUBCATEGORYDATA = "categorySubcategoryData";

    public static  final String LATITUDE="latitude";
    public static  final String LONGITUDE="longitude";
    public static final String PENDING_CNT = "pendingCnt";
    public static  final String AVAILABLE_CNT="availableCnt";
    public static  final String ASSIGNED_CNT="assignedCnt";
    public static  final String COMPLETED_CNT="complCnt";
    public static  final String BASE_URL="baseUrl";

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

    public static void setLanguage(Context mContext,String language_code){
        writeString(mContext, APP_LANGUAGE,language_code);
    }
    public static String getLanguage(Context mContext){
        return readString(mContext, APP_LANGUAGE,null);
    }
    public static void setBaseUrl(Context mContext,String url){
        writeString(mContext, BASE_URL,url);
    }
    public static String getBaseUrl(Context mContext){
        return readString(mContext, BASE_URL,null);
    }
    public static void saveUserPrefs(Context mContext, RecordUser record,String userName,String token,String userId, String userIntID , String emailTO) {

        writeString(mContext, TOKEN,token);
        writeString(mContext, USER_ID,userId);
        writeString(mContext, USER_NAME,userName);
        writeString(mContext,FIRST_NAME,record.getFname());
        writeString(mContext,LAST_NAME,record.getLname());
        writeString(mContext,ROLE_ID,record.getRoleID());
        writeString(mContext,BU_ID,record.getBuID());
        writeString(mContext,ENTITIY_ID,record.getEntityID());
        writeInteger(mContext,OTP,record.getOtp());
        writeString(mContext,REPORTING_TO,record.getReportingTo());
        writeString(mContext,USER_INT_ID,userIntID);
        writeString(mContext,EMAIL_TO,emailTO);

        Log.v(TAG, "SAVED USER IN PREFERENCE - " + record.getUserID());

    }

    public static void saveCategoryData(Context mContext,String categorySubcategoryData){
        writeString(mContext, CATEGORY_SUBCATEGORYDATA,categorySubcategoryData);
    }
    public static String getCategoryData(Context mContext){
       return readString(mContext, CATEGORY_SUBCATEGORYDATA,null);
    }
    public static RecordUser getUserRecord(Context mContext) {


        String userID=readString(mContext, USER_ID,null);
        String userName=readString(mContext, USER_NAME,null);
        String firstName=readString(mContext, FIRST_NAME,null);
        String lastName=readString(mContext, LAST_NAME,null);
        String roleID=readString(mContext, ROLE_ID,null);
        String buID=readString(mContext, BU_ID,null);
        String entityID=readString(mContext, ENTITIY_ID,null);
        String reportingTO=readString(mContext, REPORTING_TO,null);
        String status=readString(mContext, STATUS,null);
        String token=readString(mContext, TOKEN,null);
        int otp=readInteger(mContext, OTP,0);
        String emailTO=readString(mContext,EMAIL_TO,null);


        RecordUser recordUser=new RecordUser(userID,userName,firstName,lastName,roleID,buID,entityID,reportingTO,status,otp,token,emailTO);


    return recordUser;

    }

    public static void updateOTP(Context mContext,int otp)
    {
        writeInteger(mContext,OTP,otp);
    }

    public static void saveImagesData(Context mContext, String images) {

        writeString(mContext,IMAGES,images.toString());

    }
    public static String getImagesData(Context mContext) {
        return readString(mContext,IMAGES,null);
    }
}
