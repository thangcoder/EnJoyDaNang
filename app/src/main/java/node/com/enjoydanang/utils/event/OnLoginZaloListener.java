package node.com.enjoydanang.utils.event;

import com.zing.zalo.zalosdk.oauth.OauthResponse;

/**
 * Author: Tavv
 * Created on 26/08/2018
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public interface OnLoginZaloListener {

    void onLoginSuccess(OauthResponse response);

    void onLoginFailed(String message);
}
