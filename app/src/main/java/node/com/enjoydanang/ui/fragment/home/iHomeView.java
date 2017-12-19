package node.com.enjoydanang.ui.fragment.home;

import java.util.List;

import node.com.enjoydanang.constant.AppError;
import node.com.enjoydanang.iBaseView;
import node.com.enjoydanang.model.Banner;
import node.com.enjoydanang.model.Category;
import node.com.enjoydanang.model.Partner;

/**
 * Created by chien on 10/8/17.
 */

public interface iHomeView  extends iBaseView {

    void onGetPartnerSuccess(List<Partner> data);

    void onGetPartnerFailure(AppError error);

    void addFavoriteSuccess();

    void addFavoriteFailure(AppError error);

    void onFetchAllDataSuccess(List<Partner> partners, List<Banner> banners, List<Category> categories);

    void onFetchFailure(AppError error);

}
