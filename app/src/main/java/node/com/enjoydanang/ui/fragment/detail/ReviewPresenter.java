package node.com.enjoydanang.ui.fragment.detail;

import java.util.ArrayList;
import java.util.List;

import node.com.enjoydanang.BasePresenter;
import node.com.enjoydanang.model.ReviewDetailModel;
import node.com.enjoydanang.model.ItemReviewModel;

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
        List<ItemReviewModel> models = new ArrayList<>();
        String reviewContent = "Looking for a quick and easy way to tackle the breakfast rush? They";
        ItemReviewModel model_1 = new ItemReviewModel("Iris Louis", 3.5f, "https://www.famousbirthdays.com/headshots/justin-bieber-2.jpg", reviewContent);
        models.add(model_1);

        ItemReviewModel model_2 = new ItemReviewModel("Iris Louis",4.5f, "https://www.famousbirthdays.com/headshots/justin-bieber-2.jpg", reviewContent);
        models.add(model_2);


        ItemReviewModel model_3 = new ItemReviewModel("Iris Louis",3f, "https://www.famousbirthdays.com/headshots/justin-bieber-2.jpg", reviewContent);
        models.add(model_3);


        ItemReviewModel model_4 = new ItemReviewModel("Iris Louis",2.5f, "https://www.famousbirthdays.com/headshots/justin-bieber-2.jpg", reviewContent);
        models.add(model_4);


        ItemReviewModel model_5 = new ItemReviewModel("Iris Louis",5f, "https://www.famousbirthdays.com/headshots/justin-bieber-2.jpg", reviewContent);
        models.add(model_5);

        ItemReviewModel model_6 = new ItemReviewModel("Iris Louis",4f, "https://www.famousbirthdays.com/headshots/justin-bieber-2.jpg", reviewContent);
        models.add(model_6);


        ItemReviewModel model_7 = new ItemReviewModel("Iris Louis",1f, "https://www.famousbirthdays.com/headshots/justin-bieber-2.jpg", reviewContent);
        models.add(model_7);


        ReviewDetailModel reviewDetailModel = new ReviewDetailModel(3.5f, "http://images6.fanpop.com/image/photos/38400000/-Taylor-Swift-taylor-swift-38425207-500-500.jpg", 69);

        mvpView.onFetchLastReview(reviewDetailModel);
        mvpView.onFetchReviews(models);
    }
}
