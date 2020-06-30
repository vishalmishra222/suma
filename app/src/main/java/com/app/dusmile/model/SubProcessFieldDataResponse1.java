package com.app.dusmile.model;

/**
 * Created by sumasoft on 30/01/17.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SubProcessFieldDataResponse1 {

   /* @SerializedName("TemplateFields")
    @Expose
    private List<TemplateField> templateFields = null;

    public List<TemplateField> getTemplateFields() {
        return templateFields;
    }

    public void setTemplateFields(List<TemplateField> templateFields) {
        this.templateFields = templateFields;
    }





    public class TemplateField {

        @SerializedName("_id")
        @Expose
        private Id id;
        @SerializedName("NBFCName")
        @Expose
        private String nBFCName;
        @SerializedName("TemplateFor")
        @Expose
        private String templateFor;
        @SerializedName("TemplateID")
        @Expose
        private Integer templateID;
        @SerializedName("TemplateName")
        @Expose
        private String templateName;
        @SerializedName("subProcessName")
        @Expose
        private String subProcessName;
        @SerializedName("subProcessFields")
        @Expose
        private List<SubProcessField> subProcessFields = null;
        @SerializedName("controllerName")
        @Expose
        private String controllerName;
        @SerializedName("isTableExist")
        @Expose
        private Boolean isTableExist;
        @SerializedName("tableHeaders")
        @Expose
        private List<String> tableHeaders = null;
        @SerializedName("tableFieldKeys")
        @Expose
        private List<String> tableFieldKeys = null;
        @SerializedName("mandatoryTableFields")
        @Expose
        private List<String> mandatoryTableFields = null;
        @SerializedName("mandatoryTableHeaders")
        @Expose
        private List<String> mandatoryTableHeaders = null;
        @SerializedName("ClearSubProcessFields")
        @Expose
        private List<String> clearSubProcessFields = null;
        @SerializedName("tableID")
        @Expose
        private String tableID;

        public Id getId() {
            return id;
        }

        public void setId(Id id) {
            this.id = id;
        }

        public String getNBFCName() {
            return nBFCName;
        }

        public void setNBFCName(String nBFCName) {
            this.nBFCName = nBFCName;
        }

        public String getTemplateFor() {
            return templateFor;
        }

        public void setTemplateFor(String templateFor) {
            this.templateFor = templateFor;
        }

        public Integer getTemplateID() {
            return templateID;
        }

        public void setTemplateID(Integer templateID) {
            this.templateID = templateID;
        }

        public String getTemplateName() {
            return templateName;
        }

        public void setTemplateName(String templateName) {
            this.templateName = templateName;
        }

        public String getSubProcessName() {
            return subProcessName;
        }

        public void setSubProcessName(String subProcessName) {
            this.subProcessName = subProcessName;
        }

        public List<SubProcessField> getSubProcessFields() {
            return subProcessFields;
        }

        public void setSubProcessFields(List<SubProcessField> subProcessFields) {
            this.subProcessFields = subProcessFields;
        }

        public String getControllerName() {
            return controllerName;
        }

        public void setControllerName(String controllerName) {
            this.controllerName = controllerName;
        }

        public Boolean getIsTableExist() {
            return isTableExist;
        }

        public void setIsTableExist(Boolean isTableExist) {
            this.isTableExist = isTableExist;
        }

        public List<String> getTableHeaders() {
            return tableHeaders;
        }

        public void setTableHeaders(List<String> tableHeaders) {
            this.tableHeaders = tableHeaders;
        }

        public List<String> getTableFieldKeys() {
            return tableFieldKeys;
        }

        public void setTableFieldKeys(List<String> tableFieldKeys) {
            this.tableFieldKeys = tableFieldKeys;
        }

        public List<String> getMandatoryTableFields() {
            return mandatoryTableFields;
        }

        public void setMandatoryTableFields(List<String> mandatoryTableFields) {
            this.mandatoryTableFields = mandatoryTableFields;
        }

        public List<String> getMandatoryTableHeaders() {
            return mandatoryTableHeaders;
        }

        public void setMandatoryTableHeaders(List<String> mandatoryTableHeaders) {
            this.mandatoryTableHeaders = mandatoryTableHeaders;
        }

        public List<String> getClearSubProcessFields() {
            return clearSubProcessFields;
        }

        public void setClearSubProcessFields(List<String> clearSubProcessFields) {
            this.clearSubProcessFields = clearSubProcessFields;
        }

        public String getTableID() {
            return tableID;
        }

        public void setTableID(String tableID) {
            this.tableID = tableID;
        }

    }
    public class Id {

        @SerializedName("$oid")
        @Expose
        private String $oid;

        public String get$oid() {
            return $oid;
        }

        public void set$oid(String $oid) {
            this.$oid = $oid;
        }

    }


    public class SubProcessField {

        @SerializedName("fieldID")
        @Expose
        private Integer fieldID;
        @SerializedName("lable")
        @Expose
        private String lable;
        @SerializedName("fieldType")
        @Expose
        private String fieldType;
        @SerializedName("key")
        @Expose
        private String key;
        @SerializedName("defaultValue")
        @Expose
        private String defaultValue;
        @SerializedName("value")
        @Expose
        private List<String> value;
        @SerializedName("dependentField")
        @Expose
        private String dependentField;
        @SerializedName("masterCollection")
        @Expose
        private String masterCollection;
        @SerializedName("validation")
        @Expose
        private String validation;
        @SerializedName("validationType")
        @Expose
        private String validationType;
        @SerializedName("validationPattern")
        @Expose
        private String validationPattern;
        @SerializedName("CustomValidationFunction")
        @Expose
        private String customValidationFunction;
        @SerializedName("isDataList")
        @Expose
        private Boolean isDataList;
        @SerializedName("isMandatory")
        @Expose
        private Boolean isMandatory;
        @SerializedName("validationMessage")
        @Expose
        private String validationMessage;
        @SerializedName("maxlength")
        @Expose
        private String maxlength;
        @SerializedName("onchange")
        @Expose
        private String onchange;
        @SerializedName("fieldName")
        @Expose
        private String fieldName;
        @SerializedName("isreadonly")
        @Expose
        private Boolean isreadonly;
        @SerializedName("columnName")
        @Expose
        private String columnName;
        @SerializedName("onclick")
        @Expose
        private String onclick;

        public Integer getFieldID() {
            return fieldID;
        }

        public void setFieldID(Integer fieldID) {
            this.fieldID = fieldID;
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

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getDefaultValue() {
            return defaultValue;
        }

        public void setDefaultValue(String defaultValue) {
            this.defaultValue = defaultValue;
        }

        public List<String> getValue() {
            return value;
        }

        public void setValue(List<String> value) {
            this.value = value;
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

        public String getValidation() {
            return validation;
        }

        public void setValidation(String validation) {
            this.validation = validation;
        }

        public String getValidationType() {
            return validationType;
        }

        public void setValidationType(String validationType) {
            this.validationType = validationType;
        }

        public String getValidationPattern() {
            return validationPattern;
        }

        public void setValidationPattern(String validationPattern) {
            this.validationPattern = validationPattern;
        }

        public String getCustomValidationFunction() {
            return customValidationFunction;
        }

        public void setCustomValidationFunction(String customValidationFunction) {
            this.customValidationFunction = customValidationFunction;
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

        public String getValidationMessage() {
            return validationMessage;
        }

        public void setValidationMessage(String validationMessage) {
            this.validationMessage = validationMessage;
        }

        public String getMaxlength() {
            return maxlength;
        }

        public void setMaxlength(String maxlength) {
            this.maxlength = maxlength;
        }

        public String getOnchange() {
            return onchange;
        }

        public void setOnchange(String onchange) {
            this.onchange = onchange;
        }

        public String getFieldName() {
            return fieldName;
        }

        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }

        public Boolean getIsreadonly() {
            return isreadonly;
        }

        public void setIsreadonly(Boolean isreadonly) {
            this.isreadonly = isreadonly;
        }

        public String getColumnName() {
            return columnName;
        }

        public void setColumnName(String columnName) {
            this.columnName = columnName;
        }

        public String getOnclick() {
            return onclick;
        }

        public void setOnclick(String onclick) {
            this.onclick = onclick;
        }

    }*/
}