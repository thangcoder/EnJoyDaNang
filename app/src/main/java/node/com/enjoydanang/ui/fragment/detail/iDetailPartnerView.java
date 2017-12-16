package node.com.enjoydanang.ui.fragment.detail;

import java.util.List;

import node.com.enjoydanang.constant.AppError;
import node.com.enjoydanang.iBaseView;
import node.com.enjoydanang.model.DetailPartner;
import node.com.enjoydanang.model.Partner;
import node.com.enjoydanang.model.PartnerAlbum;

/**
 * Author: Tavv
 * Created on 10/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public interface iDetailPartnerView extends iBaseView {

    void onFetchFailure(AppError appError);
    void onFetchAllData(List<DetailPartner> lstDetailPartner, List<PartnerAlbum> lstAlbum, List<Partner> lstPartnerAround);
}
