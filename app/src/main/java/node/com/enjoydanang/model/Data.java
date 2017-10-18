package node.com.enjoydanang.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Author: Tavv
 * Created on 12/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class Data {
    @SerializedName("is_silhouette")
    @Expose
    private boolean isSilhouette;
    @SerializedName("url")
    @Expose
    private String url;

    public boolean isIsSilhouette() {
        return isSilhouette;
    }

    public void setIsSilhouette(boolean isSilhouette) {
        this.isSilhouette = isSilhouette;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}