package com.example.art_gallery;

import java.io.Serializable;

public class upload_Image_Model implements Serializable {

    String title,
            image,
            curretntUser,
            docId,
            description;

    public upload_Image_Model() {
    }

    public upload_Image_Model(String title, String image, String curretntUser, String docId, String description) {
        this.title = title;
        this.image = image;
        this.curretntUser = curretntUser;
        this.docId = docId;
        this.description = description;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCurretntUser() {
        return curretntUser;
    }

    public void setCurretntUser(String curretntUser) {
        this.curretntUser = curretntUser;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}






