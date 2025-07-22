package com.madhavsteel.model;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Products extends ExpandableGroup<Sizes> implements Serializable {

    String productId, productTitle, productImage, productThumbImage, productMediumImage, status, dateTimeAdded;
    ArrayList<Sizes> sizeList;

    public Products(String title, List<Sizes> items) {
        super(title, items);
    }

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

    public String getDateTimeAdded() {
        return dateTimeAdded;
    }

    public void setDateTimeAdded(String dateTimeAdded) {
        this.dateTimeAdded = dateTimeAdded;
    }

    public ArrayList<Sizes> getSizeList() {
        return sizeList;
    }

    public void setSizeList(ArrayList<Sizes> sizeList) {
        this.sizeList = sizeList;
    }
}
