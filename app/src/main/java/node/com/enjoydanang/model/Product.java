package node.com.enjoydanang.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by chien on 10/8/17.
 */

public class Product {
    @SerializedName("meta")
    private String meta;
    @SerializedName("title")
    private String title;
    @SerializedName("image")
    private String image;


    public String getMeta() {
        return meta;
    }

    public void setMeta(String name) {
        this.meta = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Product() {
    }

    public Product(String meta, String title, String image) {
        this.meta = meta;
        this.title = title;
        this.image = image;
    }
}