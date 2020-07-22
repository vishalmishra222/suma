package com.app.dusmile.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class TabsDataResponse {

    @SerializedName("TemplateMaster")
    @Expose
    private List<TemplateMaster> templateMaster = null;

    public List<TemplateMaster> getTemplateMaster() {
        return templateMaster;
    }

    public void setTemplateMaster(List<TemplateMaster> templateMaster) {
        this.templateMaster = templateMaster;
    }


    public class TemplateMaster {

        @SerializedName("_id")
        @Expose
        private Id id;
        @SerializedName("TemplateID")
        @Expose
        private Integer templateID;
        @SerializedName("TemplateName")
        @Expose
        private String templateName;
        @SerializedName("NBFCName")
        @Expose
        private String nBFCName;
        @SerializedName("tabs")
        @Expose
        private List<Tab> tabs = null;
        @SerializedName("isDefault")
        @Expose
        private Boolean isDefault;
        @SerializedName("TemplateFor")
        @Expose
        private String templateFor;

        public String getVersion_no() {
            return version_no;
        }

        public void setVersion_no(String version_no) {
            this.version_no = version_no;
        }

        public String getIsLatest() {
            return isLatest;
        }

        public void setIsLatest(String isLatest) {
            this.isLatest = isLatest;
        }

        @SerializedName("version_no")
        @Expose
        private String version_no;
        @SerializedName("isLatest")
        @Expose
        private String isLatest;
        public Id getId() {
            return id;
        }

        public void setId(Id id) {
            this.id = id;
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

        public String getNBFCName() {
            return nBFCName;
        }

        public void setNBFCName(String nBFCName) {
            this.nBFCName = nBFCName;
        }

        public List<Tab> getTabs() {
            return tabs;
        }

        public void setTabs(List<Tab> tabs) {
            this.tabs = tabs;
        }

        public Boolean getIsDefault() {
            return isDefault;
        }

        public void setIsDefault(Boolean isDefault) {
            this.isDefault = isDefault;
        }

        public String getTemplateFor() {
            return templateFor;
        }

        public void setTemplateFor(String templateFor) {
            this.templateFor = templateFor;
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


    public class Tab {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("key")
        @Expose
        private String key;
        @SerializedName("isactive")
        @Expose
        private Boolean isactive;
        @SerializedName("isSubTabsExists")
        @Expose
        private Boolean isSubTabsExists;
        @SerializedName("subProcesses")
        @Expose
        private List<String> subProcesses = null;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public Boolean getIsactive() {
            return isactive;
        }

        public void setIsactive(Boolean isactive) {
            this.isactive = isactive;
        }

        public Boolean getIsSubTabsExists() {
            return isSubTabsExists;
        }

        public void setIsSubTabsExists(Boolean isSubTabsExists) {
            this.isSubTabsExists = isSubTabsExists;
        }

        public List<String> getSubProcesses() {
            return subProcesses;
        }

        public void setSubProcesses(List<String> subProcesses) {
            this.subProcesses = subProcesses;
        }

    }

}