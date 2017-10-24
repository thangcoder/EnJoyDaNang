package node.com.enjoydanang.ui.activity.start;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import node.com.enjoydanang.MvpActivity;
import node.com.enjoydanang.R;
import node.com.enjoydanang.ui.activity.login.LoginActivity;
import node.com.enjoydanang.ui.activity.main.MainActivity;
import node.com.enjoydanang.ui.activity.signup.SignUpActivity;

/**
 * Author: Tavv
 * Created on 25/10/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick({R.id.btnGoToSignUp, R.id.btnGoToLogin, R.id.btnContinue})
    public void onClick(View view){
        Intent intent = null;
        switch (view.getId()){
            case R.id.btnGoToLogin :
                intent = new Intent(this, LoginActivity.class);
                break;
            case R.id.btnGoToSignUp :
                intent = new Intent(this, SignUpActivity.class);
                break;
            case R.id.btnContinue :
                intent = new Intent(this, MainActivity.class);
                break;
        }
        startActivity(intent);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }
}
