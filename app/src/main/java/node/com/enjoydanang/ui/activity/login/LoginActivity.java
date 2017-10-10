package node.com.enjoydanang.ui.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import node.com.enjoydanang.MvpActivity;
import node.com.enjoydanang.R;
import node.com.enjoydanang.ui.activity.detail.DetailActivity;
import node.com.enjoydanang.utils.support.StatusBarCompat;

/**
 * Author: Tavv
 * Created on 09/10/2017.
 * Project Name: EnJoyDanang
 * Version : 1.0
 */

public class LoginActivity extends MvpActivity<LoginPresenter> implements LoginView{

    @BindView(R.id.lrlSignIn)
    public LinearLayout lrlSignIn;

    @BindView(R.id.txtCreateAccount)
    public TextView txtCreateAccount;

    @BindView(R.id.txtForgotPwd)
    public TextView txtForgotPwd;

    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_login);
    }

    @Override
    public void init() {

    }

    @Override
    public void bindViews() {
        ButterKnife.bind(this);
    }

    @Override
    public void setValue(Bundle savedInstanceState) {

    }

    @Override
    public void setEvent() {

    }

    @OnClick({R.id.lrlSignIn, R.id.txtCreateAccount, R.id.txtForgotPwd})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.lrlSignIn:
                Intent intent = new Intent(mActivity, DetailActivity.class);
                startActivity(intent);
                break;
            case R.id.txtCreateAccount:
                Toast.makeText(this, "Create Acc Click", Toast.LENGTH_SHORT).show();
                break;
            case R.id.txtForgotPwd:
                Toast.makeText(this, "Forgot Click", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void showToast(String desc) {

    }

    @Override
    public void unKnownError() {

    }

    @Override
    public void setTranslucentStatusBar() {
        StatusBarCompat.translucentStatusBar(this);
    }
}
