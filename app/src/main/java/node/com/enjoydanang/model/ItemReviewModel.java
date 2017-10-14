package node.com.enjoydanang.model;

import com.google.gson.annotations.SerializedName;

/**
 * Author: Tavv
 * Created on 11/10/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public class ItemReviewModel {
    @SerializedName("reviewerName")
    private String reviewerName;

    @SerializedName("rate")
    private float rate;

    @SerializedName("avatar")
    private String avatar;

    @SerializedName("comment")
    private String comment;

    public ItemReviewModel() {
    }

    public ItemReviewModel(String reviewerName, float rate, String avatar, String comment) {
        this.reviewerName = reviewerName;
        this.rate = rate;
        this.avatar = avatar;
        this.comment = comment;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
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
                "reviewerName='" + reviewerName + '\'' +
                ", rate=" + rate +
                ", avatar='" + avatar + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }
}
