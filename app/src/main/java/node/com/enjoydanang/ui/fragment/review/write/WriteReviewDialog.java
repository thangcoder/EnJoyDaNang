package node.com.enjoydanang.ui.fragment.review.write;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.refactor.lib.colordialog.PromptDialog;
import node.com.enjoydanang.GlobalApplication;
import node.com.enjoydanang.R;
import node.com.enjoydanang.annotation.DialogType;
import node.com.enjoydanang.api.ApiCallback;
import node.com.enjoydanang.api.ApiStores;
import node.com.enjoydanang.api.model.Repository;
import node.com.enjoydanang.api.module.AppClient;
import node.com.enjoydanang.constant.Constant;
import node.com.enjoydanang.model.ImageData;
import node.com.enjoydanang.model.Partner;
import node.com.enjoydanang.model.UserInfo;
import node.com.enjoydanang.ui.fragment.review.reply.ImagePreviewAdapter;
import node.com.enjoydanang.utils.DialogUtils;
import node.com.enjoydanang.utils.FileUtils;
import node.com.enjoydanang.utils.Utils;
import node.com.enjoydanang.utils.event.OnBackFragmentListener;
import node.com.enjoydanang.utils.helper.LanguageHelper;
import node.com.enjoydanang.utils.helper.PhotoHelper;
import node.com.enjoydanang.utils.helper.SoftKeyboardManager;
import okhttp3.MultipartBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static android.app.Activity.RESULT_OK;

/**
 * Author: Tavv
 * Created on 29/10/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public class WriteReviewDialog extends DialogFragment implements View.OnTouchListener {
    private static final String TAG = WriteReviewDialog.class.getSimpleName();

    @BindView(R.id.lrlWriteReview)
    LinearLayout lrlWriteReview;

    @BindView(R.id.ratingBar)
    AppCompatRatingBar ratingBar;

    @BindView(R.id.edtName)
    EditText edtName;

    @BindView(R.id.edtTitle)
    EditText edtTitle;

    @BindView(R.id.edtAriaContent)
    EditText edtAriaContent;

    @BindView(R.id.rcvAttachImgPreview)
    RecyclerView rcvAttachImgPreview;

    @BindView(R.id.lblFullName)
    TextView lbFullName;

    @BindView(R.id.lblTitle)
    TextView lblTitle;

    @BindView(R.id.btnSubmitReview)
    AppCompatButton btnSubmitReview;

    @BindView(R.id.lblContent)
    TextView lblContent;

    private UserInfo userInfo;

    private Partner partner;

    private PhotoHelper mPhotoHelper;

    private OnBackFragmentListener onBackListener;

    private boolean isBack;

    private ImagePreviewAdapter mPreviewAdapter;

    private ProgressDialog prgLoading;

    private CompositeSubscription mCompositeSubscription;

    private ApiStores apiStores;

    private static final int START_PAGE = 0;

    private List<ImageData> images;

    public void setOnBackListener(OnBackFragmentListener onBackListener) {
        this.onBackListener = onBackListener;
    }

    public static WriteReviewDialog newInstance(Partner partner) {
        WriteReviewDialog fragment = new WriteReviewDialog();
        Bundle bundle = new Bundle();
        bundle.putParcelable(TAG, partner);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().setTitle(Utils.getLanguageByResId(R.string.WriteReview).toUpperCase());
        mCompositeSubscription = new CompositeSubscription();
        apiStores = AppClient.getClient().create(ApiStores.class);
        return inflater.inflate(R.layout.fragment_write_review, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mPhotoHelper = new PhotoHelper(this);
        initLabelView();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rcvAttachImgPreview.setLayoutManager(layoutManager);
        rcvAttachImgPreview.setHasFixedSize(false);
        images = new ArrayList<>();
        mPreviewAdapter = new ImagePreviewAdapter(images, getContext());
        rcvAttachImgPreview.setAdapter(mPreviewAdapter);
        userInfo = GlobalApplication.getUserInfo();
        if (Utils.hasLogin()) {
            edtName.setText(userInfo.getFullName());
        }
        Bundle bundle = getArguments();
        if (bundle != null) {
            partner = (Partner) bundle.getParcelable(TAG);
        }
        edtAriaContent.setOnTouchListener(this);
        lrlWriteReview.setOnTouchListener(this);
    }


    @OnClick({R.id.btnSubmitReview, R.id.btnAttachImage})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSubmitReview:
                writeReview();
                break;
            case R.id.btnAttachImage:
                mPhotoHelper.startGalleryIntent();
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() == null) return;
        getDialog().getWindow().getAttributes().windowAnimations = R.style.dialog_animation_fade;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final RelativeLayout root = new RelativeLayout(getActivity());
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

//         creating the fullscreen dialog
        final Dialog dialog = new Dialog(getActivity(), getTheme()) {
            @Override
            public void onBackPressed() {
                isBack = true;
                dismiss();
            }
        };
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(root);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return dialog;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.lrlWriteReview) {
            SoftKeyboardManager.hideSoftKeyboard(getContext(), v.getWindowToken(), 0);
        } else {
            v.getParent().requestDisallowInterceptTouchEvent(true);
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_UP:
                    v.getParent().requestDisallowInterceptTouchEvent(false);
                    break;
            }
        }
        return false;
    }

    private void writeReview() {
        String name = String.valueOf(edtName.getText());
        String title = String.valueOf(edtTitle.getText());
        String content = String.valueOf(edtAriaContent.getText());
        float ratingCount = ratingBar.getRating();
        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(title) || StringUtils.isEmpty(content) || ratingCount == 0) {
            DialogUtils.showDialog(getActivity(), DialogType.WRONG, DialogUtils.getTitleDialog(3), Utils.getLanguageByResId(R.string.Validate_Message_All_Field_Empty));
            return;
        }
        if (partner != null) {
            showSending();
            long userId = Utils.hasLogin() ? userInfo.getUserId() : 0;
            MultipartBody.Part[] lstParts = null;
            if (mPreviewAdapter != null) {
                if (CollectionUtils.isNotEmpty(mPreviewAdapter.getImages())) {
                    lstParts = getFilePartsRequest(mPreviewAdapter.getImages());
                }
            }
            addSubscription(apiStores.postComment(0, userId, partner.getId(), 0,
                    (int) ratingCount, title, content,
                    lstParts[0], lstParts[1], lstParts[2]), new ApiCallback<Repository>() {
                @Override
                public void onSuccess(Repository model) {
                    if (Utils.isResponseError(model)) {
                        DialogUtils.showDialog(getActivity(), DialogType.WRONG, DialogUtils.getTitleDialog(3), model.getMessage());
                        return;
                    }
                    DialogUtils.showDialog(getActivity(), DialogType.SUCCESS, DialogUtils.getTitleDialog(1), Utils.getLanguageByResId(R.string.Dialog_Title_Success), new PromptDialog.OnPositiveListener() {
                        @Override
                        public void onClick(PromptDialog promptDialog) {
                            promptDialog.dismiss();
                            dismiss();
                        }
                    });
                }

                @Override
                public void onFailure(String msg) {
                    hideSending();
                    DialogUtils.showDialog(getActivity(), DialogType.WRONG, DialogUtils.getTitleDialog(3),
                            Utils.getLanguageByResId(R.string.Message_Add_Review_Failed));
                }

                @Override
                public void onFinish() {
                    hideSending();
                }
            });
        }
    }

    private void fetchReply(int reviewId) {
        addSubscription(apiStores.getReplyByReviewId(START_PAGE, reviewId), new ApiCallback<Repository>() {

            @Override
            public void onSuccess(Repository model) {
                Log.i(TAG, "onSuccess " + model);
            }

            @Override
            public void onFailure(String msg) {
                DialogUtils.showDialog(getActivity(), DialogType.WRONG, DialogUtils.getTitleDialog(3), msg);
            }

            @Override
            public void onFinish() {

            }
        });
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onBackListener != null) {
            onBackListener.onDismiss(dialog, isBack);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PhotoHelper.SELECT_FROM_GALLERY_CODE && resultCode == RESULT_OK
                && null != data) {
            if (CollectionUtils.isNotEmpty(images)) {
                images.clear();
            }
            images.addAll(mPhotoHelper.parseGalleryResult(data, Constant.MAX_SIZE_GALLERY_SELECT));
            mPreviewAdapter.notifyItemRangeChanged(0, images.size());
            mPreviewAdapter.notifyDataSetChanged();
        }
    }

    private void initLabelView() {
        LanguageHelper.getValueByViewId(lbFullName, lblContent, lblTitle, btnSubmitReview);
    }

    private void showSending() {
        if (prgLoading == null) {
            prgLoading = new ProgressDialog(getActivity());
            prgLoading.setMessage(Utils.getLanguageByResId(R.string.Sending));
            prgLoading.setCancelable(false);
            prgLoading.show();
        } else {
            if (!prgLoading.isShowing()) {
                prgLoading.show();
            }
        }
    }

    private void hideSending() {
        if (prgLoading != null) {
            if (prgLoading.isShowing()) {
                prgLoading.dismiss();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        onUnsubscribe();
    }

    public void onUnsubscribe() {
        if (mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.clear();
        }
    }

    public void addSubscription(Observable observable, Subscriber subscriber) {

        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }

        mCompositeSubscription.add(observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    private MultipartBody.Part[] getFilePartsRequest(List<ImageData> images) {
        MultipartBody.Part[] arrMultipart = new MultipartBody.Part[]{null, null, null};
        if (CollectionUtils.isNotEmpty(images)) {
            int count = -1;
            for (ImageData item : images) {
                if (item.getUri() != null) {
                    count++;
                    File file = new File(FileUtils.getPath(getContext(), item.getUri()));
                    if (file != null) {
                        MultipartBody.Part part = Utils.createContentBody(file);
                        arrMultipart[count] = part;
                    }
                }
            }
        }
        return arrMultipart;
    }
}
