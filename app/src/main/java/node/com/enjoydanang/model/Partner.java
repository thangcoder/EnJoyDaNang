
package node.com.enjoydanang.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.StringUtils;

import node.com.enjoydanang.constant.Constant;

public class Partner implements Parcelable {

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

    @SerializedName("CategoryName")
    @Expose
    private String categoryName;

    private String locationAddress;

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

    public String getLocationAddress() {
        return locationAddress;
    }

    public void setLocationAddress(String locationAddress) {
        this.locationAddress = locationAddress;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.picture);
        dest.writeString(this.phone);
        dest.writeString(this.address);
        dest.writeInt(this.starReview);
        dest.writeInt(this.discount);
        dest.writeInt(this.favorite);
        dest.writeString(this.date);
        dest.writeString(this.distance);
        dest.writeInt(this.displayDistance);
        dest.writeString(this.geoLat);
        dest.writeString(this.geoLng);
        dest.writeString(this.geoLocation);
        dest.writeString(this.locationAddress);
        dest.writeString(this.categoryName);
    }

    public Partner() {
    }

    protected Partner(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.picture = in.readString();
        this.phone = in.readString();
        this.address = in.readString();
        this.starReview = in.readInt();
        this.discount = in.readInt();
        this.favorite = in.readInt();
        this.date = in.readString();
        this.distance = in.readString();
        this.displayDistance = in.readInt();
        this.geoLat = in.readString();
        this.geoLng = in.readString();
        this.geoLocation = in.readString();
        this.locationAddress = in.readString();
        this.categoryName = in.readString();
    }

    public static final Creator<Partner> CREATOR = new Creator<Partner>() {
        @Override
        public Partner createFromParcel(Parcel source) {
            return new Partner(source);
        }

        @Override
        public Partner[] newArray(int size) {
            return new Partner[size];
        }
    };
}
