package node.com.enjoydanang.ui.activity.login;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.internal.CallbackManagerImpl;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.kakao.auth.Session;
import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkBuilder;

import org.apache.commons.lang3.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.refactor.lib.colordialog.PromptDialog;
import node.com.enjoydanang.GlobalApplication;
import node.com.enjoydanang.MvpActivity;
import node.com.enjoydanang.R;
import node.com.enjoydanang.annotation.DialogType;
import node.com.enjoydanang.api.model.Repository;
import node.com.enjoydanang.common.Common;
import node.com.enjoydanang.constant.AppError;
import node.com.enjoydanang.constant.Constant;
import node.com.enjoydanang.constant.LoginType;
import node.com.enjoydanang.model.UserInfo;
import node.com.enjoydanang.ui.activity.main.MainActivity;
import node.com.enjoydanang.ui.activity.signup.SignUpActivity;
import node.com.enjoydanang.ui.activity.term.TermActivity;
import node.com.enjoydanang.utils.DialogUtils;
import node.com.enjoydanang.utils.Utils;
import node.com.enjoydanang.utils.config.ForceUpdateChecker;
import node.com.enjoydanang.utils.helper.LanguageHelper;
import node.com.enjoydanang.utils.helper.SoftKeyboardManager;
import node.com.enjoydanang.utils.helper.StatusBarCompat;

import static node.com.enjoydanang.ui.activity.login.LoginViaGoogle.RC_SIGN_IN;

/**
 * Author: Tavv
 * Created on 09/10/2017.
 * Project Name: EnJoyDanang
 * Version : 1.0
 */

public class LoginActivity extends MvpActivity<LoginPresenter> implements LoginView, LoginCallBack, View.OnTouchListener,
        ForceUpdateChecker.OnUpdateNeededListener {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final int PERMISSION_REQUEST_CODE = 200;

    @BindView(R.id.edtUserName)
    EditText edtUserName;

    @BindView(R.id.edtPassWord)
    EditText edtPassword;

    @BindView(R.id.btnLoginNormal)
    AppCompatButton btnLoginNormal;

    @BindView(R.id.txtOr)
    TextView txtOr;

    @BindView(R.id.txtCreateAccount)
    TextView txtCreateAccount;

    @BindView(R.id.txtForgotPwd)
    TextView txtForgotPwd;

    @BindView(R.id.txtContinue)
    TextView txtContinue;

    @BindView(R.id.txtTermSystem)
    TextView txtTermSystem;

    @BindView(R.id.lrlLogin)
    LinearLayout lrlLogin;

    private LoginViaFacebook loginViaFacebook;
    private LoginViaGoogle loginViaGoogle;
    private LoginViaKakaoTalk loginViaKakaoTalk;
    private LoginPresenter loginPresenter;

    private boolean isExit;


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
        setTextTerm();
        LoginFactory loginFactory = new LoginFactory();
        loginViaFacebook = (LoginViaFacebook) loginFactory.getLoginType(LoginType.FACEBOOK, this, this);
        loginViaGoogle = (LoginViaGoogle) loginFactory.getLoginType(LoginType.GOOGLE, this, this);
        loginViaKakaoTalk = (LoginViaKakaoTalk) loginFactory.getLoginType(LoginType.KAKAOTALK, this, this);

        //init
        loginViaFacebook.init();
        loginViaGoogle.init();
        setPresenter();
    }

    private void setTextTerm(){
        String strTerm = Utils.getString(R.string.term_license);
        txtTermSystem.setText(strTerm);
        Link link = new Link(Utils.getString(R.string.term_link));
        link.setTextColor(Utils.getColorRes(R.color.colorPrimary));
        link.setOnClickListener(new Link.OnClickListener() {
            @Override
            public void onClick(String clickedText) {
                Intent intent = new Intent(LoginActivity.this, TermActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });
        LinkBuilder.on(txtTermSystem)
                .addLink(link)
                .build();
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
        lrlLogin.setOnTouchListener(this);
    }

    @Override
    public void initViewLabel() {
        LanguageHelper.getValueByViewId(edtUserName, edtPassword, txtOr, txtCreateAccount,
                txtForgotPwd, btnLoginNormal, txtContinue);
    }


    @OnClick({R.id.btnLoginFb, R.id.btnLoginGPlus, R.id.btnLoginKakaotalk, R.id.btnLoginNormal
            , R.id.txtCreateAccount, R.id.txtForgotPwd, R.id.txtContinue})
    public void onLoginClick(View view) {
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
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constant.URL_FORGOT_PWD));
                break;
            case R.id.txtContinue:
                intent = new Intent(this, MainActivity.class);
                GlobalApplication.getGlobalApplicationContext().setHasSessionLogin(false);
                break;
        }
        if (intent != null) {
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
            Toast.makeText(mActivity, Utils.getLanguageByResId(R.string.Message_Login_Failed), Toast.LENGTH_SHORT).show();
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
            Common.validLanguageLogin(userInfo);
            GlobalApplication.setUserInfo(userInfo);
            SoftKeyboardManager.hideSoftKeyboard(this, btnLoginNormal.getWindowToken(), 0);
            Utils.clearForm(edtUserName, edtPassword);
            Utils.saveUserInfo(userInfo);
            redirectMain();
            hideLoading();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ForceUpdateChecker.with(this).onUpdateNeeded(this).check();
    }

    @Override
    protected void redirectMain() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransitionEnter();
    }

    @Override
    public void onLoginFailure(AppError error) {
        hideLoading();
        DialogUtils.showDialog(this, PromptDialog.DIALOG_TYPE_WRONG, Utils.getLanguageByResId(R.string.Dialog_Title_Wrong), error.getMessage());
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
        } else {
            DialogUtils.showDialog(LoginActivity.this, DialogType.WRONG, DialogUtils.getTitleDialog(3), Utils.getLanguageByResId(R.string.Validate_Message_UserName_Pwd_Empty));
        }
    }

    @Override
    public void onBackPressed() {
        if (isExit) {
            finish();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
        } else {
            Toast.makeText(this, Utils.getLanguageByResId(R.string.Action_DoubleTap),
                    Toast.LENGTH_SHORT).show();
            isExit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isExit = false;
                }
            }, 2500);
        }
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.lrlLogin) {
            SoftKeyboardManager.hideSoftKeyboard(this, v.getWindowToken(), 0);
        }
        return true;
    }

    @Override
    public void onUpdateNeeded(final String updateUrl) {
        DialogUtils.showDialog(LoginActivity.this, DialogType.WARNING, Utils.getLanguageByResId(R.string.Message_Warning_Version_Title),
                Utils.getLanguageByResId(R.string.Message_Confirm_Update_Title), new PromptDialog.OnPositiveListener() {
                    @Override
                    public void onClick(PromptDialog promptDialog) {
                        promptDialog.dismiss();
                        GlobalApplication.getGlobalApplicationContext().setHasClickedUpdate(true);
                        Utils.redirectStore(LoginActivity.this, updateUrl);
                    }
                });
    }

}
