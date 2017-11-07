package node.com.enjoydanang.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import node.com.enjoydanang.utils.Utils;

/**
 * Author: Tavv
 * Created on 10/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class PartnerAlbum implements Parcelable{

    @SerializedName("Id")
    @Expose
    private int id;
    @SerializedName("Picture")
    @Expose
    private String picture;
    @SerializedName("Title")
    @Expose
    private String title;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPicture() {
        return Utils.getImageNormalOrSocial(picture);
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.picture);
        dest.writeString(this.title);
    }

    public PartnerAlbum() {
    }

    protected PartnerAlbum(Parcel in) {
        this.id = in.readInt();
        this.picture = in.readString();
        this.title = in.readString();
    }

    public static final Creator<PartnerAlbum> CREATOR = new Creator<PartnerAlbum>() {
        @Override
        public PartnerAlbum createFromParcel(Parcel source) {
            return new PartnerAlbum(source);
        }

        @Override
        public PartnerAlbum[] newArray(int size) {
            return new PartnerAlbum[size];
        }
    };

    @Override
    public String toString() {
        return "PartnerAlbum{" +
                "id=" + id +
                ", picture='" + picture + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
