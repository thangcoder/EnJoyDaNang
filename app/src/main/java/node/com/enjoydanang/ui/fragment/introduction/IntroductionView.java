package node.com.enjoydanang.ui.fragment.introduction;

import node.com.enjoydanang.constant.AppError;
import node.com.enjoydanang.iBaseView;
import node.com.enjoydanang.model.Introduction;

/**
 * Author: Tavv
 * Created on 20/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public interface IntroductionView extends iBaseView {

    void onGetIntroductionSuccess(Introduction introduction);

    void onLoadFailure(AppError error);
}
