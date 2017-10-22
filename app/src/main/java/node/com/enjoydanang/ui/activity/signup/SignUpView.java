package node.com.enjoydanang.ui.activity.signup;

import node.com.enjoydanang.api.model.BaseRepository;
import node.com.enjoydanang.iBaseView;
import node.com.enjoydanang.model.UserInfo;
import node.com.enjoydanang.constant.AppError;

/**
 * Author: Tavv
 * Created on 20/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public interface SignUpView extends iBaseView {
    void onRegisterSuccess(BaseRepository<UserInfo> resultCallBack);

    void onRegisterFailure(AppError error);
}
