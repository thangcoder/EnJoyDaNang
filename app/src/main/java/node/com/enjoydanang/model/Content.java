package node.com.enjoydanang.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import node.com.enjoydanang.utils.Utils;

/**
 * Author: Tavv
 * Created on 18/12/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class Content {
    @SerializedName("Title")
    @Expose
    private String title;

    @SerializedName("Picture")
    @Expose
    private String picture;

    @SerializedName("Content")
    @Expose
    private String content;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPicture() {
        return Utils.getImageNormalOrSocial(picture);
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
