package node.com.enjoydanang.ui.fragment.profile;

import node.com.enjoydanang.constant.AppError;
import node.com.enjoydanang.iBaseView;
import node.com.enjoydanang.model.UserInfo;

/**
 * Author: Tavv
 * Created on 27/10/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public interface ProfileView extends iBaseView {

    void onUpdateSuccess(UserInfo userInfo);

    void onUpdateFailure(AppError error);
}
