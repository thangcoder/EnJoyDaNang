package android.com.enjoydanang.api.model;

/**
 * Created by chientruong on 3/27/17.
 */

public class AlarmEvent {
    public int type ;
    public int requestCode;
    private String time;

    public AlarmEvent(int type, int requestCode) {
        this.type = type;
        this.requestCode = requestCode;
    }

    public AlarmEvent(int type, int requestCode, String time) {
        this.type = type;
        this.requestCode = requestCode;
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
