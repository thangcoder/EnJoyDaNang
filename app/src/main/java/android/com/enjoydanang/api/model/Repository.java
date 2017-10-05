package android.com.enjoydanang.api.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by chientruong on 12/26/16.
 */

public class Repository {
    @SerializedName("status")
    private String status;
    @SerializedName("token")
    private String token;
    @SerializedName("message")
    private String message;
    @SerializedName("ans_token")
    private String ANSToken;


    public Repository(String status, String token, String message) {
        this.status = status;
        this.token = token;
        this.message = message;
    }

    public String getANSToken() {
        return ANSToken;
    }

    public void setANSToken(String ANSToken) {
        this.ANSToken = ANSToken;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
