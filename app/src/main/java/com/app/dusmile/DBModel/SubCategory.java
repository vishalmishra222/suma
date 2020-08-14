package com.app.dusmile.DBModel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by suma on 14/03/17.
 */

public class SubCategory implements Parcelable {
    String ID;
    String category_id;
    String subcategory_name;
    String sequence_no;
    String isFormMenu;
    String icon;
    String action;
    public SubCategory()
    {

    }

    protected SubCategory(Parcel in) {
        ID = in.readString();
        category_id = in.readString();
        subcategory_name = in.readString();
        sequence_no = in.readString();
        isFormMenu = in.readString();
        icon = in.readString();
        action = in.readString();
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getSubcategory_name() {
        return subcategory_name;
    }

    public void setSubcategory_name(String subcategory_name) {
        this.subcategory_name = subcategory_name;
    }

    public String getIsFormMenu() {
        return isFormMenu;
    }

    public void setIsFormMenu(String isFormMenu) {
        this.isFormMenu = isFormMenu;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getSequence_no() {
        return sequence_no;
    }

    public void setSequence_no(String sequence_no) {
        this.sequence_no = sequence_no;
    }

    public static Creator<SubCategory> getCREATOR() {
        return CREATOR;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ID);
        dest.writeString(category_id);
        dest.writeString(subcategory_name);
        dest.writeString(sequence_no);
        dest.writeString(isFormMenu);
        dest.writeString(icon);
        dest.writeString(action);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SubCategory> CREATOR = new Creator<SubCategory>() {
        @Override
        public SubCategory createFromParcel(Parcel in) {
            return new SubCategory(in);
        }

        @Override
        public SubCategory[] newArray(int size) {
            return new SubCategory[size];
        }
    };
}
