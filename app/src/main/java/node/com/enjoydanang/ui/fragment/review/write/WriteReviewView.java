package node.com.enjoydanang.ui.fragment.review.write;

import node.com.enjoydanang.api.model.Repository;
import node.com.enjoydanang.constant.AppError;
import node.com.enjoydanang.iBaseView;

/**
 * Author: Tavv
 * Created on 29/10/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public interface WriteReviewView extends iBaseView {

    void onSubmitSuccess(Repository data);

    void onSubmitFailure(AppError error);
}
