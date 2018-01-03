package node.com.enjoydanang.ui.fragment.review.reply;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.refactor.lib.colordialog.ColorDialog;
import cn.refactor.lib.colordialog.PromptDialog;
import node.com.enjoydanang.GlobalApplication;
import node.com.enjoydanang.R;
import node.com.enjoydanang.annotation.DialogType;
import node.com.enjoydanang.api.ApiCallback;
import node.com.enjoydanang.api.ApiStores;
import node.com.enjoydanang.api.model.Repository;
import node.com.enjoydanang.api.module.AppClient;
import node.com.enjoydanang.constant.AppError;
import node.com.enjoydanang.constant.Constant;
import node.com.enjoydanang.model.ImageData;
import node.com.enjoydanang.model.PartnerAlbum;
import node.com.enjoydanang.model.Reply;
import node.com.enjoydanang.model.Review;
import node.com.enjoydanang.model.ReviewImage;
import node.com.enjoydanang.model.UserInfo;
import node.com.enjoydanang.ui.activity.scan.ScanActivity;
import node.com.enjoydanang.utils.DialogUtils;
import node.com.enjoydanang.utils.FileUtils;
import node.com.enjoydanang.utils.ImageUtils;
import node.com.enjoydanang.utils.Utils;
import node.com.enjoydanang.utils.event.OnBackFragmentListener;
import node.com.enjoydanang.utils.event.OnItemClickListener;
import node.com.enjoydanang.utils.helper.EndlessScrollListener;
import node.com.enjoydanang.utils.helper.LanguageHelper;
import node.com.enjoydanang.utils.helper.PhotoHelper;
import node.com.enjoydanang.utils.helper.SoftKeyboardManager;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static android.app.Activity.RESULT_OK;

/**
 * Author: Tavv
 * Created on 09/11/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public class WriteReplyDialog extends DialogFragment implements View.OnTouchListener, OnItemClickListener,
        ImagePreviewAdapter.OnImageReviewClickListener {
    private static final String TAG = WriteReplyDialog.class.getSimpleName();
    private static final String KEY_EXTRAS_PARTNER_ID = "partner_id";

    private UserInfo userInfo;

    private Review review;

    private PhotoHelper mPhotoHelper;

    private OnBackFragmentListener onBackListener;

    private boolean isBack;

    private ImagePreviewAdapter mPreviewAdapter;

    private ImagePreviewAdapter mImageReviewAdapter;

    private ProgressDialog prgLoading;

    private CompositeSubscription mCompositeSubscription;

    private ApiStores apiStores;

    private static final int START_PAGE = 0;

    @BindView(R.id.imgAvatar)
    SimpleDraweeView imgAvatar;

    @BindView(R.id.txtReviewerName)
    TextView txtReviewerName;

    @BindView(R.id.txtNumRate)
    TextView txtNumRate;

    @BindView(R.id.txtTitleReview)
    TextView txtTitleReview;

    @BindView(R.id.txtContentReview)
    TextView txtContentReview;

    @BindView(R.id.imgExpanCollapseContent)
    ImageView imgExpanCollapseContent;

    @BindView(R.id.txtDate)
    TextView txtDate;

    @BindView(R.id.rcvImageReply)
    RecyclerView rcvImageReply;

    @BindView(R.id.rcvReplies)
    RecyclerView rcvReplies;

    @BindView(R.id.imgAvtCurrent)
    SimpleDraweeView imgAvtCurrent;

    @BindView(R.id.edtWriteReply)
    EditText edtWriteReply;

    @BindView(R.id.rcvImagePicked)
    RecyclerView rcvImagePicked;

    @BindView(R.id.lrlWriteReply)
    LinearLayout lrlWriteReply;

    @BindView(R.id.lrlReplies)
    LinearLayout lrlReplies;

    @BindView(R.id.name)
    TextView toolbarName;
    @BindView(R.id.edit_profile)
    TextView tvProfile;
    @BindView(R.id.img_scan)
    ImageView imgScan;
    @BindView(R.id.frToolBar)
    FrameLayout frToolBar;

    private ReplyAdapter replyAdapter;

    private List<Reply> lstReplies;

    private LinearLayoutManager replyLayoutManager;

    private List<ImageData> imageChoose;

    private int partnerId;

    private boolean isRefreshAfterSubmit;

    public void setOnBackListener(OnBackFragmentListener onBackListener) {
        this.onBackListener = onBackListener;
    }

    public static WriteReplyDialog newInstance(Review review, int partnerId) {
        WriteReplyDialog fragment = new WriteReplyDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG, review);
        bundle.putInt(KEY_EXTRAS_PARTNER_ID, partnerId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() == null) return;
        getDialog().getWindow().getAttributes().windowAnimations = R.style.dialog_animation_fade;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().setTitle(Utils.getLanguageByResId(R.string.WriteReview).toUpperCase());
        mCompositeSubscription = new CompositeSubscription();
        apiStores = AppClient.getClient().create(ApiStores.class);
        return inflater.inflate(R.layout.fragment_write_reply, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        showLoading(Utils.getLanguageByResId(R.string.Loading));
        mPhotoHelper = new PhotoHelper(this);
        lstReplies = new ArrayList<>();
        imageChoose = new ArrayList<>();
        replyAdapter = new ReplyAdapter(lstReplies, this, this);
        mPreviewAdapter = new ImagePreviewAdapter(imageChoose, getContext(), 120);
        rcvImagePicked.setAdapter(mPreviewAdapter);
        initLabelView();
        initToolbar();
        replyLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rcvReplies.setLayoutManager(replyLayoutManager);
        rcvReplies.setHasFixedSize(false);
        rcvReplies.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        rcvReplies.setAdapter(replyAdapter);

        initAdapter(rcvImagePicked, LinearLayoutManager.HORIZONTAL, true);
        initAdapter(rcvImageReply, LinearLayoutManager.HORIZONTAL, false);
        userInfo = Utils.getUserInfo();
        Bundle bundle = getArguments();
        if (bundle != null) {
            review = (Review) bundle.getSerializable(TAG);
            partnerId = bundle.getInt(KEY_EXTRAS_PARTNER_ID);
            if (review != null) {
                fetchContentReview(review);
                ImageUtils.loadImageWithFreso(imgAvtCurrent, GlobalApplication.getUserInfo().getImage());
                fetchReplies(userInfo.getCode(), review.getId(), START_PAGE);
            }
        }
        setEvents();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final RelativeLayout root = new RelativeLayout(getActivity());
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
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
    public boolean onTouch(View view, MotionEvent motionEvent) {
        SoftKeyboardManager.hideSoftKeyboard(getContext(), view.getWindowToken(), 0);
        return false;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onBackListener != null) {
            onBackListener.onDismiss(dialog, isBack);
        }
    }

    private void initLabelView() {
        LanguageHelper.getValueByViewId(edtWriteReply);
    }

    private void showLoading(String title) {
        if (prgLoading == null) {
            prgLoading = new ProgressDialog(getActivity());
            prgLoading.setMessage(title);
            prgLoading.setCancelable(false);
            prgLoading.show();
        } else {
            prgLoading.setMessage(title);
            if (!prgLoading.isShowing()) {
                prgLoading.show();
            }
        }
    }

    private void hideLoading() {
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

    private List<String> getListImageBase64(List<ImageData> images) {
        List<String> lstImageBase64 = new ArrayList<>();
        for (int i = 0; i < Constant.MAX_SIZE_GALLERY_SELECT; i++) {
            lstImageBase64.add(i, StringUtils.EMPTY);
        }
        if (CollectionUtils.isNotEmpty(images)) {
            int count = -1;
            for (ImageData item : images) {
                if (item.getUri() != null) {
                    count++;
                    File file = new File(FileUtils.getPath(getContext(), item.getUri()));
                    String strConvert = ImageUtils.encodeTobase64(file);
                    lstImageBase64.set(count, strConvert);
                }
            }
        }
        return lstImageBase64;
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

    private boolean isShowWarningLogin() {
        if (!Utils.hasLogin()) {
            DialogUtils.showDialog(getContext(), DialogType.WARNING, DialogUtils.getTitleDialog(2),
                    Utils.getLanguageByResId(R.string.Message_You_Need_Login));
            return true;
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PhotoHelper.SELECT_FROM_GALLERY_CODE && resultCode == RESULT_OK
                && null != data) {
            List<ImageData> images = mPhotoHelper.parseGalleryResult(data, Constant.MAX_SIZE_GALLERY_SELECT);
            clearDataImagePreview(images);
        }
    }

    @OnClick({R.id.btnSubmitReply, R.id.btnAttachImage})
    public void onSubmitClick(View view) {
        switch (view.getId()) {
            case R.id.btnAttachImage:
                mPhotoHelper.startGalleryIntent();
                break;
            case R.id.btnSubmitReply:
                writeReply();
                break;
        }
    }

    private void fetchReplies(String code, int reviewId, int page) {
        DialogUtils.showDialogConfirm(getContext(), DialogUtils.getTitleDialog(2),
                Utils.getLanguageByResId(R.string.Delete),
                Utils.getLanguageByResId(R.string.Message_Confirm_Ok),
                Utils.getLanguageByResId(R.string.Message_Confirm_Cancel),
                new ColorDialog.OnPositiveListener() {
                    @Override
                    public void onClick(ColorDialog colorDialog) {
                        colorDialog.dismiss();
                    }
                }, new ColorDialog.OnNegativeListener() {
                    @Override
                    public void onClick(ColorDialog colorDialog) {
                        colorDialog.dismiss();
                    }
                }
        );
        addSubscription(apiStores.getReplyByReviewId("LISTREPLYREVIEW", code, page, reviewId), new ApiCallback<Repository<Reply>>() {

            @Override
            public void onSuccess(Repository<Reply> model) {
                hideLoading();
                if (Utils.isResponseError(model)) {
                    DialogUtils.showDialog(getContext(), DialogType.WRONG, DialogUtils.getTitleDialog(3), model.getMessage());
                    return;
                }
                updateReplies(model.getData());
            }

            @Override
            public void onFailure(String msg) {
                hideLoading();
                DialogUtils.showDialog(getContext(), DialogType.WRONG, DialogUtils.getTitleDialog(3), msg);
            }

            @Override
            public void onFinish() {
                hideLoading();
            }
        });
    }

    private void removeReply(final int position){
        DialogUtils.showDialogConfirm(getContext(), DialogUtils.getTitleDialog(2),
                Utils.getLanguageByResId(R.string.Delete),
                Utils.getLanguageByResId(R.string.Message_Confirm_Ok),
                Utils.getLanguageByResId(R.string.Message_Confirm_Cancel),
                new ColorDialog.OnPositiveListener() {
                    @Override
                    public void onClick(ColorDialog colorDialog) {
                        colorDialog.dismiss();
                        showLoading(Utils.getLanguageByResId(R.string.Loading));
                        addSubscription(apiStores.removeReview("REMOVEREVIEW", userInfo.getCode(), lstReplies.get(position).getId()), new ApiCallback<Repository<Reply>>() {

                            @Override
                            public void onSuccess(Repository<Reply> model) {
                                hideLoading();
                                replyAdapter.removeAt(position);
                            }

                            @Override
                            public void onFailure(String msg) {
                                hideLoading();
                                DialogUtils.showDialog(getContext(), DialogType.WRONG, DialogUtils.getTitleDialog(3), msg);
                            }

                            @Override
                            public void onFinish() {
                                hideLoading();
                            }
                        });
                    }
                }, new ColorDialog.OnNegativeListener() {
                    @Override
                    public void onClick(ColorDialog colorDialog) {
                        colorDialog.dismiss();
                    }
                }
        );
    }

    @Override
    public void onClick(View view, int position) {
        switch (view.getId()){
            case R.id.txtRemoveReply :
                removeReply(position);
                break;
        }
    }

    private void initAdapter(RecyclerView recyclerView, int orientation, boolean rightToLeft) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), orientation, true);
        recyclerView.setLayoutManager(layoutManager);
        layoutManager.setReverseLayout(rightToLeft);
        DividerItemDecoration decoration = new DividerItemDecoration(getContext(), orientation);
        recyclerView.addItemDecoration(decoration);
        recyclerView.setHasFixedSize(false);
    }

    void writeReply() {
        if (!Utils.hasLogin() || review == null || partnerId == 0) return;
        showLoading(Utils.getLanguageByResId(R.string.Sending));
        MultipartBody.Part[] lstParts = getFilePartsRequest(mPreviewAdapter.getImages());
        final int reviewId = review.getId();
        long userId = GlobalApplication.getUserInfo().getUserId();
        String content = String.valueOf(edtWriteReply.getText());

        String title = StringUtils.EMPTY;
        if (StringUtils.isEmpty(content)) {
            DialogUtils.showDialog(getActivity(), DialogType.WRONG, DialogUtils.getTitleDialog(3), Utils.getLanguageByResId(R.string.Validate_Message_All_Field_Empty));
            return;
        }
        RequestBody contentBody = RequestBody.create(MediaType.parse("text/plain"), content);
        addSubscription(apiStores.postComment(0, userId, partnerId, reviewId, Constant.DEFAULT_RATING_STAR, title, contentBody,
                lstParts[0], lstParts[1], lstParts[2]), new ApiCallback<Repository>() {

            @Override
            public void onSuccess(Repository model) {
                if (Utils.isResponseError(model)) {
                    onWriteReplyFailure(new AppError(new Throwable(model.getMessage())));
                    return;
                }
                DialogUtils.showDialog(getContext(), DialogType.SUCCESS, DialogUtils.getTitleDialog(1),
                        Utils.getLanguageByResId(R.string.Review_Write_Reply_Success), new PromptDialog.OnPositiveListener() {
                            @Override
                            public void onClick(PromptDialog promptDialog) {
                                isRefreshAfterSubmit = true;
                                promptDialog.dismiss();
                                clearData();
                                fetchReplies(userInfo.getCode(), reviewId, START_PAGE);
                            }
                        });
            }

            @Override
            public void onFailure(String msg) {
                onWriteReplyFailure(new AppError(new Throwable(msg)));
            }

            @Override
            public void onFinish() {
                hideLoading();
            }
        });
    }

    private void fetchContentReview(final Review review) {
        if(!CollectionUtils.isEmpty(review.getImages())){
            List<ImageData> imgDatas = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(review.getImages())) {
                for (ReviewImage image : review.getImages()) {
                    imgDatas.add(new ImageData(null, null, image.getPicture()));
                }
            }
            mImageReviewAdapter = new ImagePreviewAdapter(imgDatas, getContext(), 150, this);
            rcvImageReply.setAdapter(mImageReviewAdapter);
            rcvImageReply.setVisibility(View.VISIBLE);
        }else{
            rcvImageReply.setVisibility(View.GONE);
            setLayoutWeight(lrlReplies, 0.7f);
        }
        if (StringUtils.isNotBlank(review.getContent())) {
            txtContentReview.setText(review.getContent());
        } else {
            txtContentReview.setVisibility(View.GONE);
        }
        txtNumRate.setText(String.valueOf(review.getStar()));
        txtReviewerName.setText(review.getName());
        txtTitleReview.setText(review.getTitle());
        txtDate.setText(review.getDate());
        ImageUtils.loadImageWithFreso(imgAvatar, review.getAvatar());
        if (txtContentReview.getLineCount() > 3) {
            imgExpanCollapseContent.setVisibility(View.VISIBLE);
            imgExpanCollapseContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (review.isExpanded()) {
                        review.setExpanded(false);
                        expand(txtContentReview, false);
                        imgExpanCollapseContent.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                    } else {
                        review.setExpanded(true);
                        expand(txtContentReview, true);
                        imgExpanCollapseContent.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                    }
                }
            });
        } else {
            imgExpanCollapseContent.setVisibility(View.GONE);
        }
    }

    private void expand(TextView textView, boolean collapse) {
        ObjectAnimator animation = null;
        if (collapse) {
            animation = ObjectAnimator.ofInt(
                    textView,
                    "maxLines",
                    10);
        } else {
            animation = ObjectAnimator.ofInt(
                    textView,
                    "maxLines",
                    3);
        }
        animation.setDuration(500);
        animation.start();
    }

    private void updateReplies(List<Reply> replies) {
//        if(hasLoadmore){
//            replyAdapter.setProgressMore(false);
//        }
        int oldSize = lstReplies.size();
        int newSize = 0;
        if (isRefreshAfterSubmit) {
            if (CollectionUtils.isNotEmpty(lstReplies)) {
                lstReplies.clear();
                replyAdapter.notifyItemRangeRemoved(0, oldSize);
            }
            lstReplies.addAll(replies);
            newSize = lstReplies.size();
        } else {
            newSize = oldSize + replies.size();
            lstReplies.addAll(replies);
        }
        replyAdapter.notifyItemRangeChanged(0, newSize);
        replyAdapter.notifyDataSetChanged();
        if (CollectionUtils.isEmpty(lstReplies)) {
            rcvReplies.setVisibility(View.GONE);
        } else {
            rcvReplies.setVisibility(View.VISIBLE);
        }
    }

    private void setEvents() {
        rcvReplies.addOnScrollListener(new EndlessScrollListener(replyLayoutManager) {

            @Override
            public void onLoadMore(int page) {
//                hasLoadmore = true;
//                replyAdapter.setProgressMore(true);
                isRefreshAfterSubmit = false;
                fetchReplies(userInfo.getCode(), review.getId(), page);
            }
        });
        lrlWriteReply.setOnTouchListener(this);
    }

    private void initToolbar() {
        setHeightToolbar();
        tvProfile.setVisibility(View.GONE);
        imgScan.setVisibility(View.VISIBLE);
    }


    @Override
    public void onImageClick(View view, int position, String url, ArrayList<PartnerAlbum> lstModel) {
        position = position >= lstModel.size() ? 0 : position;
        DialogUtils.showDialogAlbum(getFragmentManager(), lstModel, position);
    }


    private void clearData() {
        clearDataImagePreview(new ArrayList<ImageData>());
        Utils.clearForm(edtWriteReply);
    }

    private void clearDataImagePreview(List<ImageData> lstData) {
        int oldSize = imageChoose.size();
        if (CollectionUtils.isNotEmpty(imageChoose)) {
            imageChoose.clear();
            mPreviewAdapter.notifyItemRangeRemoved(0, oldSize);
        }
        imageChoose.addAll(lstData);
        mPreviewAdapter.notifyItemRangeChanged(0, imageChoose.size());
        mPreviewAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.img_scan, R.id.img_back})
    public void onMenuOptionsClick(View view) {
        switch (view.getId()) {
            case R.id.img_scan:
                if (Utils.hasLogin()) {
                    startActivity(new Intent(getActivity(), ScanActivity.class));
                    getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                } else {
                    DialogUtils.showDialog(getActivity(), DialogType.WARNING, DialogUtils.getTitleDialog(2), Utils.getLanguageByResId(R.string.Message_You_Need_Login));
                }
                break;
            case R.id.img_back:
                isBack = true;
                dismiss();
                break;
        }
    }

    private void setHeightToolbar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            frToolBar.setPadding(0, Utils.getStatusBarHeight(), 0, 0);
        }
    }

    private void onWriteReplyFailure(AppError appError) {
        hideLoading();
        DialogUtils.showDialog(getContext(), DialogType.WRONG, DialogUtils.getTitleDialog(3), appError.getMessage());
    }

    private void setLayoutWeight(LinearLayout relativeLayout, float weight) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, 0, weight);
        relativeLayout.setLayoutParams(layoutParams);
    }
}
