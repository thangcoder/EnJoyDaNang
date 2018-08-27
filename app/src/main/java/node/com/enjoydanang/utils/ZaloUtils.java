package node.com.enjoydanang.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;

import com.zing.zalo.zalosdk.core.helper.AppInfo;
import com.zing.zalo.zalosdk.core.http.HttpClientRequest;
import com.zing.zalo.zalosdk.oauth.LoginChannel;
import com.zing.zalo.zalosdk.oauth.LoginVia;
import com.zing.zalo.zalosdk.oauth.OAuthCompleteListener;
import com.zing.zalo.zalosdk.oauth.OauthResponse;
import com.zing.zalo.zalosdk.oauth.OpenAPIService;
import com.zing.zalo.zalosdk.oauth.ValidateOAuthCodeCallback;
import com.zing.zalo.zalosdk.oauth.ZaloOpenAPICallback;
import com.zing.zalo.zalosdk.oauth.ZaloSDK;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import node.com.enjoydanang.model.PostZalo;
import node.com.enjoydanang.model.UserInfo;
import node.com.enjoydanang.utils.event.OnLoginZaloListener;

/**
 * Author: Tavv
 * Created on 26/08/2018
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public class ZaloUtils {
    private static final String TAG = ZaloUtils.class.getSimpleName();

    public boolean isGuest() {
        return LoginChannel.GUEST.equalsName(ZaloSDK.Instance.getLastestLoginChannel());
    }

    public boolean isAuthenticated() {
        return ZaloSDK.Instance.isAuthenticate(null);
    }

    public static void postToWall(Context context, String url, String title) {
        OpenAPIService.getInstance().postToWall(context, url, title, new ZaloOpenAPICallback() {
            @Override
            public void onResult(JSONObject jsonObject) {
                Log.d(TAG, "onResult: " + jsonObject);
            }
        });
    }

    public static JSONObject getAccessToken(Context context){
        JSONObject accessToken = new JSONObject();
        HttpClientRequest request = new HttpClientRequest(HttpClientRequest.Type.POST, "https://oauth.zaloapp.com/v3/mobile/access_token");
        request.addParams("code", ZaloSDK.Instance.getOAuthCode());
        request.addParams("pkg_name", AppInfo.getPackageName(context));
        request.addParams("sign_key", AppInfo.getApplicationHashKey(context));
        request.addParams("app_id", ZaloSDK.Instance.getAppID() + "");
        request.addParams("version", ZaloSDK.Instance.getVersion());
        return request.getJSON();
    }
}
