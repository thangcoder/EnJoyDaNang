package node.com.enjoydanang.ui.fragment.change_password;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import node.com.enjoydanang.GlobalApplication;
import node.com.enjoydanang.MvpFragment;
import node.com.enjoydanang.R;
import node.com.enjoydanang.constant.AppError;
import node.com.enjoydanang.constant.Constant;
import node.com.enjoydanang.model.UserInfo;
import node.com.enjoydanang.utils.Utils;
import node.com.enjoydanang.utils.ValidUtils;

/**
 * Author: Tavv
 * Created on 26/10/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public class ChangePwdFragment extends MvpFragment<ChangePwdPresenter> implements ChangePwdView {
    private ValidUtils validator;

    @BindView(R.id.edtOldPwd)
    EditText edtOldPwd;

    @BindView(R.id.edtPwd)
    EditText edtNewPwd;

    @BindView(R.id.edtReEnterPwd)
    EditText edtReEnterNewPwd;

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
        mBaseActivity.getToolbar().setTitle(Utils.getString(R.string.Change_Pwd_Screen_Title));
        userInfo = Utils.getUserInfo();
        validator = new ValidUtils();
    }

    @Override
    protected void setEvent(View view) {

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
        if(userInfo != null){
            this.userInfo = userInfo;
            GlobalApplication.setUserInfo(userInfo);
        }
        hideLoading();
    }

    @Override
    public void onChangeFailure(AppError error) {
        Utils.showDialog(getContext(), 2, Constant.TITLE_ERROR, error.getMessage());
    }

    @OnClick({R.id.btnCancel, R.id.btnSave})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCancel:
                break;
            case R.id.btnSave:
                changePwd();
                break;
        }
    }

    private void changePwd(){
        String oldPwd = edtOldPwd.getText().toString();
        String newPwd = edtNewPwd.getText().toString();
        String confirmPwd = edtReEnterNewPwd.getText().toString();
        if(isPwdMatched(newPwd, confirmPwd) && StringUtils.isNotEmpty(oldPwd)){
            showLoading();
            mvpPresenter.changePwd(userInfo.getUserId(), oldPwd, newPwd);
        }else {
            Utils.showDialog(getContext(), 4, Constant.TITLE_WARNING, Utils.getString(R.string.msg_valid_old_new_pwd));
        }
    }

    private boolean isPwdMatched(String pwd, String confirmPwd) {
        return !(StringUtils.isEmpty(pwd) || StringUtils.isEmpty(confirmPwd)) && validator.isValidConfirmPasswrod(confirmPwd, pwd);
    }
}
