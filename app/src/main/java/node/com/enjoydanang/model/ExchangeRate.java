package node.com.enjoydanang.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Author: Tavv
 * Created on 23/10/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public class ExchangeRate {
    @SerializedName("Type")
    @Expose
    private String type;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("FlagEN")
    @Expose
    private String flagEN;
    @SerializedName("FlagVN")
    @Expose
    private String flagVN;
    @SerializedName("Rate")
    @Expose
    private String rate;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFlagEN() {
        return flagEN;
    }

    public void setFlagEN(String flagEN) {
        this.flagEN = flagEN;
    }

    public String getFlagVN() {
        return flagVN;
    }

    public void setFlagVN(String flagVN) {
        this.flagVN = flagVN;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
}
