package node.com.enjoydanang.model;

/**
 * Author: Tavv
 * Created on 10/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class AlbumModel {

    private String name;

    private String image;

    private String time;

    public AlbumModel() {
    }

    public AlbumModel(String name, String image, String time) {
        this.name = name;
        this.image = image;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "AlbumModel{" +
                "name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
