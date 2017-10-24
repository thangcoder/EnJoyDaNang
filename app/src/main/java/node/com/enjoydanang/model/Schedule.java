package node.com.enjoydanang.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Author: Tavv
 * Created on 24/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class Schedule{
    @SerializedName("Id")
    @Expose
    private int id;
    @SerializedName("STT")
    @Expose
    private int sTT;
    @SerializedName("DayOfWeek")
    @Expose
    private String dayOfWeek;
    @SerializedName("Open")
    @Expose
    private String open;
    @SerializedName("Close")
    @Expose
    private String close;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSTT() {
        return sTT;
    }

    public void setSTT(int sTT) {
        this.sTT = sTT;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getClose() {
        return close;
    }

    public void setClose(String close) {
        this.close = close;
    }

}
