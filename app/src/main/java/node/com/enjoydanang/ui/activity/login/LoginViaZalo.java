package node.com.enjoydanang.ui.activity.login;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zing.zalo.zalosdk.oauth.LoginVia;
import com.zing.zalo.zalosdk.oauth.OAuthCompleteListener;
import com.zing.zalo.zalosdk.oauth.OauthResponse;
import com.zing.zalo.zalosdk.oauth.ZaloOpenAPICallback;
import com.zing.zalo.zalosdk.oauth.ZaloSDK;

import org.json.JSONException;
import org.json.JSONObject;

import node.com.enjoydanang.constant.LoginType;
import node.com.enjoydanang.model.User;
import node.com.enjoydanang.model.Zalo;
import node.com.enjoydanang.ui.activity.BaseActivity;
import node.com.enjoydanang.utils.ZaloUtils;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Author: Tavv
 * Created on 27/08/2018
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public class LoginViaZalo extends OAuthCompleteListener implements ILogin<OauthResponse, User> {
    private static final String TAG = LoginViaZalo.class.getSimpleName();

    private final int authenRequestCode = 1;
    private final int exitRequestCode = 2;

    private BaseActivity activity;

    private LoginPresenter mLoginPresenter;

    private LoginCallBack loginCallBack;

    private LoginVia loginVia = LoginVia.APP_OR_WEB;

    private User user;

    public LoginViaZalo(BaseActivity activity, LoginCallBack loginCallBack) {
        this.activity = activity;
        this.loginCallBack = loginCallBack;
    }


    @Override
    public void init() {
    }

    @Override
    public void login() {
        ZaloSDK.Instance.authenticate(activity, loginVia, this);
    }

    @Override
    public void handleCallbackResult(OauthResponse callback) {
        ZaloSDK.Instance.getProfile(activity, new ZaloOpenAPICallback() {
            @Override
            public void onResult(JSONObject response) {
                try {
                    GsonBuilder gsonb = new GsonBuilder();
                    Gson gson = gsonb.create();
                    Zalo zalo = gson.fromJson(response.toString(), Zalo.class);
                    user = new User();
                    user.setId(zalo.getId());
                    user.setPicture(zalo.getPicture());
                    user.setBirthday(zalo.getBirthday());
                    user.setGender(zalo.getGender());
                    user.setType(LoginType.ZALO);
                    user.setFullName(zalo.getName());
                    user.setFirstName(zalo.getName());
                    getAccessToken();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new String[]{"id", "birthday", "gender", "picture", "name"});
    }

    @Override
    public BaseActivity getActivity() {
        return activity;
    }

    @Override
    public void pushToServer(User model) {
        if (user != null) {
            mLoginPresenter.showLoading();
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

    @Override
    public void onGetOAuthComplete(OauthResponse response) {
        handleCallbackResult(response);
    }

    @Override
    public void onZaloNotInstalled(Context context) {
        super.onZaloNotInstalled(context);
    }

    @Override
    public void onAuthenError(int errorCode, String message) {
        switch (errorCode) {
            case 2:
                Log.e(TAG, "onAuthenError: Code 2, Message: "+ message);
                break;
            case -1004:
                Log.e(TAG, "onAuthenError: Code -1004, Message: "+ message);
                break;
            default:
                break;
        }
        super.onAuthenError(errorCode, message);
    }

    public void setZaloLoginVia(LoginVia loginVia) {
        this.loginVia = loginVia;
    }

    public void getAccessToken(){
        Observable.create(new Observable.OnSubscribe<JSONObject>() {
            @Override
            public void call(Subscriber<? super JSONObject> subscriber) {
                try {
                    if(ZaloSDK.Instance.getOAuthCode() != null && !ZaloSDK.Instance.getOAuthCode().equals("")) {
                        JSONObject accessToken = ZaloUtils.getAccessToken(activity);
                        if(accessToken == null) {
                            try {
                                subscriber.onNext(new JSONObject("{\"error\":-1008}"));
                            } catch (JSONException var5) {
                                var5.printStackTrace();
                            }
                        } else {
                            subscriber.onNext(accessToken);
                        }
                    } else {
                        try {
                            subscriber.onNext(new JSONObject("{\"error\":-1004}"));
                        } catch (JSONException var6) {
                            var6.printStackTrace();
                        }
                    }
                    subscriber.onCompleted();
                }catch (Exception e){
                    subscriber.onError(e.getCause());
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JSONObject>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(JSONObject token) {
                        if (user != null) {
                            String accessToken = null;
                            try {
                                accessToken = token.getJSONObject("data").getString("access_token");
                                user.setAccessToken(accessToken);
                                pushToServer(user);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        //removeAccessToken();
                    }
                });
    }
}
