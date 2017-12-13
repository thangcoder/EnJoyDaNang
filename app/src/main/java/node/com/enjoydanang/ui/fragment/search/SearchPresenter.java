package node.com.enjoydanang.ui.fragment.search;

import node.com.enjoydanang.BasePresenter;
import node.com.enjoydanang.api.ApiCallback;
import node.com.enjoydanang.api.model.Repository;
import node.com.enjoydanang.constant.AppError;
import node.com.enjoydanang.model.Partner;
import node.com.enjoydanang.utils.Utils;

/**
 * Author: Tavv
 * Created on 28/10/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public class SearchPresenter extends BasePresenter<iSearchView> {
    public SearchPresenter(iSearchView view) {
        super(view);
    }

    void searchWithTitle(String query) {
        addSubscription(apiStores.searchWithQuery(query), new ApiCallback<Repository<Partner>>() {

            @Override
            public void onSuccess(Repository<Partner> model) {
                mvpView.OnQuerySearchResult(model.getData());
            }

            @Override
            public void onFailure(String msg) {

            }

            @Override
            public void onFinish() {

            }
        });
    }

    void searchPlaceByCurrentLocation(long customerId, int distance, String geoLat, String geoLng){
        addSubscription(apiStores.listSearchByLocation(customerId, distance, geoLat, geoLng), new ApiCallback<Repository<Partner>>(){

            @Override
            public void onSuccess(Repository<Partner> model) {
                if(Utils.isResponseError(model)){
                    mvpView.onError(new AppError(new Throwable(model.getMessage())));
                    return;
                }
                mvpView.onResultPlaceByRange(model.getData());
            }

            @Override
            public void onFailure(String msg) {
                mvpView.onError(new AppError(new Throwable(msg)));
            }

            @Override
            public void onFinish() {

            }
        });

    }
    void showDialog(){
        mvpView.showLoading();
    }
    void hideDialog(){
        mvpView.hideLoading();
    }
}
