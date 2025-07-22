package com.madhavsteel.model;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Order extends ExpandableGroup<OrderInquiry> implements Serializable {

    String id, userId, customerId, customerName, quoteNo, total, grandTotal, gst, cgstTotal, sgstTotal, pdfLink, orderStatus, status, payStatus, readStatus, dateTimeAdded;
    ArrayList<OrderInquiry> inquiryList;

    public Order(String title, List<OrderInquiry> items) {
        super(title, items);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getQuoteNo() {
        return quoteNo;
    }

    public void setQuoteNo(String quoteNo) {
        this.quoteNo = quoteNo;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(String grandTotal) {
        this.grandTotal = grandTotal;
    }

    public String getGst() {
        return gst;
    }

    public void setGst(String gst) {
        this.gst = gst;
    }

    public String getCgstTotal() {
        return cgstTotal;
    }

    public void setCgstTotal(String cgstTotal) {
        this.cgstTotal = cgstTotal;
    }

    public String getSgstTotal() {
        return sgstTotal;
    }

    public void setSgstTotal(String sgstTotal) {
        this.sgstTotal = sgstTotal;
    }

    public String getPdfLink() {
        return pdfLink;
    }

    public void setPdfLink(String pdfLink) {
        this.pdfLink = pdfLink;
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

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public String getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(String readStatus) {
        this.readStatus = readStatus;
    }

    public String getDateTimeAdded() {
        return dateTimeAdded;
    }

    public void setDateTimeAdded(String dateTimeAdded) {
        this.dateTimeAdded = dateTimeAdded;
    }

    public ArrayList<OrderInquiry> getInquiryList() {
        return inquiryList;
    }

    public void setInquiryList(ArrayList<OrderInquiry> inquiryList) {
        this.inquiryList = inquiryList;
    }
}
