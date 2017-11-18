package node.com.enjoydanang.ui.activity.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import node.com.enjoydanang.GlobalApplication;
import node.com.enjoydanang.MvpActivity;
import node.com.enjoydanang.R;
import node.com.enjoydanang.annotation.DialogType;
import node.com.enjoydanang.constant.AppError;
import node.com.enjoydanang.constant.Constant;
import node.com.enjoydanang.model.UserInfo;
import node.com.enjoydanang.ui.activity.login.LoginActivity;
import node.com.enjoydanang.ui.activity.main.MainActivity;
import node.com.enjoydanang.utils.DialogUtils;
import node.com.enjoydanang.utils.FileUtils;
import node.com.enjoydanang.utils.JsonUtils;
import node.com.enjoydanang.utils.SharedPrefsUtils;
import node.com.enjoydanang.utils.Utils;
import node.com.enjoydanang.utils.helper.LanguageHelper;
import node.com.enjoydanang.utils.network.NetworkUtils;

import static node.com.enjoydanang.constant.Constant.SPLASH_TIME_OUT;


public class ScreenSplashActivity extends MvpActivity<SplashScreenPresenter> implements SplashScreenView {
    private static final String TAG = ScreenSplashActivity.class.getSimpleName();

    @BindView(R.id.txtLoadingContent)
    TextView txtLoadingContent;

    private boolean hasTextContent;

    @Override
    protected SplashScreenPresenter createPresenter() {
        return new SplashScreenPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (NetworkUtils.isNetworkContented(ScreenSplashActivity.this)) {
            mvpPresenter = createPresenter();
            mvpPresenter.loadLanguage();
        } else {
            DialogUtils.showDialog(ScreenSplashActivity.this, DialogType.WARNING, DialogUtils.getTitleDialog(2), Utils.getLanguageByResId(R.string.Message_No_Internet));
        }

    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_splash);
    }

    @Override
    public void init() {
//        start();
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


    /**
     * Delay 1s to start Home Activity
     */
    private void start() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                openNextActivity();
            }
        }, SPLASH_TIME_OUT);
    }

    private void openNextActivity() {
        if (Utils.hasSessionLogin()) {
            UserInfo userInfo = JsonUtils.convertJsonToObject(SharedPrefsUtils.getStringFromPrefs(Constant.SHARED_PREFS_NAME,
                    Constant.KEY_EXTRAS_USER_INFO), UserInfo.class);
            if (userInfo != null) {
                GlobalApplication.setUserInfo(userInfo);
            }
        }
        Class<?> nextClass = Utils.hasSessionLogin() ? MainActivity.class : LoginActivity.class;
        Intent i = new Intent(ScreenSplashActivity.this, nextClass);
        startActivity(i);
        finish();
    }

    @Override
    public void showToast(String desc) {

    }

    @Override
    public void unKnownError() {

    }

    @Override
    public void onLoadLanguageSuccess(JSONObject json) {
        if (json != null) {
            FileUtils.saveFilePrivateMode(Constant.FILE_NAME_LANGUAGE, json.toString());
            GlobalApplication.getGlobalApplicationContext().setJsLanguage(json);
            if (!hasTextContent) {
                LanguageHelper.getValueByViewId(txtLoadingContent);
            }
            start();
        }
    }

    @Override
    public void onLoadFailre(AppError appError) {
        DialogUtils.showDialog(ScreenSplashActivity.this, DialogType.WRONG, DialogUtils.getTitleDialog(3), appError.getMessage());
    }

    @Override
    public void initViewLabel() {
        String value = LanguageHelper.getValueByKey(txtLoadingContent.getText().toString().trim());
        if (StringUtils.isNotEmpty(value)) {
            txtLoadingContent.setText(value);
            hasTextContent = true;
        }
    }


}
