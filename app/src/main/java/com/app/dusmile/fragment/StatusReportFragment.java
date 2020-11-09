package com.app.dusmile.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.error.VolleyError;
import com.app.dusmile.R;
import com.app.dusmile.activity.ReportDetailsActivity;
import com.app.dusmile.connection.HttpVolleyRequest;
import com.app.dusmile.connection.MyListener;
import com.app.dusmile.constant.AppConstant;
import com.app.dusmile.interfaces.BtnClickListener;
import com.app.dusmile.model.ReportFilter;
import com.app.dusmile.model.ReportFilterModel;
import com.app.dusmile.preferences.Const;
import com.app.dusmile.preferences.UserPreference;
import com.app.dusmile.recording.RecordingMainActivity;
import com.app.dusmile.unhandleException.TopExceptionHandler;
import com.app.dusmile.utils.IOUtils;
import com.app.dusmile.view.DatabaseUI;
import com.app.dusmile.view.UI;
import com.desai.vatsal.mydynamictoast.MyDynamicToast;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by suma on 03/02/17.
 */

public class StatusReportFragment extends Fragment {
    private Context mContext;
    private LinearLayout reportFiltersLin;
    public static String date;
    Date startDate;
    Date endDate;
    private String Tag = "StatusReportFragment";
    private List<ReportFilterModel.ReportFilter> reportFiltersList = new ArrayList<>();

    public StatusReportFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_report_filters, container, false);
        this.findViews(rootView);
        Activity activity = this.getActivity();
        mContext = activity;
        if (IOUtils.isInternetPresent(mContext)) {
            getReportsFilters();
        } else {
            IOUtils.showErrorMessage(mContext, "No internet connection");
        }
        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        StatusReportFragment statusReportFragment = new StatusReportFragment();
        if (statusReportFragment != null && statusReportFragment.isAdded()) {
            FragmentTransaction ft = statusReportFragment.getFragmentManager().beginTransaction();
            ft.remove(statusReportFragment);
            ft.commit();
        }


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void findViews(View view) {
        reportFiltersLin = (LinearLayout) view.findViewById(R.id.reportFiltersLin);
    }

    public void getReportsFilters() {
        IOUtils.startLoading(getActivity(), "Loading.....");
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("FOSExecutiveID", UserPreference.getUserRecord(mContext).getUserID());
            new HttpVolleyRequest(getActivity(), jsonObject, new Const().REQUEST_REPORT_FILTERS + "/" + UserPreference.getUserRecord(mContext).getUserID(), listenerReportFilters);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    MyListener listenerReportFilters = new MyListener() {
        @Override
        public void success(Object obj) throws JSONException {

        }

        @Override
        public void success(Object obj, JSONObject jsonObject) throws JSONException {

            IOUtils.stopLoading();
            try {
                IOUtils.stopLoading();
                if (obj != null) {
                    String response = obj.toString();
                    JSONObject jsonObject1 = new JSONObject(response);
                    if (jsonObject1.length() > 0) {
                        Log.d(Const.TAG, response);
                        createUI(response);
                    } else {
                        MyDynamicToast.errorMessage(mContext, "Unexpected Response");
                    }
                } else {
                    MyDynamicToast.errorMessage(mContext, "Unexpected Response");
                }
            } catch (Exception e) {
                e.printStackTrace();
                MyDynamicToast.errorMessage(mContext, "Unexpected Response");
            }
        }

        @Override
        public void failure(VolleyError volleyError) {
            try {
                IOUtils.stopLoading();
                if (volleyError != null) {
                    String responseBody = new String(volleyError.networkResponse.data, "utf-8");
                    JSONObject jsonObject = new JSONObject(responseBody);
                    String success = jsonObject.getString("success");
                    if (success.equals("false")) {
                        String message = jsonObject.getString("message");
                        MyDynamicToast.informationMessage(mContext, message);
                    } else {
                        MyDynamicToast.warningMessage(mContext, "Unable to connect");
                        if (volleyError.networkResponse.statusCode == 800) {
                            IOUtils.sendUserToLogin(mContext, getActivity());
                        }
                    }
                } else {
                    MyDynamicToast.errorMessage(mContext, "Server Error !!");
                }
            } catch (Exception e) {
                e.printStackTrace();
                MyDynamicToast.errorMessage(mContext, "Server Error !!");
            }
        }
    };


    private void createUI(String response) {
        try {
            Gson gson = new Gson();
            ReportFilter reportFilterModel = gson.fromJson(response.trim(), ReportFilter.class);
            IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + " API " + new Const().REQUEST_CHECK_ASSIGNED_JOBS + "\nRESPONSE " + reportFilterModel.getSuccess());
            reportFiltersList = reportFilterModel.getReportFilters();
            reportFiltersLin.removeAllViews();
            for (int i = 0; i < reportFiltersList.size(); i++) {
                DatabaseUI.createHorizontalLayoutAndFieldsForReport(mContext, reportFiltersLin, reportFiltersList.get(i));
            }
            if (reportFiltersList.size() > 0) {
                DatabaseUI.createReportButton(mContext, reportFiltersLin, reportFiltersList.get(0), reportFiltersList, submitbtnClickListener);
            } else {
                DatabaseUI.createReportButton(mContext, reportFiltersLin, null, null, submitbtnClickListener);

            }
        } catch (Exception e) {
            e.printStackTrace();
            //Toast.makeText(mContext,"Error occurred",Toast.LENGTH_LONG).show();
            MyDynamicToast.errorMessage(mContext, "Error occurred");
        }
    }


    BtnClickListener submitbtnClickListener = new BtnClickListener() {
        @Override
        public void uploadImageListener() {

        }

        @Override
        public void saveListerners() {

        }

        @Override
        public void submitListener() {


        }

        @Override
        public void audioListener() {

        }

        @Override
        public void cancelListener() {

        }

        @Override
        public void clickListener(String val) {
            try {


                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    startDate = sdf2.parse(val);
                    String sDate = sdf2.format(startDate);
                    Intent intent = new Intent(getActivity(), ReportDetailsActivity.class);
                    ReportDetailsActivity.date = sDate;
                    startActivity(intent);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
               /* String dateDiff = IOUtils.getDateDifference(startDate, endDate);
                long days = Long.parseLong(dateDiff);
                if (days > 30) {
                    //Toast.makeText(getActivity(), "Date difference should not be more than 30 days", Toast.LENGTH_LONG).show();
                    MyDynamicToast.errorMessage(mContext, "Date difference should not be more than 30 days");
                } else {*/

                /* }*/
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void buttonListener(int pos) {

        }

        @Override
        public void viewListener(int pos) {

        }

        @Override
        public void sendGeoLocationListener() {

        }

        @Override
        public void holdListener(String date, String reason, String jobID, String nbfcName) {

        }


        @Override
        public void showHoldPopupListener(int pos) {

        }
    };


}
