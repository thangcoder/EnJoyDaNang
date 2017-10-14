package node.com.enjoydanang.ui.fragment.home;

import java.util.List;

import node.com.enjoydanang.BasePresenter;
import node.com.enjoydanang.model.MenuItem;

/**
 * Created by chien on 10/8/17.
 */

public class HomePresenter extends BasePresenter<iHomeView> {

    public HomePresenter(iHomeView view) {
        super(view);
    }

    public void getMenuItem(List<MenuItem> menuItems){
        mvpView.getMenuFinish(menuItems);
    }
    public void getProducts(){


    }
}
