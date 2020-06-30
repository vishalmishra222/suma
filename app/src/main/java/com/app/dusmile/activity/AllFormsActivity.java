package com.app.dusmile.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;


import com.app.dusmile.DBModel.SubCategory;
import com.app.dusmile.R;
import com.app.dusmile.adapter.ScreenSlidePagerAdapter;
import com.app.dusmile.constant.AppConstant;
import com.app.dusmile.database.LoginTemplateDB;
import com.app.dusmile.database.SubCategoryDB;
import com.app.dusmile.fragment.AllFormsFragment;
import com.app.dusmile.preferences.UserPreference;
import com.app.dusmile.utils.IOUtils;
import com.app.dusmile.utils.SimpleGestureFilter;
import com.example.sumaforms2.DBHelper;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;


public class AllFormsActivity extends AppCompatActivity implements  AllFormsFragment.OnFragmentInteractionListener {

    private ViewPager mPager;
    private ScreenSlidePagerAdapter mPagerAdapter;
    public static String subCategoryName;
    public static String job_id;
    private Toast toast;
    private static TextView tv_msg;
    private static LinearLayout parent_layout;
    private View toastRoot;
    public static String nbfcName,cityName,pinCode,mobileNo, totalAssignedTime, address,applicationFormNo, jobStartTime, coApplicantArray , jobType;
    public static String templateName;
    public static String response;
    JSONArray coApplicantJsonArr;
    private TabLayout tabLayout;
    public static boolean isAccept,hasCoApplicant,isCoApplicantExistsInTemplate;
   // private NavigationTabBar navigationTabBar;
    private List<SubCategory> subCategoryMenuList;
    private DBHelper dbHelper;
    private Toolbar toolbar;
    private TabHost tabHost;
    public static int menuImages[] = new int[]{R.drawable.forminfo,R.drawable.primaryinfo,R.drawable.resiinfo,

            R.drawable.personalinfo,R.drawable.loaninfo,R.drawable.addidproof,R.drawable.uploadimage,R.drawable.savelocation,R.drawable.firemark,R.drawable.resiinfo,R.drawable.personalinfo
           };
    private int cPosition;
    private LinearLayout layout_wheelview;
    private Animation slide_down;
    private Animation slide_up;
    private LinearLayout openCurve;
    private SimpleGestureFilter detector;
    public static String creationTime;
    private HorizontalScrollView imageHorizontalScrollView;
    private LinearLayout imagesLinearLayout;
    private BottomNavigationView bottomNavigationView;
    public static List<SubCategory> subCategoryMenusList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_forms);
        Intent intent = getIntent();
        response = intent.getExtras().getString("response");
        isAccept = intent.getExtras().getBoolean("isAccept");
        job_id = intent.getExtras().getString("job_id");
        nbfcName = intent.getExtras().getString("nbfcName");
        jobType = intent.getExtras().getString("templateName");
        creationTime = intent.getExtras().getString("creationTime");
        templateName = intent.getExtras().getString("templateName");
        subCategoryName = intent.getExtras().getString("subCategoryName");
        cityName = intent.getExtras().getString("cityName");
        pinCode = intent.getExtras().getString("pinCode");
        hasCoApplicant = intent.getExtras().getBoolean("hasCoApplicant");
        applicationFormNo = intent.getExtras().getString("applicationFormNo");
        mobileNo = intent.getExtras().getString("mobileNo");
        address = intent.getExtras().getString("address");
        totalAssignedTime = intent.getExtras().getString("totalAssignedTime");
        jobStartTime = intent.getExtras().getString("jobStartTime");
        coApplicantArray = intent.getExtras().getString("coApplicantArray");
        try {
            if(!TextUtils.isEmpty(coApplicantArray) && !(coApplicantArray.equalsIgnoreCase("-"))) {
                coApplicantJsonArr = new JSONArray(coApplicantArray);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        cPosition = intent.getExtras().getInt("position");
        mPager = (ViewPager) findViewById(R.id.pager);
        ///wheelView = (WheelView) findViewById(R.id.wheelview);
        dbHelper = DBHelper.getInstance(getApplicationContext());
       // layout_wheelview = (LinearLayout) findViewById(R.id.layout_wheelview);
        //navigationTabBar = (NavigationTabBar) findViewById(R.id.ntb_horizontal);
        subCategoryMenuList = new ArrayList<>();
        int loginJsonTemplateId = LoginTemplateDB.getLoginJsontemplateID(dbHelper,"Menu Details", UserPreference.getLanguage(getApplicationContext()));
        subCategoryMenuList = SubCategoryDB.getSubCategoriesDependsOnIsMenuFlag(dbHelper,String.valueOf(loginJsonTemplateId),"true");
        initView();
        //initAnimation();

        if(subCategoryMenuList.size()>0) {
            ArrayList<SubCategory> subCategoryArrayList = new ArrayList<>();
            ArrayList<SubCategory> subCategoryFilteredArrayList = new ArrayList<>();
            isCoApplicantExistsInTemplate = false;
            for(int i =0;i<subCategoryMenuList.size();i++)
            {
                if(subCategoryMenuList.get(i).getSubcategory_name().contains("CoApplicant"))
                {
                    try {
                        if(coApplicantJsonArr!=null) {
                            for (int k = 0; k < coApplicantJsonArr.length(); k++) {
                                try {
                                    if (coApplicantJsonArr.get(k).toString().equalsIgnoreCase(subCategoryMenuList.get(i).getSubcategory_name())) {
                                        subCategoryFilteredArrayList.add(subCategoryMenuList.get(i));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    isCoApplicantExistsInTemplate = true;
                }
                else
                {
                    subCategoryArrayList.add(subCategoryMenuList.get(i));
                    subCategoryFilteredArrayList.add(subCategoryMenuList.get(i));
                }
            }

            if(isCoApplicantExistsInTemplate && hasCoApplicant==false) {

                initViewPager(subCategoryArrayList);
               // setupTabIcons(subCategoryArrayList);
                //setNavigationTabs(subCategoryArrayList);
                subCategoryMenusList = subCategoryArrayList;

            }
            else
            {

                initViewPager(subCategoryFilteredArrayList);
               // setupTabIcons(subCategoryMenuList);
                //setNavigationTabs(subCategoryMenuList);
                subCategoryMenusList = subCategoryFilteredArrayList;

            }
        }

    }



    @Override
    protected void onResume() {
        super.onResume();

    }


    private void initViewPager(final List<SubCategory> subCategoryMenuList) {

        tabLayout.setupWithViewPager(mPager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), subCategoryMenuList);
        mPager.setAdapter(mPagerAdapter);
        mPager.setCurrentItem(cPosition,true);

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            boolean isSwipeEnabled = true;
            int positionOfPage = 0;
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    positionOfPage = position;
            }

            @Override
            public void onPageSelected(int position) {
              //  ImageView imageView = (ImageView) imageHorizontalScrollView.findViewWithTag("imageView"+position);
                if(!isSwipeEnabled)
                {
                    mPager.setCurrentItem(0);
                    mPager.getAdapter().notifyDataSetChanged();
                    showAToast("Please select option Person is Available to swipe to next screen");
                }
                else
                {
                    mPager.setCurrentItem(position);
                    mPager.getAdapter().notifyDataSetChanged();
                }
                    // mPager.destroyDrawingCache();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
               /* if(state==1||state==2) {*/
                if(AppConstant.isRedRadioButtonClicked)
                {
                    isSwipeEnabled = false;
                    positionOfPage = 0;
                }
                else
                {
                    isSwipeEnabled = true;
                }
                    Fragment f = (Fragment) mPager.getAdapter().instantiateItem(mPager, positionOfPage);
                    if (f instanceof AllFormsFragment) {
                        ((AllFormsFragment) f).saveData();
                    }

                // }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.info_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                IOUtils.showExitFormMessage(AllFormsActivity.this,getResources().getString(R.string.form_exit),AllFormsActivity.this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /*private void addMenusToScollView(final List<SubCategory> subCategoryMenuList)
    {
        final String[] colors = getResources().getStringArray(R.array.horizontal_ntb);
        imagesLinearLayout.removeAllViews();
        imagesLinearLayout.invalidate();
        for (int i = 0; i < subCategoryMenuList.size(); i++) {
            try {
                final ImageView imageView = new ImageView(getApplicationContext());
                LinearLayout.LayoutParams relparams = new LinearLayout.LayoutParams(50, 50);
                relparams.setMargins(10,5,5,10);
                imageView.setLayoutParams(relparams);
                if(subCategoryMenuList.size()>menuImages.length) {
                    imageView.setBackgroundResource(R.drawable.ic_launcher);

                }
                else
                {
                    imageView.setBackgroundResource(menuImages[i]);
                }
                imageView.setTag("imageView".concat(""+i));
                imageView.setId(i);

                imagesLinearLayout.addView(imageView);

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String tag = imageView.getTag().toString();
                        tag = tag.replace("imageView", "");
                        int position = Integer.parseInt(tag);
                        mPager.setCurrentItem(position);
                    }
                });
            }
            catch (Exception e)
            {

            }
        }

    }*/

   /* private void addMenusToScollView1(final List<SubCategory> subCategoryMenuList)
    {
        final String[] colors = getResources().getStringArray(R.array.horizontal_ntb);
      //  imagesLinearLayout.removeAllViews();
      //  imagesLinearLayout.invalidate();
        for (int i = 0; i < subCategoryMenuList.size(); i++) {
            try {
                Menu menu = bottomNavigationView.getMenu();
                menu.add(subCategoryMenuList.get(i).getSubcategory_name());
            }
            catch (Exception e)
            {

            }
        }

    }*/

  /*private void setNavigationTabs(final List<SubCategory> subCategoryMenuList) {
       final String[] colors = getResources().getStringArray(R.array.horizontal_ntb);
       final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
       for (int i = 0; i < subCategoryMenuList.size(); i++) {
               try {
                   models.add(
                           new NavigationTabBar.Model.Builder(
                                   getResources().getDrawable(menuImages[i]),
                                   Color.parseColor(colors[i]))
                                   .selectedIcon(getResources().getDrawable(menuImages[i]))
                                   .build()
                   );
               } catch (Exception e) {
                   models.add(
                           new NavigationTabBar.Model.Builder(
                                   getResources().getDrawable(R.drawable.ic_launcher),
                                   getResources().getColor(R.color.accent))
                                   .selectedIcon(getResources().getDrawable(R.drawable.ic_launcher))
                                   .build()
                   );
               }
       }


       navigationTabBar.setModels(models);
       navigationTabBar.setViewPager(mPager, cPosition);
       navigationTabBar.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
           @Override
           public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {

           }

           @Override
           public void onPageSelected(final int position) {

           }

           @Override
           public void onPageScrollStateChanged(final int state) {

           }
       });
   }*/

    @Override
    public void onBackPressed() {
        IOUtils.showExitFormMessage(AllFormsActivity.this,getResources().getString(R.string.form_exit),AllFormsActivity.this);


    }

    public void initView() {
         toolbar = (Toolbar) findViewById(R.id.app_bar);
         tabLayout = (TabLayout) findViewById(R.id.tablayout);
         //imageHorizontalScrollView = (HorizontalScrollView) findViewById(R.id.imagesHorizontalScrollView);
         //imagesLinearLayout = (LinearLayout) findViewById(R.id.imagesLinearLayout);
        // bottomNavigationView.setItemTextColor(getResources().getColor(R.color.accent));
         setSupportActionBar(toolbar);
         getSupportActionBar().setDisplayShowTitleEnabled(true);
         getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



    }
    private void showAToast(String message) {

        try {
            if (toast != null) {
                toast.cancel();
            }
           /* toast = Toast.makeText(mContext, message, Toast.LENGTH_SHORT);
            TextView toastMessage = (TextView) toast.getView().findViewById(android.R.id.message);
            toastMessage.setTextColor(Color.WHITE);
            toastMessage.setBackgroundColor(Color.RED);
            toast.show();*/
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            toastRoot = inflater.inflate(R.layout.my_toast, null);
            ImageView iv_left_icon = (ImageView) toastRoot.findViewById(R.id.iv_left_icon);
            tv_msg = (TextView) toastRoot.findViewById(R.id.tv_msg);
            parent_layout = (LinearLayout) toastRoot.findViewById(R.id.parent_layout);
            iv_left_icon.setVisibility(View.VISIBLE);
            iv_left_icon.setImageResource(android.R.drawable.ic_dialog_alert);
            tv_msg.setText(message);
            tv_msg.setTextColor(Color.WHITE);
            tv_msg.setTextSize(16);

//        parent_layout.setBackgroundDrawable(createToastBackground(context, parent_layout));
            parent_layout.setBackgroundResource(R.drawable.info_message_background);

            toast = new Toast(getApplicationContext());
            toast.setView(toastRoot);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}