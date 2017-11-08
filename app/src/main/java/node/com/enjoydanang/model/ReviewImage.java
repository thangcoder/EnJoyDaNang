package node.com.enjoydanang.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import node.com.enjoydanang.utils.Utils;

/**
 * Author: Tavv
 * Created on 08/11/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class ReviewImage {

    @SerializedName("Id")
    @Expose
    private int id;

    @SerializedName("Picture")
    @Expose
    private String picture;

    public ReviewImage() {
    }

    public ReviewImage(int id, String picture) {
        this.id = id;
        this.picture = picture;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPicture() {
        return Utils.getImageNormalOrSocial(picture);
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
