package node.com.enjoydanang.ui.fragment.detail;

import node.com.enjoydanang.BasePresenter;
import node.com.enjoydanang.model.DetailModel;

/**
 * Author: Tavv
 * Created on 10/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class DetailPresenter extends BasePresenter<iDetailView> {

    public DetailPresenter(iDetailView view) {
        super(view);
    }

    public void doDummyData() {
        DetailModel detailModel = new DetailModel();
        detailModel.setContent("A glass of lemon juice contains less than 25 calories. It is a rich source of nutrients like calcium, potassium, vitamin C and pectin fibre. It also has medicinal values and antibacterial properties. It also contains traces of iron and vitamin A. \n" +
                "\n" +
                "Lemon, a fruit popular for its therapeutic properties, helps maintain your immune system and thus, protects you from the clutches of most types of infections. It also plays the role of a blood purifier. Lemon is a fabulous antiseptic and lime-water juice also works wonders for people having heart problems, owing to its high potassium content. So, make it a part of your daily routine to drink a glass of warm lemon water in the morning and enjoy its health benefits. Read on for more interesting information on the benefits of warm lemon water.");
        detailModel.setName("Cocktail");
        detailModel.setRate(3.5f);
        mvpView.showData(detailModel);
    }
}
