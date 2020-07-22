package com.app.dusmile.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.app.dusmile.DBModel.SubCategory;
import com.app.dusmile.DBModel.TemplateJson;
import com.app.dusmile.R;
import com.app.dusmile.common.TemplateOperations;
import com.app.dusmile.database.AssignedJobsDB;
import com.app.dusmile.database.CategoryDB;
import com.app.dusmile.database.LoginTemplateDB;
import com.app.dusmile.database.SubCategoryDB;
import com.app.dusmile.database.TemplateJsonDB;
import com.app.dusmile.database.helper.DBHelper;
import com.app.dusmile.preferences.UserPreference;
import com.app.dusmile.utils.IOUtils;

import java.util.List;

public class TranslationActivity extends AppCompatActivity {
    String jobID,clientTempId;
    private DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translation);
        dbHelper = DBHelper.getInstance(getApplicationContext());
        Bundle b  = getIntent().getExtras();
        if(b!=null)
        {
            jobID = b.getString("jobId");
            clientTempId = b.getString("clientTempId");
            updateMenuInDB(jobID,clientTempId);
        }

    }

    private void updateMenuInDB(String jobID, String clientTempId) {
        IOUtils.startLoading(getApplicationContext(), "Updating Menus... Please wait..");
        AssignedJobsDB.updateAllJobsStatus(dbHelper, "false");
        AssignedJobsDB.updateJobsStatusByJobId(dbHelper, "true", jobID);
        TemplateOperations.getFormsFromDBAndInsertIntoMenus(Integer.parseInt(clientTempId),getApplicationContext());
        IOUtils.stopLoading();
        Intent i = new Intent(TranslationActivity.this, DusmileBaseActivity.class);
        startActivity(i);
        finish();
    }
}
