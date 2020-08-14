package com.app.dusmile.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChangeTemplateUpdateStatus {

@SerializedName("success")
@Expose
private Boolean success;
@SerializedName("details")
@Expose
private String details;

public Boolean getSuccess() {
return success;
}

public void setSuccess(Boolean success) {
this.success = success;
}

public String getDetails() {
return details;
}

public void setDetails(String details) {
this.details = details;
}

}