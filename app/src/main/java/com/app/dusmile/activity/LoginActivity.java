package com.app.dusmile.activity;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.error.VolleyError;
import com.app.dusmile.DBModel.Category;
import com.app.dusmile.DBModel.LoginTemplate;
import com.app.dusmile.DBModel.SubCategory;
import com.app.dusmile.R;
import com.app.dusmile.User;
import com.app.dusmile.application.DusmileApplication;
import com.app.dusmile.common.UpdateJobStatus;
import com.app.dusmile.connection.HttpVolleyRequest;
import com.app.dusmile.connection.MyListener;
import com.app.dusmile.constant.AppConstant;
import com.app.dusmile.database.CategoryDB;
import com.app.dusmile.database.SubCategoryDB;
import com.app.dusmile.database.LoginTemplateDB;
import com.app.dusmile.gps.GPSTracker;
import com.app.dusmile.model.LoginResponse;
import com.app.dusmile.model.MenuDetails;
import com.app.dusmile.preferences.Const;
import com.app.dusmile.preferences.FirebasePreference;
import com.app.dusmile.preferences.RecordUser;
import com.app.dusmile.preferences.UserPreference;
import com.app.dusmile.utils.IOUtils;
import com.desai.vatsal.mydynamictoast.MyDynamicToast;
import com.example.sumaforms2.DBHelper;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import permission.auron.com.marshmallowpermissionhelper.ActivityManagePermission;
import permission.auron.com.marshmallowpermissionhelper.PermissionResult;
import permission.auron.com.marshmallowpermissionhelper.PermissionUtils;

public class LoginActivity extends ActivityManagePermission {
    private Context mContext;
    private EditText edtUsername;
    private EditText edtPassword;
    private Button btnLogin;
    private TextView powerdBy;
    private TextView appVersion;
    Dialog gpsDialog;
    private DBHelper dbHelper;
    private String emailTo;
    //List<MenuDetails.GroupToCategoryToSubCategory> groupToCategoryToSubCategoriesList = new ArrayList<>();
    List<MenuDetails.GroupToCategoryToSubCategory.SubCategory> subCategoriesList = new ArrayList<MenuDetails.GroupToCategoryToSubCategory.SubCategory>();
    private static String Tag = "LoginActivity";
    private DatabaseReference firebaseDatabase;
    private ProgressDialog loginProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = LoginActivity.this;

        Const.setmContext(getApplicationContext());
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        this.checkPermission();
        checkLocationStatus();
        findViews();
        UpdateJobStatus.jobIdList.clear();
        dbHelper = DBHelper.getInstance(mContext);
    }


    private void findViews() {
        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        powerdBy = (TextView) findViewById(R.id.powerd_by);
        appVersion = (TextView) findViewById(R.id.app_version);
        appVersion.setText(AppConstant.VERSION);
        SpannableString content = new SpannableString("Suma Soft Pvt Ltd");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        powerdBy.setText(content);

        powerdBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://www.sumasoft.com/"));
                startActivity(browserIntent);
            }
        });

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtUsername.getText().toString();
                String password = edtPassword.getText().toString();
                if (IOUtils.isInternetPresent(mContext)) {
                    if (validateLogin(username, password)) {
                        doLogin(username, password);
                    } else {
                        MyDynamicToast.errorMessage(LoginActivity.this, "Invalid Credentials");
                    }
                } else {
                    IOUtils.showErrorMessage(mContext, "No internet connection");
                }
            }
        });
    }

    private boolean validateLogin(String username, String password) {
        if (username.isEmpty() && password.isEmpty()) {
            edtUsername.setError(getString(R.string.enter_username));
            edtPassword.setError(getString(R.string.enter_password));
            return false;
        } else if (username.isEmpty()) {
            edtUsername.setError(getResources().getString(R.string.enter_username));
            edtUsername.requestFocus();
            return false;
        } else if (password.isEmpty()) {
            edtPassword.setError(getResources().getString(R.string.enter_password));
            edtPassword.requestFocus();
            return false;
        }
        return true;
    }

    public void doFirebaseApiCall(String uname,String token){

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", uname);
            jsonObject.put("token", token);
        } catch (JSONException e) {
            IOUtils.stopLoading();
            e.printStackTrace();
        } catch (Exception e) {
            IOUtils.stopLoading();
            e.printStackTrace();
        }
        new HttpVolleyRequest(mContext, jsonObject, new Const().REQUEST_FIREBASE, listenerfirebase);
    }

    MyListener listenerfirebase = new MyListener() {
        @Override
        public void success(Object obj) throws JSONException {

        }

        @Override
        public void success(Object obj, JSONObject jsonReqObject) throws JSONException {

            IOUtils.stopLoading();
            if (obj != null) {
                try {
                    String response = obj.toString();
                    JSONObject responseJson = new JSONObject(response);
                    if (responseJson.length() > 0) {
                       // Log.d(Const.TAG, response);
                       // gson = new Gson();
                      //  PushNotificationResponse pushNotificationResponse = gson.fromJson(response, PushNotificationResponse.class);
                       // MyDynamicToast.informationMessage(mContext,pushNotificationResponse.getMessage());
                        Intent intent = new Intent(LoginActivity.this, DusmileBaseActivity.class);
                        //intent.putExtra("categorySubcategoryData",loginResponse.getMenudetails());
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                        finish();
                    } else {
                       // MyDynamicToast.errorMessage(LoginActivity.this, "Unexpected response");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void failure(VolleyError volleyError) {
            try {
                IOUtils.stopLoading();
                if (volleyError != null && volleyError.networkResponse != null) {
                    String responseBody = new String(volleyError.networkResponse.data, "utf-8");
                    //JSONObject jsonObject = new JSONObject(responseBody);
                    // String success = jsonObject.getString("success");
                    if (responseBody != null) {
                        String message = responseBody;
                      //  MyDynamicToast.informationMessage(mContext, "Invalid username or password");
                    } else {
                        //MyDynamicToast.warningMessage(LoginActivity.this, "Unable to Connect");
                        int statusCode = volleyError.networkResponse.statusCode;
                        Log.i("Status Code", "" + statusCode);
                       // IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + " API " + new Const().REQUEST_LOGIN1 + "\nRESPONSE" + volleyError.getMessage());
                    }
                } else {
                    // Toast.makeText(mContext, "Server Erorr !!", Toast.LENGTH_LONG).show();
                //    MyDynamicToast.errorMessage(LoginActivity.this, "Server Error !!");
                }
            } catch (Exception e) {
                e.printStackTrace();
              //  MyDynamicToast.warningMessage(LoginActivity.this, "Invalid User");
            }
        }
    };

    public void doLogin(String uname, String pass) {

        IOUtils.startLoading(mContext, "Authenticating....User");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", uname);
            jsonObject.put("password", pass);
            jsonObject.put("clientname", new Const().DATABASE_NAME);
            jsonObject.put("inputSource", "M");

            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, jsonObject.toString());
            DusmileApplication.getFirebaseAnalytics().logEvent(FirebaseAnalytics.Event.LOGIN, bundle);

        } catch (JSONException e) {
            IOUtils.stopLoading();
            e.printStackTrace();
        } catch (Exception e) {
            IOUtils.stopLoading();
            e.printStackTrace();
        }
        IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + " API " + new Const().REQUEST_LOGIN1 + "\nREQUEST" + jsonObject.toString());
        new HttpVolleyRequest(mContext, jsonObject, new Const().REQUEST_LOGIN1, listenerLogin, "true");
    }

    private Gson gson;
    MyListener listenerLogin = new MyListener() {
        @Override
        public void success(Object obj) throws JSONException {

        }

        @Override
        public void success(Object obj, JSONObject jsonReqObject) throws JSONException {

            //IOUtils.stopLoading();
            if (obj != null) {
                try {
                    String response = obj.toString();
                    JSONObject responseJson = new JSONObject(response);
                    if (responseJson.length() > 0) {
                        Log.d(Const.TAG, response);
                        gson = new Gson();
                        LoginResponse loginResponse = gson.fromJson(response, LoginResponse.class);
                        IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + " API " + new Const().REQUEST_LOGIN1 + "\nRESPONSE" + loginResponse.getSuccess());
                        if (loginResponse.getSuccess().equals("success")) {
                            List<LoginResponse.MenuList> jsonMenuList = loginResponse.getLoggedInUser().getMenuList();
                            String jsonStringImages = "";
                            String firebaseToken = DusmileApplication.getFirebaseToken();
                            UserPreference.saveUserPrefs(mContext, new RecordUser(), loginResponse.getLoggedInUser().getUsername(), loginResponse.getLoggedInUser().getToken(), String.valueOf(loginResponse.getLoggedInUser().getUserId()), loginResponse.getLoggedInUser().getUserId().toString(), "");
                            UserPreference.saveCategoryData(mContext, gson.toJson(loginResponse.getLoggedInUser().getMenuList()));
                            saveMenusInDB(jsonMenuList, jsonStringImages);
                            List<Integer> updatedTemplateVersionList = new ArrayList<>();
                            writeNewUser(UserPreference.readString(getApplicationContext(), UserPreference.USER_ID, ""), UserPreference.readString(getApplicationContext(), UserPreference.USER_NAME, ""), UserPreference.readString(getApplicationContext(), FirebasePreference.getTokenID(getApplicationContext()), ""), "", "");
                            doFirebaseApiCall(loginResponse.getLoggedInUser().getUsername(),firebaseToken);
                            //update template received status
                            // ConfirmTemplateUpdate.sendTemplateUpdateStatusToserver(mContext);
                        } else {
                            IOUtils.stopLoading();
                            MyDynamicToast.errorMessage(LoginActivity.this, "wrong username or password");
                        }
                    } else {
                        IOUtils.stopLoading();
                        MyDynamicToast.errorMessage(LoginActivity.this, "Unexpected response");
                    }
                } catch (Exception e) {
                    IOUtils.stopLoading();
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void failure(VolleyError volleyError) {
            try {
                IOUtils.stopLoading();
                if (volleyError != null && volleyError.networkResponse != null) {
                    String responseBody = new String(volleyError.networkResponse.data, "utf-8");
                    //JSONObject jsonObject = new JSONObject(responseBody);
                    // String success = jsonObject.getString("success");
                    if (responseBody != null) {
                        String message = responseBody;
                        MyDynamicToast.informationMessage(mContext, "Invalid username or password");
                    } else {
                        MyDynamicToast.warningMessage(LoginActivity.this, "Unable to Connect");
                        int statusCode = volleyError.networkResponse.statusCode;
                        Log.i("Status Code", "" + statusCode);
                        IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + " API " + new Const().REQUEST_LOGIN1 + "\nRESPONSE" + volleyError.getMessage());
                    }
                } else {
                    // Toast.makeText(mContext, "Server Erorr !!", Toast.LENGTH_LONG).show();
                    MyDynamicToast.errorMessage(LoginActivity.this, "Server Error !!");
                }
            } catch (Exception e) {
                e.printStackTrace();
                MyDynamicToast.warningMessage(LoginActivity.this, "Invalid User");
            }
        }
    };

    public void checkLocationStatus() {
        GPSTracker gpsTracker = new GPSTracker(this);
        if (gpsTracker.canGetLocation()) {
            if (gpsDialog != null) {
                if (gpsDialog.isShowing()) {
                    gpsDialog.dismiss();
                }
            }
        } else {
            showGPSSettingAlert();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    //Check Premission for marshmallow
    public void checkPermission() {
        //String permissionAsk[] = {PermissionUtils.Manifest_CAMERA, PermissionUtils.Manifest_WRITE_EXTERNAL_STORAGE,PermissionUtils.Manifest_READ_CALL_LOG,PermissionUtils.Manifest_WRITE_CALL_LOG,PermissionUtils.Manifest_READ_PHONE_STATE};
        String permissionAsk[] = {PermissionUtils.Manifest_READ_EXTERNAL_STORAGE, PermissionUtils.Manifest_WRITE_EXTERNAL_STORAGE, PermissionUtils.Manifest_ACCESS_FINE_LOCATION, PermissionUtils.Manifest_CAMERA, PermissionUtils.Manifest_CALL_PHONE, Manifest.permission.RECORD_AUDIO};
        askCompactPermissions(permissionAsk, new PermissionResult() {

            public void permissionGranted() {
            }


            public void permissionDenied() {

            }

            @Override
            public void permissionForeverDenied() {
                {
                    String permissionAsk[] = {PermissionUtils.Manifest_READ_EXTERNAL_STORAGE, PermissionUtils.Manifest_WRITE_EXTERNAL_STORAGE, PermissionUtils.Manifest_ACCESS_FINE_LOCATION, PermissionUtils.Manifest_CAMERA, PermissionUtils.Manifest_CALL_PHONE, Manifest.permission.RECORD_AUDIO};
                    boolean isGranted = isPermissionsGranted(LoginActivity.this, permissionAsk);
                    if (!isGranted) {
                        // showDialog();
                    } else {
                        System.out.println("IS GRANTED -- " + isGranted);
                    }
                }
            }
        });
    }


    private void saveMenusInDB(List<LoginResponse.MenuList> jsonMenuDetails, String jsonStringImages) {
        try {
            LoginTemplateDB.deleteLoginTemplate(dbHelper);
            CategoryDB.deleteCategoryTable(dbHelper);
            SubCategoryDB.deleteSubCategoryTable(dbHelper);
            Gson gson = new Gson();

            LoginTemplate menuTemplate = new LoginTemplate();
            menuTemplate.setJson_key("Menu Details");
            menuTemplate.setJson_value(gson.toJson(jsonMenuDetails));
            menuTemplate.setLanguage(UserPreference.getLanguage(mContext));
            LoginTemplate imagesTemplate = new LoginTemplate();
            imagesTemplate.setJson_key("Images");
            imagesTemplate.setJson_value(jsonStringImages);
            imagesTemplate.setLanguage(UserPreference.getLanguage(mContext));
            IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + " Adding Login menus in DB");
            long menuRowID = LoginTemplateDB.addLoginTemplateEntry(menuTemplate, dbHelper);
            long imagesRowID = LoginTemplateDB.addLoginTemplateEntry(imagesTemplate, dbHelper);
            //UserPreference.saveImagesData(mContext, jsonStringImages);
            int count = 0;

            for (int i = 0; i < jsonMenuDetails.size(); i++) {
                String categoryName = jsonMenuDetails.get(i).getCategoryName();
                Category category = new Category();
                category.setCategory_name(categoryName);
                category.setLogin_json_template_id(String.valueOf(menuRowID));
                long categoryRowId = CategoryDB.addCategoryEntry(category, dbHelper);
          /*      String categoryName = jsonMenuDetails.get(i).getCategoryName();
                Category category = new Category();
                category.setCategory_name(categoryName);
                category.setLogin_json_template_id(String.valueOf(menuRowID));
                long categoryRowId = CategoryDB.addCategoryEntry(category, dbHelper);
               // subCategoriesList = (List<MenuDetails.GroupToCategoryToSubCategory.SubCategory>) jsonMenuDetails.get(i);
                for (int j = 0; j < jsonMenuDetails.size(); j++) {*/
                count = jsonMenuDetails.get(i).getSeqId();
                String action = jsonMenuDetails.get(i).getAction();
                String subCategoryName = jsonMenuDetails.get(i).getCategoryName();
                String icon_name = jsonMenuDetails.get(i).getIconClass();
                SubCategory subCategory = new SubCategory();
                subCategory.setSubcategory_name(subCategoryName);
                subCategory.setCategory_id(String.valueOf(categoryRowId));
                subCategory.setSequence_no("" + count);
                subCategory.setIsFormMenu("false");
                subCategory.setAction(action);
                subCategory.setIcon(String.valueOf(icon_name));
                long subCategoryRowId = SubCategoryDB.addSubCategoryEntry(subCategory, dbHelper);
            }
            // int loginJsonTemplateId = LoginTemplateDB.getLoginJsontemplateID(dbHelper, "Menu Details", UserPreference.getLanguage(getApplicationContext()));
         /*   if (loginJsonTemplateId > 0) {
                int categoryId = CategoryDB.getCategoryIdDependsOnLoginJsonID(dbHelper, String.valueOf(loginJsonTemplateId));
                TemplateOperations.addStaticMenus(categoryId, count, getApplicationContext());
            }*/
            IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + " Added Login menus in DB");

       /* else
        {
            IOUtils.appendLog(Tag +" "+IOUtils.getCurrentTimeStamp()+" Updating Login menus in DB");
            int menuRowID = LoginTemplateDB.getLoginJsontemplateID(dbHelper,menuTemplate.getJson_key(),menuTemplate.getLanguage());
            LoginTemplateDB.updateLoginTemplate(menuTemplate, dbHelper);
            LoginTemplateDB.updateLoginTemplate(imagesTemplate, dbHelper);
            int count = 0;
            groupToCategoryToSubCategoriesList = menuDetails.getGroupToCategoryToSubCategory();
            for(int i=0;i<groupToCategoryToSubCategoriesList.size();i++)
            {
                String categoryName = groupToCategoryToSubCategoriesList.get(i).getCategoryName();
                Category category = new Category();
                category.setCategory_name(categoryName);
                category.setLogin_json_template_id(String.valueOf(menuRowID));
                long rows = CategoryDB.updateCategory(category,dbHelper);
                int category_id = CategoryDB.getCategoryID(dbHelper,categoryName,String.valueOf(menuRowID));
                subCategoriesList = groupToCategoryToSubCategoriesList.get(i).getSubCategory();
                for(int j=0;j<subCategoriesList.size();j++)
                {
                    count++;
                    cnt = count;
                    String subCategoryName = subCategoriesList.get(j).getName();
                    SubCategory subCategory = new SubCategory();
                    subCategory.setSubcategory_name(subCategoryName);
                    subCategory.setCategory_id(String.valueOf(category_id));
                    subCategory.setSequence_no(""+count);
                    subCategory.setIsFormMenu("false");
                    long rowsCount = SubCategoryDB.updateSubCategory(subCategory,dbHelper);
                }
            }
            IOUtils.appendLog(Tag+" "+IOUtils.getCurrentTimeStamp()+" Updated Login menus in DB");

        }*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Setup a recurring alarm every half hour
   /* public void scheduleAlarm() {
        // Construct an intent that will execute the AlarmReceiver
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        // Create a PendingIntent to be triggered when the alarm goes off
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, AlarmReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Setup periodic alarm every 5 seconds
        long firstMillis = System.currentTimeMillis(); // alarm is set right away
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        // First parameter is the type: ELAPSED_REALTIME, ELAPSED_REALTIME_WAKEUP, RTC_WAKEUP
        // Interval can be INTERVAL_FIFTEEN_MINUTES, INTERVAL_HALF_HOUR, INTERVAL_HOUR, INTERVAL_DAY
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, firstMillis,
                3000, pIntent);
    }
*/
    private void writeNewUser(String userId, String name, String email, String tokenId, String userProgress) {
        User user = new User(name, email, tokenId, userProgress);
        firebaseDatabase.child("users").child(userId).setValue(user);

    }

/*    public void showGPSSettingsAlert() {
        if (mContext != null && ((Activity) mContext)!=null) {
            if (!((Activity) mContext).isFinishing()) {
                alertDialog = new AlertDialog.Builder(mContext);
                alertDialog.setCancelable(false);
                // Setting Dialog Title
                alertDialog.setTitle(getString(R.string.app_name));

                // Setting Dialog Message
                alertDialog
                        .setMessage("Not able access your location. GPS need to be ON.");
                // On pressing Settings button
                alertDialog.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(
                                        Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivityForResult(intent, 1);
                                //((Activity) mContext).finish();
                                dialog.cancel();
                            }
                        });

           *//* // on pressing cancel button
            alertDialog.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });*//*

                // Showing Alert Message
                alertDialog.show();
            }
        }
    }*/

    public void showGPSSettingAlert() {
        gpsDialog = new Dialog(this);
        gpsDialog.setCancelable(false);
        gpsDialog.setContentView(R.layout.gps_alert_layout);
        gpsDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView textView = (TextView) gpsDialog.findViewById(R.id.gpsAlertText);
        textView.setText("Not able access your location. GPS need to be ON.");
        Button okButton = (Button) gpsDialog.findViewById(R.id.gpsOkButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(intent, 1);
            }

        });
        gpsDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            GPSTracker gpsTracker = new GPSTracker(getApplicationContext());
            if (gpsTracker.canGetLocation()) {
                if (gpsDialog != null) {
                    if (gpsDialog.isShowing()) {
                        gpsDialog.dismiss();
                    }
                }
            }
        }
    }
}
