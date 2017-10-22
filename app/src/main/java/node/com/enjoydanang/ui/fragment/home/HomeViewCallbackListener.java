package node.com.enjoydanang.ui.fragment.home;

import node.com.enjoydanang.api.model.BaseRepository;
import node.com.enjoydanang.model.Banner;
import node.com.enjoydanang.model.Category;
import node.com.enjoydanang.model.Partner;
import node.com.enjoydanang.constant.AppError;

/**
 * Author: Tavv
 * Created on 21/10/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public interface HomeViewCallbackListener extends iHomeView {

    void onGetBannerSuccess(BaseRepository<Banner> data);

    void onGetBannerFailure(AppError error);

    void onGetPartnerSuccess(BaseRepository<Partner> data);

    void onGetPartnerFailure(AppError error);

    void onGetCategorySuccess(BaseRepository<Category> data);

    void onGetCategoryFailure(AppError error);

    void onGetPartnerByCategorySuccess(BaseRepository<Partner> data);

    void onGetPartnerByCategoryFailure(AppError error);
}
