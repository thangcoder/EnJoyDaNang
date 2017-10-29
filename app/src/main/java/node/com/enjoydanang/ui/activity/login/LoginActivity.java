package node.com.enjoydanang.ui.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.internal.CallbackManagerImpl;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.kakao.auth.Session;

import org.apache.commons.lang3.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.refactor.lib.colordialog.PromptDialog;
import node.com.enjoydanang.GlobalApplication;
import node.com.enjoydanang.MvpActivity;
import node.com.enjoydanang.R;
import node.com.enjoydanang.api.model.Repository;
import node.com.enjoydanang.constant.AppError;
import node.com.enjoydanang.constant.LoginType;
import node.com.enjoydanang.model.UserInfo;
import node.com.enjoydanang.ui.activity.main.MainActivity;
import node.com.enjoydanang.ui.activity.signup.SignUpActivity;
import node.com.enjoydanang.utils.DialogUtils;
import node.com.enjoydanang.utils.Utils;
import node.com.enjoydanang.utils.helper.StatusBarCompat;

import static node.com.enjoydanang.ui.activity.login.LoginViaGoogle.RC_SIGN_IN;

/**
 * Author: Tavv
 * Created on 09/10/2017.
 * Project Name: EnJoyDanang
 * Version : 1.0
 */

public class LoginActivity extends MvpActivity<LoginPresenter> implements LoginView, LoginCallBack {
    private static final String TAG = LoginActivity.class.getSimpleName();

//    @BindView(R.id.toolbar)
//    public Toolbar toolbar;

    @BindView(R.id.edtUserName)
    EditText edtUserName;

    @BindView(R.id.edtPassWord)
    EditText edtPassword;

    private LoginViaFacebook loginViaFacebook;
    private LoginViaGoogle loginViaGoogle;
    private LoginViaKakaoTalk loginViaKakaoTalk;
    private LoginPresenter loginPresenter;


    @Override
    protected LoginPresenter createPresenter() {
        loginPresenter = new LoginPresenter(this);
        return loginPresenter;
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_login_v2);
    }

    @Override
    public void init() {
//        setTitle(Utils.getString(R.string.Login_Screen_Title));
        LoginFactory loginFactory = new LoginFactory();
        loginViaFacebook = (LoginViaFacebook) loginFactory.getLoginType(LoginType.FACEBOOK, this, this);
        loginViaGoogle = (LoginViaGoogle) loginFactory.getLoginType(LoginType.GOOGLE, this, this);
        loginViaKakaoTalk = (LoginViaKakaoTalk) loginFactory.getLoginType(LoginType.KAKAOTALK, this, this);

        //init
        loginViaFacebook.init();
        loginViaGoogle.init();
        setPresenter();
//        initToolbar(toolbar);
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
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//                overridePendingTransitionExit();
//            }
//        });
    }


    @OnClick({R.id.btnLoginFb, R.id.btnLoginGPlus, R.id.btnLoginKakaotalk, R.id.btnLoginNormal
            , R.id.txtCreateAccount, R.id.txtForgotPwd, R.id.txtContinue})
    public void onLoginClick(View view) {
        showLoading();
        Intent intent = null;
        switch (view.getId()) {
            case R.id.btnLoginFb:
                loginViaFacebook.login();
                break;
            case R.id.btnLoginGPlus:
                loginViaGoogle.login();
                break;
            case R.id.btnLoginKakaotalk:
                loginViaKakaoTalk.login();
                break;
            case R.id.btnLoginNormal:
                loginNormal();
                break;
            case R.id.txtCreateAccount:
                intent = new Intent(this, SignUpActivity.class);
                break;
            case R.id.txtForgotPwd:
                //TODO : redirect to web forgot
                break;
            case R.id.txtContinue:
                intent = new Intent(this, MainActivity.class);
                break;
        }
        if (intent != null) {
            hideLoading();
            startActivity(intent);
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CallbackManagerImpl.RequestCodeOffset.Login.toRequestCode()) {
            loginViaFacebook.getCallbackManager().onActivityResult(requestCode, resultCode, data);
        } else if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInGoogleResult(result);
        }
    }

    private void handleSignInGoogleResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            if (acct != null) {
                loginViaGoogle.handleCallbackResult(acct);
                loginViaGoogle.getTokenGoogle(result);
            }
        } else {
            Toast.makeText(mActivity, "Login Failed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showToast(String desc) {
        Log.e(TAG, "Error " + desc);
    }

    @Override
    public void unKnownError() {

    }

    private void setPresenter() {
        loginViaGoogle.setLoginPresenter(loginPresenter);
        loginViaFacebook.setLoginPresenter(loginPresenter);
        loginViaKakaoTalk.setLoginPresenter(loginPresenter);
    }

    @Override
    public void setTranslucentStatusBar() {
        StatusBarCompat.translucentStatusBar(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        loginViaKakaoTalk.getSession().removeCallback(loginViaKakaoTalk.getSessionCallback());
    }


    @Override
    public void configScreen() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public void onLoginSuccess(Repository<UserInfo> resultCallBack) {
        if (Utils.isNotEmptyContent(resultCallBack)) {
            UserInfo userInfo = resultCallBack.getData().get(0);
            GlobalApplication.setUserInfo(userInfo);
            Utils.clearForm(edtUserName, edtPassword);
            hideLoading();
            redirectMain();
        }
    }

    @Override
    public void onLoginFailure(AppError error) {
        hideLoading();
        DialogUtils.showDialog(this, PromptDialog.DIALOG_TYPE_WRONG, Utils.getString(R.string.login), error.getMessage());
    }

    @Override
    public void hideWaiting() {
        hideLoading();
    }

    private void loginNormal() {
        String userName = edtUserName.getText().toString();
        String password = edtPassword.getText().toString();
        if (StringUtils.isNotBlank(userName) && StringUtils.isNotBlank(password)) {
            showLoading();
            mvpPresenter.normalLogin(userName, password);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransitionExit();
    }
}
