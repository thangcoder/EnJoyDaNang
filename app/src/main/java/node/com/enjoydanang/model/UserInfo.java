package node.com.enjoydanang.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

import node.com.enjoydanang.constant.Role;
import node.com.enjoydanang.utils.Utils;

/**
 * Author: Tavv
 * Created on 20/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class UserInfo implements Serializable {

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

    private boolean isIgnoreLogin;

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
        try {
            return StringUtils.isNoneBlank(fullName) ? fullName : StringUtils.EMPTY;
        } catch (Exception e) {
            if (fullName != null && !fullName.equals("")) {
                return fullName;
            }
            return "";
        }
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

    public boolean isIgnoreLogin() {
        return isIgnoreLogin;
    }

    public void setIgnoreLogin(boolean ignoreLogin) {
        isIgnoreLogin = ignoreLogin;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserInfo userInfo = (UserInfo) o;

        if (userId != userInfo.userId) return false;
        if (userName != null ? !userName.equals(userInfo.userName) : userInfo.userName != null)
            return false;
        if (password != null ? !password.equals(userInfo.password) : userInfo.password != null)
            return false;
        if (email != null ? !email.equals(userInfo.email) : userInfo.email != null) return false;
        if (fullName != null ? !fullName.equals(userInfo.fullName) : userInfo.fullName != null)
            return false;
        if (phone != null ? !phone.equals(userInfo.phone) : userInfo.phone != null) return false;
        if (image != null ? !image.equals(userInfo.image) : userInfo.image != null) return false;
        if (code != null ? !code.equals(userInfo.code) : userInfo.code != null) return false;
        if (role != userInfo.role) return false;
        return type != null ? type.equals(userInfo.type) : userInfo.type == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (userId ^ (userId >>> 32));
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (fullName != null ? fullName.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        result = 31 * result + (image != null ? image.hashCode() : 0);
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }
}
