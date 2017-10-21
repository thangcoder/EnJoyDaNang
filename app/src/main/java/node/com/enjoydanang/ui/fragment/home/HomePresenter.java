package node.com.enjoydanang.ui.fragment.home;

import java.util.List;

import node.com.enjoydanang.BasePresenter;
import node.com.enjoydanang.api.ApiCallback;
import node.com.enjoydanang.api.model.BaseRepository;
import node.com.enjoydanang.model.Banner;
import node.com.enjoydanang.model.Category;
import node.com.enjoydanang.model.MenuItem;
import node.com.enjoydanang.model.Partner;
import node.com.enjoydanang.utils.network.NetworkError;

/**
 * Created by chien on 10/8/17.
 */

public class HomePresenter extends BasePresenter<HomeViewCallbackListener> {

    public HomePresenter(HomeViewCallbackListener view) {
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

    void getBanner(){
        addSubscription(apiStores.getBanner(), new ApiCallback<BaseRepository<Banner>>(){

            @Override
            public void onSuccess(BaseRepository<Banner> data) {
                mvpView.onGetBannerSuccess(data);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.onGetBannerFailure(new NetworkError(new Throwable(msg)));
            }

            @Override
            public void onFinish() {

            }
        });
    }

    void getPartner(int page){
        addSubscription(apiStores.getPartner(page), new ApiCallback<BaseRepository<Partner>>(){

            @Override
            public void onSuccess(BaseRepository<Partner> data) {
                mvpView.onGetPartnerSuccess(data);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.onGetPartnerFailure(new NetworkError(new Throwable(msg)));
            }

            @Override
            public void onFinish() {

            }
        });
    }

    void getAllCategories(){
        addSubscription(apiStores.getAllCategories(), new ApiCallback<BaseRepository<Category>>(){

            @Override
            public void onSuccess(BaseRepository<Category> data) {
                mvpView.onGetCategorySuccess(data);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.onGetCategoryFailure(new NetworkError(new Throwable(msg)));
            }

            @Override
            public void onFinish() {

            }
        });
    }
}
