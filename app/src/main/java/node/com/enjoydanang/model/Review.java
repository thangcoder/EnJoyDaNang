package node.com.enjoydanang.model;

import android.animation.TimeInterpolator;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import node.com.enjoydanang.utils.Utils;

/**
 * Author: Tavv
 * Created on 29/10/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public class Review implements Serializable{

    @SerializedName("Id")
    @Expose
    private int id;
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

    @SerializedName("Images")
    @Expose
    private List<ReviewImage> images;

    @SerializedName("Avatar")
    @Expose
    private String avatar;

    private boolean isExpanded;

    private boolean isExpandedComment;

    private  TimeInterpolator interpolator;

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

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public boolean isExpandedComment() {
        return isExpandedComment;
    }

    public void setExpandedComment(boolean expandedComment) {
        isExpandedComment = expandedComment;
    }

    public TimeInterpolator getInterpolator() {
        return interpolator;
    }

    public void setInterpolator(TimeInterpolator interpolator) {
        this.interpolator = interpolator;
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
}
