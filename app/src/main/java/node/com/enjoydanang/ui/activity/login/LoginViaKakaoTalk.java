package node.com.enjoydanang.ui.activity.login;

import android.content.Intent;

import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.kakaotalk.response.KakaoTalkProfile;
import com.kakao.usermgmt.response.model.User;
import com.kakao.util.exception.KakaoException;

import node.com.enjoydanang.ui.activity.BaseActivity;

/**
 * Author: Tavv
 * Created on 13/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class LoginViaKakaoTalk implements ILogin<KakaoTalkProfile, User> {

    private BaseActivity activity;

    private SessionCallback sessionCallback;

    public LoginViaKakaoTalk(BaseActivity activity) {
        this.activity = activity;
    }

    @Override
    public void init() {
    }

    @Override
    public void login() {
        if(sessionCallback == null){
            sessionCallback = new SessionCallback();
            Session.getCurrentSession().addCallback(sessionCallback);
        }
        Session.getCurrentSession().open(AuthType.KAKAO_TALK, activity);
    }

    @Override
    public void handleCallbackResult(KakaoTalkProfile callback) {

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

    private class SessionCallback implements ISessionCallback{

        @Override
        public void onSessionOpened() {
            redirectSignupActivity();
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {

        }

    }

    protected void redirectSignupActivity() {
        final Intent intent = new Intent(activity, KakaoSignupActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    public SessionCallback getSessionCallback() {
        return sessionCallback;
    }

    public void setSessionCallback(SessionCallback sessionCallback) {
        this.sessionCallback = sessionCallback;
    }
}
