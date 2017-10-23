
package node.com.enjoydanang.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.StringUtils;

import node.com.enjoydanang.constant.Constant;

public class Category {

    @SerializedName("Icon")
    @Expose
    private String icon;
    @SerializedName("Id")
    @Expose
    private Integer id;
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
    private Integer starReview;
    @SerializedName("Discount")
    @Expose
    private Integer discount;

    public String getIcon() {
        return StringUtils.isNoneBlank(icon) ? Constant.URL_HOST_IMAGE + icon : icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Integer getStarReview() {
        return starReview;
    }

    public void setStarReview(Integer starReview) {
        this.starReview = starReview;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    @Override
    public String toString() {
        return "Category{" +
                "icon='" + icon + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", picture='" + picture + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", starReview=" + starReview +
                ", discount=" + discount +
                '}';
    }
}