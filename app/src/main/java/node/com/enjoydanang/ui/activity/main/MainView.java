package node.com.enjoydanang.ui.activity.main;


import node.com.enjoydanang.api.model.Repository;
import node.com.enjoydanang.iBaseView;

/**
 * Created by chientruong on 3/27/17.
 */

public interface MainView extends iBaseView {

    void onShowPopup(Repository response);

}
