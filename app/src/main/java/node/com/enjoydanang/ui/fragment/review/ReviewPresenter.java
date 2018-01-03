package node.com.enjoydanang.ui.fragment.review;

import node.com.enjoydanang.BasePresenter;
import node.com.enjoydanang.api.ApiCallback;
import node.com.enjoydanang.api.model.Repository;
import node.com.enjoydanang.constant.AppError;
import node.com.enjoydanang.model.Reply;
import node.com.enjoydanang.model.Review;
import node.com.enjoydanang.utils.Utils;

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

    void fetchReviewByPartner(int partnerId, int page, String code) {
        addSubscription(apiStores.getListReviewByPartnerId("LISTREVIEW", partnerId, page, code), new ApiCallback<Repository<Review>>() {

            @Override
            public void onSuccess(Repository<Review> model) {
                if (Utils.isResponseError(model)) {
                    mvpView.onFetchFailure(new AppError(new Throwable(model.getMessage())));
                    return;
                }
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

    void fetchReplyByReviewId(int reviewId, int page) {
        addSubscription(apiStores.getReplyByReviewId(page, reviewId), new ApiCallback<Repository<Reply>>() {

            @Override
            public void onSuccess(Repository<Reply> model) {
                if (Utils.isResponseError(model)) {
                    mvpView.onFetchFailure(new AppError(new Throwable(model.getMessage())));
                    return;
                }
                mvpView.onFetchReplyByReview(model);
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

    void fetchReplyByReviewId(String code, int reviewId, int page) {
        addSubscription(apiStores.getReplyByReviewId("LISTREPLYREVIEW", code, page, reviewId), new ApiCallback<Repository<Reply>>() {

            @Override
            public void onSuccess(Repository<Reply> model) {
                if (Utils.isResponseError(model)) {
                    mvpView.onFetchFailure(new AppError(new Throwable(model.getMessage())));
                    return;
                }
                mvpView.onFetchReplyByReview(model);
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

    void refreshReviewByPartner(String code, int partnerId, int page) {
        addSubscription(apiStores.getListReviewByPartnerId("LISTREVIEW", partnerId, page, code), new ApiCallback<Repository<Review>>() {

            @Override
            public void onSuccess(Repository<Review> model) {

                mvpView.onRefreshReviews(model.getData());
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

    void removeReview(String code, int reviewId) {
        addSubscription(apiStores.removeReview("REMOVEREVIEW", code, reviewId), new ApiCallback<Repository>() {

            @Override
            public void onSuccess(Repository response) {
                if (Utils.isResponseError(response)) {
                    mvpView.onFetchFailure(new AppError(new Throwable(response.getMessage())));
                    return;
                }
                mvpView.onRemoveSuccess();
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
