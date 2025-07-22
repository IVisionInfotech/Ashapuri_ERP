package com.madhavsteel.model;

import android.os.Parcel;
import android.os.Parcelable;

public class OrderInquiry implements Parcelable {

    String quoteDetailsId, quoteId, customerId, sizeId, sizeTitle, catId, catTitle, quantity, price, measurement, productId, productTitle,
            pieceQuantity, readStatus, orderStatus, status, dateTimeAdded;

    public OrderInquiry() {
    }

    protected OrderInquiry(Parcel in) {
        quoteDetailsId = in.readString();
        quoteId = in.readString();
        customerId = in.readString();
        sizeId = in.readString();
        sizeTitle = in.readString();
        catId = in.readString();
        catTitle = in.readString();
        quantity = in.readString();
        measurement = in.readString();
        productId = in.readString();
        productTitle = in.readString();
        price = in.readString();
        pieceQuantity = in.readString();
        readStatus = in.readString();
        orderStatus = in.readString();
        status = in.readString();
        dateTimeAdded = in.readString();
    }

    public static final Creator<OrderInquiry> CREATOR = new Creator<OrderInquiry>() {
        @Override
        public OrderInquiry createFromParcel(Parcel in) {
            return new OrderInquiry(in);
        }

        @Override
        public OrderInquiry[] newArray(int size) {
            return new OrderInquiry[size];
        }
    };

    public String getQuoteDetailsId() {
        return quoteDetailsId;
    }

    public void setQuoteDetailsId(String quoteDetailsId) {
        this.quoteDetailsId = quoteDetailsId;
    }

    public String getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(String quoteId) {
        this.quoteId = quoteId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getSizeId() {
        return sizeId;
    }

    public void setSizeId(String sizeId) {
        this.sizeId = sizeId;
    }

    public String getSizeTitle() {
        return sizeTitle;
    }

    public void setSizeTitle(String sizeTitle) {
        this.sizeTitle = sizeTitle;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getCatTitle() {
        return catTitle;
    }

    public void setCatTitle(String catTitle) {
        this.catTitle = catTitle;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getMeasurement() {
        return measurement;
    }

    public void setMeasurement(String measurement) {
        this.measurement = measurement;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPieceQuantity() {
        return pieceQuantity;
    }

    public void setPieceQuantity(String pieceQuantity) {
        this.pieceQuantity = pieceQuantity;
    }

    public String getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(String readStatus) {
        this.readStatus = readStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(quoteDetailsId);
        dest.writeString(quoteId);
        dest.writeString(customerId);
        dest.writeString(sizeId);
        dest.writeString(sizeTitle);
        dest.writeString(catId);
        dest.writeString(catTitle);
        dest.writeString(quantity);
        dest.writeString(measurement);
        dest.writeString(productId);
        dest.writeString(productTitle);
        dest.writeString(price);
        dest.writeString(pieceQuantity);
        dest.writeString(readStatus);
        dest.writeString(orderStatus);
        dest.writeString(status);
        dest.writeString(dateTimeAdded);
    }
}
