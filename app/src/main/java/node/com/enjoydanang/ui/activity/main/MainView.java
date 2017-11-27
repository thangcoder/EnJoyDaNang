package node.com.enjoydanang.ui.activity.main;


import node.com.enjoydanang.api.model.Repository;
import node.com.enjoydanang.constant.AppError;
import node.com.enjoydanang.iBaseView;
import node.com.enjoydanang.model.Popup;

/**
 * Created by chientruong on 3/27/17.
 */

public interface MainView extends iBaseView {

    void onShowPopup(Repository<Popup> response);

    void onError(AppError error);

}
