package com.app.dusmile.adapter;

import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import com.app.dusmile.DBModel.SubCategory;
import com.app.dusmile.R;
import com.app.dusmile.activity.AllFormsActivity;
import com.app.dusmile.application.DusmileApplication;
import com.app.dusmile.constant.AppConstant;
import com.app.dusmile.fragment.AllFormsFragment;
import com.app.dusmile.fragment.JobDetailsFragment;
import com.app.dusmile.fragment.MapUpdateFragment;
import com.app.dusmile.fragment.SaveLocationFragment;
import com.app.dusmile.fragment.UploadImageFragment;
import com.app.dusmile.utils.IOUtils;

import java.util.List;

/**
 * Created by sumasoft on 10/03/17.
 */

 public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
    private final List<SubCategory> subCategoryMenuList;

    public ScreenSlidePagerAdapter(FragmentManager fm,  List<SubCategory> subCategoryMenuList) {
        super(fm);
        this.subCategoryMenuList = subCategoryMenuList;
    }

    @Override
    public Fragment getItem(int position) {

       // IOUtils.startLoading(DusmileApplication.getAppContext(), "Retrieving Job Details...");
       /* if(position == subCategoryMenuList.size()-3)
        {
            return UploadImageFragment.newInstance(position);
        }
        else if(position == subCategoryMenuList.size()-2)
        {
           *//* return SaveLocationFragment.newInstance(position);*//*
            return MapUpdateFragment.newInstance(position);
        }
        else if(position == subCategoryMenuList.size()-1)
        {
            return AllFormsFragment.newInstance(subCategoryMenuList.size()-3);
        }
        else*/
       /*  {
            return AllFormsFragment.newInstance(position);
       }*/
            return AllFormsFragment.newInstance(position, subCategoryMenuList.get(position).getSubcategory_name());
    }

    @Override
    public int getCount() {
        return subCategoryMenuList.size();
    }

    /*public int getItemPosition(Object item) {
        if(item instanceof AllFormsFragment) {
            return POSITION_NONE;
        }
        else
        {
            return POSITION_UNCHANGED;
        }
    }*/

    @Override
    public CharSequence getPageTitle(int position) {
        Drawable image = null;
        try {
          image = DusmileApplication.getAppContext().getResources().getDrawable(AllFormsActivity.menuImages[position]);
        }
        catch (Exception e)
        {
            image = DusmileApplication.getAppContext().getResources().getDrawable(R.drawable.ic_launcher);
        }
        //image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
        image.setBounds(0, 0, 60,60);
        SpannableString sb = new SpannableString(" ");
        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }

}

