package node.com.enjoydanang.ui.fragment.review.write;

import java.util.List;

import node.com.enjoydanang.BasePresenter;
import node.com.enjoydanang.api.ApiCallback;
import node.com.enjoydanang.api.model.Repository;
import node.com.enjoydanang.constant.AppError;
import node.com.enjoydanang.utils.Utils;

/**
 * Author: Tavv
 * Created on 29/10/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public class WriteReviewPresenter extends BasePresenter<WriteReviewView> {

    public WriteReviewPresenter(WriteReviewView view) {
        super(view);
    }

    void writeReview(long customerId, int partnerId, int star, String title, String name, String content, List<String> lstImages) {
        addSubscription(apiStores.writeReview(customerId, partnerId, star, title, name, content, lstImages), new ApiCallback<Repository>() {

            @Override
            public void onSuccess(Repository model) {
                if(Utils.isResponseError(model)){
                    mvpView.onSubmitFailure(new AppError(new Throwable(model.getMessage())));
                    return;
                }
                mvpView.onSubmitSuccess(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.onSubmitFailure(new AppError(new Throwable(msg)));
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }
}
