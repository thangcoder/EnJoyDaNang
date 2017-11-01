package node.com.enjoydanang.ui.activity.scan;

import node.com.enjoydanang.BasePresenter;
import node.com.enjoydanang.api.ApiCallback;
import node.com.enjoydanang.api.model.Repository;
import node.com.enjoydanang.constant.AppError;
import node.com.enjoydanang.model.HistoryCheckin;
import node.com.enjoydanang.model.Partner;
import node.com.enjoydanang.utils.Utils;

/**
 * Author: Tavv
 * Created on 18/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class ScanQRCodePresenter extends BasePresenter<ScanQRCodeView> {
    public ScanQRCodePresenter(ScanQRCodeView view) {
        super(view);
    }

    void requestOrder(int partnerId, long customerId, int amount) {
        addSubscription(apiStores.checkIn(partnerId, customerId, amount), new ApiCallback<Repository<HistoryCheckin>>() {

            @Override
            public void onSuccess(Repository<HistoryCheckin> response) {
                if (Utils.isResponseError(response)) {
                    mvpView.onRequestOrderSuccessError(new AppError(new Throwable(response.getMessage())));
                    return;
                }
                mvpView.onRequestOrderSuccess(response.getData().get(0));
            }

            @Override
            public void onFailure(String msg) {
                mvpView.onRequestOrderSuccessError(new AppError(new Throwable(msg)));
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }

    void getInfoScanQr(String qrCode) {
        addSubscription(apiStores.getInforByQRCode(qrCode), new ApiCallback<Repository<Partner>>() {

            @Override
            public void onSuccess(Repository<Partner> data) {
                if (Utils.isResponseError(data)) {
                    mvpView.onFetchError(new AppError(new Throwable(data.getMessage())));
                    return;
                }
                mvpView.onFetchInfoSuccess(data.getData().get(0));
            }

            @Override
            public void onFailure(String msg) {
                mvpView.onFetchError(new AppError(new Throwable(msg)));
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }
}
