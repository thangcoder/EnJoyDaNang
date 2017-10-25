package node.com.enjoydanang.ui.activity.signup;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import org.apache.commons.lang3.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import node.com.enjoydanang.GlobalApplication;
import node.com.enjoydanang.MvpActivity;
import node.com.enjoydanang.R;
import node.com.enjoydanang.api.model.Repository;
import node.com.enjoydanang.constant.AppError;
import node.com.enjoydanang.model.UserInfo;
import node.com.enjoydanang.utils.Utils;
import node.com.enjoydanang.utils.ValidUtils;

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

    private ValidUtils validator;


    @Override
    protected SignUpPresenter createPresenter() {
        return new SignUpPresenter(this);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_sign_up_v2);
    }

    @Override
    public void init() {
        validator = new ValidUtils();
    }

    @Override
    public void bindViews() {
        ButterKnife.bind(this);
    }

    @Override
    public void setValue(Bundle savedInstanceState) {
        initToolbar(toolbar);
        setTitle(getResources().getString(R.string.sign_up).toUpperCase());
    }

    @OnClick({R.id.txtBackToSignIn, R.id.btnSignUp})
    public void onClickListener(View view) {
        switch (view.getId()) {
            case R.id.txtBackToSignIn:
                finish();
                overridePendingTransitionExit();
                break;
            case R.id.btnSignUp:
                register();
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

    private boolean isUserNameAndPwdInValid(String userName, String pwd) {
        return StringUtils.isBlank(userName) || StringUtils.isBlank(pwd);
    }

    @Override
    public void onRegisterSuccess(Repository<UserInfo> resultCallBack) {
        if (Utils.isNotEmptyContent(resultCallBack)) {
            UserInfo userInfo = resultCallBack.getData().get(0);
            GlobalApplication.setUserInfo(userInfo);
            clearFormAfterSuccess();
            redirectMain();
        }
    }

    @Override
    public void onRegisterFailure(AppError error) {
        Utils.showDialog(this, 1, Utils.getString(R.string.sign_up), error.getMessage());
    }

    private void register() {
        String userName = edtUserName.getText().toString();
        String pwd = edtPassWord.getText().toString();
        String email = edtEmail.getText().toString();
        String fullName = edtFullName.getText().toString();
        String strError = getStrErrorInValid(userName, pwd, email, fullName);
        if (StringUtils.isEmpty(strError)) {
            String phoneNum = edtPhoneNum.getText().toString();
            UserInfo userInfo = new UserInfo(userName, pwd, email, fullName, phoneNum);
            showLoading();
            mvpPresenter.normalRegister(userInfo);
        } else {
            Utils.showDialog(this, 1, "Validator", strError);
        }
    }

    private String getStrErrorInValid(String userName, String pwd, String email, String fullName) {
        if (isUserNameAndPwdInValid(userName, pwd)) {
            return Utils.getString(R.string.user_or_pwd_not_empty);
        }
        if (!validator.isValidEmail(email)) {
            return Utils.getString(R.string.email_invalid);
        }
        if(StringUtils.isEmpty(fullName)){
            return Utils.getString(R.string.full_name_empty);
        }
        return StringUtils.EMPTY;
    }

    private void clearFormAfterSuccess(){
        Utils.clearForm(edtUserName, edtPassWord, edtFullName, edtEmail, edtPhoneNum);
    }


}
