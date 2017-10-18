package node.com.enjoydanang.ui.activity.login;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import com.kakao.auth.ApiResponseCallback;
import com.kakao.auth.AuthService;
import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.auth.network.response.AccessTokenInfoResponse;
import com.kakao.kakaotalk.response.KakaoTalkProfile;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.response.model.User;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import node.com.enjoydanang.ui.activity.BaseActivity;

import static com.kakao.util.helper.Utility.getPackageInfo;


/**
 * Author: Tavv
 * Created on 13/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class LoginViaKakaoTalk implements ILogin<KakaoTalkProfile, User> {
    private static final String TAG = LoginViaKakaoTalk.class.getSimpleName();

    private BaseActivity activity;

    private SessionCallback sessionCallback;

    private Session session;

    public LoginViaKakaoTalk(BaseActivity activity) {
        this.activity = activity;
    }

    @Override
    public void init() {
        if (sessionCallback == null) {
            sessionCallback = new SessionCallback();
            session = Session.getCurrentSession();
            session.addCallback(sessionCallback);
            session.checkAndImplicitOpen();
        }
    }

    @Override
    public void login() {
        session.open(AuthType.KAKAO_TALK, activity);
    }

    @Override
    public void handleCallbackResult(KakaoTalkProfile callback) {
        Log.i(TAG, "handleCallbackResult " + callback);
    }

    @Override
    public BaseActivity getActivity() {
        return activity;
    }

    @Override
    public void pushToServer(User model) {

    }

    @Override
    public void removeAccessToken() {

    }

    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {
            redirectSignupActivity();
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if (exception != null) {
                Logger.e(exception);
            }
        }
    }

    protected void redirectSignupActivity() {
        final Intent intent = new Intent(activity, KakaoSignupActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }


    private void requestAccessTokenInfo() {
        AuthService.requestAccessTokenInfo(new ApiResponseCallback<AccessTokenInfoResponse>() {
            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                // TODO: Handle something here !!! onSessionClosed
            }

            @Override
            public void onNotSignedUp() {
                // not happened
            }

            @Override
            public void onFailure(ErrorResult errorResult) {
                Logger.e("failed to get access token info. msg=" + errorResult);
            }

            @Override
            public void onSuccess(AccessTokenInfoResponse accessTokenInfoResponse) {
                long userId = accessTokenInfoResponse.getUserId();
                Logger.d("this access token is for userId=" + userId);

                long expiresInMilis = accessTokenInfoResponse.getExpiresInMillis();
                Logger.d("this access token expires after " + expiresInMilis + " milliseconds.");
            }
        });
    }


    public static String getKeyHash(final Context context) {
        PackageInfo packageInfo = getPackageInfo(context, PackageManager.GET_SIGNATURES);
        if (packageInfo == null)
            return null;

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                return Base64.encodeToString(md.digest(), Base64.NO_WRAP);
            } catch (NoSuchAlgorithmException e) {
                Log.w(TAG, "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
        return null;
    }

    public SessionCallback getSessionCallback() {
        return sessionCallback;
    }

    public void setSessionCallback(SessionCallback sessionCallback) {
        this.sessionCallback = sessionCallback;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }
}
