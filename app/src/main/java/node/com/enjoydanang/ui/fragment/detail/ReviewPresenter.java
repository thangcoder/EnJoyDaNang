package node.com.enjoydanang.ui.fragment.detail;

import java.util.ArrayList;
import java.util.List;

import node.com.enjoydanang.BasePresenter;
import node.com.enjoydanang.model.ReviewDetailModel;

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

    public void doDummyData() {
        List<ReviewDetailModel> models = new ArrayList<>();
        String reviewContent = "Looking for a quick and easy way to tackle the breakfast rush? They";
        ReviewDetailModel model_1 = new ReviewDetailModel(3.5f, 99, "https://www.famousbirthdays.com/headshots/justin-bieber-2.jpg", reviewContent);
        models.add(model_1);

        ReviewDetailModel model_2 = new ReviewDetailModel(4.5f, 69, "https://www.famousbirthdays.com/headshots/justin-bieber-2.jpg", reviewContent);
        models.add(model_2);


        ReviewDetailModel model_3 = new ReviewDetailModel(3f, 96, "https://www.famousbirthdays.com/headshots/justin-bieber-2.jpg", reviewContent);
        models.add(model_3);


        ReviewDetailModel model_4 = new ReviewDetailModel(2.5f, 99, "https://www.famousbirthdays.com/headshots/justin-bieber-2.jpg", reviewContent);
        models.add(model_4);


        ReviewDetailModel model_5 = new ReviewDetailModel(5f, 29, "https://www.famousbirthdays.com/headshots/justin-bieber-2.jpg", reviewContent);
        models.add(model_5);

        ReviewDetailModel model_6 = new ReviewDetailModel(4f, 19, "https://www.famousbirthdays.com/headshots/justin-bieber-2.jpg", reviewContent);
        models.add(model_6);


        ReviewDetailModel model_7 = new ReviewDetailModel(1f, 99, "https://www.famousbirthdays.com/headshots/justin-bieber-2.jpg", reviewContent);
        models.add(model_7);
        mvpView.onFetchSuccess(models);
    }
}
