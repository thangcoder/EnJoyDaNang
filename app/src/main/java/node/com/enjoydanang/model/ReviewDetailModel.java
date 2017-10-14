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

    @SerializedName("avatar")
    private String avatar;

    @SerializedName("numberOfRated")
    private int numberOfRated;

    public ReviewDetailModel(float rate, String avatar, int numberOfRated) {
        this.rate = rate;
        this.avatar = avatar;
        this.numberOfRated = numberOfRated;
    }

    public ReviewDetailModel() {
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

    public int getNumberOfRated() {
        return numberOfRated;
    }

    public void setNumberOfRated(int numberOfRated) {
        this.numberOfRated = numberOfRated;
    }

    @Override
    public String toString() {
        return "ReviewDetailModel{" +
                "rate=" + rate +
                ", avatar='" + avatar + '\'' +
                ", numberOfRated=" + numberOfRated +
                '}';
    }
}
