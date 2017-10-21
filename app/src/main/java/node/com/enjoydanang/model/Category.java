
package node.com.enjoydanang.model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.StringUtils;

import javax.annotation.Generated;

import node.com.enjoydanang.api.model.Repository;
import node.com.enjoydanang.constant.Constant;


public class Category {

    @SerializedName("Id")
    @Expose
    private int id;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Icon")
    @Expose
    private String icon;
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

    public String getIcon() {
        return StringUtils.isNoneBlank(icon) ? Constant.URL_HOST_IMAGE + icon : icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getPicture() {
        return StringUtils.isNoneBlank(picture) ? Constant.URL_HOST_IMAGE + picture : picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                ", picture='" + picture + '\'' +
                '}';
    }
}
