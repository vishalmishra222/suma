package com.app.dusmile.preferences;

import android.os.Parcel;
import android.os.Parcelable;

public class RecordUser implements Parcelable {

    String userID;
    String username;
    String fname;
    String lname;
    String roleID;
    String buID;
    String entityID;
    String reportingTo;
    String status;

    public String getEmailTO() {
        return emailTO;
    }

    public void setEmailTO(String emailTO) {
        this.emailTO = emailTO;
    }

    /**/
    String emailTO;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    String token;
    int otp;

    public RecordUser() {
    }

    public RecordUser(String userID, String username, String fname, String lname, String roleID, String buID, String entityID, String reportingTo, String status, int otp, String token, String emailTO) {
        this.userID = userID;
        this.username = username;
        this.fname = fname;
        this.lname = lname;
        this.roleID = roleID;
        this.buID = buID;
        this.entityID = entityID;
        this.otp = otp;
        this.reportingTo = reportingTo;
        this.status = status;
        this.token = token;
        this.emailTO = emailTO;
    }

    protected RecordUser(Parcel in) {
        userID = in.readString();
        username = in.readString();
        fname = in.readString();
        lname = in.readString();
        roleID = in.readString();
        buID = in.readString();
        entityID = in.readString();
        reportingTo = in.readString();
        status = in.readString();
        otp = in.readInt();
        emailTO = in.readString();
    }

    public static final Creator<RecordUser> CREATOR = new Creator<RecordUser>() {
        @Override
        public RecordUser createFromParcel(Parcel in) {
            return new RecordUser(in);
        }

        @Override
        public RecordUser[] newArray(int size) {
            return new RecordUser[size];
        }
    };

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getRoleID() {
        return roleID;
    }

    public void setRoleID(String roleID) {
        this.roleID = roleID;
    }

    public String getBuID() {
        return buID;
    }

    public void setBuID(String buID) {
        this.buID = buID;
    }

    public String getEntityID() {
        return entityID;
    }

    public void setEntityID(String entityID) {
        this.entityID = entityID;
    }

    public String getReportingTo() {
        return reportingTo;
    }

    public void setReportingTo(String reportingTo) {
        this.reportingTo = reportingTo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getOtp() {
        return otp;
    }

    public void setOtp(int otp) {
        this.otp = otp;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(userID);
        parcel.writeString(username);
        parcel.writeString(fname);
        parcel.writeString(lname);
        parcel.writeString(roleID);
        parcel.writeString(buID);
        parcel.writeString(entityID);
        parcel.writeString(reportingTo);
        parcel.writeString(status);
        parcel.writeInt(otp);
        parcel.writeString(emailTO);
    }
}
