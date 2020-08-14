package com.app.dusmile.DBModel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by suma on 14/03/17.
 */

public class Applicant implements Parcelable{

    String ID;
    String json;

    public Applicant() {
    }

    protected Applicant(Parcel in) {
        ID = in.readString();
        json = in.readString();
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public static Creator<Applicant> getCREATOR() {
        return CREATOR;
    }

    public static final Creator<Applicant> CREATOR = new Creator<Applicant>() {
        @Override
        public Applicant createFromParcel(Parcel in) {
            return new Applicant(in);
        }

        @Override
        public Applicant[] newArray(int size) {
            return new Applicant[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ID);
        dest.writeString(json);
    }
}
