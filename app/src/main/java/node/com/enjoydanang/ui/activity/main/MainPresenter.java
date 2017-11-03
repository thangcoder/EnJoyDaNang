package node.com.enjoydanang.ui.activity.main;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.apache.commons.lang3.StringUtils;

import node.com.enjoydanang.BasePresenter;
import node.com.enjoydanang.GlobalApplication;
import node.com.enjoydanang.model.UserInfo;
import node.com.enjoydanang.utils.ImageUtils;

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
            ImageUtils.loadImageWithFreso(imgAvatar,userInfo.getImage());
        } else {
            fullName.setVisibility(View.GONE);
            email.setVisibility(View.GONE);
            imgAvatar.setVisibility(View.GONE);
        }
    }
}
