package node.com.enjoydanang.ui.fragment.album;

import node.com.enjoydanang.BasePresenter;
import node.com.enjoydanang.api.ApiCallback;
import node.com.enjoydanang.api.model.Repository;
import node.com.enjoydanang.constant.AppError;
import node.com.enjoydanang.model.PartnerAlbum;
import node.com.enjoydanang.utils.Utils;

/**
 * Author: Tavv
 * Created on 10/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class AlbumDetailPresenter extends BasePresenter<iAlbumView>{

    public AlbumDetailPresenter(iAlbumView view) {
        super(view);
    }

    void getAlbum(int partnerId){
        addSubscription(apiStores.getAlbumPartnerById(partnerId), new ApiCallback<Repository<PartnerAlbum>>(){

            @Override
            public void onSuccess(Repository<PartnerAlbum> model) {
                mvpView.hideLoading();
                if(Utils.isNotEmptyContent(model)){
                    mvpView.onFetchAlbumSuccess(model.getData());
                }else{
                    mvpView.onFetchFail(new AppError(new Throwable(model.getMessage())));
                }
            }

            @Override
            public void onFailure(String msg) {
                mvpView.hideLoading();
                mvpView.onFetchFail(new AppError(new Throwable(msg)));
            }

            @Override
            public void onFinish() {

            }
        });
    }

    void onFetchFail(AppError error){
        mvpView.onFetchFail(error);
    }
}

