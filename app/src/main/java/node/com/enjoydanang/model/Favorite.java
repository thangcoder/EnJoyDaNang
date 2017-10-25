package node.com.enjoydanang.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.StringUtils;

import node.com.enjoydanang.constant.Constant;

/**
 * Author: Tavv
 * Created on 25/10/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public class Favorite {

    @SerializedName("Id")
    @Expose
    private int id;

    @SerializedName("Name")
    @Expose
    private String name;

    @SerializedName("Picture")
    @Expose
    private String picture;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return StringUtils.isEmpty(picture) ? picture : Constant.URL_HOST_IMAGE + picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
