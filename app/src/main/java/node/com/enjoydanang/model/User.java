package node.com.enjoydanang.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import node.com.enjoydanang.constant.LoginType;

/**
 * Author: Tavv
 * Created on 12/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class User {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String fullName;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("birthday")
    @Expose
    private String birthday;
    @SerializedName("gender")
    @Expose
    private String gender;

    @SerializedName("picture")
    @Expose
    private Picture picture;

    private String accessToken;

    private LoginType type;

    public User() {
    }

    public User(String firstName, String lastName, String fullName, Picture picture, String id, String email, String birthday, String gender, String accessToken, LoginType type) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullName = fullName;
        this.picture = picture;
        this.id = id;
        this.email = email;
        this.birthday = birthday;
        this.gender = gender;
        this.accessToken = accessToken;
        this.type = type;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Picture getPicture() {
        return picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public LoginType getType() {
        return type;
    }

    public void setType(LoginType type) {
        this.type = type;
    }


}
