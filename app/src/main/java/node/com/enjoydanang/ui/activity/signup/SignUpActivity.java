package node.com.enjoydanang.ui.activity.signup;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import node.com.enjoydanang.MvpActivity;
import node.com.enjoydanang.R;
import node.com.enjoydanang.api.model.Repository;
import node.com.enjoydanang.model.UserInfo;
import node.com.enjoydanang.utils.Utils;
import node.com.enjoydanang.constant.AppError;

/**
 * Author: Tavv
 * Created on 20/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class SignUpActivity extends MvpActivity<SignUpPresenter> implements SignUpView {
    private static final String TAG = SignUpActivity.class.getSimpleName();

    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    @BindView(R.id.lrlSignIn)
    LinearLayout lrlSign;

    @BindView(R.id.edtUserName)
    EditText edtUserName;
    @BindView(R.id.edtEmail)
    EditText edtEmail;
    @BindView(R.id.edtPassWord)
    EditText edtPassWord;
    @BindView(R.id.edtPhoneNum)
    EditText edtPhoneNum;
    @BindView(R.id.edtFullName)
    EditText edtFullName;

    private TextView txtSignAction;

    @Override
    protected SignUpPresenter createPresenter() {
        return new SignUpPresenter(this);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_sign_up);
    }

    @Override
    public void init() {
        txtSignAction = (TextView) lrlSign.findViewById(R.id.txtSignAction);
    }

    @Override
    public void bindViews() {
        ButterKnife.bind(this);
    }

    @Override
    public void setValue(Bundle savedInstanceState) {
        txtSignAction.setText(getResources().getString(R.string.sign_up));
        initToolbar(toolbar);
        toolbar.getBackground().setAlpha(0);
        setTitle(getResources().getString(R.string.sign_up));
    }

    @OnClick({R.id.lrlSignIn, R.id.txtBackToSignIn})
    public void onClickListener(View view) {
        switch (view.getId()) {
            case R.id.lrlSignIn:
                String userName = edtUserName.getText().toString();
                String pwd = edtPassWord.getText().toString();
                if (!isValidBeforeSubmit(userName, pwd)) {
                    String email = edtEmail.getText().toString();
                    String phoneNum = edtPhoneNum.getText().toString();
                    String fullName = edtFullName.getText().toString();
                    UserInfo userInfo = new UserInfo(userName, pwd, email, fullName, phoneNum);
                    showLoading();
                    mvpPresenter.normalRegister(userInfo);
                } else {
                    Toast.makeText(mActivity, Utils.getString(R.string.user_or_pwd_not_empty), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.txtBackToSignIn:
                finish();
                overridePendingTransitionExit();
                break;
        }
    }

    @Override
    public void setEvent() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransitionExit();
            }
        });
    }

    @Override
    public void showToast(String desc) {

    }

    @Override
    public void unKnownError() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransitionExit();
    }

    private boolean isValidBeforeSubmit(String userName, String pwd) {
        return StringUtils.isBlank(userName) || StringUtils.isBlank(pwd);
    }

    @Override
    public void onRegisterSuccess(Repository<UserInfo> resultCallBack) {
        Log.i(TAG, "onRegisterSuccess " + resultCallBack);
    }

    @Override
    public void onRegisterFailure(AppError error) {
        Log.i(TAG, "onRegisterFailure " + error.getMessage());
    }
}
