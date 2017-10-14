package node.com.enjoydanang.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author: Tavv
 * Created on 10/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class AlbumModel implements Parcelable{

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
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.image);
        dest.writeString(this.time);
    }

    protected AlbumModel(Parcel in) {
        this.name = in.readString();
        this.image = in.readString();
        this.time = in.readString();
    }

    public static final Creator<AlbumModel> CREATOR = new Creator<AlbumModel>() {
        @Override
        public AlbumModel createFromParcel(Parcel source) {
            return new AlbumModel(source);
        }

        @Override
        public AlbumModel[] newArray(int size) {
            return new AlbumModel[size];
        }
    };

    @Override
    public String toString() {
        return "AlbumModel{" +
                "name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
