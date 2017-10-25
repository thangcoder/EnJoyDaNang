package node.com.enjoydanang.ui.activity.signup;

import node.com.enjoydanang.BasePresenter;
import node.com.enjoydanang.api.ApiCallback;
import node.com.enjoydanang.api.model.Repository;
import node.com.enjoydanang.constant.AppError;
import node.com.enjoydanang.model.UserInfo;
import node.com.enjoydanang.utils.Utils;

/**
 * Author: Tavv
 * Created on 20/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class SignUpPresenter extends BasePresenter<SignUpView>{
    public SignUpPresenter(SignUpView view) {
        super(view);
    }

    void normalRegister(UserInfo userInfo){
        addSubscription(apiStores.normalRegister(userInfo.getUserName(), userInfo.getPassword(),
                userInfo.getFullName(), userInfo.getEmail(), userInfo.getPhone()), new ApiCallback<Repository<UserInfo>>(){

            @Override
            public void onSuccess(Repository<UserInfo> userInfo) {
                mvpView.hideLoading();
                if(Utils.isResponseError(userInfo)){
                    mvpView.onRegisterFailure(new AppError(new Throwable(userInfo.getMessage())));
                    return;
                }
                mvpView.onRegisterSuccess(userInfo);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.hideLoading();
                mvpView.onRegisterFailure(new AppError(new Throwable(msg)));
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }
}
