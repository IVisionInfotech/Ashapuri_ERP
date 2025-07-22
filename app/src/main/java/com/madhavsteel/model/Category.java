package com.madhavsteel.model;

import java.io.Serializable;

public class Category implements Serializable {
    String catId, productId, catTitle, catImage, catThumbImage, catMediumImage, addStatus, dateTimeAdded;

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getCatTitle() {
        return catTitle;
    }

    public void setCatTitle(String catTitle) {
        this.catTitle = catTitle;
    }

    public String getCatImage() {
        return catImage;
    }

    public void setCatImage(String catImage) {
        this.catImage = catImage;
    }

    public String getCatThumbImage() {
        return catThumbImage;
    }

    public void setCatThumbImage(String catThumbImage) {
        this.catThumbImage = catThumbImage;
    }

    public String getCatMediumImage() {
        return catMediumImage;
    }

    public void setCatMediumImage(String catMediumImage) {
        this.catMediumImage = catMediumImage;
    }

    public String getAddStatus() {
        return addStatus;
    }

    public void setAddStatus(String addStatus) {
        this.addStatus = addStatus;
    }

    public String getDateTimeAdded() {
        return dateTimeAdded;
    }

    public void setDateTimeAdded(String dateTimeAdded) {
        this.dateTimeAdded = dateTimeAdded;
    }
}
