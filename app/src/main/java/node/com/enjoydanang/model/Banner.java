
package node.com.enjoydanang.model;

import java.util.List;
import javax.annotation.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import node.com.enjoydanang.constant.Constant;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Banner {

    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("Picture")
    @Expose
    private String picture;
    @SerializedName("Href")
    @Expose
    private String href;
    @SerializedName("Target")
    @Expose
    private String target;
    @SerializedName("Title")
    @Expose
    private String title;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPicture() {
        return Constant.URL_HOST_IMAGE + picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
