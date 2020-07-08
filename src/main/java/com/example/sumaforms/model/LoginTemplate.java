package com.example.sumaforms.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by suma on 14/03/17.
 */

public class LoginTemplate implements Parcelable{

    String ID;
    String json_key;
    String json_value;
     String language;
    public LoginTemplate() {

    }


    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getJson_key() {
        return json_key;
    }

    public void setJson_key(String json_key) {
        this.json_key = json_key;
    }

    public String getJson_value() {
        return json_value;
    }

    public void setJson_value(String json_value) {
        this.json_value = json_value;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public static Creator<LoginTemplate> getCREATOR() {
        return CREATOR;
    }

    public LoginTemplate(Parcel in) {
        ID = in.readString();
        json_key = in.readString();
        json_value = in.readString();
        language = in.readString();
    }

    public static final Creator<LoginTemplate> CREATOR = new Creator<LoginTemplate>() {
        @Override
        public LoginTemplate createFromParcel(Parcel in) {
            return new LoginTemplate(in);
        }

        @Override
        public LoginTemplate[] newArray(int size) {
            return new LoginTemplate[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ID);
        dest.writeString(json_key);
        dest.writeString(json_value);
        dest.writeString(language);
    }
}
