package com.example.sumaforms.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReportFilterModel {

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
    @SerializedName("reportFilters")
    @Expose
    private List<ReportFilter> reportFilters = null;

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

    public List<ReportFilter> getReportFilters() {
        return reportFilters;
    }

    public void setReportFilters(List<ReportFilter> reportFilters) {
        this.reportFilters = reportFilters;
    }

    public class ReportFilter {

        @SerializedName("validationPattern")
        @Expose
        private String validationPattern;
        @SerializedName("fieldName")
        @Expose
        private String fieldName;
        @SerializedName("isDataList")
        @Expose
        private Boolean isDataList;
        @SerializedName("maxlength")
        @Expose
        private Integer maxlength;
        @SerializedName("defaultValue")
        @Expose
        private String defaultValue;
        @SerializedName("validationType")
        @Expose
        private String validationType;
        @SerializedName("validationMessage")
        @Expose
        private String validationMessage;
        @SerializedName("dependentField")
        @Expose
        private String dependentField;
        @SerializedName("masterCollection")
        @Expose
        private String masterCollection;
        @SerializedName("lable")
        @Expose
        private String lable;
        @SerializedName("fieldType")
        @Expose
        private String fieldType;
        @SerializedName("value")
        @Expose
        private String value;
        @SerializedName("key")
        @Expose
        private String key;
        @SerializedName("validation")
        @Expose
        private String validation;
        @SerializedName("isMandatory")
        @Expose
        private Boolean isMandatory;
        @SerializedName("fieldID")
        @Expose
        private Integer fieldID;

        public String getValidationPattern() {
            return validationPattern;
        }

        public void setValidationPattern(String validationPattern) {
            this.validationPattern = validationPattern;
        }

        public String getFieldName() {
            return fieldName;
        }

        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }

        public Boolean getIsDataList() {
            return isDataList;
        }

        public void setIsDataList(Boolean isDataList) {
            this.isDataList = isDataList;
        }

        public Integer getMaxlength() {
            return maxlength;
        }

        public void setMaxlength(Integer maxlength) {
            this.maxlength = maxlength;
        }

        public String getDefaultValue() {
            return defaultValue;
        }

        public void setDefaultValue(String defaultValue) {
            this.defaultValue = defaultValue;
        }

        public String getValidationType() {
            return validationType;
        }

        public void setValidationType(String validationType) {
            this.validationType = validationType;
        }

        public String getValidationMessage() {
            return validationMessage;
        }

        public void setValidationMessage(String validationMessage) {
            this.validationMessage = validationMessage;
        }

        public String getDependentField() {
            return dependentField;
        }

        public void setDependentField(String dependentField) {
            this.dependentField = dependentField;
        }

        public String getMasterCollection() {
            return masterCollection;
        }

        public void setMasterCollection(String masterCollection) {
            this.masterCollection = masterCollection;
        }

        public String getLable() {
            return lable;
        }

        public void setLable(String lable) {
            this.lable = lable;
        }

        public String getFieldType() {
            return fieldType;
        }

        public void setFieldType(String fieldType) {
            this.fieldType = fieldType;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValidation() {
            return validation;
        }

        public void setValidation(String validation) {
            this.validation = validation;
        }

        public Boolean getIsMandatory() {
            return isMandatory;
        }

        public void setIsMandatory(Boolean isMandatory) {
            this.isMandatory = isMandatory;
        }

        public Integer getFieldID() {
            return fieldID;
        }

        public void setFieldID(Integer fieldID) {
            this.fieldID = fieldID;
        }

    }

}