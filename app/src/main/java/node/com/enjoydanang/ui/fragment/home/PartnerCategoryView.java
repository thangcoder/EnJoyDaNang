package node.com.enjoydanang.ui.fragment.home;

import node.com.enjoydanang.api.model.Repository;
import node.com.enjoydanang.constant.AppError;
import node.com.enjoydanang.iBaseView;
import node.com.enjoydanang.model.Partner;

/**
 * Author: Tavv
 * Created on 22/11/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public interface PartnerCategoryView extends iBaseView {

    void onGetPartnerByCategorySuccess(Repository<Partner> data);

    void onGetPartnerByCategoryFailure(AppError error);

    void addFavoriteSuccess();

    void addFavoriteFailure(AppError error);
}
