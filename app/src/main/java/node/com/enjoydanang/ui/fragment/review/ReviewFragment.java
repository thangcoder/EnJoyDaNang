package node.com.enjoydanang.ui.fragment.review;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import node.com.enjoydanang.GlobalApplication;
import node.com.enjoydanang.MvpFragment;
import node.com.enjoydanang.R;
import node.com.enjoydanang.annotation.DialogType;
import node.com.enjoydanang.api.model.Repository;
import node.com.enjoydanang.constant.AppError;
import node.com.enjoydanang.constant.Constant;
import node.com.enjoydanang.model.ImageData;
import node.com.enjoydanang.model.Partner;
import node.com.enjoydanang.model.PartnerAlbum;
import node.com.enjoydanang.model.Reply;
import node.com.enjoydanang.model.Review;
import node.com.enjoydanang.ui.fragment.review.reply.ImagePreviewAdapter;
import node.com.enjoydanang.ui.fragment.review.write.WriteReviewDialog;
import node.com.enjoydanang.utils.DialogUtils;
import node.com.enjoydanang.utils.FileUtils;
import node.com.enjoydanang.utils.ImageUtils;
import node.com.enjoydanang.utils.Utils;
import node.com.enjoydanang.utils.event.OnBackFragmentListener;
import node.com.enjoydanang.utils.event.OnItemClickListener;
import node.com.enjoydanang.utils.helper.EndlessRecyclerViewScrollListener;
import node.com.enjoydanang.utils.helper.LanguageHelper;
import node.com.enjoydanang.utils.helper.PhotoHelper;
import node.com.enjoydanang.utils.helper.SoftKeyboardManager;

import static android.app.Activity.RESULT_OK;

/**
 * Author: Tavv
 * Created on 10/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class ReviewFragment extends MvpFragment<ReviewPresenter> implements iReviewView, OnItemClickListener,
        ImagePreviewAdapter.OnImageReviewClickListener, ReviewAdapter.OnReplyClickListener,
        View.OnTouchListener {
    private static final String TAG = ReviewFragment.class.getSimpleName();

    @BindView(R.id.rcv_review)
    RecyclerView recyclerView;

    @BindView(R.id.imgPartner2)
    SimpleDraweeView imgPartner;

    @BindView(R.id.txtEmpty)
    TextView txtEmpty;

    @BindView(R.id.txtPartnerName)
    TextView txtPartnerName;

    @BindView(R.id.txtAddReview)
    TextView txtAddReview;

    @BindView(R.id.progress_bar)
    ProgressBar prgLoading;

    @BindView(R.id.lrlContentReview)
    LinearLayout lrlContentReview;

    private ReviewAdapter mAdapter;

    private final int START_PAGE = 0;

    private int currentPage;

    private Partner partner;

    private LinearLayoutManager mLayoutManager;

    private List<Review> lstReviews;
    private List<List<Reply>> lstReply;
    private List<List<ImageData>> lstImageSelected;

    private ProgressBar prgLoadingReply;

    private int rowIndexClick;

    private PhotoHelper mPhotoHelper;

    public static ReviewFragment newInstance(Partner partner) {
        ReviewFragment fragment = new ReviewFragment();
        Bundle bundle = new Bundle();
        //TODO : Remove hardcode Id
        partner.setId(91);
        bundle.putSerializable(TAG, partner);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected ReviewPresenter createPresenter() {
        return new ReviewPresenter(this);
    }

    @Override
    protected void init(View view) {
        lstReply = new ArrayList<>();
        lstImageSelected = new ArrayList<>();
        mPhotoHelper = new PhotoHelper(this);
        recyclerView.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        lstReviews = new ArrayList<>();
        mAdapter = new ReviewAdapter(lstReviews, lstReply, lstImageSelected, getContext(), this, this, this);
        recyclerView.setHasFixedSize(false);
        recyclerView.setAdapter(mAdapter);
        Bundle bundle = getArguments();
        if (bundle != null) {
            partner = (Partner) bundle.getSerializable(TAG);
            if (partner != null) {
//                Glide.with(getActivity()).load(partner.getPicture()).crossFade()
//                        .skipMemoryCache(false).placeholder(R.drawable.placeholder)
//                        .into(imgPartner);
                ImageUtils.loadImageWithFreso(imgPartner, partner.getPicture());
                txtPartnerName.setText(partner.getName());
            }
        }

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mvpPresenter = createPresenter();
        if (partner != null) {
            mvpPresenter.fetchReviewByPartner(partner.getId(), START_PAGE);
        }
    }

    @Override
    protected void setEvent(View view) {
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(mLayoutManager) {

            @Override
            public int getFooterViewType(int defaultNoFooterViewType) {
                return 1;
            }

            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                currentPage = page;
                onRetryGetListReview(page);
            }
        });
        lrlContentReview.setOnTouchListener(this);
    }

    @OnClick(R.id.txtAddReview)
    void onClick(View view) {
        if (Utils.hasLogin()) {

            if (partner != null) {
                WriteReviewDialog dialog = WriteReviewDialog.newInstance(partner);
//            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//                @Override
//                public void onDismiss(DialogInterface dialog) {
//                    prgLoading.setVisibility(View.VISIBLE);
//                    lrlContentReview.setVisibility(View.GONE);
//                    dialog.dismiss();
//                    mvpPresenter.fetchReviewByPartner(partner.getId(), START_PAGE);
//                }
//            });
                dialog.setOnBackListener(new OnBackFragmentListener() {
                    @Override
                    public void onBack(boolean isBack) {

                    }

                    @Override
                    public void onDismiss(DialogInterface dialog, boolean isBack) {
                        if (!isBack) {
                            prgLoading.setVisibility(View.VISIBLE);
                            lrlContentReview.setVisibility(View.GONE);
                            mvpPresenter.fetchReviewByPartner(partner.getId(), START_PAGE);
                        }
                        dialog.dismiss();
                    }
                });
                dialog.show(mFragmentManager, TAG);
            }
        } else {
            DialogUtils.showDialog(getContext(), DialogType.WARNING, DialogUtils.getTitleDialog(2), Utils.getLanguageByResId(R.string.Message_You_Need_Login));
        }
    }

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_review_v2;
    }

    @Override
    public void bindView(View view) {
        ButterKnife.bind(this, view);
    }

    @Override
    public void showToast(String desc) {

    }

    @Override
    public void unKnownError() {

    }

    @Override
    public void onFetchReviews(List<Review> models) {
        if (CollectionUtils.isEmpty(models) && currentPage == 0) {
            lrlContentReview.setVisibility(View.VISIBLE);
            prgLoading.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            txtEmpty.setVisibility(View.VISIBLE);
            return;
        }
        updateItems(models);
        txtEmpty.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        lrlContentReview.setVisibility(View.VISIBLE);
        prgLoading.setVisibility(View.GONE);
    }

    @Override
    public void onFetchFailure(AppError error) {
        DialogUtils.showDialog(getContext(), DialogType.WRONG, DialogUtils.getTitleDialog(3), error.getMessage());
    }

    @Override
    public void onFetchReplyByReview(Repository<Reply> data) {
        prgLoadingReply.setVisibility(View.GONE);
        lstReply.get(rowIndexClick).addAll(data.getData());
        mAdapter.notifyItemRangeChanged(0, lstReply.size());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onWriteReplySuccess(Repository repository) {

    }

    private void onRetryGetListReview(int page) {
        mvpPresenter.fetchReviewByPartner(partner.getId(), page);
    }

    public void updateItems(List<Review> lstReviews) {
        int oldSize = this.lstReviews.size();
        int newSize = lstReviews.size();
        this.lstReviews.addAll(lstReviews);
        mAdapter.notifyItemRangeChanged(0, oldSize + newSize);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(ProgressBar prgLoadingReply, View view, final int position) {
        rowIndexClick = position;
        this.prgLoadingReply = prgLoadingReply;
        prgLoadingReply.setVisibility(View.VISIBLE);
        mvpPresenter.fetchReplyByReviewId(lstReviews.get(position).getId(), START_PAGE);
    }

    @Override
    public void onSubmitClick(View view, String content , int position) {
        rowIndexClick = position;
        switch (view.getId()) {
            case R.id.btnAttachImage:
                mPhotoHelper.startGalleryIntent();
                break;
            case R.id.btnSubmitReply:
                if (CollectionUtils.isNotEmpty(lstReviews)) {
                    if (!Utils.hasLogin() || partner == null) return;
                    List<String> lstBase64 = getListImageBase64(position);
                    int reviewId = lstReviews.get(position).getId();
                    long userId = GlobalApplication.getUserInfo().getUserId();
                    String userName = GlobalApplication.getUserInfo().getFullName();
                    if (StringUtils.isEmpty(content)) {
                        DialogUtils.showDialog(getActivity(), DialogType.WRONG, DialogUtils.getTitleDialog(3), Utils.getLanguageByResId(R.string.Validate_Message_All_Field_Empty));
                        return;
                    }
                    mvpPresenter.writeReply(reviewId, userId, partner.getId(),
                            content, 0, userName, lstBase64.get(0), lstBase64.get(1), lstBase64.get(3));
                }
                break;
        }
    }

    @Override
    public void initViewLabel(View view) {
        LanguageHelper.getValueByViewId(txtAddReview, txtEmpty);
    }

    @Override
    public void onImageClick(View view, int position, String url, ArrayList<PartnerAlbum> lstModel) {
        DialogUtils.showDialogAlbum(mFragmentManager, lstModel, position);
    }

    @Override
    public void onClick(View view, int position) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        SoftKeyboardManager.hideSoftKeyboard(getContext(), v.getWindowToken(), 0);
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PhotoHelper.SELECT_FROM_GALLERY_CODE && resultCode == RESULT_OK
                && null != data) {
            List<ImageData> images = mPhotoHelper.parseGalleryResult(data, Constant.MAX_SIZE_GALLERY_SELECT);
            lstImageSelected.get(rowIndexClick).addAll(images);
            mAdapter.notifyDataSetChanged();
        }
    }

    private List<String> getListImageBase64(int position){
        List<String> lstImageBase64 = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(lstImageSelected.get(position))) {
            for (int i = 0; i < Constant.MAX_SIZE_GALLERY_SELECT; i++) {
                lstImageBase64.add(i, null);
            }
            int count = -1;
            for (ImageData item : lstImageSelected.get(position)) {
                if (item.getUri() != null) {
                    count++;
                    File file = new File(FileUtils.getFilePath(getContext(), item.getUri()));
                    String strConvert = ImageUtils.encodeTobase64(file);
                    lstImageBase64.add(count, strConvert);
                }
            }
        }
        return lstImageBase64;
    }
}
