package node.com.enjoydanang.api.model;

/**
 * Created by chientruong on 4/13/17.
 */

public class PushEvent {
    private  boolean isPush;

    public boolean isUpdate() {
        return isPush;
    }

    public void setUpdate(boolean isPush) {
        isPush = isPush;
    }

    public PushEvent(boolean isPush) {
        this.isPush = isPush;
    }
}
