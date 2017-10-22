package node.com.enjoydanang.ui.fragment.detail;

import java.util.List;

import node.com.enjoydanang.iBaseView;
import node.com.enjoydanang.model.ItemReviewModel;
import node.com.enjoydanang.model.ReviewDetailModel;
import node.com.enjoydanang.constant.AppError;

/**
 * Author: Tavv
 * Created on 10/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public interface iReviewView extends iBaseView {
    void onFetchReviews(List<ItemReviewModel> models);

    void onFetchSuccess(AppError error);

    void onFetchLastReview(ReviewDetailModel reviewDetail);
}
