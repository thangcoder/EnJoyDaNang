package node.com.enjoydanang.ui.activity.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import node.com.enjoydanang.GlobalApplication;
import node.com.enjoydanang.MvpActivity;
import node.com.enjoydanang.R;
import node.com.enjoydanang.api.model.BaseRepository;
import node.com.enjoydanang.constant.AppLanguage;
import node.com.enjoydanang.constant.Constant;
import node.com.enjoydanang.model.Language;
import node.com.enjoydanang.ui.activity.BaseActivity;
import node.com.enjoydanang.ui.activity.login.LoginActivity;
import node.com.enjoydanang.ui.activity.main.MainActivity;
import node.com.enjoydanang.utils.FileUtils;
import node.com.enjoydanang.utils.Utils;
import node.com.enjoydanang.utils.network.NetworkError;

import static android.R.id.list;


public class ScreenSplashActivity extends MvpActivity<SplashScreenPresenter> implements SplashScreenView {
    private static final String TAG = ScreenSplashActivity.class.getSimpleName();
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;


    @Override
    protected SplashScreenPresenter createPresenter() {
        return new SplashScreenPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mvpPresenter = createPresenter();
        mvpPresenter.loadLanguage();
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

    }

    @Override
    public void setValue(Bundle savedInstanceState) {

    }

    @Override
    public void setEvent() {

    }


    /**
     * Delay 3s to start Home Activity
     */
    private void start() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                openMainActivity();
            }
        }, SPLASH_TIME_OUT);
    }

    private void openMainActivity() {
        Intent i = new Intent(ScreenSplashActivity.this, MainActivity.class);
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
            start();
        }
    }

    @Override
    public void onLoadFailre(NetworkError networkError) {
        Log.e(TAG, "onLoadFailre: " + networkError.getMessage());
    }

}
