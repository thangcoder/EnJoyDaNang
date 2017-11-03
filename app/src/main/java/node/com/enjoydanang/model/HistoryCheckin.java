package node.com.enjoydanang.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.StringUtils;

import node.com.enjoydanang.constant.Constant;

/**
 * Author: Tavv
 * Created on 30/10/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public class HistoryCheckin {
    @SerializedName("Id")
    @Expose
    private int id;
    @SerializedName("PartnerId")
    @Expose
    private int partnerId;
    @SerializedName("CustomerId")
    @Expose
    private int customerId;
    @SerializedName("PartnerName")
    @Expose
    private String partnerName;
    @SerializedName("Picture")
    @Expose
    private String picture;
    @SerializedName("Amount")
    @Expose
    private double amount;
    @SerializedName("Discount")
    @Expose
    private int discount;
    @SerializedName("Payment")
    @Expose
    private double payment;

    @SerializedName("Date")
    @Expose
    private String date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(int partnerId) {
        this.partnerId = partnerId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public String getPicture() {
        return StringUtils.isEmpty(picture) ? picture : Constant.URL_HOST_IMAGE + picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public double getPayment() {
        return payment;
    }

    public void setPayment(double payment) {
        this.payment = payment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
