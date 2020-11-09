package com.app.dusmile.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.allenliu.badgeview.BadgeFactory;
import com.allenliu.badgeview.BadgeView;
import com.android.volley.error.VolleyError;
import com.app.dusmile.DBModel.Category;
import com.app.dusmile.DBModel.SubCategory;
import com.app.dusmile.R;
import com.app.dusmile.activity.DusmileBaseActivity;
import com.app.dusmile.activity.UploadActivity;
import com.app.dusmile.connection.HttpVolleyRequest;
import com.app.dusmile.connection.MyListener;
import com.app.dusmile.constant.AppConstant;
import com.app.dusmile.database.AssignedJobsDB;
import com.app.dusmile.database.CategoryDB;
import com.app.dusmile.database.LoginTemplateDB;
import com.app.dusmile.database.SubCategoryDB;
import com.app.dusmile.database.helper.DBHelper;
import com.app.dusmile.model.CategorySubcategoryResponse;
import com.app.dusmile.model.GetAssignJobCountModel;
import com.app.dusmile.model.GetJobCountModel;
import com.app.dusmile.preferences.Const;
import com.app.dusmile.preferences.UserPreference;
import com.app.dusmile.unhandleException.TopExceptionHandler;
import com.app.dusmile.utils.IOUtils;
import com.desai.vatsal.mydynamictoast.MyDynamicToast;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.app.dusmile.utils.IOUtils.Tag;


/**
 * Fragment used for managing interactions for and presentation of a navigation drawer.
 * See the <a href="https://developer.android.com/design/patterns/navigation-drawer.html#Interaction">
 * design guidelines</a> for a complete explanation of the behaviors implemented here.
 */
public class NavigationDrawerFragment extends Fragment {

    /**
     * Remember the position of the selected item.
     */
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    /**
     * Per the design guidelines, you should show the drawer on launch until the user manually
     * expands it. This shared preference tracks this.
     */
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

    /**
     * A pointer to the current callbacks instance (the Activity).
     */
    private NavigationDrawerCallbacks mCallbacks;

    /**
     * Helper component that ties the action bar to the navigation drawer.
     */
    private ActionBarDrawerToggle mDrawerToggle;

    public DrawerLayout mDrawerLayout;

    private View mFragmentContainerView;

    private int mCurrentSelectedPosition = -1;
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;


    public static final int SIDEMENU_HOME = -1;

    private int selectedFragment = 0;
    private LinearLayout mLinearListView;
    private LinearLayout mLinearWelcomeView;
    private boolean isThirdViewClick = false;
    private TextView txtWelcome;
    private DBHelper dbHelper;
    int fragmentId;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    private Context context;
    private ProgressDialog countProgressBar;

    public NavigationDrawerFragment() {
    }

    private Typeface tfHelvetica;
    private RelativeLayout rlLogout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            // Read in the flag indicating whether or not the user has demonstrated awareness of the
            // drawer. See PREF_USER_LEARNED_DRAWER for details.
            dbHelper = DBHelper.getInstance(getActivity());
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
            mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

            if (savedInstanceState != null) {
                mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
                mFromSavedInstanceState = true;
            }


            // Select either the default item (0) or the last selected item.
            selectItem(SIDEMENU_HOME);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        context = (DusmileBaseActivity) getActivity();
        mLinearListView = (LinearLayout) layout.findViewById(R.id.linear_listview);
        mLinearWelcomeView = (LinearLayout) layout.findViewById(R.id.welcomeLayout);
        txtWelcome = (TextView) layout.findViewById(R.id.txtWelcome);
        txtWelcome.setText("Welcome " + UserPreference.getUserRecord(getActivity()).getUsername());
        mLinearWelcomeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return layout;
    }

    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        getJobCount();
    }

    /**
     * Users of this fragment must call this method to set up the navigation drawer interactions.
     *
     * @param fragmentId   The android:id of this fragment in its activity's layout.
     * @param drawerLayout The DrawerLayout containing this fragment's UI.
     */
    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
        this.fragmentId = fragmentId;
        this.drawerLayout = drawerLayout;
        this.toolbar = toolbar;
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        // set a custom shadow that overlays the main content when the drawer opens
        //   mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),                    /* host Activity */
                mDrawerLayout,                    /* DrawerLayout object */
                toolbar,             /* nav drawer image to replace 'Up' caret */
                R.string.navigation_drawer_open,  /* "open drawer" description for accessibility */
                R.string.navigation_drawer_close  /* "close drawer" description for accessibility */
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

                getActivity().invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                if (!mUserLearnedDrawer) {
                    // The user manually opened the drawer; store this flag to prevent auto-showing
                    // the navigation drawer automatically in the future.
                    mUserLearnedDrawer = true;
                    SharedPreferences sp = PreferenceManager
                            .getDefaultSharedPreferences(getActivity());
                    sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();
                }

                getActivity().invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                toolbar.setAlpha(1 - slideOffset / 2);
            }
        };

        // If the user hasn't 'learned' about the drawer, open it to introduce them to the drawer,
        // per the navigation drawer design guidelines.
        /*if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(mFragmentContainerView);
        }*/

        // Defer code dependent on restoration of previous instance state.
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        //  initList();
    }

    private void selectItem(int position) {

        // Utils.selectLanguage(getActivity());
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
        if (mCallbacks != null) {

            if (mCurrentSelectedPosition != position) {
                mCurrentSelectedPosition = position;

                mCallbacks.onNavigationDrawerItemSelected(position);
            }
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Forward the new configuration the drawer toggle component.
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    /**
     * Callbacks interface that all activities using this fragment must implement.
     */
    public static interface NavigationDrawerCallbacks {
        /**
         * Called when an item in the navigation drawer is selected.
         */
        void onNavigationDrawerItemSelected(String subCategoryName);

        void onNavigationDrawerItemSelected(int position);

    }


    public void initList() {
        List<Category> getMenuList = new ArrayList<>();
        try {
            mLinearListView.removeAllViews();
            mLinearListView.invalidate();
            int loginJsonId = LoginTemplateDB.getLoginJsontemplateID(dbHelper, "Menu Details", UserPreference.getLanguage(getActivity()));
            getMenuList = CategoryDB.getAllCategoriesDependsOnLoginJsonId(dbHelper, String.valueOf(loginJsonId));
           /* Category category = new Category();
            category.setLogin_json_template_id("0");
            category.setCategory_name(getString(R.string.pending_menu));
            category.setID("0");
            getMenuList.add(category);*/
                /*Category category1 = new Category();
                category1.setLogin_json_template_id("0");
                category1.setCategory_name(getString(R.string.upload_menu));
                category1.setID("0");
                getMenuList.add(category1);*/
            for (int i = 0; i < getMenuList.size(); i++) {

                LayoutInflater inflater = null;
                inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View mLinearView = inflater.inflate(R.layout.row_first, null);

                final TextView mProductName = (TextView) mLinearView.findViewById(R.id.textViewName);
                final RelativeLayout mLinearFirstArrow = (RelativeLayout) mLinearView.findViewById(R.id.linearFirst);
                final ImageView mImageArrowFirst = (ImageView) mLinearView.findViewById(R.id.imageFirstArrow);
                final LinearLayout mLinearScrollSecond = (LinearLayout) mLinearView.findViewById(R.id.linear_scroll);
                mLinearScrollSecond.setTag("subcategory" + i);
                mImageArrowFirst.setTag("img_subcategory" + i);
                final int finalI = i;
                mLinearScrollSecond.setVisibility(View.GONE);
                final List<SubCategory> subCategoryList = SubCategoryDB.getSubCategoriesDependsOnCategoryIdAndIsMenuFlag(dbHelper, getMenuList.get(i).getID(), "false");


               /* if (i < getMenuList.size()) {
                    if (AppConstant.isFirstViewClick == false) {

                        if (getMenuList.size() > 0) {
                            mImageArrowFirst.setBackgroundResource(android.R.drawable.arrow_down_float);
                        }
                    } else {
                        if (subCategoryList.size() > 0) {
                            mImageArrowFirst.setBackgroundResource(android.R.drawable.arrow_up_float);
                        }
                    }*/
                if (!getMenuList.get(i).getCategory_name().equalsIgnoreCase(getString(R.string.pending_menu)) && !getMenuList.get(i).getCategory_name().equalsIgnoreCase(getString(R.string.upload_menu))) {
                    mImageArrowFirst.setVisibility(View.VISIBLE);
                    mImageArrowFirst.setBackgroundResource(android.R.drawable.arrow_down_float);
                } else {
                    mImageArrowFirst.setVisibility(View.GONE);
                }

                final String name = getMenuList.get(i).getCategory_name();
                mLinearFirstArrow.setTag(getMenuList.get(i).getID());
                mProductName.setText(name);
                final List<Category> finalGetMenuList = getMenuList;
                mLinearFirstArrow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (int i = 0; i < finalGetMenuList.size(); i++) {
                            LinearLayout addedSecondViews = (LinearLayout) mLinearListView.findViewWithTag("subcategory" + i);
                            ImageView addedImageView = (ImageView) mLinearListView.findViewWithTag("img_subcategory" + i);
                            if (mLinearScrollSecond.getTag() != addedSecondViews.getTag()) {
                                addedSecondViews.setVisibility(View.GONE);
                                addedImageView.setBackgroundResource(android.R.drawable.arrow_down_float);
                            } else {
                                if (mLinearScrollSecond.getVisibility() == View.VISIBLE) {
                                    addedSecondViews.setVisibility(View.GONE);
                                    addedImageView.setBackgroundResource(android.R.drawable.arrow_down_float);
                                } else {
                                    addedSecondViews.setVisibility(View.VISIBLE);
                                    mLinearScrollSecond.setVisibility(View.VISIBLE);
                                    addedImageView.setBackgroundResource(android.R.drawable.arrow_up_float);
                                }

                            }
                        }
                        if (finalGetMenuList.get(finalI).getCategory_name().equalsIgnoreCase(getString(R.string.pending_menu))) {
                            mCallbacks.onNavigationDrawerItemSelected(finalGetMenuList.get(finalI).getCategory_name());
                        }
                    }

                });


                for (int j = 0; j < subCategoryList.size(); j++) {

                    LayoutInflater inflater2 = null;
                    inflater2 = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View mLinearView2 = inflater2.inflate(R.layout.row_second, null);

                    final TextView mSubItemName = (TextView) mLinearView2.findViewById(R.id.textViewTitle);
                    final RelativeLayout mLinearSecondArrow = (RelativeLayout) mLinearView2.findViewById(R.id.linearSecond);
                    final ImageView mImageArrowSecond = (ImageView) mLinearView2.findViewById(R.id.imageSecondArrow);
                    final LinearLayout mLinearScrollThird = (LinearLayout) mLinearView2.findViewById(R.id.linear_scroll_third);
                    final int finalJ = j;

                    mLinearSecondArrow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // if (AppConstant.isSecondViewClick == false) {
                            AppConstant.isSecondViewClick = true;
                            mLinearScrollThird.setVisibility(View.VISIBLE);
                            mLinearScrollSecond.setVisibility(View.GONE);
                            mImageArrowFirst.setBackgroundResource(android.R.drawable.arrow_down_float);
                            // mSubItemName.setTextColor(ContextCompat.getColor(getActivity(), R.color.clrToolbarBg));
                            mCallbacks.onNavigationDrawerItemSelected(subCategoryList.get(finalJ).getSubcategory_name());
                           /* } else {
                                mSubItemName.setTextColor(ContextCompat.getColor(getActivity(),R.color.black));
                                AppConstant.isSecondViewClick = false;
                                mLinearScrollThird.setVisibility(View.GONE);
                            }*/
                        }
                    });
                    final String subName = subCategoryList.get(finalJ).getSubcategory_name();
                    mSubItemName.setText(subName);

                    if (subName.equalsIgnoreCase("Available Jobs")) {
                        if (UserPreference.readInteger(context, UserPreference.AVAILABLE_CNT, 0) > 0) {
                            setBadge(mImageArrowFirst, UserPreference.readInteger(context, UserPreference.AVAILABLE_CNT, 0), finalI, finalJ);
                        }
                    } else if (subName.equalsIgnoreCase("Assigned Jobs")) {
                        if (UserPreference.readInteger(context, UserPreference.ASSIGNED_CNT, 0) > 0) {
                            setBadge(mImageArrowSecond, UserPreference.readInteger(context, UserPreference.ASSIGNED_CNT, 0), finalI, finalJ);
                        }
                    }
                    mLinearScrollSecond.addView(mLinearView2);
                }
                mLinearListView.addView(mLinearView);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    private void setBadge(View view, int count, int i, int j) {
        /* if(j==0 && i==0) {*/
        BadgeFactory.create(getActivity())
                .setTextColor(Color.BLACK)
                .setWidthAndHeight(25, 25)
                .setBadgeBackground(Color.parseColor("#ffffff"))
                .setTextSize(15)
                .setBadgeGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL)
                .setBadgeCount(count)
                .setShape(BadgeView.SHAPE_OVAL)
                .setSpace(30, 0)
                .bind(view);
        /*  }*/
       /* if(j==1 && i==0)
        {
            BadgeFactory.create(getActivity())
                    .setTextColor(Color.RED)
                    .setWidthAndHeight(25, 25)
                    .setBadgeBackground(Color.parseColor("#ffffff"))
                    .setTextSize(15)
                    .setBadgeGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL)
                    .setBadgeCount("✔")
                    .setShape(BadgeView.SHAPE_CIRCLE)
                    .setSpace(30, 0)
                    .bind(view);
        }*/
    }

    public void getJobCount() {
        IOUtils.startLoading(context, "Please wait.....");
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
        IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + " API " + new Const().REQUEST_GET_JOB_COUNT + "\nREQUEST " + jsonObject.toString());
        new HttpVolleyRequest(context, new Const().REQUEST_GET_JOB_COUNT + "/" + UserPreference.getUserRecord(context).getUserID(), listenerGetAssignJobCount);
    }

    MyListener listenerGetAssignJobCount = new MyListener() {
        @Override
        public void success(Object obj) throws JSONException {
            if (obj != null) {
                String jobCountResponse = obj.toString();
                IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + " API " + new Const().REQUEST_GET_JOB_COUNT + "\nRESPONSE " + jobCountResponse.toString());
                try {
                    Gson gson = new Gson();
                    GetAssignJobCountModel getJobCountModel = gson.fromJson(jobCountResponse, GetAssignJobCountModel.class);
                    int assignedJobCnt = getJobCountModel.getAssignedJobsCount();
                    UserPreference.writeInteger(context, UserPreference.ASSIGNED_CNT, assignedJobCnt);
                    initList();
                } catch (Exception e) {
                    IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + " API " + new Const().REQUEST_GET_JOB_COUNT + "\nRESPONSE " + jobCountResponse.toString());
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
                IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + " API " + new Const().REQUEST_GET_JOB_COUNT + "\nRESPONSE FAILED" + volleyError.networkResponse.toString() + " " + volleyError.getMessage());
                if (volleyError != null) {
                    MyDynamicToast.warningMessage(context, "Unable to connect");
                    if (volleyError.networkResponse.statusCode == 800) {
                        IOUtils.sendUserToLogin(context, getActivity());
                    }
                } else {
                    MyDynamicToast.errorMessage(context, "Server Error !!");
                }
            } catch (Exception e) {
                e.printStackTrace();
                IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + " API " + new Const().REQUEST_GET_JOB_COUNT + "\nRESPONSE EXCEPTION" + volleyError.networkResponse.toString() + " " + volleyError.getMessage());
                MyDynamicToast.errorMessage(context, "Server Error !!");
            }
        }
    };

}

