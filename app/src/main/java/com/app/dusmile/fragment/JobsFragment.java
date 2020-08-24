package com.app.dusmile.fragment;

import android.app.Activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.error.VolleyError;
import com.app.dusmile.DBModel.AssignedJobs;
import com.app.dusmile.DBModel.ClientTemplate;
import com.app.dusmile.DBModel.OfflineAssignedJobs;
import com.app.dusmile.DBModel.SubCategory;
import com.app.dusmile.DBModel.UpdatedTemplet;
import com.app.dusmile.R;
import com.app.dusmile.activity.AllFormsActivity;
import com.app.dusmile.activity.DusmileBaseActivity;
import com.app.dusmile.adapter.GenericAdapter;
import com.app.dusmile.application.DusmileApplication;
import com.app.dusmile.common.ConfirmTemplateUpdate;
import com.app.dusmile.common.TemplateOperations;
import com.app.dusmile.common.UpdateJobStatus;
import com.app.dusmile.connection.HttpVolleyRequest;
import com.app.dusmile.connection.MyListener;
import com.app.dusmile.constant.AppConstant;
import com.app.dusmile.database.AssignedJobsDB;
import com.app.dusmile.database.AssignedJobsStatusDB;
import com.app.dusmile.database.ClientTemplateDB;
import com.app.dusmile.database.LoginTemplateDB;
import com.app.dusmile.database.OfflineAssignedJobsDB;
import com.app.dusmile.database.SubCategoryDB;
import com.app.dusmile.database.helper.DBHelper;
import com.app.dusmile.interfaces.BtnClickListener;
import com.app.dusmile.model.JobsResources;
import com.app.dusmile.model.AvailableJobsDataModel;
import com.app.dusmile.model.HoldJobResponseModel;
import com.app.dusmile.model.JobDetailsResponse;
import com.app.dusmile.preferences.Const;
import com.app.dusmile.preferences.UserPreference;
import com.app.dusmile.utils.IOUtils;
import com.app.dusmile.utils.RecyclerItemClickListener;
import com.desai.vatsal.mydynamictoast.MyCustomToast;
import com.desai.vatsal.mydynamictoast.MyDynamicToast;
import com.google.gson.Gson;

import org.apache.commons.lang3.time.DateUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


/**
 * Created by suma on 30/01/17.
 */

public class JobsFragment extends Fragment {
    private String AURL = null;
    private String CURL = null;
    private Context mContext;
    private List<SubCategory> subCategoryMenuList;
    private RecyclerView recyclerView;
    private Gson gson;
    private TextView tv_msg;
    private LinearLayout parent_layout;
    private View toastRoot;
    private RecyclerView.LayoutManager mLayoutManager;
    private GenericAdapter genericAdapter;
    private EditText searchEditText;
    private Button cancelButton;
    TextView txtFromDate, txtToDate;
    private LinearLayout dateFilter;
    private JSONArray reportHeadersArray;
    private JSONArray reportDataArray;
    private JSONArray reportHeadersUIArray;
    JSONArray filterJsonArray;
    JSONArray cardHeadersKeyArray;
    JSONObject resourceJsonObj;
    private Toast toast;
    private String job_id, cityName, pincode, applicationFormNo, jobStartTime, totalAssignedTime, mobileNo, address, coApplicantArray;
    String hasCoApplicant;
    private int mYear, mMonth, mDay;
    String latestVersion;
    private String checkAssignedResp;
    private JobDetailsResponse jobDetailsResponseGlobal;
    private DBHelper dbHelper;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String Tag = "JobsFragment";
    public FragmentTransaction fragmentTransaction;

    public JobsFragment() {
        super();
    }

    boolean isAvailable;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.jobs_fragment, container, false);
        this.findViews(rootView);
        fragmentTransaction = getChildFragmentManager().beginTransaction();
        mLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(mLayoutManager);
        Activity activity = this.getActivity();
        mContext = activity;
        dbHelper = DBHelper.getInstance(mContext);
        this.searchJobListener();
        this.onCancelButtonClicked();
        this.fromDateClick();
        this.fromToClick();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        this.swipeRefreshListener();
        if (!AppConstant.isCallButtonClicked) {
            searchEditText.setText("");
            if (genericAdapter != null) {
                reportHeadersUIArray = new JSONArray();
                reportHeadersArray = new JSONArray();
                reportDataArray = new JSONArray();
                cardHeadersKeyArray = new JSONArray();
                genericAdapter.refresh(reportDataArray);
            }
            assignUrl();
            if (IOUtils.isInternetPresent(mContext)) {
                UpdateJobStatus.jobIdList.clear();
                if (AppConstant.isAssinedJobs) {
                    getAssignedJobs(AURL);
                    getActivity().setTitle(getString(R.string.assigned_jobs));
                } else {
                    dateFilter.setVisibility(View.VISIBLE);
                    getCompletedJobs(CURL);
                    getActivity().setTitle(getString(R.string.completed_jobs));
                }
            } else {
                if (AppConstant.isAssinedJobs) {
                    getActivity().setTitle(getString(R.string.assigned_jobs));
                    showOfflineAssignedJobs();
                } else {
                    IOUtils.showErrorMessage(mContext, "No internet connection");
                    if (AppConstant.isAssinedJobs) {
                        getActivity().setTitle(getString(R.string.assigned_jobs));
                    } else if (AppConstant.isCompletedJobs) {
                        getActivity().setTitle(getString(R.string.completed_jobs));
                    }
                }
            }
        }
        AppConstant.isCallButtonClicked = false;
    }

    private void assignUrl() {
        subCategoryMenuList = new ArrayList<>();
        int loginJsonTemplateId = LoginTemplateDB.getLoginJsontemplateID(dbHelper, "Menu Details", UserPreference.getLanguage(mContext));
        subCategoryMenuList = SubCategoryDB.getSubCategoriesDependsOnIsMenuFlag(dbHelper, String.valueOf(loginJsonTemplateId), "false");
        for (int i = 0; i <= subCategoryMenuList.size() - 1; i++) {
            String jobClicked = subCategoryMenuList.get(i).getSubcategory_name();
            String usrId = UserPreference.readString(mContext, UserPreference.USER_INT_ID, "");
            if (jobClicked.equalsIgnoreCase("Assigned Jobs")) {
                String act = subCategoryMenuList.get(i).getAction();
                String st = act.substring(1, act.length());
                String replaceString = st.replace("userId", usrId);
                AURL = new Const().BASE_URL + replaceString;
            }
            if (jobClicked.equalsIgnoreCase("Completed Jobs")) {
                String act = subCategoryMenuList.get(i).getAction();
                String st = act.substring(1, act.length());
                String replaceString = st.replace("userId", usrId);
                CURL = new Const().BASE_URL + replaceString;
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void findViews(View view) {

        recyclerView = (RecyclerView) view.findViewById(R.id.job_recycler_view);
        searchEditText = (EditText) view.findViewById(R.id.searchbox);
        cancelButton = (Button) view.findViewById(R.id.searchBoxCancel);
        txtFromDate = (TextView) view.findViewById(R.id.from_date);
        txtToDate = (TextView) view.findViewById(R.id.to_date);
        dateFilter = (LinearLayout) view.findViewById(R.id.dateFilter);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentTime = sdf.format(new Date());
        txtFromDate.setText(currentTime);
        txtToDate.setText(currentTime);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
    }

    public void setAdapter(JSONArray reportHeadersArray, JSONArray reportDataArray, JSONArray reportHeadersUIArray, JSONArray cardHeadersKeyArray) {
        genericAdapter = new GenericAdapter(mContext, reportHeadersArray, reportDataArray, reportHeadersUIArray, cardHeadersKeyArray, btnClickListener);
        genericAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(genericAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(mContext, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onItemLongClick(View view, int position) {
            }
        }));
    }

    private void searchJobListener() {
        searchEditText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence query, int start, int before, int count) {
                try {

                    query = query.toString().toLowerCase();
                    filterJsonArray = new JSONArray();
                    if (query.length() > 0) {
                        cancelButton.setVisibility(View.VISIBLE);
                        for (int i = 0; i < reportDataArray.length(); i++) {
                            try {
                                if (reportDataArray.get(i).toString().toLowerCase().contains(query.toString().toLowerCase())) {
                                    filterJsonArray.put(reportDataArray.get(i));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        if (filterJsonArray != null) {
                            if (filterJsonArray.length() == 0) {
                                showAToast("No Matching Records Found");
                            } else {
                            }
                            genericAdapter = new GenericAdapter(mContext, reportHeadersArray, filterJsonArray, reportHeadersUIArray, cardHeadersKeyArray, btnClickListener);
                            recyclerView.setAdapter(genericAdapter);
                        }

                    } else {
                        try {
                            cancelButton.setVisibility(View.GONE);
                            if (reportDataArray != null) {
                                genericAdapter = new GenericAdapter(mContext, reportHeadersArray, reportDataArray, reportHeadersUIArray, cardHeadersKeyArray, btnClickListener);
                                recyclerView.setAdapter(genericAdapter);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });

    }


    private void onCancelButtonClicked() {
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    searchEditText.setText("");
                    cancelButton.setVisibility(View.GONE);
                    if (reportDataArray != null) {
                        genericAdapter = new GenericAdapter(mContext, reportHeadersArray, reportDataArray, reportHeadersUIArray, cardHeadersKeyArray, btnClickListener);
                        recyclerView.setAdapter(genericAdapter);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void fromDateClick() {
        txtFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == txtFromDate) {
                    final Calendar c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                            new DatePickerDialog.OnDateSetListener() {
                                private String DayOfMonth;
                                private String MonthOfYear;

                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    if (monthOfYear < 10) {
                                        MonthOfYear = "0" + (monthOfYear + 1);
                                    } else {
                                        MonthOfYear = String.valueOf(monthOfYear);
                                    }
                                    if (dayOfMonth < 10) {
                                        DayOfMonth = "0" + dayOfMonth;
                                    } else {
                                        DayOfMonth = String.valueOf(dayOfMonth);
                                    }
                                    txtFromDate.setText(year + "-" + MonthOfYear + "-" + DayOfMonth);
                                    searchEditText.setText("");
                                    if (genericAdapter != null) {
                                        reportHeadersUIArray = new JSONArray();
                                        reportHeadersArray = new JSONArray();
                                        reportDataArray = new JSONArray();
                                        cardHeadersKeyArray = new JSONArray();
                                        genericAdapter.refresh(reportDataArray);
                                    }
                                    UpdateJobStatus.jobIdList.clear();
                                    getCompletedJobs(CURL);
                                    getActivity().setTitle(getString(R.string.completed_jobs));
                                }
                            }, mYear, mMonth, mDay);
                    datePickerDialog.show();
                }
            }
        });
    }

    private void fromToClick() {
        txtToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == txtToDate) {
                    final Calendar c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                            new DatePickerDialog.OnDateSetListener() {
                                private String DayOfMonth;
                                private String MonthOfYear;

                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    if (monthOfYear < 10) {
                                        MonthOfYear = "0" + (monthOfYear + 1);
                                    } else {
                                        MonthOfYear = String.valueOf(monthOfYear);
                                    }
                                    if (dayOfMonth < 10) {
                                        DayOfMonth = "0" + dayOfMonth;
                                    } else {
                                        DayOfMonth = String.valueOf(dayOfMonth);
                                    }
                                    txtToDate.setText(year + "-" + MonthOfYear + "-" + DayOfMonth);
                                    searchEditText.setText("");
                                    if (genericAdapter != null) {
                                        reportHeadersUIArray = new JSONArray();
                                        reportHeadersArray = new JSONArray();
                                        reportDataArray = new JSONArray();
                                        cardHeadersKeyArray = new JSONArray();
                                        genericAdapter.refresh(reportDataArray);
                                    }
                                    UpdateJobStatus.jobIdList.clear();
                                    getCompletedJobs(CURL);
                                    getActivity().setTitle(getString(R.string.completed_jobs));
                                }
                            }, mYear, mMonth, mDay);
                    datePickerDialog.show();
                }
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private String templateName, creationTime, nbfcName, productName;

    BtnClickListener btnClickListener = new BtnClickListener() {
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
        public void cancelListener() {

        }

        @Override
        public void audioListener() {

        }

        @Override
        public void clickListener(String val) {

        }

        @Override
        public void buttonListener(int pos) {
            try {
                JSONObject jsonObject = null;
                if (searchEditText.getText().toString().length() > 0) {
                    jsonObject = filterJsonArray.getJSONObject(pos);
                } else {
                    jsonObject = reportDataArray.getJSONObject(pos);
                }
                job_id = jsonObject.getString("job_id");
                if (jsonObject.has("NBFCName")) {
                    nbfcName = jsonObject.getString("NBFCName");
                } else {
                    nbfcName = new Const().DATABASE_NAME;
                }
                templateName = jsonObject.getString("JobType");
                if (jsonObject.has("city_Name")) {
                    cityName = jsonObject.getString("city_Name");
                }
                if (jsonObject.has("Product")) {
                    productName = jsonObject.getString("Product");
                }
                if (jsonObject.has("Pincode")) {
                    pincode = jsonObject.getString("Pincode");
                } else {
                    pincode = "-";
                }
                if (jsonObject.has("job_creation_date_STR")) {
                    creationTime = jsonObject.getString("job_creation_date_STR");
                }
                if (jsonObject.has("ApplicantOrCoApplicant")) {
                    hasCoApplicant = jsonObject.getString("ApplicantOrCoApplicant");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void viewListener(int pos) {
            try {
                JSONObject jsonObject = null;
                if (searchEditText.getText().toString().length() > 0) {
                    jsonObject = filterJsonArray.getJSONObject(pos);
                } else {
                    jsonObject = reportDataArray.getJSONObject(pos);
                }
                isAvailable = false;
                job_id = jsonObject.getString("job_id");
                //nbfcName = jsonObject.getString("NBFCName");
                if (jsonObject.has("city_Name")) {
                    cityName = jsonObject.getString("city_Name");
                }
                if (jsonObject.has("Pincode")) {
                    pincode = jsonObject.getString("Pincode");
                }
                if (jsonObject.has("Product")) {
                    productName = jsonObject.getString("Product");
                }
                templateName = jsonObject.getString("JobType");
                // creationTime = jsonObject.getString("RequestDateandTime");
                if (jsonObject.has("ApplicantOrCoApplicant")) {
                    hasCoApplicant = jsonObject.getString("ApplicantOrCoApplicant");
                }
                //applicationFormNo = jsonObject.getString("applicationFormNo");
                jobStartTime = jsonObject.getString("job_creation_date_STR");
                totalAssignedTime = jsonObject.getString("ASSIGNED_TAT");
                mobileNo = jsonObject.getString("Mobile");
                if (jsonObject.has("Address")) {
                    address = jsonObject.getString("Address");
                }
                if (jsonObject.has("CoApplicantSubprocesses")) {
                    coApplicantArray = jsonObject.getString("CoApplicantSubprocesses");
                }

                String jobStatus = jsonObject.getString("status");
                AppConstant.formPosition = 0;
                AssignedJobs assignedJobs = AssignedJobsDB.getJobsByJobId(dbHelper, job_id);
                String clientTemplateId = assignedJobs.getClient_template_id();
                String isSubmittedJob = assignedJobs.getIs_submit();
                if (!TextUtils.isEmpty(isSubmittedJob) && isSubmittedJob.equalsIgnoreCase("true") && !(jobStatus.equalsIgnoreCase("Mobile Upload Pending"))) {
                    MyDynamicToast.informationMessage(mContext, "You can not perform already submitted Job");
                } else if (!TextUtils.isEmpty(jobStatus) && jobStatus.equalsIgnoreCase("On Hold")) {
                    MyDynamicToast.informationMessage(mContext, "Sorry you can't perform this job. This job is on hold.");
                } else if (UpdateJobStatus.jobIdList.contains(job_id)) {
                    MyDynamicToast.informationMessage(mContext, "You can not perform already completed Job");
                } else {
                    if (!TextUtils.isEmpty(clientTemplateId)) {
                        TemplateOperations.getFormsFromDBAndInsertIntoMenus(Integer.parseInt(clientTemplateId), mContext);
                        sendToAllFormActivity(isAvailable, "");
                    } else {
                        String type = templateName;
                        getUpdatedTemplate(type + "/" + productName);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
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

    public void getCompletedJobs(String url) {
        IOUtils.startLoading(mContext, "Loading......");
        try {
            IOUtils.appendLog(Tag + " : " + IOUtils.getCurrentTimeStamp() + " API " + url);
            new HttpVolleyRequest(mContext, url, completedlistenerJobs);
        } catch (Exception e) {
            IOUtils.stopLoading();
            e.printStackTrace();
        }
    }

    MyListener completedlistenerJobs = new MyListener() {

        @Override
        public void success(Object obj) throws JSONException {
            try {
                IOUtils.stopLoading();
                if (obj != null) {
                    String response = obj.toString();
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.length() > 0) {
                        Log.d("DUSMILE", response);
                        reportHeadersArray = new JSONArray();
                        reportHeadersUIArray = new JSONArray();
                        cardHeadersKeyArray = new JSONArray();
                        reportDataArray = new JSONArray();
                        reportHeadersArray = jsonObject.getJSONArray("reportHeaders");
                        reportHeadersUIArray = jsonObject.getJSONArray("reportHeadersKeys");
                        cardHeadersKeyArray = jsonObject.getJSONArray("cardHeadersKeys");
                        resourceJsonObj = jsonObject.getJSONObject("resources");
                        getCompletedJobDataApi(jsonObject, resourceJsonObj);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                MyDynamicToast.errorMessage(mContext, "Unexpected Response");
            }
        }

        @Override
        public void success(Object obj, JSONObject jsonReqObject) throws JSONException {
            try {
                IOUtils.stopLoading();
                if (obj != null) {
                    String url = getURL();
                    String response = obj.toString();
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.length() > 0) {
                        Log.d("DUSMILE", response);
                        boolean status = jsonObject.getBoolean("success");
                        if (status == true) {
                            IOUtils.appendLog(Tag + " : " + IOUtils.getCurrentTimeStamp() + " API " + url + "\nRESPONSE " + String.valueOf(status));
                            reportHeadersArray = new JSONArray();
                            reportDataArray = new JSONArray();
                            reportHeadersUIArray = new JSONArray();
                            cardHeadersKeyArray = new JSONArray();
                            reportHeadersArray = jsonObject.getJSONArray("reportHeaders");
                            reportDataArray = jsonObject.getJSONArray("reportData");
                            reportHeadersUIArray = jsonObject.getJSONArray("reportHeadersKeys");
                            cardHeadersKeyArray = jsonObject.getJSONArray("cardHeadersKeys");
                            if (AppConstant.isAssinedJobs) {
                                DBHelper dbHelper = DBHelper.getInstance(mContext);
                                OfflineAssignedJobsDB.removeOfflineAssignedJobs(dbHelper);
                                OfflineAssignedJobs offlineAssignedJobs = new OfflineAssignedJobs();
                                offlineAssignedJobs.setOffline_assigned_jobs_json(response);
                                OfflineAssignedJobsDB.addOfflineAssignedJobsEntry(offlineAssignedJobs, dbHelper);
                                UserPreference.writeInteger(mContext, UserPreference.ASSIGNED_CNT, reportDataArray.length());
                            }
                            if (reportDataArray != null) {
                                if (reportDataArray.length() > 0) {
                                    setAdapter(reportHeadersArray, reportDataArray, reportHeadersUIArray, cardHeadersKeyArray);
                                } else {
                                    MyDynamicToast.informationMessage(mContext, "No Data Available");
                                }
                            }
                        } else {
                            String message = jsonObject.getString("message");
                            MyDynamicToast.informationMessage(mContext, message);
                        }
                    }
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
                    String url = getURL();
                    String responseBody = new String(volleyError.networkResponse.data, "utf-8");
                    JSONObject jsonObject = new JSONObject(responseBody);
                    String success = jsonObject.getString("success");
                    if (success.equals("false")) {
                        String message = jsonObject.getString("message");
                        MyDynamicToast.informationMessage(mContext, message);
                    } else {
                        MyDynamicToast.warningMessage(mContext, "Unable to Connect");
                        IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + " API " + url + "\nRESPONSE " + volleyError.getMessage());
                        if (volleyError.networkResponse != null && volleyError.networkResponse.statusCode == 800) {
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

    private void showJobAlreadyAssignedDialog() {
        IOUtils.showWarningMessage(getActivity(), getResources().getString(R.string.job_already_assigned));
    }

    private void showAToast(String message) {
        try {
            if (toast != null) {
                toast.cancel();
            }
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            toastRoot = inflater.inflate(R.layout.my_toast, null);
            ImageView iv_left_icon = (ImageView) toastRoot.findViewById(R.id.iv_left_icon);
            tv_msg = (TextView) toastRoot.findViewById(R.id.tv_msg);
            parent_layout = (LinearLayout) toastRoot.findViewById(R.id.parent_layout);
            iv_left_icon.setVisibility(View.VISIBLE);
            iv_left_icon.setImageResource(android.R.drawable.ic_dialog_alert);
            tv_msg.setText(message);
            tv_msg.setTextColor(Color.WHITE);
            tv_msg.setTextSize(16);
            parent_layout.setBackgroundResource(R.drawable.error_msg_back);
            toast = new Toast(mContext);
            toast.setView(toastRoot);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String removeSpaceFromUrl(String url) {
        String newUrl = null;
        newUrl = url.replaceAll(" ", "%20");
        return newUrl;
    }

    public void getUpdatedTemplate(String clientTemplateName) {
        String modefiedURL = removeSpaceFromUrl(clientTemplateName);
        IOUtils.startLoading(getActivity(), "Updating template... Please wait.....");
        IOUtils.appendLog(Tag + " : " + IOUtils.getCurrentTimeStamp() + " API " + new Const().REQUEST_GET_UPDATED_TEMPLATE);
        //JSONObject jsonObject = new JSONObject();
        new HttpVolleyRequest(mContext, new Const().REQUEST_GET_UPDATED_TEMPLATE + "/" + modefiedURL, listenerGetUpdatedTemplate);
    }

    MyListener listenerGetUpdatedTemplate = new MyListener() {
        @Override
        public void success(Object obj) throws JSONException {
            if (obj != null) {
                String updateTemplateResponse = obj.toString();
                try {
                    gson = new Gson();
                    UpdatedTemplet updatedTemplet = gson.fromJson(updateTemplateResponse, UpdatedTemplet.class);
                    TemplateOperations.saveUpdatedTemplateInDatabase(updatedTemplet, mContext);
                    //Update template update status
                    int clientTemplateId = 0;
                    List<Integer> updatedTemplateList = new ArrayList<>();
                    if (isAvailable) {
                        clientTemplateId = ClientTemplateDB.getClientTemplateID(dbHelper, templateName, nbfcName, UserPreference.getLanguage(mContext), latestVersion);
                        updatedTemplateList.add(Double.valueOf(latestVersion).intValue());
                    } else {
                        ClientTemplate clientTemplate = ClientTemplateDB.getSingleClientTemplateByTemplateNameClientLanguage(dbHelper, templateName, new Const().DATABASE_NAME, UserPreference.getLanguage(mContext), "false");
                        clientTemplateId = ClientTemplateDB.getClientTemplateID(dbHelper, templateName, new Const().DATABASE_NAME, UserPreference.getLanguage(mContext), clientTemplate.getVersion());
                        updatedTemplateList.add(Double.valueOf(clientTemplate.getVersion()).intValue());
                    }
                    if (clientTemplateId > 0) {
                        TemplateOperations.getFormsFromDBAndInsertIntoMenus(clientTemplateId, mContext);
                        if (isAvailable) {
                            //sendToAllFormActivity(isAvailable, checkAssignedResp);
                            AppConstant.isAvilableJobs = true;
                            AppConstant.isAssinedJobs = false;
                            AppConstant.isCompletedJobs = false;
                            //add entries in assigned table in case clientTemplate not found and new template downloaded.
                            handleResponseToCheckVersion(checkAssignedResp, jobDetailsResponseGlobal, null);
                            onResume();
                        } else {
                            sendToAllFormActivity(isAvailable, "");
                        }

                        String userID = UserPreference.getUserRecord(DusmileApplication.getAppContext()).getUserID();
                        ConfirmTemplateUpdate.sendTemplateUpdateStatusToserver(mContext, userID, templateName, null);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            IOUtils.stopLoading();
        }

        @Override
        public void success(Object obj1, JSONObject jsonObject) throws JSONException {

        }

        @Override
        public void failure(VolleyError volleyError) {
            try {
                IOUtils.stopLoading();
                if (volleyError != null) {
                    IOUtils.appendLog(Tag + " :" + IOUtils.getCurrentTimeStamp() + " API " + new Const().REQUEST_GET_UPDATED_TEMPLATE + "\nRESPONSE " + volleyError.getMessage().toString());
                    MyDynamicToast.warningMessage(mContext, "Unable to connect");
                    if (volleyError.networkResponse.statusCode == 800) {
                        IOUtils.sendUserToLogin(mContext, getActivity());
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

    private void sendToAllFormActivity(boolean isAvailable, String response) {
        Intent intent = new Intent(mContext, AllFormsActivity.class);
        nbfcName = new Const().DATABASE_NAME;
        intent.putExtra("response", response);
        intent.putExtra("isAccept", isAvailable);
        intent.putExtra("job_id", job_id);
        intent.putExtra("nbfcName", nbfcName);
        intent.putExtra("templateName", templateName);
        intent.putExtra("cityName", cityName);
        intent.putExtra("pinCode", pincode);
        intent.putExtra("productName", productName);
        intent.putExtra("templateName", templateName);
        intent.putExtra("creationTime", creationTime);
        intent.putExtra("hasCoApplicant", hasCoApplicant);
        intent.putExtra("applicationFormNo", applicationFormNo);
        intent.putExtra("mobileNo", mobileNo);
        intent.putExtra("address", address);
        intent.putExtra("coApplicantArray", coApplicantArray);
        intent.putExtra("totalAssignedTime", totalAssignedTime);
        intent.putExtra("jobStartTime", jobStartTime);
        intent.putExtra("position", 0);
        intent.putExtra("subCategoryName", DusmileBaseActivity.subCategoryName);
        if (isAvailable) {
            AppConstant.isAvilableJobs = true;
            AppConstant.isAssinedJobs = false;
            AppConstant.isCompletedJobs = false;
        } else {
            AppConstant.isAssinedJobs = true;
            AppConstant.isCompletedJobs = false;
            AppConstant.isAvilableJobs = false;
        }
        mContext.startActivity(intent);
    }


    private void swipeRefreshListener() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mContext != null) {
                    searchEditText.setText("");
                    onResume();
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


    }

    private String getURL() {
        String url = new Const().REQUEST_GET_AVAILABLE_JOBS;
        String UserID = UserPreference.readString(getActivity(), UserPreference.USER_INT_ID, null);
        if (AppConstant.isAssinedJobs) {
            if (!TextUtils.isEmpty(UserID)) {
                url = new Const().REQUEST_GET_ASSIGNED_JOBS + "/" + UserID;
            }
        } else if (AppConstant.isCompletedJobs) {
            url = new Const().REQUEST_GET_COMPLETED_JOBS + "/" + UserID;
        }
        return url;
    }


    private void showOfflineAssignedJobs() {
        try {
            DBHelper dbHelper = DBHelper.getInstance(mContext);
            ArrayList<OfflineAssignedJobs> allOfflineAssignedJobsJson = OfflineAssignedJobsDB.getAllOfflineAssignedJobsJson(dbHelper);
            if (allOfflineAssignedJobsJson != null && allOfflineAssignedJobsJson.size() > 0) {
                String offlineAssignedJobJson = allOfflineAssignedJobsJson.get(0).getOffline_assigned_jobs_json();
                if (!TextUtils.isEmpty(offlineAssignedJobJson)) {
                    try {
                        JSONObject jsonObject = new JSONObject(offlineAssignedJobJson);
                        String success = String.valueOf(jsonObject.getBoolean("success"));
                        if (!TextUtils.isEmpty(success) && success.equalsIgnoreCase("true")) {
                            IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + "Showing offline Assigned jobs");
                            reportHeadersArray = jsonObject.getJSONArray("reportHeaders");
                            reportDataArray = jsonObject.getJSONArray("reportData");
                            reportHeadersUIArray = jsonObject.getJSONArray("reportHeadersKeys");
                            cardHeadersKeyArray = jsonObject.getJSONArray("cardHeadersKeys");
                            if (reportDataArray != null) {
                                if (reportDataArray.length() > 0) {
                                    setAdapter(reportHeadersArray, reportDataArray, reportHeadersUIArray, cardHeadersKeyArray);
                                } else {
                                    MyDynamicToast.informationMessage(mContext, "No Data Available");
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    MyDynamicToast.errorMessage(mContext, "Something went wrong");
                }
            } else {
                MyDynamicToast.errorMessage(mContext, "Something went wrong");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void handleResponseToCheckVersion(String response, JobDetailsResponse jobDetailsResponse, ProgressDialog assignJobProgressBar) {
        try {
            String applicant_Json = "";
            JSONObject jsonObject = new JSONObject(response);
            if (response.contains("details")) {
                JSONObject details = jsonObject.getJSONObject("details");
                if (details.has("ProcessDataJSON")) {
                    JSONObject processDataJson = details.getJSONObject("ProcessDataJSON");
                    if (processDataJson.has("Applicant")) {
                        applicant_Json = processDataJson.getString("Applicant");
                    }
                }
            }
            IOUtils.appendLog(Tag + ": API " + new Const().REQUEST_JOB_DETAILS1 + "\nRESPONSE " + jobDetailsResponse.getSuccess().toString());
            String response_version_no = jobDetailsResponse.getLatestVersion();
            String app_language = UserPreference.getLanguage(mContext);
            ClientTemplate clientTemplate = ClientTemplateDB.getSingleClientTemplateAccVerionNo(dbHelper, templateName, nbfcName, app_language, response_version_no);
            String clientTemplateVersion = clientTemplate.getVersion();
            if (clientTemplate.getID() != null) {
                addEntryInDB(app_language, jobDetailsResponse, clientTemplate.getIs_deprecated(), applicant_Json);
                IOUtils.stopLoading();
            } else {
                if (!TextUtils.isEmpty(clientTemplateVersion)) {
                    getUpdatedTemplate(clientTemplateVersion);
                } else {
                    getUpdatedTemplate("Residence");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addEntryInDB(String app_language, JobDetailsResponse jobDetailsResponse, String isDeprecatedFlag, String applicant_Json) {
        AssignedJobs assignedJob = AssignedJobsDB.getJobsByJobId(dbHelper, jobDetailsResponse.getJOBID());
        if (AllFormsActivity.isAccept == true || assignedJob.getAssigned_jobId() == null) {
            ClientTemplate clientTemplate = ClientTemplateDB.getSingleClientTemplateByTemplateNameClientLanguage(dbHelper, templateName, nbfcName, app_language, isDeprecatedFlag);
            insertIntoAssignedJobs(clientTemplate.getID(), jobDetailsResponse.getJOBID(), applicant_Json);
        }
    }


    private void insertIntoAssignedJobs(String client_template_id, String jobID, String applicantJson) {

        IOUtils.appendLog(Tag + ": Inserting entries into assigned job");

        //do not add entry if its already present in db
        AssignedJobs assignedJobsExisting = AssignedJobsDB.getJobsByJobId(dbHelper, jobID);
        if (assignedJobsExisting.getID() == null) {

            SimpleDateFormat formatter = null;
            if (creationTime.contains("/")) {
                formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.US);
            } else {
                formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
            }
            Date oldDate = null;
            String endTime = null;
            try {
                oldDate = formatter.parse(creationTime);
                Date newDate = DateUtils.addHours(oldDate, 3);
                endTime = formatter.format(newDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            AssignedJobs assignedJobs = new AssignedJobs();
            assignedJobs.setClient_template_id(client_template_id);
            assignedJobs.setAssigned_jobId(jobID);
            assignedJobs.setOriginal_applicant_json(applicantJson);
            assignedJobs.setApplicant_json(applicantJson);
            assignedJobs.setJob_end_time(endTime);
            assignedJobs.setIS_IN_PROGRESS("true");
            assignedJobs.setIs_submit("false");
            AssignedJobsDB.addAssignedJobsEntry(assignedJobs, dbHelper);
        }
    }


    public void storeOfflineAssignedJobs(String url) {
        try {
            IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + " API " + url);
            new HttpVolleyRequest(mContext, url, listenerAssignedJobs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    MyListener listenerAssignedJobs = new MyListener() {

        @Override
        public void success(Object obj) throws JSONException {
            try {
                if (obj != null) {
                    String url = getURL();
                    String response = obj.toString();
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.length() > 0) {
                        Log.d("DUSMILE", response);
                        boolean status = jsonObject.getBoolean("success");
                        if (status == true) {
                            IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + " API " + url + "\nRESPONSE " + String.valueOf(status));
                            JSONArray reportHeadersArray = jsonObject.getJSONArray("reportHeaders");
                            JSONArray reportDataArray = jsonObject.getJSONArray("reportData");
                            JSONArray reportHeadersUIArray = jsonObject.getJSONArray("reportHeadersKeys");
                            DBHelper dbHelper = DBHelper.getInstance(mContext);
                            OfflineAssignedJobsDB.removeOfflineAssignedJobs(dbHelper);
                            OfflineAssignedJobs offlineAssignedJobs = new OfflineAssignedJobs();
                            offlineAssignedJobs.setOffline_assigned_jobs_json(response);
                            OfflineAssignedJobsDB.addOfflineAssignedJobsEntry(offlineAssignedJobs, dbHelper);
                            UserPreference.writeInteger(mContext, UserPreference.ASSIGNED_CNT, reportDataArray.length());
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                MyDynamicToast.errorMessage(mContext, "Unexpected Response");
            }

        }

        @Override
        public void success(Object obj, JSONObject jsonReqObject) throws JSONException {

        }

        @Override
        public void failure(VolleyError volleyError) {
            try {
                //IOUtils.stopLoading();
                if (volleyError != null) {
                    String url = getURL();
                    MyDynamicToast.warningMessage(mContext, "Unable to Connect");
                    IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + " API " + url + "\nRESPONSE " + volleyError.getMessage());
                    if (volleyError.networkResponse != null && volleyError.networkResponse.statusCode == 800) {
                        IOUtils.sendUserToLogin(mContext, getActivity());
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


    private void deleteJobDetailsIfJobDeAssigned(String jobId) {
        try {
            dbHelper = DBHelper.getInstance(getActivity());
            AssignedJobs assignedJobs = AssignedJobsDB.getJobsByJobId(dbHelper, jobId);
            if (!TextUtils.isEmpty(assignedJobs.getAssigned_jobId())) {
                AssignedJobsDB.removeJobByJobId(dbHelper, jobId);
                AssignedJobsStatusDB.removeJobStatusByJobId(dbHelper, jobId);
                if (UpdateJobStatus.jobIdList.contains(jobId)) {
                    UpdateJobStatus.jobIdList.remove(jobId);
                }
                File sdCard = Environment.getExternalStorageDirectory();
                File directory = new File(sdCard.getAbsolutePath() + "/Dusmile/pdf/" + UserPreference.getUserRecord(DusmileApplication.getAppContext()).getUsername() + "/" + jobId);
                if (directory.isDirectory()) {
                    String[] children = directory.list();
                    for (int i = 0; i < children.length; i++) {
                        new File(directory, children[i]).delete();
                    }
                    directory.delete();
                    IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + jobId + " Job is DeAssign. Deleted job info from table and from folder");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getAssignedJobs(String url) {

        IOUtils.startLoading(mContext, "Loading......");
        try {
            IOUtils.appendLog(Tag + " : " + IOUtils.getCurrentTimeStamp() + " API " + url);
            new HttpVolleyRequest(mContext, url, listenerReportMetaData);
        } catch (Exception e) {
            IOUtils.stopLoading();
            e.printStackTrace();
        }
    }

    MyListener listenerReportMetaData = new MyListener() {

        @Override
        public void success(Object obj) throws JSONException {
            try {
                IOUtils.stopLoading();
                if (obj != null) {
                    String url = getURL();
                    String response = obj.toString();
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.length() > 0) {
                        Log.d("DUSMILE", response);
                        reportHeadersArray = new JSONArray();
                        reportHeadersUIArray = new JSONArray();
                        cardHeadersKeyArray = new JSONArray();
                        reportDataArray = new JSONArray();
                        reportHeadersArray = jsonObject.getJSONArray("reportHeaders");
                        reportHeadersUIArray = jsonObject.getJSONArray("reportHeadersKeys");
                        cardHeadersKeyArray = jsonObject.getJSONArray("cardHeadersKeys");
                        resourceJsonObj = jsonObject.getJSONObject("resources");
                        getJobDataApi(jsonObject, resourceJsonObj);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                MyDynamicToast.errorMessage(mContext, "Unexpected Response");
            }

        }

        @Override
        public void success(Object obj, JSONObject jsonReqObject) throws JSONException {

        }

        @Override
        public void failure(VolleyError volleyError) {
            try {
                IOUtils.stopLoading();
                if (volleyError != null) {
                    String url = getURL();
                    String responseBody = new String(volleyError.networkResponse.data, "utf-8");
                    JSONObject jsonObject = new JSONObject(responseBody);
                    String success = jsonObject.getString("success");
                    if (success.equals("false")) {
                        String message = jsonObject.getString("message");
                        MyDynamicToast.informationMessage(mContext, message);
                    } else {
                        MyDynamicToast.warningMessage(mContext, "Unable to Connect");
                        IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + " API " + url + "\nRESPONSE " + volleyError.getMessage());
                        if (volleyError.networkResponse != null && volleyError.networkResponse.statusCode == 800) {
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

    private void getJobDataApi(JSONObject jsonObject, JSONObject resources) {
        IOUtils.startLoading(mContext, "Loading......");
        try {
            JSONObject reportHeaderKeys = jsonObject.getJSONObject("reportHeaderKeys");
            gson = new Gson();
            JobsResources jobsResources = gson.fromJson(String.valueOf(resources), JobsResources.class);
            String removeSlash = jobsResources.getDataApi().substring(1);
            String URL = new Const().BASE_URL + removeSlash;
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("reportHeaderKeys", reportHeaderKeys);
            new HttpVolleyRequest(mContext, jsonObject1, URL, listenerReporData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    MyListener listenerReporData = new MyListener() {

        @Override
        public void success(Object obj) throws JSONException {
        }

        @Override
        public void success(Object obj, JSONObject jsonReqObject) throws JSONException {
            try {
                IOUtils.stopLoading();
                if (obj != null) {
                    String response = obj.toString();
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.length() > 0) {
                        Log.d("DUSMILE", response);
                        reportDataArray = jsonObject.getJSONArray("reportData");
                        if (AppConstant.isAssinedJobs) {
                            DBHelper dbHelper = DBHelper.getInstance(mContext);
                            OfflineAssignedJobsDB.removeOfflineAssignedJobs(dbHelper);
                            OfflineAssignedJobs offlineAssignedJobs = new OfflineAssignedJobs();
                            offlineAssignedJobs.setOffline_assigned_jobs_json(response);
                            OfflineAssignedJobsDB.addOfflineAssignedJobsEntry(offlineAssignedJobs, dbHelper);
                            UserPreference.writeInteger(mContext, UserPreference.ASSIGNED_CNT, reportDataArray.length());
                        }
                        if (reportDataArray != null) {
                            if (reportDataArray.length() > 0) {
                                setAdapter(reportHeadersArray, reportDataArray, reportHeadersUIArray, cardHeadersKeyArray);
                            } else {
                                MyDynamicToast.informationMessage(mContext, "No Data Available");
                            }
                        }
                    }
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
                    MyDynamicToast.errorMessage(mContext, volleyError.getLocalizedMessage());
                } else {
                    MyDynamicToast.errorMessage(mContext, "Server Error !!");
                }
            } catch (Exception e) {
                e.printStackTrace();
                MyDynamicToast.errorMessage(mContext, "Server Error !!");
            }
        }
    };

    private void getCompletedJobDataApi(JSONObject jsonObject, JSONObject resources) {
        IOUtils.startLoading(mContext, "Loading......");
        try {
            JSONObject dateFilterJson = new JSONObject();
            String startD, endD;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String currentTime = sdf.format(new Date());
            String startDate = (String) txtFromDate.getText();
            String endDate = (String) txtToDate.getText();
            if (startDate != null) {
                startD = startDate;
            } else {
                startD = currentTime;
            }
            if (endDate != null) {
                endD = endDate;
            } else {
                endD = currentTime;
            }
            dateFilterJson.put("startDate", startD);
            dateFilterJson.put("endDate", endD);
            dateFilterJson.put("FOSExecutiveID", UserPreference.getUserRecord(mContext).getUserID());

            JSONObject reportHeaderKeysJson = jsonObject.getJSONObject("reportHeaderKeys");
            gson = new Gson();
            JobsResources jobsResources = gson.fromJson(String.valueOf(resources), JobsResources.class);
            String removeSlash = jobsResources.getDataApi().substring(1);
            String URL = new Const().BASE_URL + removeSlash;
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("reportHeaderKeys", reportHeaderKeysJson);
            jsonObject1.put("queryCriteria", dateFilterJson);
            new HttpVolleyRequest(mContext, jsonObject1, URL, listenerCompleted);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    MyListener listenerCompleted = new MyListener() {

        @Override
        public void success(Object obj) throws JSONException {
        }

        @Override
        public void success(Object obj, JSONObject jsonReqObject) throws JSONException {
            try {
                IOUtils.stopLoading();
                if (obj != null) {
                    String response = obj.toString();
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.length() > 0) {
                        Log.d("DUSMILE", response);
                        reportDataArray = jsonObject.getJSONArray("reportData");
                        if (AppConstant.isAssinedJobs) {
                            DBHelper dbHelper = DBHelper.getInstance(mContext);
                            OfflineAssignedJobsDB.removeOfflineAssignedJobs(dbHelper);
                            OfflineAssignedJobs offlineAssignedJobs = new OfflineAssignedJobs();
                            offlineAssignedJobs.setOffline_assigned_jobs_json(response);
                            OfflineAssignedJobsDB.addOfflineAssignedJobsEntry(offlineAssignedJobs, dbHelper);
                            UserPreference.writeInteger(mContext, UserPreference.ASSIGNED_CNT, reportDataArray.length());
                        }
                        if (reportDataArray != null) {
                            if (reportDataArray.length() > 0) {
                                setAdapter(reportHeadersArray, reportDataArray, reportHeadersUIArray, cardHeadersKeyArray);
                            } else {
                                MyDynamicToast.informationMessage(mContext, "No Data Available");
                            }
                        }
                    }
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
                    MyDynamicToast.errorMessage(mContext, volleyError.getLocalizedMessage());
                } else {
                    MyDynamicToast.errorMessage(mContext, "Server Error !!");
                }
            } catch (Exception e) {
                e.printStackTrace();
                MyDynamicToast.errorMessage(mContext, "Server Error !!");
            }
        }
    };
}
