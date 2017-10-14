package node.com.enjoydanang.ui.fragment.home;

import java.util.List;

import node.com.enjoydanang.BasePresenter;
import node.com.enjoydanang.api.ApiCallback;
import node.com.enjoydanang.model.Category;
import node.com.enjoydanang.model.MenuItem;

/**
 * Created by chien on 10/8/17.
 */

public class HomePresenter extends BasePresenter<iHomeView> {

    public HomePresenter(iHomeView view) {
        super(view);
    }

    public void getMenuItem(List<MenuItem> menuItems){

        mvpView.getMenuFinish(menuItems);
    }
    public void getCategorys(int page) {
//        mvpView.showLoading();
        addSubscription(apiStores.getCategorys(page), new ApiCallback<Category>() {

            @Override
            public void onSuccess(Category model) {
                mvpView.getCategorysFinish(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.hideLoading();
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }
}
