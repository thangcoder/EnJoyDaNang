package node.com.enjoydanang.ui.fragment.change_password;

import node.com.enjoydanang.constant.AppError;
import node.com.enjoydanang.iBaseView;
import node.com.enjoydanang.model.UserInfo;

/**
 * Author: Tavv
 * Created on 26/10/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public interface ChangePwdView extends iBaseView {

    void onChangeSuccess(UserInfo userInfo);

    void onChangeFailure(AppError error);
}
