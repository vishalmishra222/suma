package com.app.dusmile.DBModel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by suma on 14/03/17.
 */

public class Category implements Parcelable{

    String ID;
    String category_name;
    String login_json_template_id;
    public Category()
    {

    }

    protected Category(Parcel in) {
        ID = in.readString();
        category_name = in.readString();
        login_json_template_id = in.readString();
    }


    public String getLogin_json_template_id() {
        return login_json_template_id;
    }

    public void setLogin_json_template_id(String login_json_template_id) {
        this.login_json_template_id = login_json_template_id;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public static Creator<Category> getCREATOR() {
        return CREATOR;
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ID);
        dest.writeString(category_name);
        dest.writeString(login_json_template_id);
    }
}
