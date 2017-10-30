package node.com.enjoydanang.ui.fragment.logcheckin;

import java.util.List;

import node.com.enjoydanang.constant.AppError;
import node.com.enjoydanang.iBaseView;
import node.com.enjoydanang.model.HistoryCheckin;

/**
 * Author: Tavv
 * Created on 30/10/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public interface CheckinHistoryView extends iBaseView {

    void onFetchHistorySuccess(List<HistoryCheckin> lstHistoryCheckins);

    void onFetchFailure(AppError error);
}
