package node.com.enjoydanang.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.StringUtils;

import node.com.enjoydanang.constant.Constant;

/**
 * Author: Tavv
 * Created on 22/10/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public class DetailPartner {

    @SerializedName("Id")
    @Expose
    private int id;
    @SerializedName("Code")
    @Expose
    private String code;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("Picture")
    @Expose
    private String picture;
    @SerializedName("Phone")
    @Expose
    private String phone;
    @SerializedName("Email")
    @Expose
    private String email;
    @SerializedName("EmailBooking")
    @Expose
    private String emailBooking;
    @SerializedName("Booking")
    @Expose
    private boolean booking;
    @SerializedName("Address")
    @Expose
    private String address;
    @SerializedName("StarReview")
    @Expose
    private int starReview;
    @SerializedName("Discount")
    @Expose
    private int discount;
    @SerializedName("TotalView")
    @Expose
    private int totalView;
    @SerializedName("AllowCustomerReviews")
    @Expose
    private boolean allowCustomerReviews;
    @SerializedName("GeoLat")
    @Expose
    private String geoLat;
    @SerializedName("GeoLng")
    @Expose
    private String geoLng;
    @SerializedName("GeoLocation")
    @Expose
    private String geoLocation;
    @SerializedName("IncentivesOther")
    @Expose
    private boolean incentivesOther;
    @SerializedName("IncentivesContent")
    @Expose
    private String incentivesContent;
    @SerializedName("Video")
    @Expose
    private String video;

    @SerializedName("Distance")
    @Expose
    private String distance;

    @SerializedName("DisplayDistance")
    @Expose
    private int displayDistance;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicture() {
        return StringUtils.isNotBlank(picture) ? Constant.URL_HOST_IMAGE + picture : picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmailBooking() {
        return emailBooking;
    }

    public void setEmailBooking(String emailBooking) {
        this.emailBooking = emailBooking;
    }

    public boolean isBooking() {
        return booking;
    }

    public void setBooking(boolean booking) {
        this.booking = booking;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getStarReview() {
        return starReview;
    }

    public void setStarReview(int starReview) {
        this.starReview = starReview;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getTotalView() {
        return totalView;
    }

    public void setTotalView(int totalView) {
        this.totalView = totalView;
    }

    public boolean isAllowCustomerReviews() {
        return allowCustomerReviews;
    }

    public void setAllowCustomerReviews(boolean allowCustomerReviews) {
        this.allowCustomerReviews = allowCustomerReviews;
    }

    public String getGeoLat() {
        return geoLat;
    }

    public void setGeoLat(String geoLat) {
        this.geoLat = geoLat;
    }

    public String getGeoLng() {
        return geoLng;
    }

    public void setGeoLng(String geoLng) {
        this.geoLng = geoLng;
    }

    public String getGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation(String geoLocation) {
        this.geoLocation = geoLocation;
    }

    public boolean isIncentivesOther() {
        return incentivesOther;
    }

    public void setIncentivesOther(boolean incentivesOther) {
        this.incentivesOther = incentivesOther;
    }

    public String getIncentivesContent() {
        return incentivesContent;
    }

    public void setIncentivesContent(String incentivesContent) {
        this.incentivesContent = incentivesContent;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public int getDisplayDistance() {
        return displayDistance;
    }

    public void setDisplayDistance(int displayDistance) {
        this.displayDistance = displayDistance;
    }

    public boolean isDisplayDistance() {
        return displayDistance == 1;
    }
}
