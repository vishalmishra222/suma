package com.app.dynamicform;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DynamicFields {

    @SerializedName("CustomValidationFunction")
    @Expose
    private String customValidationFunction;
    @SerializedName("defaultValue")
    @Expose
    private String defaultValue;
    @SerializedName("dependentField")
    @Expose
    private String dependentField;
    @SerializedName("fieldID")
    @Expose
    private Integer fieldID;
    @SerializedName("fieldName")
    @Expose
    private String fieldName;
    @SerializedName("fieldType")
    @Expose
    private String fieldType;
    @SerializedName("isDataList")
    @Expose
    private Boolean isDataList;
    @SerializedName("isMandatory")
    @Expose
    private Boolean isMandatory;
    @SerializedName("isreadonly")
    @Expose
    private Boolean isreadonly;
    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("lable")
    @Expose
    private String lable;
    @SerializedName("masterCollection")
    @Expose
    private String masterCollection;
    @SerializedName("maxlength")
    @Expose
    private Integer maxlength;
    @SerializedName("validation")
    @Expose
    private String validation;
    @SerializedName("validationMessage")
    @Expose
    private String validationMessage;
    @SerializedName("validationPattern")
    @Expose
    private String validationPattern;
    @SerializedName("validationType")
    @Expose
    private String validationType;
    @SerializedName("value")
    @Expose
    private List<Object> value = null;

    public String getCustomValidationFunction() {
        return customValidationFunction;
    }

    public void setCustomValidationFunction(String customValidationFunction) {
        this.customValidationFunction = customValidationFunction;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getDependentField() {
        return dependentField;
    }

    public void setDependentField(String dependentField) {
        this.dependentField = dependentField;
    }

    public Integer getFieldID() {
        return fieldID;
    }

    public void setFieldID(Integer fieldID) {
        this.fieldID = fieldID;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public Boolean getIsDataList() {
        return isDataList;
    }

    public void setIsDataList(Boolean isDataList) {
        this.isDataList = isDataList;
    }

    public Boolean getIsMandatory() {
        return isMandatory;
    }

    public void setIsMandatory(Boolean isMandatory) {
        this.isMandatory = isMandatory;
    }

    public Boolean getIsreadonly() {
        return isreadonly;
    }

    public void setIsreadonly(Boolean isreadonly) {
        this.isreadonly = isreadonly;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLable() {
        return lable;
    }

    public void setLable(String lable) {
        this.lable = lable;
    }

    public String getMasterCollection() {
        return masterCollection;
    }

    public void setMasterCollection(String masterCollection) {
        this.masterCollection = masterCollection;
    }

    public Integer getMaxlength() {
        return maxlength;
    }

    public void setMaxlength(Integer maxlength) {
        this.maxlength = maxlength;
    }

    public String getValidation() {
        return validation;
    }

    public void setValidation(String validation) {
        this.validation = validation;
    }

    public String getValidationMessage() {
        return validationMessage;
    }

    public void setValidationMessage(String validationMessage) {
        this.validationMessage = validationMessage;
    }

    public String getValidationPattern() {
        return validationPattern;
    }

    public void setValidationPattern(String validationPattern) {
        this.validationPattern = validationPattern;
    }

    public String getValidationType() {
        return validationType;
    }

    public void setValidationType(String validationType) {
        this.validationType = validationType;
    }

    public List<Object> getValue() {
        return value;
    }

    public void setValue(List<Object> value) {
        this.value = value;
    }
}