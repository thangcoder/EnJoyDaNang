package node.com.enjoydanang.ui.activity.splash;

import node.com.enjoydanang.R;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import node.com.enjoydanang.R;
import node.com.enjoydanang.ui.activity.BaseActivity;
import node.com.enjoydanang.ui.activity.login.LoginActivity;


public class ScreenSplashActivity extends BaseActivity {
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_splash);
    }

    @Override
    public void init() {
        start();
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
        Intent i = new Intent(ScreenSplashActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }


}
