package com.app.dusmile.DBModel;

import java.util.List;

import com.app.dusmile.model.SubProcessFieldDataResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdatedTemplet {

    @SerializedName("tabData")
    @Expose
    private TabData tabData;
    @SerializedName("subProcessFieldsData")
    @Expose
    private List<SubProcessFieldsDatum> subProcessFieldsData = null;
    @SerializedName("sectionwiseData")
    @Expose
    private Object sectionwiseData;

    public TabData getTabData() {
        return tabData;
    }

    public void setTabData(TabData tabData) {
        this.tabData = tabData;
    }

    public List<SubProcessFieldsDatum> getSubProcessFieldsData() {
        return subProcessFieldsData;
    }

    public void setSubProcessFieldsData(List<SubProcessFieldsDatum> subProcessFieldsData) {
        this.subProcessFieldsData = subProcessFieldsData;
    }

    public Object getSectionwiseData() {
        return sectionwiseData;
    }

    public void setSectionwiseData(Object sectionwiseData) {
        this.sectionwiseData = sectionwiseData;
    }


    public class TabData {

        @SerializedName("tabs")
        @Expose
        private List<Tab> tabs = null;
        @SerializedName("subProcesses")
        @Expose
        private List<String> subProcesses = null;
        @SerializedName("version_no")
        @Expose
        private String versionNo;
        @SerializedName("templateID")
        @Expose
        private Integer templateID;
        @SerializedName("latest")
        @Expose
        private Boolean latest;
        @SerializedName("templateName")
        @Expose
        private String templateName;
        @SerializedName("nbfcname")
        @Expose
        private String nbfcname;
        @SerializedName("templateFor")
        @Expose
        private String templateFor;
        @SerializedName("tat")
        @Expose
        private Integer tat;
        @SerializedName("default")
        @Expose
        private Boolean _default;

        public List<Tab> getTabs() {
            return tabs;
        }

        public void setTabs(List<Tab> tabs) {
            this.tabs = tabs;
        }

        public List<String> getSubProcesses() {
            return subProcesses;
        }

        public void setSubProcesses(List<String> subProcesses) {
            this.subProcesses = subProcesses;
        }

        public String getVersionNo() {
            return versionNo;
        }

        public void setVersionNo(String versionNo) {
            this.versionNo = versionNo;
        }

        public Integer getTemplateID() {
            return templateID;
        }

        public void setTemplateID(Integer templateID) {
            this.templateID = templateID;
        }

        public Boolean getLatest() {
            return latest;
        }

        public void setLatest(Boolean latest) {
            this.latest = latest;
        }

        public String getTemplateName() {
            return templateName;
        }

        public void setTemplateName(String templateName) {
            this.templateName = templateName;
        }

        public String getNbfcname() {
            return nbfcname;
        }

        public void setNbfcname(String nbfcname) {
            this.nbfcname = nbfcname;
        }

        public String getTemplateFor() {
            return templateFor;
        }

        public void setTemplateFor(String templateFor) {
            this.templateFor = templateFor;
        }

        public Integer getTat() {
            return tat;
        }

        public void setTat(Integer tat) {
            this.tat = tat;
        }

        public Boolean getDefault() {
            return _default;
        }

        public void setDefault(Boolean _default) {
            this._default = _default;
        }

    }

    public class SubProcessFieldsDatum {

        @SerializedName("asssociatedTemplateIDs")
        @Expose
        private List<AsssociatedTemplateID> asssociatedTemplateIDs = null;
        @SerializedName("subProcessName")
        @Expose
        private String subProcessName;
        @SerializedName("subProcessFields")
        @Expose
        private List<SubProcessFieldDataResponse.SubProcessField> subProcessFields = null;
        @SerializedName("controllerName")
        @Expose
        private String controllerName;
        @SerializedName("version_no")
        @Expose
        private String versionNo;
        @SerializedName("latest")
        @Expose
        private Boolean latest;
        @SerializedName("nbfcname")
        @Expose
        private String nbfcname;
        @SerializedName("tableExist")
        @Expose
        private Boolean tableExist;

        public List<AsssociatedTemplateID> getAsssociatedTemplateIDs() {
            return asssociatedTemplateIDs;
        }

        public void setAsssociatedTemplateIDs(List<AsssociatedTemplateID> asssociatedTemplateIDs) {
            this.asssociatedTemplateIDs = asssociatedTemplateIDs;
        }

        public String getSubProcessName() {
            return subProcessName;
        }

        public void setSubProcessName(String subProcessName) {
            this.subProcessName = subProcessName;
        }

        public List<SubProcessFieldDataResponse.SubProcessField> getSubProcessFields() {
            return subProcessFields;
        }

        public void setSubProcessFields(List<SubProcessFieldDataResponse.SubProcessField> subProcessFields) {
            this.subProcessFields = subProcessFields;
        }

        public String getControllerName() {
            return controllerName;
        }

        public void setControllerName(String controllerName) {
            this.controllerName = controllerName;
        }

        public String getVersionNo() {
            return versionNo;
        }

        public void setVersionNo(String versionNo) {
            this.versionNo = versionNo;
        }

        public Boolean getLatest() {
            return latest;
        }

        public void setLatest(Boolean latest) {
            this.latest = latest;
        }

        public String getNbfcname() {
            return nbfcname;
        }

        public void setNbfcname(String nbfcname) {
            this.nbfcname = nbfcname;
        }

        public Boolean getTableExist() {
            return tableExist;
        }

        public void setTableExist(Boolean tableExist) {
            this.tableExist = tableExist;
        }

    }


    public class Tab {

        @SerializedName("isactive")
        @Expose
        private Boolean isactive;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("isSubTabsExists")
        @Expose
        private Boolean isSubTabsExists;
        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("subProcesses")
        @Expose
        private List<String> subProcesses = null;
        @SerializedName("key")
        @Expose
        private String key;

        public Boolean getIsactive() {
            return isactive;
        }

        public void setIsactive(Boolean isactive) {
            this.isactive = isactive;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Boolean getIsSubTabsExists() {
            return isSubTabsExists;
        }

        public void setIsSubTabsExists(Boolean isSubTabsExists) {
            this.isSubTabsExists = isSubTabsExists;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public List<String> getSubProcesses() {
            return subProcesses;
        }

        public void setSubProcesses(List<String> subProcesses) {
            this.subProcesses = subProcesses;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
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
