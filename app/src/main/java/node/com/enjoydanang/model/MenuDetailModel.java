package node.com.enjoydanang.model;

import com.google.gson.annotations.SerializedName;

/**
 * Author: Tavv
 * Created on 10/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class MenuDetailModel {

    @SerializedName("name")
    private String name;

    @SerializedName("weigh")
    private String weigh;

    @SerializedName("image")
    private String image;

    public MenuDetailModel() {
    }

    public MenuDetailModel(String name, String weigh, String image) {
        this.name = name;
        this.weigh = weigh;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWeigh() {
        return weigh;
    }

    public void setWeigh(String weigh) {
        this.weigh = weigh;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "MenuDetailModel{" +
                "name='" + name + '\'' +
                ", weigh='" + weigh + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
