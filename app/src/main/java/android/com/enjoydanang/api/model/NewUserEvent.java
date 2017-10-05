package android.com.enjoydanang.api.model;

/**
 * Created by chientruong on 4/19/17.
 */

public class NewUserEvent {
    private  boolean isNewUser;

    public boolean isNewUser() {
        return isNewUser;
    }

    public void setisNewUser(boolean isNewUser) {
        isNewUser = isNewUser;
    }

    public NewUserEvent(boolean isNewUser) {
        this.isNewUser = isNewUser;
    }
}
