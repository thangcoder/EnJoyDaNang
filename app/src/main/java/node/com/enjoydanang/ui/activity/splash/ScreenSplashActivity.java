package node.com.enjoydanang.ui.activity.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import node.com.enjoydanang.GlobalApplication;
import node.com.enjoydanang.MvpActivity;
import node.com.enjoydanang.R;
import node.com.enjoydanang.annotation.DialogType;
import node.com.enjoydanang.api.ApiCallback;
import node.com.enjoydanang.api.ApiStores;
import node.com.enjoydanang.api.model.Repository;
import node.com.enjoydanang.api.module.AppClient;
import node.com.enjoydanang.common.Common;
import node.com.enjoydanang.constant.AppError;
import node.com.enjoydanang.constant.Constant;
import node.com.enjoydanang.model.Language;
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

    private UserInfo localUser;


    @Override
    protected SplashScreenPresenter createPresenter() {
        return new SplashScreenPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mvpPresenter = createPresenter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (NetworkUtils.isNetworkContented(ScreenSplashActivity.this)) {
            if (!Utils.hasSessionLogin()) {
                mvpPresenter.loadLanguage();
            }
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
        if (Utils.hasSessionLogin()) {
            localUser = JsonUtils.convertJsonToObject(SharedPrefsUtils.getStringFromPrefs(Constant.SHARED_PREFS_NAME,
                    Constant.KEY_EXTRAS_USER_INFO), UserInfo.class);
            mvpPresenter.getDataCombine(localUser.getUserId());
        }
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
                Common.validLanguageLogin(userInfo);
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
        }
        changeBaseUrl();
    }

    @Override
    public void onLoadFailure(AppError appError) {
        DialogUtils.showDialog(ScreenSplashActivity.this, DialogType.WRONG, DialogUtils.getTitleDialog(3), appError.getMessage());
    }

    @Override
    public void onGetUserInfoSuccess(UserInfo userInfo) {
        if (!localUser.equals(userInfo)) {
            Utils.saveUserInfo(userInfo);
        }
        start();
    }

    @Override
    public void onFailure(AppError appError) {
        Log.e(TAG, "onFailure: " + appError.getMessage());
    }

    @Override
    public void onCombined(JSONObject json, UserInfo userInfo) {
        if (json != null) {
            FileUtils.saveFilePrivateMode(Constant.FILE_NAME_LANGUAGE, json.toString());
            GlobalApplication.getGlobalApplicationContext().setJsLanguage(json);
            if (!hasTextContent) {
                LanguageHelper.getValueByViewId(txtLoadingContent);
            }
        }
        if (!localUser.equals(userInfo)) {
            Utils.saveUserInfo(userInfo);
        }
        changeBaseUrl();
    }

    @Override
    public void initViewLabel() {
        String value = LanguageHelper.getValueByKey(txtLoadingContent.getText().toString().trim());
        if (StringUtils.isNotEmpty(value)) {
            txtLoadingContent.setText(value);
            hasTextContent = true;
        }
    }

    private void changeBaseUrl() {
        ApiStores changeStores = AppClient.setNewBaseUrl(Constant.URL_HOST_VN).create(ApiStores.class);
        addSubscription(changeStores.getLanguage(), new ApiCallback<Repository<Language>>() {

            @Override
            public void onSuccess(Repository<Language> data) {
                if (Utils.isNotEmptyContent(data)) {
                    List<Language> lstLanguages = data.getData();
                    Map<String, String> maps = new HashMap<String, String>();
                    int length = lstLanguages.size();
                    for (int i = 0; i < length; i++) {
                        Language language = lstLanguages.get(i);
                        maps.put(language.getName(), language.getValue());
                    }
                    JSONObject json = new JSONObject(maps);
                    FileUtils.saveFilePrivateMode(Constant.FILE_NAME_LANGUAGE_VN, json.toString());
                    start();
                }
            }

            @Override
            public void onFailure(String msg) {
                Log.e(TAG, "changeBaseUrl onFailure " + msg);
            }

            @Override
            public void onFinish() {

            }
        });
    }

}
