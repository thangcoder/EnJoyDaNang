package node.com.enjoydanang.ui.fragment.review;

import node.com.enjoydanang.BasePresenter;
import node.com.enjoydanang.api.ApiCallback;
import node.com.enjoydanang.api.model.Repository;
import node.com.enjoydanang.constant.AppError;
import node.com.enjoydanang.model.Review;

/**
 * Author: Tavv
 * Created on 10/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class ReviewPresenter extends BasePresenter<iReviewView> {
    public ReviewPresenter(iReviewView view) {
        super(view);
    }


    void fetchReviewByPartner(int partnerId, int page) {
        addSubscription(apiStores.getListReviewByPartnerId(partnerId, page), new ApiCallback<Repository<Review>>() {

            @Override
            public void onSuccess(Repository<Review> model) {
                mvpView.onFetchReviews(model.getData());
            }

            @Override
            public void onFailure(String msg) {
                mvpView.onFetchFailure(new AppError(new Throwable(msg)));
            }

            @Override
            public void onFinish() {
            }
        });
    }

}
