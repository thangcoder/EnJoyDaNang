package node.com.enjoydanang.ui.fragment.profile;

import android.view.View;
import android.widget.EditText;

import org.apache.commons.lang3.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import node.com.enjoydanang.GlobalApplication;
import node.com.enjoydanang.MvpFragment;
import node.com.enjoydanang.R;
import node.com.enjoydanang.constant.AppError;
import node.com.enjoydanang.constant.Constant;
import node.com.enjoydanang.model.UserInfo;
import node.com.enjoydanang.utils.ImageUtils;
import node.com.enjoydanang.utils.Utils;

/**
 * Author: Tavv
 * Created on 27/10/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public class ProfileFragment extends MvpFragment<ProfilePresenter> implements ProfileView {
    private static final String TAG = ProfileFragment.class.getSimpleName();

    @BindView(R.id.edtUserName)
    EditText edtUserName;

    @BindView(R.id.edtFullname)
    EditText edtFullname;

    @BindView(R.id.edtPhone)
    EditText edtPhone;

    @BindView(R.id.edtEmail)
    EditText edtEmail;

    @BindView(R.id.imgAvatarUser)
    CircleImageView imgAvatarUser;

    private UserInfo userInfo;

    @Override
    public void showToast(String desc) {

    }

    @Override
    public void unKnownError() {

    }

    @Override
    protected ProfilePresenter createPresenter() {
        return new ProfilePresenter(this);
    }

    @Override
    protected void init(View view) {
        userInfo = Utils.getUserInfo();
        edtUserName.setText(userInfo.getUserName());
        edtFullname.setText(StringUtils.isEmpty(userInfo.getFullName()) ? "" : userInfo.getFullName());
        edtEmail.setText(StringUtils.isEmpty(userInfo.getEmail()) ? "" : userInfo.getEmail());
        edtPhone.setText(StringUtils.isEmpty(userInfo.getPhone()) ? "" : userInfo.getPhone());
        ImageUtils.loadImageNoRadius(getContext(), imgAvatarUser, userInfo.getImage());
    }

    @Override
    protected void setEvent(View view) {

    }

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_update_profile;
    }

    @Override
    public void bindView(View view) {
        ButterKnife.bind(this, view);
    }

    @Override
    public void onUpdateSuccess(UserInfo userInfo) {
        if (userInfo != null) {
            this.userInfo = userInfo;
            GlobalApplication.setUserInfo(userInfo);
        }
    }

    @Override
    public void onUpdateFailure(AppError error) {
        Utils.showDialog(getContext(), 2, Constant.TITLE_ERROR, error.getMessage());
    }

    @OnClick({R.id.btnUpdate, R.id.txtTakeAPhoto, R.id.txtUploadFrGallery})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnUpdate:
                break;
            case R.id.txtTakeAPhoto:
                break;
            case R.id.txtUploadFrGallery:
                break;
        }
    }
}
