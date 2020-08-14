package com.app.dusmile.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DocumentUploadResponseModel {

    @SerializedName("redirect")
    @Expose
    private Boolean redirect;
    @SerializedName("redirectURL")
    @Expose
    private String redirectURL;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("tempCaseID")
    @Expose
    private String tempCaseID;

    public Boolean getRedirect() {
        return redirect;
    }

    public void setRedirect(Boolean redirect) {
        this.redirect = redirect;
    }

    public String getRedirectURL() {
        return redirectURL;
    }

    public void setRedirectURL(String redirectURL) {
        this.redirectURL = redirectURL;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getTempCaseID() {
        return tempCaseID;
    }

    public void setTempCaseID(String tempCaseID) {
        this.tempCaseID = tempCaseID;
    }

}