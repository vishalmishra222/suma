package com.app.dusmile.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.error.VolleyError;
import com.app.dusmile.DBModel.OfflineAssignedJobs;
import com.app.dusmile.DBModel.SubCategory;
import com.app.dusmile.R;
import com.app.dusmile.activity.ReportDetailsActivity;
import com.app.dusmile.adapter.ReportDetailsAdapter;
import com.app.dusmile.connection.HttpVolleyRequest;
import com.app.dusmile.connection.MyListener;
import com.app.dusmile.constant.AppConstant;
import com.app.dusmile.database.LoginTemplateDB;
import com.app.dusmile.database.OfflineAssignedJobsDB;
import com.app.dusmile.database.SubCategoryDB;
import com.app.dusmile.database.helper.DBHelper;
import com.app.dusmile.model.JobsResources;
import com.app.dusmile.preferences.Const;
import com.app.dusmile.preferences.UserPreference;
import com.app.dusmile.utils.IOUtils;
import com.app.dusmile.utils.RecyclerItemClickListener;
import com.desai.vatsal.mydynamictoast.MyDynamicToast;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by suma on 03/02/17.
 */

public class ReportDetailsFragment extends Fragment {
    private Context mContext;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ReportDetailsAdapter reportDetailsAdapter;
    private EditText searchEditText;
    private Button cancelButton;
    public FragmentTransaction fragmentTransaction;
    private Toast toast;
    private List<SubCategory> subCategoryMenuList;
    private String PURL = null;
    private DBHelper dbHelper;
    private TextView tv_msg;
    private LinearLayout parent_layout;
    private SwipeRefreshLayout swipeRefreshLayout;
    private View toastRoot;
    private String Tag = "ReportDetailsFragment";
    private JSONArray reportHeadersArray;
    private JSONArray reportDataArray;
    private JSONArray reportHeadersUIArray;
    private JSONArray cardHeadersKeyArray;
    JSONObject resourceJsonObj;
    private Button exportBtn;
    private Gson gson;
    JSONArray filterJsonArray;
    JSONArray exportJsonArray = new JSONArray();

    public ReportDetailsFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_report_details, container, false);
        fragmentTransaction = getChildFragmentManager().beginTransaction();
        mLayoutManager = new LinearLayoutManager(mContext);
        this.findViews(rootView);
        recyclerView.setLayoutManager(mLayoutManager);
        dbHelper = DBHelper.getInstance(mContext);
        Activity activity = this.getActivity();
        mContext = activity;
        this.searchReportListener();
        this.onCancelButtonClicked();
        //this.onExportButtonClicked();
        this.swipeRefreshListener();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        searchEditText.setText("");
        if (reportDetailsAdapter != null) {
            reportHeadersUIArray = new JSONArray();
            reportHeadersArray = new JSONArray();
            reportDataArray = new JSONArray();
            cardHeadersKeyArray = new JSONArray();
            reportDetailsAdapter.refresh(reportDataArray);
            reportDetailsAdapter.notifyDataSetChanged();
        }
        String date = ReportDetailsActivity.date;

        if (IOUtils.isInternetPresent(mContext)) {
            assignUrl();
            String url = new Const().GET_PENDING_WITH_BRANCH_REPORT_METADATA;
            getReportDetails(PURL);
        } else {
            IOUtils.showErrorMessage(mContext, "No internet connection");
        }
    }


    private void assignUrl() {
        subCategoryMenuList = new ArrayList<>();
        int loginJsonTemplateId = LoginTemplateDB.getLoginJsontemplateID(dbHelper, "Menu Details", UserPreference.getLanguage(mContext));
        subCategoryMenuList = SubCategoryDB.getSubCategoriesDependsOnIsMenuFlag(dbHelper, String.valueOf(loginJsonTemplateId), "false");
        for (int i = 0; i <= subCategoryMenuList.size() - 1; i++) {
            String jobClicked = subCategoryMenuList.get(i).getSubcategory_name();
            String usrId = UserPreference.readString(mContext, UserPreference.USER_INT_ID, "");
            if (jobClicked.equalsIgnoreCase("Jobs Submitted To Supervisor")) {
                String act = subCategoryMenuList.get(i).getAction();
                String st = act.substring(1, act.length());
                String replaceString = st.replace("userId", usrId);
                PURL = new Const().BASE_URL + replaceString;
            }
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

        recyclerView = (RecyclerView) view.findViewById(R.id.report_recycler_view);
        searchEditText = (EditText) view.findViewById(R.id.searchbox);
        cancelButton = (Button) view.findViewById(R.id.searchBoxCancel);
        exportBtn = (Button) view.findViewById(R.id.Exportbtn);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
    }

    public void getReportDetails(String url) {

        IOUtils.startLoading(mContext, "Loading......");

        JSONObject criteriajsonObject = new JSONObject();

        try {
            criteriajsonObject.put("FOSExecutiveID", UserPreference.getUserRecord(mContext).getUserID());
        } catch (JSONException e) {
            IOUtils.stopLoading();
            e.printStackTrace();
        } catch (Exception e) {
            IOUtils.stopLoading();
            e.printStackTrace();
        }
        new HttpVolleyRequest(mContext, url, listenerJobs);
    }

    MyListener listenerJobs = new MyListener() {
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
                        getJobDataApi(jsonObject, resourceJsonObj);
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

    public void setAdapter(JSONArray reportHeadersArray, JSONArray reportDataArray, JSONArray reportHeadersUIArray, JSONArray cardHeadersKeyArray) {
        IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + " " + "Showing reports in List");
        reportDetailsAdapter = new ReportDetailsAdapter(mContext, reportHeadersArray, reportDataArray, reportHeadersUIArray, cardHeadersKeyArray);

        recyclerView.setAdapter(reportDetailsAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(mContext, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onItemLongClick(View view, int position) {
                // ...
            }
        }));

    }

    private void searchReportListener() {
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
                                //exportBtn.setVisibility(View.VISIBLE);
                            }
                            reportDetailsAdapter = new ReportDetailsAdapter(mContext, reportHeadersArray, filterJsonArray, reportHeadersUIArray, cardHeadersKeyArray);
                            recyclerView.setAdapter(reportDetailsAdapter);

                        }

                    } else {
                        try {
                            cancelButton.setVisibility(View.GONE);
                            if (reportDataArray != null) {
                                if (reportDataArray.length() == 0) {
                                    showAToast("No Data Available");
                                } else {
                                }
                                reportDetailsAdapter = new ReportDetailsAdapter(mContext, reportHeadersArray, reportDataArray, reportHeadersUIArray, cardHeadersKeyArray);
                                recyclerView.setAdapter(reportDetailsAdapter);
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
                        if (reportDataArray.length() == 0) {
                            showAToast("No Data Available");
                        } else {
                        }
                        reportDetailsAdapter = new ReportDetailsAdapter(mContext, reportHeadersArray, reportDataArray, reportHeadersUIArray, cardHeadersKeyArray);
                        recyclerView.setAdapter(reportDetailsAdapter);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
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
}
