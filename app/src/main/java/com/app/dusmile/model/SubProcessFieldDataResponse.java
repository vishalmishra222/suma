package com.app.dusmile.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SubProcessFieldDataResponse {

    @SerializedName("TemplateFields")
    @Expose
    private List<TemplateField> templateFields = null;

    public List<TemplateField> getTemplateFields() {
        return templateFields;
    }

    public void setTemplateFields(List<TemplateField> templateFields) {
        this.templateFields = templateFields;
    }

    public class SubProcessField {

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
        @SerializedName("CustomValidationFunction")
        @Expose
        private String customValidationFunction;
        @SerializedName("masterCollection")
        @Expose
        private String masterCollection;
        @SerializedName("isreadonly")
        @Expose
        private Boolean isreadonly;
        @SerializedName("lable")
        @Expose
        private String lable;
        @SerializedName("fieldType")
        @Expose
        private String fieldType;
        @SerializedName("value")
        @Expose
        private List<String> value = null;
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
        @SerializedName("onclick")
        @Expose
        private String onclick;
        @SerializedName("columnName")
        @Expose
        private String columnName;
        @SerializedName("isMultipleTextBox")
        @Expose
        private Boolean isMultipleTextBox;
        @SerializedName("dependentFieldList")
        @Expose
        private List<String> dependentFieldList = null;
        @SerializedName("isHidden")
        @Expose
        private Boolean isHidden;
        @SerializedName("isYearMonthCombo")
        @Expose
        private Boolean isYearMonthCombo;
        @SerializedName("onchange")
        @Expose
        private String onchange;
        @SerializedName("ids")
        @Expose
        private List<Integer> ids = null;
        @SerializedName("color")
        @Expose
        private List<String> color = null;

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

        public String getCustomValidationFunction() {
            return customValidationFunction;
        }

        public void setCustomValidationFunction(String customValidationFunction) {
            this.customValidationFunction = customValidationFunction;
        }

        public String getMasterCollection() {
            return masterCollection;
        }

        public void setMasterCollection(String masterCollection) {
            this.masterCollection = masterCollection;
        }

        public Boolean getIsreadonly() {
            return isreadonly;
        }

        public void setIsreadonly(Boolean isreadonly) {
            this.isreadonly = isreadonly;
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

        public List<String> getValue() {
            return value;
        }

        public void setValue(List<String> value) {
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

        public String getOnclick() {
            return onclick;
        }

        public void setOnclick(String onclick) {
            this.onclick = onclick;
        }

        public String getColumnName() {
            return columnName;
        }

        public void setColumnName(String columnName) {
            this.columnName = columnName;
        }

        public Boolean getIsMultipleTextBox() {
            return isMultipleTextBox;
        }

        public void setIsMultipleTextBox(Boolean isMultipleTextBox) {
            this.isMultipleTextBox = isMultipleTextBox;
        }

        public List<String> getDependentFieldList() {
            return dependentFieldList;
        }

        public void setDependentFieldList(List<String> dependentFieldList) {
            this.dependentFieldList = dependentFieldList;
        }

        public Boolean getIsHidden() {
            return isHidden;
        }

        public void setIsHidden(Boolean isHidden) {
            this.isHidden = isHidden;
        }

        public Boolean getIsYearMonthCombo() {
            return isYearMonthCombo;
        }

        public void setIsYearMonthCombo(Boolean isYearMonthCombo) {
            this.isYearMonthCombo = isYearMonthCombo;
        }

        public String getOnchange() {
            return onchange;
        }

        public void setOnchange(String onchange) {
            this.onchange = onchange;
        }

        public List<Integer> getIds() {
            return ids;
        }

        public void setIds(List<Integer> ids) {
            this.ids = ids;
        }

        public List<String> getColor() {
            return color;
        }

        public void setColor(List<String> color) {
            this.color = color;
        }

    }

    public class TemplateField {

        @SerializedName("version_no")
        @Expose
        private String versionNo;
        @SerializedName("NBFCName")
        @Expose
        private String nBFCName;
        @SerializedName("isLatest")
        @Expose
        private Boolean isLatest;

        @SerializedName("asssociatedTemplateIDs")
        @Expose
        private List<AsssociatedTemplateID> asssociatedTemplateIDs = null;

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

        public Boolean getIsTableExist() {
            return isTableExist;
        }

        public void setIsTableExist(Boolean isTableExist) {
            this.isTableExist = isTableExist;
        }

        public String getVersionNo() {
            return versionNo;
        }

        public void setVersionNo(String versionNo) {
            this.versionNo = versionNo;
        }

        public String getNBFCName() {
            return nBFCName;
        }

        public void setNBFCName(String nBFCName) {
            this.nBFCName = nBFCName;
        }

        public Boolean getIsLatest() {
            return isLatest;
        }

        public void setIsLatest(Boolean isLatest) {
            this.isLatest = isLatest;
        }

        public String getSubProcessName() {
            return subProcessName;
        }

        public void setSubProcessName(String subProcessName) {
            this.subProcessName = subProcessName;
        }

        public String getControllerName() {
            return controllerName;
        }

        public void setControllerName(String controllerName) {
            this.controllerName = controllerName;
        }

        public List<AsssociatedTemplateID> getAsssociatedTemplateIDs() {
            return asssociatedTemplateIDs;
        }

        public void setAsssociatedTemplateIDs(List<AsssociatedTemplateID> asssociatedTemplateIDs) {
            this.asssociatedTemplateIDs = asssociatedTemplateIDs;
        }

        public List<SubProcessField> getSubProcessFields() {
            return subProcessFields;
        }

        public void setSubProcessFields(List<SubProcessField> subProcessFields) {
            this.subProcessFields = subProcessFields;
        }

        public String getTableID() {
            return tableID;
        }

        public void setTableID(String tableID) {
            this.tableID = tableID;
        }

        public String getnBFCName() {
            return nBFCName;
        }

        public void setnBFCName(String nBFCName) {
            this.nBFCName = nBFCName;
        }

        public Boolean getLatest() {
            return isLatest;
        }

        public void setLatest(Boolean latest) {
            isLatest = latest;
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

        public Boolean getTableExist() {
            return isTableExist;
        }

        public void setTableExist(Boolean tableExist) {
            isTableExist = tableExist;
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
    }

    public class AsssociatedTemplateID {

        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("id")
        @Expose
        private Integer id;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

    }

}