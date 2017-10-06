package node.com.enjoydanang.api.model;

/**
 * Created by chientruong on 4/26/17.
 */

public class LastItemEvent {
    private boolean isLast;

    public LastItemEvent(boolean isLast) {
        this.isLast = isLast;
    }

    public boolean isLast() {
        return isLast;
    }

    public void setLast(boolean last) {
        isLast = last;
    }
}
