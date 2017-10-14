package node.com.enjoydanang.api.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by chientruong on 12/26/16.
 */

public class Repository {
    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;



    public Repository(String status, String token, String message) {
        this.status = status;
        this.message = message;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
