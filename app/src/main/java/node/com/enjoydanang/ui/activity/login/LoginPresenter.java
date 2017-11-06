package node.com.enjoydanang.ui.activity.login;

import node.com.enjoydanang.BasePresenter;
import node.com.enjoydanang.api.ApiCallback;
import node.com.enjoydanang.api.model.Repository;
import node.com.enjoydanang.constant.AppError;
import node.com.enjoydanang.model.User;
import node.com.enjoydanang.model.UserInfo;
import node.com.enjoydanang.utils.Utils;

/**
 * Author: Tavv
 * Created on 08/10/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public class LoginPresenter extends BasePresenter<LoginView> {
    public LoginPresenter(LoginView view) {
        super(view);
    }

    void loginViaSocial(User user) {
        mvpView.showLoading();
        addSubscription(apiStores.doSignOrRegisterViaSocial(user.getId(), user.getAccessToken(),
                user.getType(), user.getPicture().getData().getImage(), user.getFullName(), user.getEmail()), new ApiCallback<Repository<UserInfo>>() {

            @Override
            public void onSuccess(Repository<UserInfo> userInfo) {
                if(Utils.isResponseError(userInfo)){
                    mvpView.onLoginFailure(new AppError(new Throwable(userInfo.getMessage())));
                    return;
                }
                mvpView.onLoginSuccess(userInfo);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.onLoginFailure(new AppError(new Throwable(msg)));
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }

    void normalLogin(String userName, String password) {
        mvpView.showLoading();
        addSubscription(apiStores.normalLogin(userName, password), new ApiCallback<Repository<UserInfo>>() {

            @Override
            public void onSuccess(Repository<UserInfo> userInfo) {
                if (Utils.isResponseError(userInfo)) {
                    mvpView.onLoginFailure(new AppError(new Throwable(userInfo.getMessage())));
                    return;
                }
                mvpView.onLoginSuccess(userInfo);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.onLoginFailure(new AppError(new Throwable(msg)));
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }


}
