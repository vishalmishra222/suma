package com.app.dusmile.constant;

import com.app.dusmile.preferences.Const;

/**
 * Created by suma on 27/01/17.
 */

public class AppConstant {

    public static String MainUrl = "http://192.168.222.54:8080/Dusmile/";

    public static boolean isAvilableJobs = true;
    public static boolean isAssinedJobs = false;
    public static boolean isCompletedJobs = false;
    public static String subCategoryName="";
    public static String categoryName="";

    public static boolean isFirstViewClick=false;
    public static boolean isSecondViewClick=false;

    public static int formPosition = 0 ;
    public static String apiUrl = "http://api.androidhive.info/contacts/";

    public static String tableName;

    /*********************************************************************************/
  //  public static final String VIRSION_AVAILABLE_URL= new Const.BASE_URL + "getCurrentAppVersion/WEMI";
    public static final String VIRSION_AVAILABLE_URL= Const.BASE_URL + "getCurrentAppVersion/"+Const.DATABASE_NAME;
    public static  String VERSION = "";
    public static final String ROOT_DIRECTORY="Dusmile";
    public static String AndroidVersion = android.os.Build.VERSION.RELEASE;
    public static final String BUILD = "25th Feb 2017";

    /**********************************************************************************/

    public static boolean isRedRadioButtonClicked = false;
    public static boolean isPendingPdfVisible = false;
    public static boolean isJobFragmentVisible = false;
    public static boolean isCallButtonClicked = false;
}
