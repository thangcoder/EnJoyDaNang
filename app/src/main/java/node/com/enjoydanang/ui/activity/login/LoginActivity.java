package node.com.enjoydanang.ui.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.internal.CallbackManagerImpl;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.kakao.auth.Session;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import node.com.enjoydanang.MvpActivity;
import node.com.enjoydanang.R;
import node.com.enjoydanang.api.model.BaseRepository;
import node.com.enjoydanang.constant.LoginType;
import node.com.enjoydanang.model.UserInfo;
import node.com.enjoydanang.ui.activity.main.MainActivity;
import node.com.enjoydanang.ui.activity.signup.SignUpActivity;
import node.com.enjoydanang.utils.Utils;
import node.com.enjoydanang.utils.helper.StatusBarCompat;
import node.com.enjoydanang.constant.AppError;

import static node.com.enjoydanang.ui.activity.login.LoginViaGoogle.RC_SIGN_IN;

/**
 * Author: Tavv
 * Created on 09/10/2017.
 * Project Name: EnJoyDanang
 * Version : 1.0
 */

public class LoginActivity extends MvpActivity<LoginPresenter> implements LoginView {
    private static final String TAG = LoginActivity.class.getSimpleName();

    @BindView(R.id.lrlSignIn)
    public LinearLayout lrlSignIn;

    @BindView(R.id.txtCreateAccount)
    public TextView txtCreateAccount;

    @BindView(R.id.txtForgotPwd)
    public TextView txtForgotPwd;

    @BindView(R.id.loadingLogin)
    public ProgressBar prgLoadingLogin;


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
        setContentView(R.layout.activity_login);
    }

    @Override
    public void init() {
        LoginFactory loginFactory = new LoginFactory();
        loginViaFacebook = (LoginViaFacebook) loginFactory.getLoginType(LoginType.FACEBOOK, this);
        loginViaGoogle = (LoginViaGoogle) loginFactory.getLoginType(LoginType.GOOGLE, this);
        loginViaKakaoTalk = (LoginViaKakaoTalk) loginFactory.getLoginType(LoginType.KAKAOTALK, this);

        //init
        loginViaFacebook.init();
        loginViaGoogle.init();
        setPresenter();
    }

    @Override
    public void bindViews() {
        ButterKnife.bind(this);
    }

    @Override
    public void setValue(Bundle savedInstanceState) {
        loginViaKakaoTalk.setProgressbar(prgLoadingLogin);
    }

    @Override
    public void setEvent() {

    }


    @OnClick({R.id.lrlSignIn, R.id.txtCreateAccount, R.id.txtForgotPwd})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.lrlSignIn:
                intent = new Intent(mActivity, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.txtCreateAccount:
                intent = new Intent(mActivity, SignUpActivity.class);
                startActivity(intent);
                overridePendingTransitionEnter();
                break;
            case R.id.txtForgotPwd:
                Toast.makeText(this, "Forgot Click", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @OnClick({R.id.btnLoginFb, R.id.btnLoginGPlus, R.id.btnLoginKakaotalk})
    public void onRegisterWithSocial(View view) {
        switch (view.getId()) {
            case R.id.btnLoginFb:
                loginViaFacebook.login();
                break;
            case R.id.btnLoginGPlus:
                loginViaGoogle.login();
                break;
            case R.id.btnLoginKakaotalk:
                prgLoadingLogin.setVisibility(View.VISIBLE);
                loginViaKakaoTalk.login();
                break;
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
    public void onLoginSuccess(BaseRepository<UserInfo> resultCallBack) {
        if (Utils.isNotEmptyContent(resultCallBack)) {
            List<UserInfo> userInfoLst = resultCallBack.getData();
            Log.i(TAG, "onLoginSuccess " + userInfoLst);
        }
    }

    @Override
    public void onLoginFailure(AppError error) {
        Log.e(TAG, "onLoginFailure " + error.getMessage());
    }

}
