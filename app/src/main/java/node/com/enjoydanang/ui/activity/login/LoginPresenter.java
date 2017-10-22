package node.com.enjoydanang.ui.activity.login;

import node.com.enjoydanang.BasePresenter;
import node.com.enjoydanang.api.ApiCallback;
import node.com.enjoydanang.api.model.BaseRepository;
import node.com.enjoydanang.model.User;
import node.com.enjoydanang.model.UserInfo;
import node.com.enjoydanang.constant.AppError;

/**
 * Author: Tavv
 * Created on 08/10/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public class LoginPresenter extends BasePresenter<LoginView>{
    public LoginPresenter(LoginView view) {
        super(view);
    }

    void loginViaSocial(User user){
        addSubscription(apiStores.doSignOrRegisterViaSocial(user.getId(), user.getAccessToken(),
                user.getType().toString(), user.getPicture().getData().getImage()), new ApiCallback<BaseRepository<UserInfo>>(){

            @Override
            public void onSuccess(BaseRepository<UserInfo> userInfo) {
                mvpView.onLoginSuccess(userInfo);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.hideLoading();
                mvpView.onLoginFailure(new AppError(new Throwable(msg)));
            }

            @Override
            public void onFinish() {

            }
        });
    }

    void normalLogin(UserInfo userInfo){
        addSubscription(apiStores.normalLogin(userInfo.getUserName(), userInfo.getPassword()), new ApiCallback<BaseRepository<UserInfo>>(){

            @Override
            public void onSuccess(BaseRepository<UserInfo> userInfo) {
                mvpView.onLoginSuccess(userInfo);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.hideLoading();
                mvpView.onLoginFailure(new AppError(new Throwable(msg)));
            }

            @Override
            public void onFinish() {

            }
        });
    }


}
