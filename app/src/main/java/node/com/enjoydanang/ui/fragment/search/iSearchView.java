package node.com.enjoydanang.ui.fragment.search;

import java.util.List;

import node.com.enjoydanang.constant.AppError;
import node.com.enjoydanang.iBaseView;
import node.com.enjoydanang.model.Partner;

/**
 * Author: Tavv
 * Created on 28/10/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public interface iSearchView extends iBaseView {

    void OnQuerySearchResult(List<Partner> lstPartner);

    void onResultPlaceByRange(List<Partner> lstPartner);

    void onGetLocationAddress(List<String> lstAddress);

    void onGetAddress(String strAddress);

    void onError(AppError error);
}
