package com.app.dusmile.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sumasoft on 27/01/17.
 */

public class CategorySubcategoryResponse {

    @SerializedName("GroupToCategoryToSubCategory")
    @Expose
    private List<GroupToCategoryToSubCategory> groupToCategoryToSubCategory = null;

    public List<GroupToCategoryToSubCategory> getGroupToCategoryToSubCategory() {
        return groupToCategoryToSubCategory;
    }

    public void setGroupToCategoryToSubCategory(List<GroupToCategoryToSubCategory> groupToCategoryToSubCategory) {
        this.groupToCategoryToSubCategory = groupToCategoryToSubCategory;
    }
    public class GroupToCategoryToSubCategory {

        @SerializedName("_id")
        @Expose
        private Id id;
        @SerializedName("Seq_id")
        @Expose
        private Integer seqId;
        @SerializedName("groupID")
        @Expose
        private Integer groupID;
        @SerializedName("categoryName")
        @Expose
        private String categoryName;
        @SerializedName("SubCategory")
        @Expose
        private List<SubCategory> subCategory = null;

        public Id getId() {
            return id;
        }

        public void setId(Id id) {
            this.id = id;
        }

        public Integer getSeqId() {
            return seqId;
        }

        public void setSeqId(Integer seqId) {
            this.seqId = seqId;
        }

        public Integer getGroupID() {
            return groupID;
        }

        public void setGroupID(Integer groupID) {
            this.groupID = groupID;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public List<SubCategory> getSubCategory() {
            return subCategory;
        }

        public void setSubCategory(List<SubCategory> subCategory) {
            this.subCategory = subCategory;
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



    public class SubCategory {

        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("action")
        @Expose
        private String action;
        @SerializedName("overlay")
        @Expose
        private Boolean overlay;
        @SerializedName("isFunctionCall")
        @Expose
        private Boolean isFunctionCall;
        @SerializedName("onClick")
        @Expose
        private String onClick;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public Boolean getOverlay() {
            return overlay;
        }

        public void setOverlay(Boolean overlay) {
            this.overlay = overlay;
        }

        public Boolean getIsFunctionCall() {
            return isFunctionCall;
        }

        public void setIsFunctionCall(Boolean isFunctionCall) {
            this.isFunctionCall = isFunctionCall;
        }

        public String getOnClick() {
            return onClick;
        }

        public void setOnClick(String onClick) {
            this.onClick = onClick;
        }

    }
}
