package com.app.dusmile.activity;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;


import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.MimeTypeMap;
import android.widget.Toast;


import com.android.volley.error.VolleyError;
import com.app.dusmile.DBModel.AssignedJobs;
import com.app.dusmile.DBModel.ClientTemplate;
import com.app.dusmile.R;
import com.app.dusmile.application.DusmileApplication;
import com.app.dusmile.common.DusmileFileProvider;
import com.app.dusmile.common.UpdateJobStatus;
import com.app.dusmile.connection.HttpVolleyRequest;
import com.app.dusmile.connection.MyListener;
import com.app.dusmile.constant.AppConstant;
import com.app.dusmile.database.AssignedJobsDB;
import com.app.dusmile.database.ClientTemplateDB;
import com.app.dusmile.database.helper.DBHelper;
import com.app.dusmile.fragment.MenuItemFragment;
import com.app.dusmile.fragment.NavigationDrawerFragment;
import com.app.dusmile.fragment.PendingPdfUploadFragment;
import com.app.dusmile.model.GetJobCountModel;
import com.app.dusmile.model.MenuItems;
import com.app.dusmile.preferences.Const;
import com.app.dusmile.preferences.UserPreference;
import com.app.dusmile.utils.IOUtils;
import com.desai.vatsal.mydynamictoast.MyDynamicToast;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.support.v4.content.FileProvider.getUriForFile;
import static com.app.dusmile.utils.IOUtils.getCurrentDay;
import static java.security.AccessController.getContext;


public class DusmileBaseActivity extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, MenuItemFragment.OnListFragmentInteractionListener {

    private NavigationDrawerFragment mNavigationDrawerFragment;
    Toolbar toolbar;
    ArrayList<String> Jobs;
    List<AssignedJobs> assignedJobses;
    private String Tag = "DusmileBaseActivity";
    public static int position = 1;
    private String categorySubcategoryData = "";
    public static String subCategoryName;
    private DBHelper dbHelper;
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    private DusmileBaseActivity dusmileBaseActivity;
    int myvalue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dusmile_base);
        dbHelper = DBHelper.getInstance(getApplicationContext());
        // categorySubcategoryData = UserPreference.getCategoryData(DusmileBaseActivity.this);
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (IOUtils.isInternetPresent(getApplicationContext())) {
            initView();
        }
        else {
            IOUtils.stopLoading();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.no_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.send_log) {
            saveLog();
        }
        return super.onOptionsItemSelected(item);
    }

    public void initView() {
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.primary)));
        getSupportActionBar().setIcon(R.drawable.sumaicon);
        getSupportActionBar().setTitle("Menu");


        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        /*AppConstant.subCategoryName = "Available Jobs";
        AppConstant.isAvilableJobs = true;*/
        AppConstant.isAssinedJobs = false;
        AppConstant.isCompletedJobs = false;
        AppConstant.isFirstViewClick = true;
        AppConstant.isSecondViewClick = true;
        AppConstant.categoryName = "Jobs";
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
    }


    @Override
    public void onNavigationDrawerItemSelected(int categoryPosition) {
        // update the main content by replacing fragments

        if (mNavigationDrawerFragment.isDrawerOpen()) {
            mNavigationDrawerFragment.mDrawerLayout.closeDrawers();
        }
    }


    @Override
    public void onNavigationDrawerItemSelected(String subCategoryName) {
        if (mNavigationDrawerFragment.isDrawerOpen()) {
            mNavigationDrawerFragment.mDrawerLayout.closeDrawers();
        }
        AppConstant.subCategoryName = subCategoryName;
        this.subCategoryName = subCategoryName;
        Intent intent = null;
        switch (subCategoryName) {
            case "Logout":
                IOUtils.showLogoutMessage(DusmileBaseActivity.this, getResources().getString(R.string.logout_app), DusmileBaseActivity.this);
                break;

            case "Available Jobs":
                AppConstant.isAvilableJobs = true;
                AppConstant.isAssinedJobs = false;
                AppConstant.isCompletedJobs = false;
                intent = new Intent(DusmileBaseActivity.this, JobsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                break;//commented by netra
            case "Assigned Jobs":
                AppConstant.isAvilableJobs = false;
                AppConstant.isAssinedJobs = true;
                AppConstant.isCompletedJobs = false;
                intent = new Intent(DusmileBaseActivity.this, JobsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                break;
            case "Completed Jobs":
                AppConstant.isAvilableJobs = false;
                AppConstant.isAssinedJobs = false;
                AppConstant.isCompletedJobs = true;
                intent = new Intent(DusmileBaseActivity.this, JobsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                break;
            case "Change Password":
                startActivity(new Intent(DusmileBaseActivity.this, ChangePasswordActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                break;
            case "Status Report":
                startActivity(new Intent(DusmileBaseActivity.this, ReportActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                break;
            case "Pending":
                intent = new Intent(DusmileBaseActivity.this, PendingUploadActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                break;
            case "Upload":
                intent = new Intent(DusmileBaseActivity.this, UploadActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                break;
            case "Jobs Submitted To Supervisor": //for status report
                startActivity(new Intent(DusmileBaseActivity.this, ReportDetailsActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                break;
        }

        if (!subCategoryName.equalsIgnoreCase("Logout")) {
            getSupportActionBar().setTitle(subCategoryName);
        }

    }


    @Override
    public void onBackPressed() {
        try {

            if (mNavigationDrawerFragment.isDrawerOpen()) {
                mNavigationDrawerFragment.mDrawerLayout.closeDrawers();
            } else {
                IOUtils.showExitAppMessage(DusmileBaseActivity.this, getResources().getString(R.string.exit_app), DusmileBaseActivity.this);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onListFragmentInteraction(MenuItems item) {

    }

    public void saveLog() {

        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());
        String emailTO = UserPreference.getUserRecord(this).getEmailTO();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c.getTime());
        File sdCardRoot = Environment.getExternalStorageDirectory();
        File file = new File(sdCardRoot + "/" + AppConstant.ROOT_DIRECTORY + "/Logs/" + IOUtils.get_Current_day() + "(" + formattedDate + ").txt");
        Intent intent = new Intent(Intent.ACTION_SEND);
        Uri selectedUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", file);
        String fileExtension = MimeTypeMap.getFileExtensionFromUrl(selectedUri.toString());
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension);
        intent.setType(mimeType);
        intent.putExtra(Intent.EXTRA_STREAM, selectedUri);
        // intent.putExtra(Intent.EXTRA_EMAIL,new String[]{emailTO});
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"dusmile_support@sumasoft.net"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Dusmile WheelsEMI Log- " + IOUtils.get_Current_day() + "(" + formattedDate + ")");
        startActivity(Intent.createChooser(intent, "Share File"));
    }
}