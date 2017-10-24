package node.com.enjoydanang.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.StringUtils;

import node.com.enjoydanang.constant.Constant;

/**
 * Author: Tavv
 * Created on 24/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class Utility {
    @SerializedName("Id")
    @Expose
    private int id;
    @SerializedName("Icon")
    @Expose
    private String icon;
    @SerializedName("Name")
    @Expose
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIcon() {
        return StringUtils.isNotBlank(icon) ? Constant.URL_HOST_IMAGE + icon : icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
