package node.com.enjoydanang.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by chientruong on 12/26/16.
 */

public class Repository<T> {
    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;


    @SerializedName("data")
    private List<T> data;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public Repository(String status,  String message) {
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
