package node.com.enjoydanang.ui.fragment.profile;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

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
import node.com.enjoydanang.ui.activity.main.MainActivity;
import node.com.enjoydanang.ui.fragment.home.HomeTab;
import node.com.enjoydanang.utils.ImageUtils;
import node.com.enjoydanang.utils.Utils;
import node.com.enjoydanang.utils.helper.PhotoHelper;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;

/**
 * Author: Tavv
 * Created on 27/10/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public class ProfileFragment extends MvpFragment<ProfilePresenter> implements ProfileView {
    private static final String TAG = ProfileFragment.class.getSimpleName();
    public static String uriImageCapture;
    public static String base64Image;


    @BindView(R.id.edtUserName)
    EditText edtUserName;

    @BindView(R.id.edtFullname)
    EditText edtFullname;

    @BindView(R.id.edtPhone)
    EditText edtPhone;

    @BindView(R.id.edtEmail)
    EditText edtEmail;

    @BindView(R.id.imgAvatarUser)
//    CircleImageView imgAvatarUser;
     SimpleDraweeView imgAvatarUser;

    private UserInfo userInfo;

    private PhotoHelper mPhotoHelper;

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
            initData();
            Toast.makeText(getActivity(), "Câp nhật thành công", Toast.LENGTH_SHORT).show();
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
                //// TODO: Handle something here !!! btnUpdate onClick
                    mvpPresenter.updateProfile(userInfo.getUserId(),
                                String.valueOf(edtFullname.getText()),
                                String.valueOf(edtPhone.getText()),
                                String.valueOf(edtEmail.getText()),
                                base64Image);
                break;
            case R.id.txtTakeAPhoto:
                mPhotoHelper.openCamera();
                break;
            case R.id.txtUploadFrGallery:
                mPhotoHelper.openGallery();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mBaseActivity.getToolbar().setTitle(Utils.getString(R.string.Update_Profile_Screen_Title));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case PhotoHelper.CAPTURE_IMAGE_REQUEST_CODE:
                File imgFile = new File(uriImageCapture);
                if (imgFile.exists()) {
                    updateAvatar(imgFile);
                } else {
                    Toast.makeText(getActivity(), "Không tìm thấy ảnh", Toast.LENGTH_SHORT).show();
                }

                break;
            case PhotoHelper.SELECT_FROM_GALLERY_CODE:
                if (data != null) {
                    Uri uri = data.getData();
                    File finalFile = new File(mPhotoHelper.getRealPathFromURI(uri));
                    updateAvatar(finalFile);

                }
                break;
        }
    }
    private void updateAvatar(File file) {
        base64Image = ImageUtils.encodeTobase64(file);
        Uri uri = Uri.fromFile(file);
        ImageUtils.loadImageWithFresoURI(imgAvatarUser,uri );
    }
    private void initData(){
        mPhotoHelper = PhotoHelper.newInstance(this);
        edtUserName.setText(userInfo.getUserName());
        edtFullname.setText(StringUtils.isEmpty(userInfo.getFullName()) ? "" : userInfo.getFullName());
        edtEmail.setText(StringUtils.isEmpty(userInfo.getEmail()) ? "" : userInfo.getEmail());
        edtPhone.setText(StringUtils.isEmpty(userInfo.getPhone()) ? "" : userInfo.getPhone());
//        ImageUtils.loadImageNoRadius(getContext(), imgAvatarUser, userInfo.getImage());
        ImageUtils.loadImageWithFreso(imgAvatarUser, userInfo.getImage());
    }
}
