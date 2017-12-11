package node.com.enjoydanang.ui.fragment.profile;

import android.Manifest;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import node.com.enjoydanang.BuildConfig;
import node.com.enjoydanang.GlobalApplication;
import node.com.enjoydanang.MvpFragment;
import node.com.enjoydanang.R;
import node.com.enjoydanang.annotation.DialogType;
import node.com.enjoydanang.constant.AppError;
import node.com.enjoydanang.constant.Constant;
import node.com.enjoydanang.model.UserInfo;
import node.com.enjoydanang.ui.activity.main.MainActivity;
import node.com.enjoydanang.utils.DialogUtils;
import node.com.enjoydanang.utils.FileUtils;
import node.com.enjoydanang.utils.ImageUtils;
import node.com.enjoydanang.utils.SharedPrefsUtils;
import node.com.enjoydanang.utils.Utils;
import node.com.enjoydanang.utils.helper.LanguageHelper;
import node.com.enjoydanang.utils.helper.PhotoHelper;
import node.com.enjoydanang.utils.helper.SoftKeyboardManager;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;
import static node.com.enjoydanang.utils.helper.PhotoHelper.CAPTURE_IMAGE_REQUEST_CODE;
import static node.com.enjoydanang.utils.helper.PhotoHelper.PERMISSION_READ_EXTERNAL_CODE;
import static node.com.enjoydanang.utils.helper.PhotoHelper.SELECT_FROM_GALLERY_CODE;

/**
 * Author: Tavv
 * Created on 27/10/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public class ProfileFragment extends MvpFragment<ProfilePresenter> implements ProfileView, EasyPermissions.PermissionCallbacks
        , View.OnTouchListener {
    private static final String TAG = ProfileFragment.class.getSimpleName();
    public static final int PERMISSION_CAMERA = 200;

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
    @BindView(R.id.txtOr)
    TextView txtOr;
    @BindView(R.id.btnUpdate)
    AppCompatButton btnUpdate;

    @BindView(R.id.imgAvatarUser)
    SimpleDraweeView imgAvatarUser;

    @BindView(R.id.lrlUpdateProfile)
    LinearLayout lrlUpdateProfile;


    @BindView(R.id.lrlControlUpload)
    LinearLayout lrlControlUpload;

    private UserInfo userInfo;

    private PhotoHelper mPhotoHelper;

    private MainActivity mMainActivity;

    private File fileChoose;

    @Override
    public void showToast(String desc) {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

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
        lrlUpdateProfile.setOnTouchListener(this);
        lrlControlUpload.setOnTouchListener(this);
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
        hideLoading();
        if (userInfo != null) {
            this.userInfo = userInfo;
            GlobalApplication.setUserInfo(userInfo);
            saveUserInfo(userInfo);
            mMainActivity.refreshHeader();
            initData();
            DialogUtils.showDialog(getContext(), DialogType.SUCCESS, DialogUtils.getTitleDialog(1), Utils.getLanguageByResId(R.string.Update_Success));
        }
    }

    private void saveUserInfo(UserInfo userInfo) {
        if (userInfo != null) {
            Gson gson = new Gson();
            String strJsonUserInfo = gson.toJson(userInfo);
            SharedPrefsUtils.addDataToPrefs(Constant.SHARED_PREFS_NAME, Constant.KEY_EXTRAS_USER_INFO, strJsonUserInfo);
        }
    }

    @Override
    public void onUpdateFailure(AppError error) {
        hideLoading();
        DialogUtils.showDialog(getContext(), DialogType.WRONG, DialogUtils.getTitleDialog(3), error.getMessage());
    }

    @OnClick({R.id.btnUpdate, R.id.txtTakeAPhoto, R.id.txtUploadFrGallery})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnUpdate:
                if (StringUtils.isNotBlank(edtFullname.getText())) {
                    showLoading();
                    setValueAvatar();
                } else {
                    DialogUtils.showDialog(getContext(), DialogType.WARNING,
                            DialogUtils.getTitleDialog(2),
                            Utils.getLanguageByResId(R.string.Home_Account_Fullname_NotEmpty));
                }
                break;
            case R.id.txtTakeAPhoto:
                startCamera();
                break;
            case R.id.txtUploadFrGallery:
                openGallery();
                break;
        }

    }

    @AfterPermissionGranted(PERMISSION_CAMERA)
    private void startCamera() {
        if (EasyPermissions.hasPermissions(getContext(), Manifest.permission.CAMERA)) {
            cameraIntent();
        } else {
            EasyPermissions.requestPermissions(getActivity(),
                    Utils.getLanguageByResId(R.string.Request_Permission_Camera), PERMISSION_CAMERA,
                    Manifest.permission.CAMERA);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainActivity.setNameToolbar(Utils.getLanguageByResId(R.string.Home_Account_Profile).toUpperCase());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case CAPTURE_IMAGE_REQUEST_CODE:
                if (StringUtils.isNotBlank(mPhotoHelper.getCurrentPhotoPath())) {
                    File imgFile = new File(mPhotoHelper.getCurrentPhotoPath());
                    if (imgFile.exists()) {
                        updateAvatar(imgFile);
                    } else {
                        DialogUtils.showDialog(getContext(),
                                DialogType.WARNING, DialogUtils.getTitleDialog(2),
                                Utils.getLanguageByResId(R.string.Image_Not_Found));
                    }
                }
                break;
            case SELECT_FROM_GALLERY_CODE:
                if (data != null) {
                    Uri uri = data.getData();
                    String realPath = FileUtils.getPath(getContext(), uri);
                    if (StringUtils.isBlank(realPath)) {
                        Toast.makeText(mMainActivity, Utils.getLanguageByResId(R.string.Message_Warning_File), Toast.LENGTH_LONG).show();
                        return;
                    }
                    File file = new File(realPath);
                    updateAvatar(file);
                }
                break;
        }
    }

    private void updateAvatar(File file) {
        ImageUtils.getRightAngleImage(file.getAbsolutePath());
        Uri uri = FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".fileprovider", file);
        ImageUtils.loadImageWithFresoURI(imgAvatarUser, uri);
        fileChoose = file;
    }

    private void initData() {
        mPhotoHelper = new PhotoHelper(this);
        edtUserName.setText(userInfo.getUserName());
        edtFullname.setText(StringUtils.isEmpty(userInfo.getFullName()) ? "" : userInfo.getFullName());
        edtEmail.setText(StringUtils.isEmpty(userInfo.getEmail()) ? "" : userInfo.getEmail());
        edtPhone.setText(StringUtils.isEmpty(userInfo.getPhone()) ? "" : userInfo.getPhone());
        ImageUtils.loadImageWithFreso(imgAvatarUser, userInfo.getImage());
    }

    @Override
    public void initViewLabel(View view) {
        LanguageHelper.getValueByViewId(txtTakeAPhoto, txtUploadFrGallery, lblUserName, lblFullName, lblPhone, lblEmail, btnUpdate, txtOr);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        AppCompatActivity activity;
        if (context instanceof AppCompatActivity) {
            activity = (AppCompatActivity) context;
            mMainActivity = (MainActivity) activity;
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(getActivity(), perms)) {
            DialogUtils.showDialog(getContext(), DialogType.WARNING,
                    Utils.getLanguageByResId(R.string.Permisstion_Title),
                    Utils.getLanguageByResId(R.string.Permission_Request_Content));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    public MainActivity getParentActivity() {
        return mMainActivity;
    }

    public void cameraIntent() {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePicture.resolveActivity(getActivity().getPackageManager()) != null) {

            File photoFile = null;
            try {
                photoFile = mPhotoHelper.createImageFile();

            } catch (IOException e) {
                e.printStackTrace();
            }

            if (photoFile != null) {
                Uri u = FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID + ".fileprovider", photoFile);

                takePicture.putExtra(MediaStore.EXTRA_OUTPUT, u);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    takePicture.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    ClipData clip =
                            ClipData.newUri(getActivity().getContentResolver(), "A photo", u);

                    takePicture.setClipData(clip);
                    takePicture.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                } else {
                    List<ResolveInfo> resInfoList =
                            getActivity().getPackageManager()
                                    .queryIntentActivities(takePicture, PackageManager.MATCH_DEFAULT_ONLY);

                    for (ResolveInfo resolveInfo : resInfoList) {
                        String packageName = resolveInfo.activityInfo.packageName;
                        getActivity().grantUriPermission(packageName, u,
                                Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    }
                }
                startActivityForResult(takePicture, CAPTURE_IMAGE_REQUEST_CODE);
            }
        }
    }


    @AfterPermissionGranted(PERMISSION_READ_EXTERNAL_CODE)
    public void openGallery() {
        if (!isAdded()) return;
        if (Build.VERSION.SDK_INT >= 23) {
            Context context = getContext() == null ? getActivity() : getContext();
            if (EasyPermissions.hasPermissions(context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_FROM_GALLERY_CODE);
            } else {
                EasyPermissions.requestPermissions(this, Utils.getLanguageByResId(R.string.Request_Permission_Camera), PERMISSION_READ_EXTERNAL_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        } else {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, SELECT_FROM_GALLERY_CODE);
        }
    }

    private void setValueAvatar() {
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                if (fileChoose == null) {
                    subscriber.onNext(ImageUtils.getByteArrayFromImageURL(userInfo.getImage()));
                } else {
                    subscriber.onNext(ImageUtils.encodeTobase64(fileChoose));
                }
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + e.getMessage());
                    }

                    @Override
                    public void onNext(String base64) {
                        String endValue = StringUtils.isBlank(base64) ? StringUtils.EMPTY : base64;
                        mvpPresenter.updateProfile(userInfo.getUserId(),
                                String.valueOf(edtFullname.getText()),
                                String.valueOf(edtPhone.getText()),
                                String.valueOf(edtEmail.getText()),
                                endValue);
                    }
                });
    }

    public boolean isEmptyFullName() {
        return StringUtils.isEmpty(edtFullname.getText());
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.lrlUpdateProfile || v.getId() == R.id.lrlControlUpload) {
            SoftKeyboardManager.hideSoftKeyboard(getContext(), v.getWindowToken(), 0);
        }
        return true;
    }
}
