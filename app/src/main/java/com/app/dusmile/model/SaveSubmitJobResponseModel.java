package com.app.dusmile.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SaveSubmitJobResponseModel {

    @SerializedName("redirect")
    @Expose
    private Boolean redirect;
    @SerializedName("JOB_ID")
    @Expose
    private String jOBID;
    @SerializedName("redirectURL")
    @Expose
    private String redirectURL;
    @SerializedName("statusMsg")
    @Expose
    private String statusMsg;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("NBFC_NAME")
    @Expose
    private String nBFCNAME;
    @SerializedName("templateName")
    @Expose
    private String jobTYPE;

    public Boolean getRedirect() {
        return redirect;
    }

    public void setRedirect(Boolean redirect) {
        this.redirect = redirect;
    }

    public String getJOBID() {
        return jOBID;
    }

    public void setJOBID(String jOBID) {
        this.jOBID = jOBID;
    }

    public String getJobTYPE() {
        return jobTYPE;
    }

    public void setJobTYPE(String jobTYPE) {
        this.jobTYPE = jobTYPE;
    }

    public String getRedirectURL() {
        return redirectURL;
    }

    public void setRedirectURL(String redirectURL) {
        this.redirectURL = redirectURL;
    }

    public String getStatusMsg() {
        return statusMsg;
    }

    public void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNBFCNAME() {
        return nBFCNAME;
    }

    public void setNBFCNAME(String nBFCNAME) {
        this.nBFCNAME = nBFCNAME;
    }

}


