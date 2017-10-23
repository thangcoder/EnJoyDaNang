package node.com.enjoydanang.ui.fragment.detail;

import node.com.enjoydanang.BasePresenter;
import node.com.enjoydanang.api.ApiCallback;
import node.com.enjoydanang.api.model.Repository;
import node.com.enjoydanang.constant.AppError;
import node.com.enjoydanang.model.DetailPartner;

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
                mvpView.hideLoading();
                mvpView.onFetchDetailPartnerSuccess(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.hideLoading();
                mvpView.onFetchFailure(new AppError(new Throwable(msg)));
            }

            @Override
            public void onFinish() {

            }
        });
    }

}
