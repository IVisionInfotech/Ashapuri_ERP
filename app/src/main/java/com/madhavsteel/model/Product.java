package com.madhavsteel.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Product implements Serializable {
    String productId, productTitle, productImage, productThumbImage, productMediumImage, status, addStatus, dateTimeAdded;
    ArrayList<Category> categoryArrayList;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductThumbImage() {
        return productThumbImage;
    }

    public void setProductThumbImage(String productThumbImage) {
        this.productThumbImage = productThumbImage;
    }

    public String getProductMediumImage() {
        return productMediumImage;
    }

    public void setProductMediumImage(String productMediumImage) {
        this.productMediumImage = productMediumImage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public ArrayList<Category> getCategoryArrayList() {
        return categoryArrayList;
    }

    public void setCategoryArrayList(ArrayList<Category> categoryArrayList) {
        this.categoryArrayList = categoryArrayList;
    }
}
