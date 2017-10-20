
package node.com.enjoydanang.model;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

import node.com.enjoydanang.api.model.Repository;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Rate extends Repository {

    @SerializedName("data")
    private List<Datum> mData;

    public Rate(String status, String message) {
        super(status, message);
    }

    public List<Datum> getData() {
        return mData;
    }

    public void setData(List<Datum> data) {
        mData = data;
    }



}
