package node.com.enjoydanang.ui.fragment.detail;

import java.util.List;

import node.com.enjoydanang.BasePresenter;
import node.com.enjoydanang.api.ApiCallback;
import node.com.enjoydanang.api.model.Repository;
import node.com.enjoydanang.constant.AppError;
import node.com.enjoydanang.model.DetailPartner;
import node.com.enjoydanang.model.PartnerAlbum;
import node.com.enjoydanang.utils.Utils;

/**
 * Author: Tavv
 * Created on 10/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class DetailPartnerPresenter extends BasePresenter<iDetailPartnerView> {

    public DetailPartnerPresenter(iDetailPartnerView view) {
        super(view);
    }


    void getDetailPartner(int partnerId){
        addSubscription(apiStores.getDetailPartnerById(partnerId), new ApiCallback<Repository<DetailPartner>>(){

            @Override
            public void onSuccess(Repository<DetailPartner> model) {
                mvpView.onFetchDetailPartnerSuccess(model);
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


    void getSlideByPartnerId(int partnerId){
        addSubscription(apiStores.getSlideByPartnerId(partnerId), new ApiCallback<Repository<PartnerAlbum>>(){

            @Override
            public void onSuccess(Repository<PartnerAlbum> model) {
                if(Utils.isNotEmptyContent(model)){
                    List<PartnerAlbum> images = model.getData();
                    mvpView.onFetchSlideSuccess(images);
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
