package node.com.enjoydanang.model;

/**
 * Author: Tavv
 * Created on 26/08/2018
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public class PostZalo {

    private String title;

    private String urlShare;

    private String description;

    public PostZalo(String title, String urlShare, String description) {
        this.title = title;
        this.urlShare = urlShare;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrlShare() {
        return urlShare;
    }

    public void setUrlShare(String urlShare) {
        this.urlShare = urlShare;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "PostZalo{" +
                "title='" + title + '\'' +
                ", urlShare='" + urlShare + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
