package node.com.enjoydanang.ui.activity.login;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import com.kakao.auth.AuthType;
import com.kakao.auth.ErrorCode;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import node.com.enjoydanang.constant.LoginType;
import node.com.enjoydanang.model.Data;
import node.com.enjoydanang.model.Picture;
import node.com.enjoydanang.model.User;
import node.com.enjoydanang.ui.activity.BaseActivity;

import static com.kakao.util.helper.Utility.getPackageInfo;


/**
 * Author: Tavv
 * Created on 13/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class LoginViaKakaoTalk implements ILogin<UserProfile, User> {
    private static final String TAG = LoginViaKakaoTalk.class.getSimpleName();

    private BaseActivity activity;

    private SessionCallback sessionCallback;

    private LoginPresenter mLoginPresenter;


    private LoginCallBack loginCallBack;

    public LoginViaKakaoTalk(BaseActivity activity, LoginCallBack loginCallBack) {
        this.activity = activity;
        this.loginCallBack = loginCallBack;
    }

    @Override
    public void init() {
        // Login via Kakao haven't init. Function init config at Application
    }

    @Override
    public void login() {
        if (sessionCallback == null) {
            sessionCallback = new SessionCallback();
            Session.getCurrentSession().addCallback(sessionCallback);
            Session.getCurrentSession().checkAndImplicitOpen();
        }
        Session.getCurrentSession().open(AuthType.KAKAO_LOGIN_ALL, activity);
    }

    @Override
    public void handleCallbackResult(UserProfile callback) {
        if (callback != null) {
            User user = new User();
            user.setId(callback.getId());
            user.setFullName(callback.getNickname());
            Data data = new Data();
            data.setUrl(callback.getProfileImagePath());
            Picture picture = new Picture();
            picture.setData(data);
            user.setPicture(picture);
            user.setEmail(callback.getEmail());
            user.setType(LoginType.KAKAOTALK);
            user.setAccessToken(getAccessToken());
            pushToServer(user);
        }
    }

    @Override
    public BaseActivity getActivity() {
        return activity;
    }

    @Override
    public void pushToServer(User user) {
        if(user != null){
            mLoginPresenter.loginViaSocial(user);
        }
    }

    @Override
    public void removeAccessToken() {

    }

    @Override
    public void setLoginPresenter(LoginPresenter loginPresenter) {
        if (loginPresenter == null) {
            throw new NullPointerException("LoginPresenter not be null !");
        }
        this.mLoginPresenter = loginPresenter;
    }


    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {
            loginCallBack.hideWaiting();
            requestMe();
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if (exception != null) {
                Logger.e(exception);
            }
        }
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
        return Session.getCurrentSession();
    }

    private void requestMe() {
        List<String> propertyKeys = new ArrayList<String>();
        propertyKeys.add("kaccount_email");
        propertyKeys.add("nickname");
        propertyKeys.add("profile_image");
        propertyKeys.add("thumbnail_image");
        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                String message = "failed to get user info. msg=" + errorResult;
                Logger.d(message);

                ErrorCode result = ErrorCode.valueOf(errorResult.getErrorCode());
                if (result == ErrorCode.CLIENT_ERROR_CODE) {
                    activity.finish();
                } else {
                    Logger.e(TAG, errorResult.getErrorMessage());
                }
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Logger.e(TAG, errorResult.getErrorMessage());
            }

            @Override
            public void onNotSignedUp() {
            }

            @Override
            public void onSuccess(UserProfile userProfile) {
                handleCallbackResult(userProfile);
            }
        }, propertyKeys, false);
    }

    private String getAccessToken() {
        return Session.getCurrentSession().isOpened() ? Session.getCurrentSession().getTokenInfo().getAccessToken() : null;
    }
}
