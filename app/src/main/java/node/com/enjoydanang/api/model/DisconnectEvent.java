package node.com.enjoydanang.api.model;

/**
 * Created by chientruong on 4/21/17.
 */

public class DisconnectEvent {
private boolean isDisconnect;

    public DisconnectEvent(boolean isReconnect) {
        this.isDisconnect = isReconnect;
    }

    public boolean isReconnect() {
        return isDisconnect;
    }

    public void setReconnect(boolean reconnect) {
        isDisconnect = reconnect;
    }
}
