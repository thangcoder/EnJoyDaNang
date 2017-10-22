package node.com.enjoydanang.constant;

import com.google.gson.annotations.SerializedName;

/**
 * Author: Tavv
 * Created on 20/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public enum Role {
    @SerializedName("ADMIN")
    ADMIN,
    @SerializedName("CUSTOMER")
    CUSTOMER,
    @SerializedName("PARTNER")
    PARTNER
}
