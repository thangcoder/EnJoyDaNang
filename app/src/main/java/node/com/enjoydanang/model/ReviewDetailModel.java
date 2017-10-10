package node.com.enjoydanang.model;

import com.google.gson.annotations.SerializedName;

/**
 * Author: Tavv
 * Created on 10/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class ReviewDetailModel {

    @SerializedName("rate")
    private float rate;

    @SerializedName("numberOfRated")
    private int numberOfRated;

    @SerializedName("avatar")
    private String avatar;

    @SerializedName("comment")
    private String comment;

    public ReviewDetailModel() {
    }

    public ReviewDetailModel(float rate, int numberOfRated, String avatar, String comment) {
        this.rate = rate;
        this.numberOfRated = numberOfRated;
        this.avatar = avatar;
        this.comment = comment;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public int getNumberOfRated() {
        return numberOfRated;
    }

    public void setNumberOfRated(int numberOfRated) {
        this.numberOfRated = numberOfRated;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "ReviewDetailModel{" +
                "rate=" + rate +
                ", numberOfRated=" + numberOfRated +
                ", avatar='" + avatar + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }
}
