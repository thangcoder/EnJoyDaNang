package node.com.enjoydanang.ui.fragment.home;

import java.util.List;

import node.com.enjoydanang.iBaseView;
import node.com.enjoydanang.model.Category;
import node.com.enjoydanang.model.MenuItem;
import node.com.enjoydanang.model.Products;

/**
 * Created by chien on 10/8/17.
 */

public interface iHomeView  extends iBaseView {
    void getMenuFinish(List<MenuItem> menuList);
    void getMenuFail();
    void getCategorysFinish(Category products);
    void getCategorysFail();
}
