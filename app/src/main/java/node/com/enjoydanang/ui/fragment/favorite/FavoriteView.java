package node.com.enjoydanang.ui.fragment.favorite;

import java.util.List;

import node.com.enjoydanang.constant.AppError;
import node.com.enjoydanang.iBaseView;
import node.com.enjoydanang.model.Partner;

/**
 * Author: Tavv
 * Created on 25/10/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public interface FavoriteView extends iBaseView {

    void onFetchFavorite(List<Partner> lstFavorites);

    void onFetchFailure(AppError error);
}
