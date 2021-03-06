package com.app.dusmile.preferences;

import android.content.Context;
import android.util.Log;

import com.app.dusmile.application.DusmileApplication;

/**
 * Created by sumasoft on 25/01/17.
 */
public class Const {
    //public static final String BASE_URL = "http://dusmile.sumasoft.com/Dusmile/";
    //public static final String BASE_URL = "http://192.168.222.41:8080/Dusmile/";//Shraddha's local
    //  public static final String BASE_URL = "http://192.168.222.129:8080/Dusmile/";//Mohit local
    // public static final String BASE_URL = "http://192.168.222.54:8080/Dusmile/";
     public static final String BASE_URL = "http://demockross.sumasoft.com/Dusmile/";
    // public static final String BASE_URL = "http://192.168.222.156:8080/Dusmile/";//samiksha
    // public static final String BASE_URL = "http://192.168.222.54:8080/Dusmile/";//Ankita
   // public static final String BASE_URL = "http://192.168.222.50:8080/Dusmile/";//public
    // public static final String BASE_ ew`wURL = "http://192.168.222.129:8080/Dusmile/";//mohit
    // public static final String BASE_URL = "http://192.168.111.226:8080/Dusmile/";//UAT
    public static final String ALTERNATE_BASE_IP = "http://dusmile1.sumasoft.com/Dusmile/";//mohit

    public String REQUEST_LOGIN1 = BASE_URL + "login";

    public String REQUEST_JOB_DETAILS = BASE_URL + "getJobDetails";

    public static final String DATABASE_NAME = "WEMI";

    public String REQUEST_GET_UPDATED_TEMPLATE = BASE_URL + "ui/template/FI";

    public String REQUEST_CONFIRM_TEMPLATE_UPDATE = BASE_URL + "confirmTemplate";

    public String REQUEST_CHANGE_PASS = BASE_URL + "user/updatePassword";

    public String REQUEST_SAVE_SUBMIT_JOB_DETAILS = BASE_URL + "jobs";

    public String REQUEST_CHECK_ASSIGNED_JOBS = BASE_URL + "assignJobToContractor";

    public String REQUEST_UPLOAD_PDF = BASE_URL + "uploadFile" + "/" + DATABASE_NAME;

    public String REQUEST_AUTO_UPLOAD_LOGS = BASE_URL + "uploadLogFile";

    public String REQUEST_GET_JOB_COUNT = BASE_URL + "jobs/assignedJobCount";

    public String REQUEST_UPDATE_JOB_STATUS = BASE_URL + "job/updateStatus";

    public String REQUEST_GET_ASSIGNED_JOBS = BASE_URL + "reports/listProcess/JR4";

    public String REQUEST_GET_COMPLETED_JOBS = BASE_URL + "reports/listProcess/JR6";

    public String REQUEST_JOB_DETAILS1 = BASE_URL + "job";

    public String REQUEST_REPORT_FILTERS = BASE_URL + "reports/listProcess/JR21";

    public String REQUEST_HOLD_JOB = BASE_URL + "jobs/Hold";

    public static String TAG = "DUSMILE";

    private static Context mContext;

    public static Context getmContext() {
        return mContext;
    }

    public static void setmContext(Context nContext) {
        mContext = nContext;
    }

}
