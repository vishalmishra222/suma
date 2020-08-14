package com.app.dusmile.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.app.dusmile.DBModel.ClientTemplate;
import com.app.dusmile.DBModel.DynamicTableField;
import com.app.dusmile.DBModel.TemplateJson;
import com.app.dusmile.R;
import com.app.dusmile.constant.AppConstant;
import com.app.dusmile.database.AssignedJobsDB;
import com.app.dusmile.database.ClientTemplateDB;
import com.app.dusmile.database.DynamicFieldTableDB;
import com.app.dusmile.database.TemplateJsonDB;
import com.app.dusmile.database.helper.DBHelper;
import com.app.dusmile.preferences.Const;
import com.app.dusmile.preferences.UserPreference;
import com.app.dusmile.utils.IOUtils;
import com.eggheadgames.siren.ISirenListener;
import com.eggheadgames.siren.Siren;
import com.eggheadgames.siren.SirenAlertType;
import com.eggheadgames.siren.SirenVersionCheckType;

import java.util.ArrayList;

import permission.auron.com.marshmallowpermissionhelper.ActivityManagePermission;
import permission.auron.com.marshmallowpermissionhelper.PermissionResult;
import permission.auron.com.marshmallowpermissionhelper.PermissionUtils;

public class SplashActivity extends ActivityManagePermission {

    private static final String TAG = "SplashScreen";
    private static int SPLASH_TIME_OUT = 6200;
    TextView txtVersion;
    boolean isUpdateAvailable = false;
    ISirenListener sirenListener = new ISirenListener() {
        @Override
        public void onShowUpdateDialog() {
            Log.d(TAG, "onShowUpdateDialog");
            isUpdateAvailable = true;
        }

        @Override
        public void onLaunchGooglePlay() {
            Log.d(TAG, "onLaunchGooglePlay");
        }

        @Override
        public void onSkipVersion() {
            Log.d(TAG, "onSkipVersion");
        }

        @Override
        public void onCancel() {
            Log.d(TAG, "onCancel");
        }

        @Override
        public void onDetectNewVersionWithoutAlert(String message) {
            Log.d(TAG, "onDetectNewVersionWithoutAlert: " + message);
        }

        @Override
        public void onError(Exception e) {
            Log.d(TAG, "onError");
            e.printStackTrace();
        }
    };
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        checkPermission();
        AppConstant.VERSION = getApplicationVersion();
        mContext = SplashActivity.this;
        txtVersion = (TextView) findViewById(R.id.splashscreen_version);
        txtVersion.setText("--V-- " + AppConstant.VERSION);
        UserPreference.setLanguage(mContext, "en");
        IOUtils.saveLog();
        Const.setmContext(getApplicationContext());
        myThread();
    }

    private String getApplicationVersion() {
        String appVersion = "";
        try {
            PackageInfo packageInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            appVersion = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appVersion;
    }

    private void checkCurrentAppVersion() {
        Siren siren = Siren.getInstance(getApplicationContext());
        siren.setSirenListener(sirenListener);
        siren.setMajorUpdateAlertType(SirenAlertType.FORCE);
        siren.setMinorUpdateAlertType(SirenAlertType.FORCE);
        siren.setPatchUpdateAlertType(SirenAlertType.FORCE);
        siren.setRevisionUpdateAlertType(SirenAlertType.FORCE);
        siren.setVersionCodeUpdateAlertType(SirenAlertType.FORCE);
        siren.checkVersion(this, SirenVersionCheckType.IMMEDIATELY, AppConstant.VIRSION_AVAILABLE_URL);
    }

    public void myThread() {
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                deleteOldDeprecatedTemplates();
                IOUtils.check_file(mContext);
                IOUtils.createLog();
                if (isUpdateAvailable) {
                } else {
                    checkUser();
                }
            }
        }, SPLASH_TIME_OUT);
    }

    private void checkUser() {
        goToLoginScreen(true);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (IOUtils.isInternetPresent(this)) {
            checkCurrentAppVersion();
        }
        myThread();
    }


    public void goToLoginScreen(boolean isLoginCall) {
        if (isLoginCall) {
            Intent i = new Intent(SplashActivity.this, LoginActivity.class);
            // set the new task and clear flags
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
    }


    //Check Premission for marshmallow
    public void checkPermission() {
        //String permissionAsk[] = {PermissionUtils.Manifest_CAMERA, PermissionUtils.Manifest_WRITE_EXTERNAL_STORAGE,PermissionUtils.Manifest_READ_CALL_LOG,PermissionUtils.Manifest_WRITE_CALL_LOG,PermissionUtils.Manifest_READ_PHONE_STATE};
        String permissionAsk[] = {PermissionUtils.Manifest_READ_EXTERNAL_STORAGE, PermissionUtils.Manifest_WRITE_EXTERNAL_STORAGE};
        askCompactPermissions(permissionAsk, new PermissionResult() {

            public void permissionGranted() {
            }


            public void permissionDenied() {

            }

            @Override
            public void permissionForeverDenied() {
                {
                    String permissionAsk[] = {PermissionUtils.Manifest_READ_EXTERNAL_STORAGE, PermissionUtils.Manifest_WRITE_EXTERNAL_STORAGE};
                    boolean isGranted = isPermissionsGranted(SplashActivity.this, permissionAsk);
                    if (!isGranted) {
                        // showDialog();
                    } else {
                        System.out.println("IS GRANTED -- " + isGranted);
                    }
                }
            }
        });
    }

    private void deleteOldDeprecatedTemplates() {
        try {
            DBHelper dbHelper = DBHelper.getInstance(getApplicationContext());
            if (dbHelper != null) {
                int count = AssignedJobsDB.getAssignedJobsCount(dbHelper);
                if (count == 0) {
                    ArrayList<ClientTemplate> clientTemplateArrayList = ClientTemplateDB.getAllClientTemplateDependsOnDeprecatedFlag(dbHelper, "true");
                    if (clientTemplateArrayList != null && clientTemplateArrayList.size() > 0) {
                        for (int i = 0; i < clientTemplateArrayList.size(); i++) {
                            String clientTemplateId = clientTemplateArrayList.get(i).getID();
                            if (!TextUtils.isEmpty(clientTemplateId)) {
                                ClientTemplateDB.removeClientTemplateById(dbHelper, clientTemplateId);
                                ArrayList<TemplateJson> templateJsonArrayList = TemplateJsonDB.getAllTemplateJsonBtClientTemplateId(dbHelper, clientTemplateId);
                                if (templateJsonArrayList != null && templateJsonArrayList.size() > 0) {
                                    for (int j = 0; j < templateJsonArrayList.size(); j++) {
                                        String jsonTemplateID = templateJsonArrayList.get(j).getID();
                                        if (!TextUtils.isEmpty(jsonTemplateID)) {
                                            TemplateJsonDB.removeTemplateJsonByJsonTemplateId(dbHelper, jsonTemplateID);
                                            ArrayList<DynamicTableField> dynamicTableFieldArrayList = DynamicFieldTableDB.getAllDynamicFieldDependsOnJsonTemplateID(dbHelper, jsonTemplateID);
                                            if (dynamicTableFieldArrayList != null && dynamicTableFieldArrayList.size() > 0) {
                                                for (int k = 0; k < dynamicTableFieldArrayList.size(); k++) {
                                                    String JsonTemplateId = dynamicTableFieldArrayList.get(k).getTemplate_json_id();
                                                    if (!TextUtils.isEmpty(JsonTemplateId)) {
                                                        DynamicFieldTableDB.removeDynamicFieldsByJsonTemplateId(dbHelper, JsonTemplateId);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
