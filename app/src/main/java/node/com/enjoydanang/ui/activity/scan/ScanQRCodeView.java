package node.com.enjoydanang.ui.activity.scan;

import node.com.enjoydanang.api.model.Repository;
import node.com.enjoydanang.constant.AppError;
import node.com.enjoydanang.iBaseView;
import node.com.enjoydanang.model.HistoryCheckin;
import node.com.enjoydanang.model.Partner;

/**
 * Author: Tavv
 * Created on 18/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public interface ScanQRCodeView extends iBaseView {

    void onFetchInfoSuccess(Partner partner);

    void onRequestOrderSuccess(HistoryCheckin response);

    void onFetchError(AppError appError);

    void onRequestOrderSuccessError(AppError appError);

}
