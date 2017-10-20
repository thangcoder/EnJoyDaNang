
package node.com.enjoydanang.model;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

import node.com.enjoydanang.constant.Constant;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Datum {

    @SerializedName("Address")
    private String mAddress;
    @SerializedName("Discount")
    private Long mDiscount;
    @SerializedName("Id")
    private Long mId;
    @SerializedName("Name")
    private String mName;
    @SerializedName("Phone")
    private String mPhone;
    @SerializedName("Picture")
    private String mPicture;
    @SerializedName("StarReview")
    private Long mStarReview;

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String Address) {
        mAddress = Address;
    }

    public Long getDiscount() {
        return mDiscount;
    }

    public void setDiscount(Long Discount) {
        mDiscount = Discount;
    }

    public Long getId() {
        return mId;
    }

    public void setId(Long Id) {
        mId = Id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String Name) {
        mName = Name;
    }

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String Phone) {
        mPhone = Phone;
    }

    public String getPicture() {
        return Constant.URL_HOST_IMAGE+ mPicture;
    }

    public void setPicture(String Picture) {
        mPicture = Picture;
    }

    public Long getStarReview() {
        return mStarReview;
    }

    public void setStarReview(Long StarReview) {
        mStarReview = StarReview;
    }

}
