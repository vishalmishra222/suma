package com.app.dusmile.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HoldJobResponseModel {

@SerializedName("success")
@Expose
private Boolean success;
@SerializedName("JOB_ID")
@Expose
private String jOBID;

public Boolean getSuccess() {
return success;
}

public void setSuccess(Boolean success) {
this.success = success;
}

public String getJOBID() {
return jOBID;
}

public void setJOBID(String jOBID) {
this.jOBID = jOBID;
}

}