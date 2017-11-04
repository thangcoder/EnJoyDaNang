package node.com.enjoydanang.ui.fragment.change_password;

import android.support.v7.widget.AppCompatButton;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import node.com.enjoydanang.GlobalApplication;
import node.com.enjoydanang.MvpFragment;
import node.com.enjoydanang.R;
import node.com.enjoydanang.annotation.DialogType;
import node.com.enjoydanang.constant.AppError;
import node.com.enjoydanang.model.UserInfo;
import node.com.enjoydanang.utils.DialogUtils;
import node.com.enjoydanang.utils.Utils;
import node.com.enjoydanang.utils.ValidUtils;
import node.com.enjoydanang.utils.helper.LanguageHelper;
import node.com.enjoydanang.utils.helper.SoftKeyboardManager;

/**
 * Author: Tavv
 * Created on 26/10/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public class ChangePwdFragment extends MvpFragment<ChangePwdPresenter> implements ChangePwdView, View.OnTouchListener {
    private ValidUtils validator;

    @BindView(R.id.edtOldPwd)
    EditText edtOldPwd;

    @BindView(R.id.lrlChangePwd)
    LinearLayout lrlChangePwd;

    @BindView(R.id.edtPwd)
    EditText edtNewPwd;

    @BindView(R.id.edtReEnterPwd)
    EditText edtReEnterNewPwd;

    @BindView(R.id.lblOldPwd)
    TextView lblOldPwd;

    @BindView(R.id.lblNewPwd)
    TextView lblNewPwd;

    @BindView(R.id.lblReNew)
    TextView lblReNew;

    @BindView(R.id.btnSave)
    AppCompatButton btnSave;


    private UserInfo userInfo;


    @Override
    public void showToast(String desc) {

    }

    @Override
    public void unKnownError() {

    }

    @Override
    protected ChangePwdPresenter createPresenter() {
        return new ChangePwdPresenter(this);
    }

    @Override
    protected void init(View view) {
        mBaseActivity.getToolbar().setTitle(Utils.getLanguageByResId(R.string.Home_Account_ChangePassword).toUpperCase());
        userInfo = Utils.getUserInfo();
        validator = new ValidUtils();
    }

    @Override
    protected void setEvent(View view) {
        lrlChangePwd.setOnTouchListener(this);
    }

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_change_pwd;
    }

    @Override
    public void bindView(View view) {
        ButterKnife.bind(this, view);
    }

    @Override
    public void onChangeSuccess(UserInfo userInfo) {
        if (userInfo != null) {
            this.userInfo = userInfo;
            GlobalApplication.setUserInfo(userInfo);
        }
        Utils.clearForm(edtOldPwd, edtNewPwd, edtReEnterNewPwd);
        hideLoading();
        DialogUtils.showDialog(getContext(), DialogType.SUCCESS, Utils.getLanguageByResId(R.string.Dialog_Title_Success), Utils.getLanguageByResId(R.string.Update_Success));
    }

    @Override
    public void onChangeFailure(AppError error) {
        hideLoading();
        DialogUtils.showDialog(getContext(), DialogType.WRONG, Utils.getLanguageByResId(R.string.Dialog_Title_Wrong), error.getMessage());
    }

    @OnClick(R.id.btnSave)
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSave:
                changePwd();
                break;
        }
    }

    private void changePwd() {
        String oldPwd = edtOldPwd.getText().toString();
        String newPwd = edtNewPwd.getText().toString();
        String confirmPwd = edtReEnterNewPwd.getText().toString();
        if (isPwdMatched(newPwd, confirmPwd) && StringUtils.isNotEmpty(oldPwd)) {
            showLoading();
            mvpPresenter.changePwd(userInfo.getUserId(), oldPwd, newPwd);
        } else {
            DialogUtils.showDialog(getContext(), DialogType.WARNING, Utils.getLanguageByResId(R.string.Dialog_Title_Warning), Utils.getLanguageByResId(R.string.Home_Account_Password_NotContain));
        }
    }

    private boolean isPwdMatched(String pwd, String confirmPwd) {
        return !(StringUtils.isEmpty(pwd) || StringUtils.isEmpty(confirmPwd)) && validator.isValidConfirmPasswrod(confirmPwd, pwd);
    }

    @Override
    public void initViewLabel(View view) {
        super.initViewLabel(view);
        LanguageHelper.getValueByViewId(lblOldPwd, lblNewPwd, lblReNew, btnSave);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.lrlChangePwd) {
            SoftKeyboardManager.hideSoftKeyboard(getContext(), v.getWindowToken(), 0);
        }
        return true;
    }
}
