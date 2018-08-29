package node.com.enjoydanang.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;

import com.zing.zalo.zalosdk.core.helper.AppInfo;
import com.zing.zalo.zalosdk.core.http.HttpClientRequest;
import com.zing.zalo.zalosdk.oauth.FeedData;
import com.zing.zalo.zalosdk.oauth.LoginChannel;
import com.zing.zalo.zalosdk.oauth.LoginVia;
import com.zing.zalo.zalosdk.oauth.OAuthCompleteListener;
import com.zing.zalo.zalosdk.oauth.OauthResponse;
import com.zing.zalo.zalosdk.oauth.OpenAPIService;
import com.zing.zalo.zalosdk.oauth.ValidateOAuthCodeCallback;
import com.zing.zalo.zalosdk.oauth.ZaloOpenAPICallback;
import com.zing.zalo.zalosdk.oauth.ZaloPluginCallback;
import com.zing.zalo.zalosdk.oauth.ZaloSDK;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import node.com.enjoydanang.R;
import node.com.enjoydanang.model.PostZalo;
import node.com.enjoydanang.model.UserInfo;
import node.com.enjoydanang.utils.event.OnLoginZaloListener;

/**
 * Author: Tavv
 * Created on 26/08/2018
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public class ZaloUtils extends OAuthCompleteListener {
    private static final String TAG = ZaloUtils.class.getSimpleName();

    private OnLoginZaloListener onLoginZaloListener;


    public void setLoginZaLoListener(OnLoginZaloListener onLoginZaloListener) {
        this.onLoginZaloListener = onLoginZaloListener;
    }

    public void login(Activity activity) {
        ZaloSDK.Instance.authenticate(activity, LoginVia.APP_OR_WEB, this);
    }

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

    public static void shareFeed(Context context, String url, String title) {
        FeedData feedData = new FeedData();
        feedData.setLink(url);
        feedData.setAppName(Utils.getString(R.string.app_name));
        OpenAPIService.getInstance().shareFeed(context, feedData, new ZaloPluginCallback() {
            @Override
            public void onResult(boolean isShareSuccess, int error, String s, String s1) {
                Log.i(TAG, "onResult: " + isShareSuccess + " " + error + " "+ s);
            }
        });
    }

    public static JSONObject getAccessToken(Context context) {
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
