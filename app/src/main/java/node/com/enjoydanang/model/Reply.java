package node.com.enjoydanang.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import butterknife.BindView;
import node.com.enjoydanang.R;

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

    @SerializedName("ReviewId")
    @Expose
    private int reviewId;

    @SerializedName("UserName")
    @Expose
    private String userName;

    @SerializedName("Avatar")
    @Expose
    private String avatar;

    @SerializedName("Content")
    @Expose
    private String content;

    @SerializedName("Images")
    @Expose
    private List<String> images;

    public Reply() {
    }

    public Reply(int id, int reviewId, String avatar, String content, List<String> images) {
        this.id = id;
        this.reviewId = reviewId;
        this.avatar = avatar;
        this.content = content;
        this.images = images;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
