package node.com.enjoydanang.ui.activity.signup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import node.com.enjoydanang.GlobalApplication;
import node.com.enjoydanang.MvpActivity;
import node.com.enjoydanang.R;
import node.com.enjoydanang.api.model.Repository;
import node.com.enjoydanang.constant.AppError;
import node.com.enjoydanang.constant.AppLanguage;
import node.com.enjoydanang.model.UserInfo;
import node.com.enjoydanang.ui.activity.login.LoginActivity;
import node.com.enjoydanang.utils.DialogUtils;
import node.com.enjoydanang.utils.Utils;
import node.com.enjoydanang.utils.ValidUtils;
import node.com.enjoydanang.utils.helper.LanguageHelper;

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

    @BindView(R.id.btnSignUp)
    AppCompatButton btnSignUp;

    @BindView(R.id.txtBackToSignIn)
    TextView txtBackToSignIn;

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
        setTitle(Utils.getLanguageByResId(R.string.Home_Account_Register).toUpperCase());
    }

    @OnClick({R.id.txtBackToSignIn, R.id.btnSignUp})
    public void onClickListener(View view) {
        switch (view.getId()) {
            case R.id.txtBackToSignIn:
                startActivity(new Intent(this, LoginActivity.class));
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
    public void initViewLabel() {
        edtFullName.setHint(LanguageHelper.getValueByKey(AppLanguage.Key.FullName));
        edtEmail.setHint(LanguageHelper.getValueByKey(AppLanguage.Key.Email));
        edtPassWord.setHint(LanguageHelper.getValueByKey(AppLanguage.Key.Password));
        edtPhoneNum.setHint(LanguageHelper.getValueByKey(AppLanguage.Key.Phone));
        btnSignUp.setText(LanguageHelper.getValueByKey(AppLanguage.Key.Home_Account_Register));
        txtBackToSignIn.setText(LanguageHelper.getValueByKey(AppLanguage.Key.Home_Account_Login));
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
        DialogUtils.showDialog(this, 1, Utils.getLanguageByResId(R.string.Dialog_Title_Wrong), error.getMessage());
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
            DialogUtils.showDialog(this, 1, Utils.getLanguageByResId(R.string.Dialog_Title_Wrong), strError);
        }
    }

    private String getStrErrorInValid(String userName, String pwd, String email, String fullName) {
        if (isUserNameAndPwdInValid(userName, pwd)) {
            return Utils.getLanguageByResId(R.string.Validate_Message_UserName_Pwd_Empty);
        }
        if (!validator.isValidEmail(email)) {
            return Utils.getLanguageByResId(R.string.Home_Account_InvalidEmail);
        }
        if(StringUtils.isEmpty(fullName)){
            return Utils.getLanguageByResId(R.string.Message_NameEmpty);
        }
        return StringUtils.EMPTY;
    }

    private void clearFormAfterSuccess(){
        Utils.clearForm(edtUserName, edtPassWord, edtFullName, edtEmail, edtPhoneNum);
    }


}
