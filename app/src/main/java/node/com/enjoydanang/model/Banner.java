
package node.com.enjoydanang.model;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

import node.com.enjoydanang.api.model.Repository;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Banner extends Repository{

    @SerializedName("count")
    private Long mCount;
    @SerializedName("data")
    private List<Datum> mData;

    public Banner(String status, String message) {
        super(status, message);
    }


    public Long getCount() {
        return mCount;
    }

    public void setCount(Long count) {
        mCount = count;
    }

    public List<Datum> getData() {
        return mData;
    }

    public void setData(List<Datum> data) {
        mData = data;
    }


}
