package com.app.dusmile.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.app.dusmile.R;
import com.app.dusmile.preferences.UserPreference;
import com.app.dusmile.unhandleException.TopExceptionHandler;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = null;
        if(UserPreference.getUserRecord(MainActivity.this).getUsername()==null){
            intent = new Intent(MainActivity.this,LoginActivity.class);
        }else{
            intent = new Intent(MainActivity.this,DusmileBaseActivity.class);
            //intent.putExtra("categorySubcategoryData",UserPreference.getCategoryData(MainActivity.this));
        }
        startActivity(intent);
        finish();
    }
}
