package node.com.enjoydanang.ui.activity.splash;

import org.json.JSONObject;

import node.com.enjoydanang.constant.AppError;
import node.com.enjoydanang.iBaseView;
import node.com.enjoydanang.model.UserInfo;

/**
 * Author: Tavv
 * Created on 21/10/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public interface SplashScreenView extends iBaseView {
    void onLoadLanguageSuccess(JSONObject json);

    void onLoadFailure(AppError appError);

    void onGetUserInfoSuccess(UserInfo userInfo);

    void onFailure(AppError appError);

    void onCombined(JSONObject jsLanguage, UserInfo userInfo);
}
