package com.app.form;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.weiwangcn.betterspinner.library.BetterSpinner;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    View myView;
    ViewGroup parent;
    LinearLayout formLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myView = findViewById(R.id.custom_view);
        parent = (ViewGroup) myView.getParent();
        parent.removeView(myView);

        formLayout = findViewById(R.id.formLayout);
        formLayout.removeAllViews();
        formLayout.setOrientation(LinearLayout.VERTICAL);
        formLayout.setPadding(MyDynamicFields.dpToPx(16), MyDynamicFields.dpToPx(16), MyDynamicFields.dpToPx(16), MyDynamicFields.dpToPx(16));

        MyDynamicFields.setEditText(formLayout, this, "Enter Name");
        setSpinnerAdapter(MyDynamicFields.setSpinner(formLayout, this, "Location"));
        MyDynamicFields.setEditText(formLayout, this, "Exact Location");
        setSpinnerAdapter(MyDynamicFields.setSpinner(formLayout, this, "Department"));
        MyDynamicFields.setButton(formLayout, this, "Submit");
    }

    public void setSpinnerAdapter(BetterSpinner locationSpinner) {
        List<String> locationList = new ArrayList<>();
        locationList.add("Nitin");
        locationList.add("Nikhil");
        locationList.add("Vishal");
        locationList.add("Other");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.select_dialog_item, locationList);
        locationSpinner.setAdapter(arrayAdapter);
    }
}


