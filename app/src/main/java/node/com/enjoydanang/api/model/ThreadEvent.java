package node.com.enjoydanang.api.model;

/**
 * Created by chientruong on 3/7/17.
 */

public class ThreadEvent {
    private  boolean isUpdate;

    public boolean isUpdate() {
        return isUpdate;
    }

    public void setUpdate(boolean update) {
        isUpdate = update;
    }

    public ThreadEvent(boolean isUpdate) {
        this.isUpdate = isUpdate;
    }
}
