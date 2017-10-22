package node.com.enjoydanang.ui.fragment.home;

import node.com.enjoydanang.BasePresenter;
import node.com.enjoydanang.api.ApiCallback;
import node.com.enjoydanang.api.model.Repository;
import node.com.enjoydanang.model.Banner;
import node.com.enjoydanang.model.Category;

/**
 * Created by chien on 10/8/17.
 */

public class HomePresenter extends BasePresenter<iHomeView> {

    public HomePresenter(iHomeView view) {
        super(view);
    }


    public void getCategorys(int page) {
        mvpView.showLoading();
        addSubscription(apiStores.getCategorys(page), new ApiCallback<Repository<Category>>() {
            @Override
            public void onSuccess(Repository<Category> model) {
                if(model.getData().size()>0){
                    mvpView.getCategorysFinish(model.getData());
                }
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
    public void getListByCategorys(int id,int page) {
        mvpView.showLoading();
        addSubscription(apiStores.listByCategory(id,page), new ApiCallback<Repository<Category>>() {
            @Override
            public void onSuccess(Repository<Category> model) {
                if(model.getData().size()>0){
                    mvpView.getListByCategoryFinish( model.getData());
                }
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
    public void getBanner() {
        mvpView.showLoading();
        addSubscription(apiStores.getBanner(), new ApiCallback<Repository<Banner>>() {
            @Override
            public void onSuccess(Repository<Banner> model) {
                if(model.getData().size()>0){
                    mvpView.getBannerFinish( model.getData());
                }
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
