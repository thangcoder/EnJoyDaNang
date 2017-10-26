package node.com.enjoydanang.ui.fragment.change_password;

import node.com.enjoydanang.BasePresenter;
import node.com.enjoydanang.api.ApiCallback;
import node.com.enjoydanang.api.model.Repository;
import node.com.enjoydanang.constant.AppError;
import node.com.enjoydanang.model.UserInfo;
import node.com.enjoydanang.utils.Utils;

/**
 * Author: Tavv
 * Created on 26/10/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public class ChangePwdPresenter extends BasePresenter<ChangePwdView>{
    public ChangePwdPresenter(ChangePwdView view) {
        super(view);
    }

    void changePwd(long userId, String oldPwd, String newPwd){
        addSubscription(apiStores.changePwd(userId, oldPwd, newPwd), new ApiCallback<Repository<UserInfo>>(){

            @Override
            public void onSuccess(Repository<UserInfo> model) {
                if(Utils.isResponseError(model)){
                    mvpView.onChangeFailure(new AppError(new Throwable(model.getMessage())));
                    return;
                }
                mvpView.onChangeSuccess(model.getData().get(0));
            }

            @Override
            public void onFailure(String msg) {
                mvpView.onChangeFailure(new AppError(new Throwable(msg)));
            }

            @Override
            public void onFinish() {

            }
        });
    }
}
