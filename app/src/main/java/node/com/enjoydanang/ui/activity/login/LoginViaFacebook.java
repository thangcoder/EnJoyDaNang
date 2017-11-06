package node.com.enjoydanang.ui.activity.login;

import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.gson.Gson;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import node.com.enjoydanang.constant.LoginType;
import node.com.enjoydanang.model.User;
import node.com.enjoydanang.ui.activity.BaseActivity;

/**
 * Author: Tavv
 * Created on 13/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class LoginViaFacebook implements ILogin<AccessToken, User> {
    private static final String TAG = LoginViaFacebook.class.getSimpleName();

    private CallbackManager callbackManager;
    private AccessToken accessToken;
    private BaseActivity activity;
    private LoginPresenter mLoginPresenter;
    private LoginCallBack loginCallBack;

    public LoginViaFacebook(BaseActivity activity, LoginCallBack loginCallBack) {
        this.activity = activity;
        this.loginCallBack = loginCallBack;
    }

    @Override
    public void init() {
        callbackManager = CallbackManager.Factory.create();
    }

    @Override
    public void login() {
        loginCallBack.hideWaiting();
        accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null) {
            handleCallbackResult(accessToken);
        } else {
            LoginManager loginManager = LoginManager.getInstance();
            List<String> permission = new ArrayList<String>();
            permission.add("public_profile");
            loginManager.logInWithReadPermissions(activity, permission);

            loginManager.registerCallback(callbackManager,
                    new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {
                            accessToken = loginResult.getAccessToken();
                            handleCallbackResult(accessToken);
                        }

                        @Override
                        public void onCancel() {

                        }

                        @Override
                        public void onError(FacebookException exception) {
                            Log.i(TAG, "onError " + exception);
                        }
                    });
        }
    }

    @Override
    public void handleCallbackResult(final AccessToken accessToken) {
        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {

            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                User user = toUserObject(response.getRawResponse());
                user.setAccessToken(accessToken.getToken());
                user.setType(LoginType.FACEBOOK);
                if(StringUtils.isBlank(user.getEmail())){
                    user.setEmail(StringUtils.EMPTY);
                }
                pushToServer(user);
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, name, first_name,last_name, email, birthday, gender, picture.type(large)");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    public BaseActivity getActivity() {
        return activity;
    }


    @Override
    public void pushToServer(User user) {
        if (user != null) {

            mLoginPresenter.loginViaSocial(user);
        }
    }

    @Override
    public void removeAccessToken() {
        // TODO: removeAccessToken Facebook

    }

    @Override
    public void setLoginPresenter(LoginPresenter loginPresenter) {
        if (loginPresenter == null) {
            throw new NullPointerException("LoginPresenter not be null !");
        }
        this.mLoginPresenter = loginPresenter;
    }

    private User toUserObject(String jsonResponse) {
        return new Gson().fromJson(jsonResponse, User.class);
    }


    public CallbackManager getCallbackManager() {
        return callbackManager;
    }

    public AccessToken getAccessToken() {
        return accessToken;
    }
}
