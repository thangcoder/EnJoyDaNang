package node.com.enjoydanang.model;

import com.google.gson.annotations.SerializedName;

/**
 * Author: Tavv
 * Created on 10/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class DetailModel {

    @SerializedName("name")
    private String name;
    @SerializedName("rate")
    private float rate;
    @SerializedName("content")
    private String content;

    public DetailModel() {
    }

    public DetailModel(String name, float rate, String content) {
        this.name = name;
        this.rate = rate;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "DetailModel{" +
                "name='" + name + '\'' +
                ", rate=" + rate +
                ", content='" + content + '\'' +
                '}';
    }
}
