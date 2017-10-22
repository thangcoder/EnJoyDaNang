package node.com.enjoydanang.ui.fragment.home;

import java.util.List;

import node.com.enjoydanang.iBaseView;
import node.com.enjoydanang.model.Banner;
import node.com.enjoydanang.model.Category;

/**
 * Created by chien on 10/8/17.
 */

public interface iHomeView  extends iBaseView {
    void getCategorysFinish(List<Category> products);
    void getCategorysFail();
    void getListByCategoryFinish(List<Category> products);
    void getListByCategoryFail();
    void getBannerFinish(List<Banner> banner);
    void getBannerFail();
}
