package node.com.enjoydanang.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.StringUtils;

import node.com.enjoydanang.constant.Role;
import node.com.enjoydanang.utils.Utils;

/**
 * Author: Tavv
 * Created on 20/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class UserInfo {

    @SerializedName("id")
    @Expose
    private long userId = 0;

    @SerializedName("username")
    @Expose
    private String userName;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("fullname")
    @Expose
    private String fullName;

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("image")
    @Expose
    private String image;

    @SerializedName("code")
    @Expose
    private String code;

    @SerializedName("role")
    @Expose
    private Role role;

    @SerializedName("type")
    @Expose
    private String type;

    public UserInfo() {
    }

    public UserInfo(String userName, String password, String email, String fullName, String phone) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.fullName = fullName;
        this.phone = phone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return StringUtils.isNoneBlank(fullName) ? fullName : userName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImage() {
        return Utils.getImageNormalOrSocial(image);
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", fullName='" + fullName + '\'' +
                ", phone='" + phone + '\'' +
                ", image='" + image + '\'' +
                ", code='" + code + '\'' +
                ", role=" + role +
                ", type='" + type + '\'' +
                '}';
    }
}
