package node.com.enjoydanang.ui.activity.login;

import node.com.enjoydanang.api.model.Repository;
import node.com.enjoydanang.iBaseView;
import node.com.enjoydanang.model.UserInfo;
import node.com.enjoydanang.constant.AppError;

/**
 * Author: Tavv
 * Created on 09/10/2017.
 * Project Name: EnJoyDanang
 * Version : 1.0
 */

public interface LoginView extends iBaseView{

    void onLoginSuccess(Repository<UserInfo> resultCallBack);

    void onLoginFailure(AppError error);

}
