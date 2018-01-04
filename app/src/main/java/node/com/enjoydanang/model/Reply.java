package node.com.enjoydanang.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import node.com.enjoydanang.utils.Utils;

/**
 * Author: Tavv
 * Created on 07/11/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public class Reply {

    @SerializedName("Id")
    @Expose
    private int id;

    @SerializedName("ReviewParentId")
    @Expose
    private int reviewId;

    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("Name")
    @Expose
    private String name;

    @SerializedName("Star")
    @Expose
    private int star;

    @SerializedName("Content")
    @Expose
    private String content;

    @SerializedName("Date")
    @Expose
    private String date;

    @SerializedName("Avatar")
    @Expose
    private String avatar;

    @SerializedName("Images")
    @Expose
    private List<ReviewImage> images = null;

    @SerializedName("Access")
    @Expose
    private int access;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAvatar() {
        return Utils.getImageNormalOrSocial(avatar);
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public List<ReviewImage> getImages() {
        return images;
    }

    public void setImages(List<ReviewImage> images) {
        this.images = images;
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public int getAccess() {
        return access;
    }

    public void setAccess(int access) {
        this.access = access;
    }

    public boolean isEnableRemove() {
        return access == 1;
    }
}
