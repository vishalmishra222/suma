package com.app.dusmile.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImagesModel {

    @SerializedName("images")
    @Expose
    private List<Image> images = null;

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public class Image {

        @SerializedName("imageID")
        @Expose
        private Integer imageID;
        @SerializedName("imageName")
        @Expose
        private String imageName;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("processID")
        @Expose
        private Integer processID;
        @SerializedName("Seq_id")
        @Expose
        private Integer seqId;

        public Integer getImageID() {
            return imageID;
        }

        public void setImageID(Integer imageID) {
            this.imageID = imageID;
        }

        public String getImageName() {
            return imageName;
        }

        public void setImageName(String imageName) {
            this.imageName = imageName;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getProcessID() {
            return processID;
        }

        public void setProcessID(Integer processID) {
            this.processID = processID;
        }

        public Integer getSeqId() {
            return seqId;
        }

        public void setSeqId(Integer seqId) {
            this.seqId = seqId;
        }

    }

}