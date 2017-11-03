package node.com.enjoydanang.ui.fragment.profile;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.apache.commons.lang3.StringUtils;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import node.com.enjoydanang.BuildConfig;
import node.com.enjoydanang.GlobalApplication;
import node.com.enjoydanang.MvpFragment;
import node.com.enjoydanang.R;
import node.com.enjoydanang.annotation.DialogType;
import node.com.enjoydanang.constant.AppError;
import node.com.enjoydanang.model.UserInfo;
import node.com.enjoydanang.ui.activity.main.MainActivity;
import node.com.enjoydanang.utils.DialogUtils;
import node.com.enjoydanang.utils.ImageUtils;
import node.com.enjoydanang.utils.Utils;
import node.com.enjoydanang.utils.helper.LanguageHelper;
import node.com.enjoydanang.utils.helper.PhotoHelper;

import static android.app.Activity.RESULT_OK;

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

    @BindView(R.id.txtTakeAPhoto)
    TextView txtTakeAPhoto;
    @BindView(R.id.txtUploadFrGallery)
    TextView txtUploadFrGallery;
    @BindView(R.id.lblUserName)
    TextView lblUserName;
    @BindView(R.id.lblFullName)
    TextView lblFullName;
    @BindView(R.id.lblPhone)
    TextView lblPhone;
    @BindView(R.id.lblEmail)
    TextView lblEmail;
    @BindView(R.id.btnUpdate)
    AppCompatButton btnUpdate;

    @BindView(R.id.imgAvatarUser)
    SimpleDraweeView imgAvatarUser;

    private UserInfo userInfo;

    private PhotoHelper mPhotoHelper;

    private String base64Image;

    private MainActivity mMainActivity;

    @Override
    public void showToast(String desc) {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem editItem = menu.findItem(R.id.menu_edit);
        MenuItem scanItem = menu.findItem(R.id.menu_scan);
        editItem.setVisible(false);
        scanItem.setVisible(false);

    }

    @Override
    public void unKnownError() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected ProfilePresenter createPresenter() {
        return new ProfilePresenter(this);
    }

    @Override
    protected void init(View view) {
        setHasOptionsMenu(true);
        userInfo = Utils.getUserInfo();
        initData();
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
            mMainActivity.refreshHeader();
            initData();
            DialogUtils.showDialog(getContext(), DialogType.SUCCESS, DialogUtils.getTitleDialog(1), Utils.getLanguageByResId(R.string.Update_Success));
        }
    }

    @Override
    public void onUpdateFailure(AppError error) {
        DialogUtils.showDialog(getContext(), DialogType.WRONG, DialogUtils.getTitleDialog(3), error.getMessage());
    }

    @OnClick({R.id.btnUpdate, R.id.txtTakeAPhoto, R.id.txtUploadFrGallery})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnUpdate:
                if (!TextUtils.isEmpty(base64Image)) {
                    mvpPresenter.updateProfile(userInfo.getUserId(),
                            String.valueOf(edtFullname.getText()),
                            String.valueOf(edtPhone.getText()),
                            String.valueOf(edtEmail.getText()),
                            base64Image);
                }
                break;
            case R.id.txtTakeAPhoto:
                mPhotoHelper.cameraIntent();
                break;
            case R.id.txtUploadFrGallery:
                mPhotoHelper.openGallery();
                break;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBaseActivity.getToolbar().setTitle(Utils.getString(R.string.Update_Profile_Screen_Title).toUpperCase());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case PhotoHelper.CAPTURE_IMAGE_REQUEST_CODE:
                if (StringUtils.isNotBlank(mPhotoHelper.getCurrentPhotoPath())) {
                    File imgFile = new File(mPhotoHelper.getCurrentPhotoPath());
                    if (imgFile.exists()) {
                        base64Image = ImageUtils.encodeTobase64(imgFile);
                        updateAvatar(imgFile);
                    } else {
                        DialogUtils.showDialog(getContext(), DialogType.WARNING, DialogUtils.getTitleDialog(2), Utils.getLanguageByResId(R.string.Image_Not_Found));
                    }
                }
                break;
            case PhotoHelper.SELECT_FROM_GALLERY_CODE:
                if (data != null) {
                    Uri uri = data.getData();
                    File file = new File(mPhotoHelper.getRealPathFromURI(uri));
                    base64Image = ImageUtils.encodeTobase64(file);
                    updateAvatar(file);
                }
                break;
        }
    }

    private void updateAvatar(File file) {
        Uri uri = FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".fileprovider", file);
        ImageUtils.loadImageWithFresoURI(imgAvatarUser, uri);
    }

    private void initData() {
        mPhotoHelper = PhotoHelper.newInstance(this);
        edtUserName.setText(userInfo.getUserName());
        edtFullname.setText(StringUtils.isEmpty(userInfo.getFullName()) ? "" : userInfo.getFullName());
        edtEmail.setText(StringUtils.isEmpty(userInfo.getEmail()) ? "" : userInfo.getEmail());
        edtPhone.setText(StringUtils.isEmpty(userInfo.getPhone()) ? "" : userInfo.getPhone());
        ImageUtils.loadImageWithFreso(imgAvatarUser, userInfo.getImage());
    }

    @Override
    public void initViewLabel(View view) {
        super.initViewLabel(view);
        LanguageHelper.getValueByViewId(txtTakeAPhoto, txtUploadFrGallery, lblUserName, lblFullName, lblPhone, lblEmail, btnUpdate);
    }

    @Override
    public void onAttach(Context context) {
        AppCompatActivity activity;
        if (context instanceof AppCompatActivity) {
            activity = (AppCompatActivity) context;
            mMainActivity = (MainActivity) activity;
        }
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mMainActivity = null;
    }
}
