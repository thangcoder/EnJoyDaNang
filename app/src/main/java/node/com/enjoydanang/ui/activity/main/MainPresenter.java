package node.com.enjoydanang.ui.activity.main;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

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

    void loadInfoUserMenu(Context context, ImageView imgAvatar, TextView fullName, TextView email){
        UserInfo userInfo = GlobalApplication.getUserInfo();
        if (userInfo != null) {
            Glide.with(context).load(userInfo.getImage())
                    .placeholder(R.drawable.ic_no_user)
                    .fitCenter()
                    .crossFade()
                    .skipMemoryCache(true)
                    .into(imgAvatar);
            fullName.setText(userInfo.getFullName());
            email.setText(userInfo.getEmail());
        }
    }
}
