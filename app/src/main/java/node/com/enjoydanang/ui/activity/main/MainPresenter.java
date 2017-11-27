package node.com.enjoydanang.ui.activity.main;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.apache.commons.lang3.StringUtils;

import node.com.enjoydanang.BasePresenter;
import node.com.enjoydanang.GlobalApplication;
import node.com.enjoydanang.api.ApiCallback;
import node.com.enjoydanang.api.model.Repository;
import node.com.enjoydanang.constant.AppError;
import node.com.enjoydanang.model.Popup;
import node.com.enjoydanang.model.UserInfo;
import node.com.enjoydanang.utils.ImageUtils;
import node.com.enjoydanang.utils.Utils;

/**
 * Created by chientruong on 3/27/17.
 */

public class MainPresenter extends BasePresenter<MainView> {

    public MainPresenter(MainView view) {
        super(view);
    }

    void loadInfoUserMenu(Context context, SimpleDraweeView imgAvatar, TextView fullName, TextView email) {
        UserInfo userInfo = GlobalApplication.getUserInfo();
        if (userInfo != null) {
            if (StringUtils.isEmpty(userInfo.getFullName())) {
                fullName.setVisibility(View.GONE);
            } else {
                fullName.setText(userInfo.getFullName());
            }
            if (StringUtils.isEmpty(userInfo.getEmail())) {
                email.setVisibility(View.GONE);
            } else {
                email.setText(userInfo.getEmail());
            }
            ImageUtils.loadImageWithFreso(imgAvatar, userInfo.getImage());
        } else {
            fullName.setVisibility(View.GONE);
            email.setVisibility(View.GONE);
            imgAvatar.setVisibility(View.GONE);
        }
    }

    void getPopup() {
        addSubscription(apiStores.getPopupInformation(), new ApiCallback<Repository<Popup>>() {

            @Override
            public void onSuccess(Repository<Popup> model) {
                if(!Utils.isResponseError(model)){
                    mvpView.onShowPopup(model);
                }
            }

            @Override
            public void onFailure(String msg) {
                mvpView.onError(new AppError(new Throwable(msg)));
            }

            @Override
            public void onFinish() {

            }
        });
    }
}
