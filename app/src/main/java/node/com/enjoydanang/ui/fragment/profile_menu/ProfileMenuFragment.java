package node.com.enjoydanang.ui.fragment.profile_menu;

import android.support.design.widget.TextInputLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

import org.apache.commons.lang3.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import node.com.enjoydanang.MvpFragmentWithToolbar;
import node.com.enjoydanang.R;
import node.com.enjoydanang.model.UserInfo;
import node.com.enjoydanang.utils.ImageUtils;
import node.com.enjoydanang.utils.Utils;

/**
 * Created by chientruong on 1/5/17.
 */

public class ProfileMenuFragment extends MvpFragmentWithToolbar<ProfileMenuPresenter> implements iProfileMenuView {


    @BindView(R.id.tv_username)
    TextView tvUserName;
    @BindView(R.id.tv_email)
    TextView tvEmail;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_menthod)
    TextView tvMenthod;
    @BindView(R.id.tv_last_login)
    TextView tvLastLogin;

    @BindView(R.id.img_avatar)
    SimpleDraweeView imgAvatar;

    private UserInfo userInfo;
    @Override
    protected void init(View view) {
        setHasOptionsMenu(true);
        userInfo = Utils.getUserInfo();
        if (userInfo!= null){initData();};
    }

    @Override
    protected void setEvent(View view) {

    }

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_profile_menu;
    }

    @Override
    public void bindView(View view) {
        ButterKnife.bind(this, view);
    }

    @Override
    public void onResume() {
        mBaseActivity.getToolbar().setTitle(Utils.getString(R.string.Update_Profile_Screen_Title));
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected ProfileMenuPresenter createPresenter() {
        return new ProfileMenuPresenter(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem editItem = menu.findItem(R.id.menu_edit);
        MenuItem scanItem = menu.findItem(R.id.menu_scan);
        editItem.setVisible(true);
        scanItem.setVisible(false);

    }

    @Override
    public void showToast(String desc) {

    }

    @Override
    public void unKnownError() {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void setupActionBar() {


    }

    @OnClick(R.id.tv_login_checking)
    public void checking() {
        // TODO submit data to server...
        Toast.makeText(mMainActivity, "API ", Toast.LENGTH_SHORT).show();
    }

    private void initData(){
        tvUserName.setText(userInfo.getUserName());
        tvEmail.setText((StringUtils.isEmpty(userInfo.getEmail()) ? "" : userInfo.getEmail()));
        tvPhone.setText((StringUtils.isEmpty(userInfo.getPhone()) ? "" : userInfo.getPhone()));
        tvMenthod.setText((StringUtils.isEmpty(userInfo.getType()) ? "" : userInfo.getType()));
        ImageUtils.loadImageWithFreso( imgAvatar, userInfo.getImage());
    }
}
