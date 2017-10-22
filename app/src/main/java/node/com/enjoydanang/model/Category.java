
package node.com.enjoydanang.model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

import node.com.enjoydanang.api.model.Repository;
import node.com.enjoydanang.constant.Constant;


@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Category{

    @SerializedName("icon")
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
        return icon;
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
        return Constant.URL_HOST_IMAGE + picture;
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
}
