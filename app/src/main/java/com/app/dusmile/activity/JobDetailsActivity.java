package com.app.dusmile.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.LinearLayout;

import com.app.dusmile.R;
import com.app.dusmile.constant.AppConstant;
import com.app.dusmile.fragment.NavigationDrawerFragment;
import com.app.dusmile.preferences.UserPreference;
import com.app.dusmile.unhandleException.TopExceptionHandler;
import com.app.dusmile.utils.IOUtils;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class JobDetailsActivity extends AppCompatActivity {


    private Toolbar toolbar;

    public static String subCategoryName;
    public static String job_id;
    public static String nbfcName;
    public static String response;
    public static boolean isAccept;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        response = intent.getExtras().getString("response");
        isAccept = intent.getExtras().getBoolean("isAccept");
        job_id = intent.getExtras().getString("job_id");
        nbfcName = intent.getExtras().getString("nbfcName");
        subCategoryName = intent.getExtras().getString("subCategoryName");

        setContentView(R.layout.activity_job_details);
        initView();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dusmile_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            /*case R.id.changePassword:
                startActivity(new Intent(JobDetailsActivity.this,ChangePasswordActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |  Intent.FLAG_ACTIVITY_NEW_TASK));
               // overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
                finish();
                return true;
            case R.id.logout:
                IOUtils.showLogoutMessage(JobDetailsActivity.this,getResources().getString(R.string.logout_app),JobDetailsActivity.this);
                return true;*/
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void initView() {
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Job Details");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.primary)));
        getSupportActionBar().setIcon(R.drawable.sumaicon);



      //  AppConstant.isAvilableJobs = true;
      //  AppConstant.isAssinedJobs = false;
       // AppConstant.isCompletedJobs = false;

    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public  void showWarningForSaveMessage(Context mContext, String message){
        SweetAlertDialog sw = new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE);
        Window window = sw.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        sw.setContentText(message);
        sw.setTitleText(mContext.getString(R.string.app_name));
        sw.setCancelText("No").setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismissWithAnimation();
            }
        });
        sw.setConfirmText("Yes")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                        if (AppConstant.formPosition == 0) {
                            switch (AppConstant.subCategoryName) {

                                case "Logout":
                                    IOUtils.showLogoutMessage(JobDetailsActivity.this, getResources().getString(R.string.logout_app), JobDetailsActivity.this);
                                    break;

                                case "Available Jobs":
                                    AppConstant.isAvilableJobs = true;
                                    AppConstant.isAssinedJobs = false;
                                    AppConstant.isCompletedJobs = false;
                                    startActivity(new Intent(JobDetailsActivity.this, JobsActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                                    finish();
                                    break;//netra
                                case "Assigned Jobs":
                                    AppConstant.isAvilableJobs = false;
                                    AppConstant.isAssinedJobs = true;
                                    AppConstant.isCompletedJobs = false;
                                    startActivity(new Intent(JobDetailsActivity.this, JobsActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                                    finish();
                                    break;
                                case "View Completed Jobs":
                                    AppConstant.isAvilableJobs = false;
                                    AppConstant.isAssinedJobs = false;
                                    AppConstant.isCompletedJobs = true;
                                    startActivity(new Intent(JobDetailsActivity.this, JobsActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                                    finish();
                                    break;
                                case "Change Password":
                                    startActivity(new Intent(JobDetailsActivity.this, ChangePasswordActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                                    finish();
                                    break;
                                case "Status Report":
                                    startActivity(new Intent(JobDetailsActivity.this, ReportActivity.class).putExtra("isAppExit",false).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                                    finish();
                                    break;
                                case "Jobs Submitted To Supervisor": //for status report
                                    startActivity(new Intent(JobDetailsActivity.this, ReportDetailsActivity.class).putExtra("isAppExit",false).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                                    finish();
                                    break;
                                case "Pending":
                                    startActivity(new Intent(JobDetailsActivity.this,PendingUploadActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |  Intent.FLAG_ACTIVITY_NEW_TASK));
                                    finish();
                                    break;
                            }
                        } else {
                            //If form is other loan information then decrement formPostion twice.
                            //because personal information form and other loan information form are in same activity

                            if(AppConstant.formPosition == 4){
                                AppConstant.formPosition --;
                            }
                            AppConstant.formPosition -- ;
                            finish();
                        }
                    }
                })
                .show();
    }
}
