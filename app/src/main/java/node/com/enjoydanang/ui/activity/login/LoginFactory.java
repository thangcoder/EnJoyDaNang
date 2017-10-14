package node.com.enjoydanang.ui.activity.login;

import node.com.enjoydanang.constant.LoginType;
import node.com.enjoydanang.ui.activity.BaseActivity;

/**
 * Author: Tavv
 * Created on 13/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class LoginFactory {

    ILogin doLogin(LoginType loginType, BaseActivity activity) {
        switch (loginType) {
            case FACEBOOK:
                return new LoginViaFacebook(activity);
            case GOOGLE:
                return new LoginViaGoogle(activity);
            default:
                return null;
        }
    }

}
