package node.com.enjoydanang.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import node.com.enjoydanang.utils.Utils;

/**
 * Author: Tavv
 * Created on 27/11/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class Popup {

    @SerializedName("Title")
    @Expose
    private String title;

    @SerializedName("Href")
    @Expose
    private String href;

    @SerializedName("Image")
    @Expose
    private String image;

    @SerializedName("TextButtonLeft")
    @Expose
    private String textButtonLeft;

    @SerializedName("TextButtonCenter")
    @Expose
    private String textButtonCenter;

    @SerializedName("TextButtonRight")
    @Expose
    private String textButtonRight;

    @SerializedName("SizeWidth")
    @Expose
    private String sizeWidth;

    @SerializedName("SizeHeight")
    @Expose
    private String sizeHeight;

    @SerializedName("IsPublish")
    @Expose
    private int isPublish;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getImage() {
        return Utils.getImageNormalOrSocial(image);
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTextButtonLeft() {
        return textButtonLeft;
    }

    public void setTextButtonLeft(String textButtonLeft) {
        this.textButtonLeft = textButtonLeft;
    }

    public String getTextButtonCenter() {
        return textButtonCenter;
    }

    public void setTextButtonCenter(String textButtonCenter) {
        this.textButtonCenter = textButtonCenter;
    }

    public String getTextButtonRight() {
        return textButtonRight;
    }

    public void setTextButtonRight(String textButtonRight) {
        this.textButtonRight = textButtonRight;
    }

    public String getSizeWidth() {
        return sizeWidth;
    }

    public void setSizeWidth(String sizeWidth) {
        this.sizeWidth = sizeWidth;
    }

    public String getSizeHeight() {
        return sizeHeight;
    }

    public void setSizeHeight(String sizeHeight) {
        this.sizeHeight = sizeHeight;
    }

    public int getIsPublish() {
        return isPublish;
    }

    public void setIsPublish(int isPublish) {
        this.isPublish = isPublish;
    }
}
