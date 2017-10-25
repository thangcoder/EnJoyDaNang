package node.com.enjoydanang.ui.fragment.favorite;

import node.com.enjoydanang.BasePresenter;
import node.com.enjoydanang.api.ApiCallback;
import node.com.enjoydanang.api.model.Repository;
import node.com.enjoydanang.constant.AppError;
import node.com.enjoydanang.model.Favorite;
import node.com.enjoydanang.utils.Utils;

/**
 * Author: Tavv
 * Created on 25/10/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public class FavoritePresenter extends BasePresenter<FavoriteView>{

    public FavoritePresenter(FavoriteView view) {
        super(view);
    }

    void getFavorite(long userId){
        addSubscription(apiStores.getFavoriteByUserId(userId), new ApiCallback<Repository<Favorite>>(){

            @Override
            public void onSuccess(Repository<Favorite> model) {
                if(Utils.isNotEmptyContent(model)){
                    mvpView.onFetchFavorite(model.getData());
                }else{
                    mvpView.onFetchFailure(new AppError(new Throwable(model.getMessage())));
                }
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
