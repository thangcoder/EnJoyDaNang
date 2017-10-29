package node.com.enjoydanang.ui.fragment.review;

import java.util.List;

import node.com.enjoydanang.api.model.Repository;
import node.com.enjoydanang.iBaseView;
import node.com.enjoydanang.model.Review;
import node.com.enjoydanang.constant.AppError;

/**
 * Author: Tavv
 * Created on 10/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public interface iReviewView extends iBaseView {


    void onFetchReviews(List<Review> models);

    void onFetchFailure(AppError error);

    void onFetchReplyByReview(Repository data);



}
