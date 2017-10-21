package node.com.enjoydanang.ui.fragment.home;

import node.com.enjoydanang.api.model.BaseRepository;
import node.com.enjoydanang.model.Banner;
import node.com.enjoydanang.model.Category;
import node.com.enjoydanang.model.Partner;
import node.com.enjoydanang.utils.network.NetworkError;

/**
 * Author: Tavv
 * Created on 21/10/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public interface HomeViewCallbackListener extends iHomeView{

    void onGetBannerSuccess(BaseRepository<Banner> data);
    void onGetBannerFailure(NetworkError error);

    void onGetPartnerSuccess(BaseRepository<Partner> data);

    void onGetPartnerFailure(NetworkError error);

    void onGetCategorySuccess(BaseRepository<Category> data);

    void onGetCategoryFailure(NetworkError error);
}
