package node.com.enjoydanang.ui.activity.main;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringUtils;

import node.com.enjoydanang.BasePresenter;
import node.com.enjoydanang.GlobalApplication;
import node.com.enjoydanang.R;
import node.com.enjoydanang.model.UserInfo;

/**
 * Created by chientruong on 3/27/17.
 */

public class MainPresenter extends BasePresenter<MainView> {

    public MainPresenter(MainView view) {
        super(view);
    }

    void loadInfoUserMenu(Context context, ImageView imgAvatar, TextView fullName, TextView email) {
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
            Picasso.with(context).load(userInfo.getImage()).
                    placeholder(R.drawable.ic_no_user).
                    fit().
                    into(imgAvatar);
        } else {
            fullName.setVisibility(View.GONE);
            email.setVisibility(View.GONE);
            imgAvatar.setVisibility(View.GONE);
        }
    }
}
