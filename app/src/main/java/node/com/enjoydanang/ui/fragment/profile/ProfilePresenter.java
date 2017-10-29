package node.com.enjoydanang.ui.fragment.profile;

import node.com.enjoydanang.BasePresenter;
import node.com.enjoydanang.api.ApiCallback;
import node.com.enjoydanang.api.model.Repository;
import node.com.enjoydanang.constant.AppError;
import node.com.enjoydanang.model.UserInfo;
import node.com.enjoydanang.utils.Utils;

import static com.kakao.auth.StringSet.msg;

/**
 * Author: Tavv
 * Created on 27/10/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public class ProfilePresenter extends BasePresenter<ProfileView>{
    public ProfilePresenter(ProfileView view) {
        super(view);
    }

    void updateProfile(long userId, String fullName, String phone, String email, String picBase64){
        mvpView.showLoading();
        addSubscription(apiStores.updateProfile(userId, fullName, phone, email, picBase64), new ApiCallback<Repository<UserInfo>>(){
            @Override
            public void onSuccess(Repository<UserInfo> model) {
                if(Utils.isResponseError(model)){
                    mvpView.onUpdateFailure(new AppError(new Throwable(model.getMessage())));
                    return;
                }
                mvpView.onUpdateSuccess(model.getData().get(0));
            }

            @Override
            public void onFailure(String msg) {
                mvpView.hideLoading();
                mvpView.onUpdateFailure(new AppError(new Throwable(msg)));
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }
}
