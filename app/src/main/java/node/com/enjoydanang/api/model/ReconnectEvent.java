package node.com.enjoydanang.api.model;

/**
 * Created by chientruong on 4/21/17.
 */

public class ReconnectEvent {
    private boolean isReconnect;

    public ReconnectEvent(boolean isReconnect) {
        this.isReconnect = isReconnect;
    }

    public boolean isReconnect() {
        return isReconnect;
    }

    public void setReconnect(boolean reconnect) {
        isReconnect = reconnect;
    }
}
