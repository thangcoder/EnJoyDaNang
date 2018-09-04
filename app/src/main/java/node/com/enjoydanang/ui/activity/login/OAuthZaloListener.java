package node.com.enjoydanang.ui.activity.login;

import com.zing.zalo.zalosdk.oauth.OAuthCompleteListener;
import com.zing.zalo.zalosdk.oauth.OauthResponse;

/**
 * Author: Tavv
 * Created on 28/08/2018
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public class OAuthZaloListener extends OAuthCompleteListener{

    @Override
    public void onGetOAuthComplete(OauthResponse response) {
        super.onGetOAuthComplete(response);
    }

    @Override
    public void onAuthenError(int errorCode, String message) {
        super.onAuthenError(errorCode, message);
    }
}
