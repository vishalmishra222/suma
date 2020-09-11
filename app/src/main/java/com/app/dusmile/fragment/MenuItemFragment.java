package com.app.dusmile.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.allenliu.badgeview.BadgeFactory;
import com.allenliu.badgeview.BadgeView;
import com.android.volley.error.VolleyError;
import com.app.dusmile.DBModel.AssignedJobs;
import com.app.dusmile.DBModel.ClientTemplate;
import com.app.dusmile.DBModel.SubCategory;
import com.app.dusmile.R;
import com.app.dusmile.activity.AllFormsActivity;
import com.app.dusmile.activity.ChangePasswordActivity;
import com.app.dusmile.activity.DusmileBaseActivity;
import com.app.dusmile.activity.JobsActivity;
import com.app.dusmile.activity.PendingUploadActivity;
import com.app.dusmile.activity.ReportActivity;
import com.app.dusmile.activity.ReportDetailsActivity;
import com.app.dusmile.activity.UploadActivity;
import com.app.dusmile.adapter.MenuItemRecyclerViewAdapter;

import com.app.dusmile.application.DusmileApplication;
import com.app.dusmile.connection.HttpVolleyRequest;
import com.app.dusmile.connection.MyListener;
import com.app.dusmile.constant.AppConstant;

import com.app.dusmile.database.AssignedJobsDB;

import com.app.dusmile.database.ClientTemplateDB;

import com.app.dusmile.database.LoginTemplateDB;
import com.app.dusmile.database.SubCategoryDB;
import com.app.dusmile.database.helper.DBHelper;
import com.app.dusmile.model.GetAssignJobCountModel;
import com.app.dusmile.model.GetJobCountModel;
import com.app.dusmile.model.MenuItems;
import com.app.dusmile.preferences.Const;
import com.app.dusmile.preferences.UserPreference;
import com.app.dusmile.utils.IOUtils;
import com.app.dusmile.utils.RecyclerItemClickListener;
import com.desai.vatsal.mydynamictoast.MyDynamicToast;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class MenuItemFragment extends Fragment {

    // TODO: Customize parameter argument names
    // TODO: Customize parameters
    private int mColumnCount = 3;
    private DBHelper dbHelper;
    private List<SubCategory> subCategoryMenuList;
    private List<SubCategory> falseSubCategoryMenuList;
    private String Tag = "MenuItemFragment";
    private OnListFragmentInteractionListener mListener;
    public int menuImagesWithForms[] = new int[]{R.drawable.available_jobs, R.drawable.assigned_jobs, R.drawable.completed_jobs,
            R.drawable.upload_pending, R.drawable.status_report, R.drawable.logout, R.drawable.newchangepassword
    };
    public int menuImages[] = new int[]{R.drawable.available_jobs, R.drawable.assigned_jobs, R.drawable.completed_jobs,
            R.drawable.status_report, R.drawable.logout, R.drawable.newchangepassword
    };
    private RecyclerView recyclerView;
    private Context mContext;
    private String p = null;
    String job_id, templateName, nbfcName;
    private HashMap<File, List<String>> pendingPdfListMap = new HashMap<>();
    private ProgressDialog assignJobCountProgressBar;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MenuItemFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static MenuItemFragment newInstance(int columnCount) {
        MenuItemFragment fragment = new MenuItemFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        dbHelper = DBHelper.getInstance(mContext);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        //written to handle unhandled exceptions
        // Set the adapter
        if (view instanceof RecyclerView) {
            mContext = view.getContext();
            recyclerView = (RecyclerView) view;
            addListerner();
        }
        getActivity().setTitle("Menu");
        IOUtils.stopLoading();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (IOUtils.isInternetPresent(mContext)) {
           // getAssignJobCount();
            setDataToAdapter();
        } else {
            IOUtils.stopLoading();
        }
    }

 /*   @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PendingPdfUploadFragment.PendingCount = requestCode;
    }*/

    private void addListerner() {
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(mContext, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String selectedSubCategoryName = subCategoryMenuList.get(position).getSubcategory_name();
                AssignedJobs assignedJobs = AssignedJobsDB.getJobsByProgress(dbHelper, "true");
                job_id = assignedJobs.getAssigned_jobId();
                String client_template_id = assignedJobs.getClient_template_id();
                ClientTemplate clientTemplate = ClientTemplateDB.getSingleClientTemplate(dbHelper, client_template_id);
                templateName = clientTemplate.getTemplate_name();
                nbfcName = clientTemplate.getClient_name();

                Intent intent;

                switch (selectedSubCategoryName) {
                    case "Available Jobs": //for available jobs
                        AppConstant.isAvilableJobs = true;
                        AppConstant.isAssinedJobs = false;
                        AppConstant.isCompletedJobs = false;
                        intent = new Intent(mContext, JobsActivity.class);
                        intent.putExtra("isAppExit", false);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                        break;//commented by netra
                    case "Assigned Jobs": //for assigned jobs
                        AppConstant.isAvilableJobs = false;
                        AppConstant.isAssinedJobs = true;
                        AppConstant.isCompletedJobs = false;
                        intent = new Intent(mContext, JobsActivity.class);
                        intent.putExtra("isAppExit", false);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                        break;
                    case "Completed Jobs": // for view completed jobs
                        AppConstant.isAvilableJobs = false;
                        AppConstant.isAssinedJobs = false;
                        AppConstant.isCompletedJobs = true;
                        intent = new Intent(mContext, JobsActivity.class);
                        intent.putExtra("isAppExit", false);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                        break;
                    case "Pending": //for status report
                        intent = new Intent(mContext, PendingUploadActivity.class);
                        intent.putExtra("isAppExit", false);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                        break;
                    case "Status Report": //for status report
                        intent = new Intent(mContext, ReportActivity.class);
                        intent.putExtra("isAppExit", false);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                        break;

                    case "Logout": //for logout
                        IOUtils.showLogoutMessage(mContext, mContext.getResources().getString(R.string.logout_app), getActivity());
                        break;
                    case "Change Password": //for ChangePasswordActivity
                        intent = new Intent(mContext, ChangePasswordActivity.class);
                        intent.putExtra("isAppExit", false);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                        break;
                    case "Jobs Submitted To Supervisor": //for status report
                        intent = new Intent(mContext, ReportDetailsActivity.class);
                        intent.putExtra("isAppExit", false);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                        break;
                }
                // }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));
    }

    private void setDataToAdapter() {
        subCategoryMenuList = new ArrayList<>();
        // falseSubCategoryMenuList = new ArrayList<>();
        int loginJsonTemplateId = LoginTemplateDB.getLoginJsontemplateID(dbHelper, "Menu Details", UserPreference.getLanguage(mContext));
        // subCategoryMenuList = SubCategoryDB.getSubCategoriesDependsOnCategory(dbHelper,String.valueOf(loginJsonTemplateId));
        subCategoryMenuList = SubCategoryDB.getSubCategoriesDependsOnIsMenuFlag(dbHelper, String.valueOf(loginJsonTemplateId), "false");
        //  falseSubCategoryMenuList = SubCategoryDB.getSubCategoriesDependsOnCategoryIdAndIsMenuFlag(dbHelper,String.valueOf(loginJsonTemplateId),"false");
        IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + " Setting menus on menu screen");
        //  subCategoryMenuList = falseSubCategoryMenuList;
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(mContext, mColumnCount));
        }
        List<MenuItems> menuItemsList = new ArrayList<>();
        for (int i = 0; i < subCategoryMenuList.size(); i++) {
            try {
                Resources resources = mContext.getResources();
                Drawable dIcon;
                if (subCategoryMenuList.size() > 6) {
                    if (!(subCategoryMenuList.get(i).getSubcategory_name().equalsIgnoreCase("logout") || subCategoryMenuList.get(i).getSubcategory_name().equalsIgnoreCase("change password"))) {//||subCategoryMenuList.get(i).getSubcategory_name().equalsIgnoreCase("Available Jobs")
                        String names = subCategoryMenuList.get(i).getIcon();
                        if (names.equals("") || names.equals(null)) {
                            dIcon = resources.getDrawable(R.drawable.ic_launcher);
                        } else {
                            if (names.contains(".png")) {
                                names = names.replace(".png", "");
                            }
                            int resourceId = resources.getIdentifier(names, "drawable",
                                    mContext.getPackageName());
                            dIcon = resources.getDrawable(resourceId);
                        }
                        MenuItems menuItems = new MenuItems(subCategoryMenuList.get(i).getSubcategory_name(), dIcon);
                        menuItemsList.add(menuItems);
                    }
                } else {
                    if (!(subCategoryMenuList.get(i).getSubcategory_name().equalsIgnoreCase("logout") || subCategoryMenuList.get(i).getSubcategory_name().equalsIgnoreCase("change password"))) {//||subCategoryMenuList.get(i).getSubcategory_name().equalsIgnoreCase("Available Jobs")
                        String names = subCategoryMenuList.get(i).getIcon();
                        if (names.equals("") || names.equals(null)) {
                            dIcon = resources.getDrawable(R.drawable.ic_launcher);
                        } else {
                            if (names.contains(".png")) {
                                names = names.replace(".png", "");
                            }
                            int resourceId = resources.getIdentifier(names, "drawable",
                                    mContext.getPackageName());
                            dIcon = resources.getDrawable(resourceId);
                        }
                        MenuItems menuItems = new MenuItems(subCategoryMenuList.get(i).getSubcategory_name(), dIcon);
                        menuItemsList.add(menuItems);
                    }
                }
            } catch (Exception e) {
                MenuItems menuItems = new MenuItems(subCategoryMenuList.get(i).getSubcategory_name(), ContextCompat.getDrawable(getContext(), R.drawable.ic_launcher));
                menuItemsList.add(menuItems);
            }

        }
        recyclerView.setAdapter(new MenuItemRecyclerViewAdapter(menuItemsList, mListener, mContext));
        //menuItemsList.size();
    }

    private void setBadge(View view, int count, int position) {
        /* if(position==0) {*/
        BadgeFactory.create(getActivity())
                .setTextColor(Color.BLACK)
                .setWidthAndHeight(27, 27)
                .setBadgeBackground(Color.parseColor("#ffffff"))
                .setTextSize(15)
                .setBadgeGravity(Gravity.RIGHT | Gravity.TOP)
                .setBadgeCount(count)
                .setShape(BadgeView.SHAPE_CIRCLE)
                .setSpace(0, 0)
                .bind(view);
        /* }*/
        /*if(position==1) {
            BadgeFactory.create(context)
                    .setTextColor(Color.RED)
                    .setWidthAndHeight(27, 27)
                    .setBadgeBackground(Color.parseColor("#ffffff"))
                    .setTextSize(15)
                    .setBadgeGravity(Gravity.RIGHT | Gravity.TOP)
                    .setBadgeCount("✔")
                    .setSpace(0, 0)
                    .bind(view);
        }*/

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(MenuItems item);
    }

    private void sendToNextActivity(int position) {
        int total = subCategoryMenuList.size();
        int firstMenus = 3;
        int dynamicMenus = falseSubCategoryMenuList.size();
        int lastMenus = dynamicMenus - 3;
        int middle = dynamicMenus + lastMenus;
        Intent intent = null;
        if (position == 0) {
            AppConstant.isAvilableJobs = true;
            AppConstant.isAssinedJobs = false;
            AppConstant.isCompletedJobs = false;
            intent = new Intent(mContext, JobsActivity.class);
            intent.putExtra("isAppExit", false);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        } else if (position == 1) {
            AppConstant.isAvilableJobs = false;
            AppConstant.isAssinedJobs = true;
            AppConstant.isCompletedJobs = false;
            intent = new Intent(mContext, JobsActivity.class);
            intent.putExtra("isAppExit", false);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        } else if (position == 2) {
            AppConstant.isAvilableJobs = false;
            AppConstant.isAssinedJobs = false;
            AppConstant.isCompletedJobs = true;
            intent = new Intent(mContext, JobsActivity.class);
            intent.putExtra("isAppExit", false);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        } else if (position >= firstMenus && position < middle - 1) {
            if (!TextUtils.isEmpty(job_id)) {
                intent = new Intent(mContext, AllFormsActivity.class);
                intent.putExtra("position", position - firstMenus);
                intent.putExtra("response", "");
                intent.putExtra("job_id", job_id);
                intent.putExtra("nbfcName", nbfcName);
                intent.putExtra("templateName", templateName);
                intent.putExtra("creationTime", "");
                intent.putExtra("isAccept", false);
                intent.putExtra("subCategoryName", DusmileBaseActivity.subCategoryName);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            } else {
                // Toast.makeText(getActivity(),"Please accept atleast one job",Toast.LENGTH_LONG).show();
                MyDynamicToast.errorMessage(mContext, "Please accept atleast one job");
            }
        } else if (position == middle - 1) {
            intent = new Intent(mContext, PendingUploadActivity.class);
            intent.putExtra("isAppExit", false);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        } else if (position == middle) {
            if (!TextUtils.isEmpty(job_id)) {
                intent = new Intent(mContext, UploadActivity.class);
                intent.putExtra("isAppExit", false);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            } else {
                MyDynamicToast.errorMessage(mContext, "Please accept atleast one job");
            }
        } else if (position == middle + 1) {
            intent = new Intent(mContext, ReportActivity.class);
            intent.putExtra("isAppExit", false);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        } else if (position == middle + 2) {
            IOUtils.showLogoutMessage(mContext, mContext.getResources().getString(R.string.logout_app), getActivity());
        } else if (position == middle + 3) {
            intent = new Intent(mContext, ChangePasswordActivity.class);
            intent.putExtra("isAppExit", false);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        }

    }

/*    public void getJobCount() {
        IOUtils.startLoading(mContext, "Please wait.....");
        IOUtils.appendLog(Tag + ": API " + new Const().REQUEST_GET_JOB_COUNT);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("reportName", "Job Count");
        } catch (JSONException e) {
            IOUtils.stopLoading();
            e.printStackTrace();
        } catch (Exception e) {
            IOUtils.stopLoading();
            e.printStackTrace();
        }
        new HttpVolleyRequest(mContext, jsonObject, new Const().REQUEST_GET_JOB_COUNT, listenerGetJobCount);
    }*/

/*
    MyListener listenerGetJobCount = new MyListener() {
        @Override
        public void success(Object obj) throws JSONException {

        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void success(Object obj, JSONObject jsonObject) throws JSONException {

            if (obj != null) {
                String jobCountResponse = obj.toString();
                try {
                    Gson gson = new Gson();
                    GetJobCountModel getJobCountModel = gson.fromJson(jobCountResponse, GetJobCountModel.class);
                    int availableJobsCnt = getJobCountModel.getAvailableJobs();
                    int assignedJobCnt = getJobCountModel.getAssignedJobs();
                    int completedJobCnt = getJobCountModel.getCompletedJobs();
                    UserPreference.writeInteger(mContext, UserPreference.AVAILABLE_CNT, availableJobsCnt);
                    UserPreference.writeInteger(mContext, UserPreference.ASSIGNED_CNT, assignedJobCnt);
                    UserPreference.writeInteger(mContext, UserPreference.COMPLETED_CNT, completedJobCnt);
                    setDataToAdapter();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            IOUtils.stopLoading();
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
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
*/

    private void getMapOfPendingPdf() {
        pendingPdfListMap.clear();
        IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + " get hashmap of pending pdf files list");
        File sdCard = Environment.getExternalStorageDirectory();
        File directory = new File(sdCard.getAbsolutePath() + "/Dusmile/pdf/" + UserPreference.getUserRecord(DusmileApplication.getAppContext()).getUsername());
        if (directory.isDirectory()) {
            for (File file : directory.listFiles()) {
                if (file.isDirectory()) {
                    List<String> imageList = new ArrayList<>();
                    File pdfFile = null;
                    for (File infileFile : file.listFiles()) {
                        String fileName = infileFile.getName();
                        if (fileName.toLowerCase().contains(".pdf".toLowerCase())) {
                            pdfFile = infileFile;
                        } else {
                            imageList.add(fileName.split("_")[0]);
                        }

                    }
                    if (pdfFile != null) {
                        pendingPdfListMap.put(pdfFile, imageList);
                    }
                }

            }
        }
    }

    public void getAssignJobCount() {
        IOUtils.startLoading(mContext, "Please wait.....");
        IOUtils.appendLog(Tag + ": API " + new Const().REQUEST_GET_JOB_COUNT);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("reportName", "Job Count");
        } catch (JSONException e) {
            IOUtils.stopLoading();
            e.printStackTrace();
        } catch (Exception e) {
            IOUtils.stopLoading();
            e.printStackTrace();
        }
        new HttpVolleyRequest(mContext, new Const().REQUEST_GET_JOB_COUNT + "/" + UserPreference.getUserRecord(mContext).getUserID(), listenerGetAssignJobCount);
    }

    MyListener listenerGetAssignJobCount = new MyListener() {
        @Override
        public void success(Object obj) throws JSONException {
            if (obj != null) {
                String jobCountResponse = obj.toString();
                try {
                    Gson gson = new Gson();
                    GetAssignJobCountModel getJobCountModel = gson.fromJson(jobCountResponse, GetAssignJobCountModel.class);
                    // int availableJobsCnt = getJobCountModel.getAvailableJobs();
                    int assignedJobCnt = getJobCountModel.getAssignedJobsCount();
                    DBHelper dbHelper = DBHelper.getInstance(getContext());
                    int pendingCount = AssignedJobsDB.getPendingJobsCount(dbHelper);
                    // int completedJobCnt = getJobCountModel.getCompletedJobs();
                    // UserPreference.writeInteger(mContext,UserPreference.AVAILABLE_CNT,availableJobsCnt);
                    //  int toalAssCount = assignedJobCnt-pendingCount;
                    int toalAssCount;
                    String p = checkStatus(mContext);
                    if (p == null) {
                        toalAssCount = assignedJobCnt - pendingCount;
                    } else {
                        toalAssCount = assignedJobCnt;
                    }
                    UserPreference.writeInteger(mContext, UserPreference.ASSIGNED_CNT, toalAssCount);
                    UserPreference.writeInteger(mContext, UserPreference.PENDING_CNT, pendingCount);
                    // UserPreference.writeInteger(mContext,UserPreference.COMPLETED_CNT,completedJobCnt);
                    setDataToAdapter();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            IOUtils.stopLoading();
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void success(Object obj, JSONObject jsonObject) throws JSONException {

        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
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

    public String checkStatus(Context mContext) throws JSONException {
        DBHelper dbHelper = DBHelper.getInstance(mContext);
        List<AssignedJobs> PendingList = AssignedJobsDB.getAllAssignedSubmittedJobs(dbHelper, "true");
        if (PendingList.size() > 0) {
            for (int i = 0; i < PendingList.size(); i++) {
                p = PendingList.get(i).getIs_submit();
            }
        }
        return p;
    }
}
