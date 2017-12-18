package node.com.enjoydanang.ui.fragment.term;

import node.com.enjoydanang.constant.AppError;
import node.com.enjoydanang.iBaseView;
import node.com.enjoydanang.model.Content;

/**
 * Author: Tavv
 * Created on 18/12/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public interface TermView extends iBaseView {

    void onLoadTermSuccess(Content content);

    void onFailure(AppError error);
}
