
package node.com.enjoydanang.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

import node.com.enjoydanang.constant.Constant;

public class Partner implements Serializable {

    @SerializedName("Id")
    @Expose
    private int id;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Picture")
    @Expose
    private String picture;
    @SerializedName("Phone")
    @Expose
    private String phone;
    @SerializedName("Address")
    @Expose
    private String address;
    @SerializedName("StarReview")
    @Expose
    private int starReview;
    @SerializedName("Discount")
    @Expose
    private int discount;
    @SerializedName("Favorite")
    @Expose
    private int favorite;

    @SerializedName("Date")
    @Expose
    private String date;

    @SerializedName("Distance")
    @Expose
    private String distance;

    @SerializedName("DisplayDistance")
    @Expose
    private int displayDistance;

    @SerializedName("GeoLat")
    @Expose
    private String geoLat;

    @SerializedName("GeoLng")
    @Expose
    private String geoLng;

    @SerializedName("GeoLocation")
    @Expose
    private String geoLocation;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return StringUtils.isNoneBlank(picture) ? Constant.URL_HOST_IMAGE + picture : picture;
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

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

}
