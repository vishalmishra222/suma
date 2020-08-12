package com.app.dusmile.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AvailableJobsModel {

@SerializedName("redirect")
@Expose
private Boolean redirect;
@SerializedName("redirectURL")
@Expose
private String redirectURL;
@SerializedName("reportName")
@Expose
private String reportName;
@SerializedName("reportHeaders")
@Expose
private List<String> reportHeaders = null;
@SerializedName("success")
@Expose
private Boolean success;
@SerializedName("reportData")
@Expose
private List<List<String>> reportData = null;

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

public String getReportName() {
return reportName;
}

public void setReportName(String reportName) {
this.reportName = reportName;
}

public List<String> getReportHeaders() {
return reportHeaders;
}

public void setReportHeaders(List<String> reportHeaders) {
this.reportHeaders = reportHeaders;
}

public Boolean getSuccess() {
return success;
}

public void setSuccess(Boolean success) {
this.success = success;
}

public List<List<String>> getReportData() {
return reportData;
}

public void setReportData(List<List<String>> reportData) {
this.reportData = reportData;
}

}