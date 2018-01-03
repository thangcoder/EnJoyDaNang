package node.com.enjoydanang.ui.fragment.review;

import android.content.DialogInterface;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.refactor.lib.colordialog.ColorDialog;
import node.com.enjoydanang.MvpFragment;
import node.com.enjoydanang.R;
import node.com.enjoydanang.annotation.DialogType;
import node.com.enjoydanang.api.model.Repository;
import node.com.enjoydanang.constant.AppError;
import node.com.enjoydanang.model.Partner;
import node.com.enjoydanang.model.PartnerAlbum;
import node.com.enjoydanang.model.Reply;
import node.com.enjoydanang.model.Review;
import node.com.enjoydanang.model.UserInfo;
import node.com.enjoydanang.ui.fragment.detail.dialog.DetailHomeDialogFragment;
import node.com.enjoydanang.ui.fragment.review.reply.ImagePreviewAdapter;
import node.com.enjoydanang.ui.fragment.review.reply.WriteReplyDialog;
import node.com.enjoydanang.ui.fragment.review.write.WriteReviewDialog;
import node.com.enjoydanang.utils.DialogUtils;
import node.com.enjoydanang.utils.ImageUtils;
import node.com.enjoydanang.utils.Utils;
import node.com.enjoydanang.utils.event.OnBackFragmentListener;
import node.com.enjoydanang.utils.event.OnItemClickListener;
import node.com.enjoydanang.utils.helper.EndlessScrollListener;
import node.com.enjoydanang.utils.helper.LanguageHelper;
import node.com.enjoydanang.utils.helper.SoftKeyboardManager;

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

    private ProgressBar prgLoadingReply;

    private int rowIndexClick;

    private Review currentReviewClick;

    private UserInfo userInfo;

    private int positionClickRemove;

    private int parentIndexOfReply;

    private int indexOfReplyRemove;

    public static ReviewFragment newInstance(Partner partner) {
        ReviewFragment fragment = new ReviewFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(TAG, partner);
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
        recyclerView.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        lstReviews = new ArrayList<>();
        mAdapter = new ReviewAdapter(lstReviews, lstReply, getContext(), this, this, this);
        recyclerView.setHasFixedSize(false);
        recyclerView.setAdapter(mAdapter);
        Bundle bundle = getArguments();
        if (bundle != null) {
            partner = (Partner) bundle.getParcelable(TAG);
            if (partner != null) {
                ImageUtils.loadImageWithFreso(imgPartner, partner.getPicture());
                txtPartnerName.setText(partner.getName());
            }
        }

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mvpPresenter = createPresenter();
        userInfo = Utils.getUserInfo();
        if (partner != null) {
            mvpPresenter.fetchReviewByPartner(partner.getId(), START_PAGE, userInfo.getCode());
        }
    }

    @Override
    protected void setEvent(View view) {
        recyclerView.addOnScrollListener(new EndlessScrollListener(mLayoutManager) {

            @Override
            public void onLoadMore(int page) {
                currentPage = page;
                onRetryGetListReview(page);
            }

        });
        lrlContentReview.setOnTouchListener(this);
    }

    @OnClick(R.id.txtAddReview)
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.txtAddReview:
                if (Utils.hasLogin()) {
                    if (partner != null) {
                        WriteReviewDialog dialog = WriteReviewDialog.newInstance(partner);
                        dialog.setOnBackListener(new OnBackFragmentListener() {
                            @Override
                            public void onBack(boolean isBack) {

                            }

                            @Override
                            public void onDismiss(DialogInterface dialog, boolean isBack, boolean isNeedRefresh) {
                                if (!isBack) {
                                    prgLoading.setVisibility(View.VISIBLE);
                                    lrlContentReview.setVisibility(View.GONE);
                                    mvpPresenter.refreshReviewByPartner(userInfo.getCode(), partner.getId(), START_PAGE);
                                }
                                if(isNeedRefresh){
                                    mvpPresenter.refreshReviewByPartner(userInfo.getCode(), partner.getId(), START_PAGE);
                                }
                                dialog.dismiss();
                            }
                        });
                        DialogUtils.openDialogFragment(mFragmentManager, dialog);
                    }
                } else {
                    DialogUtils.showDialog(getContext(), DialogType.WARNING, DialogUtils.getTitleDialog(2), Utils.getLanguageByResId(R.string.Message_You_Need_Login));
                }
                break;
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
        DetailHomeDialogFragment fragment = (DetailHomeDialogFragment) getParentFragment();
        if (fragment != null) {
            fragment.countGetResultFailed += 1;
            if (fragment.countGetResultFailed == 1) {
                DialogUtils.showDialog(getContext(), DialogType.WRONG, DialogUtils.getTitleDialog(3), error.getMessage());
            }
        }
    }

    @Override
    public void onFetchReplyByReview(Repository<Reply> data) {
        prgLoadingReply.setVisibility(View.GONE);
        if (CollectionUtils.isNotEmpty(lstReply.get(rowIndexClick))) {
            lstReply.get(rowIndexClick).clear();
        }
        lstReply.get(rowIndexClick).addAll(data.getData());
        mAdapter.notifyItemRangeChanged(0, lstReply.size());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefreshReviews(List<Review> models) {
        if (CollectionUtils.isEmpty(models) && currentPage == 0) {
            lrlContentReview.setVisibility(View.VISIBLE);
            prgLoading.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            txtEmpty.setVisibility(View.VISIBLE);
            return;
        }
        refreshItems(models);
        txtEmpty.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        lrlContentReview.setVisibility(View.VISIBLE);
        prgLoading.setVisibility(View.GONE);
    }

    @Override
    public void onRemoveSuccess() {
        mAdapter.removeAt(positionClickRemove);
    }

    @Override
    public void onRemoveReplySuccess() {
        mAdapter.removeReply(parentIndexOfReply, indexOfReplyRemove);
    }

    private void onRetryGetListReview(int page) {
        if (StringUtils.isNoneBlank(userInfo.getCode()) && partner != null) {
            mvpPresenter.fetchReviewByPartner(partner.getId(), page);
        }
    }

    public void updateItems(List<Review> lstReviews) {
        int oldSize = this.lstReviews.size();
        int newSize = lstReviews.size();
        this.lstReviews.addAll(lstReviews);
        mAdapter.notifyItemRangeChanged(0, oldSize + newSize);
        mAdapter.notifyDataSetChanged();
    }

    public void refreshItems(List<Review> lstReviews) {
        if (CollectionUtils.isNotEmpty(this.lstReviews)) {
            this.lstReviews.clear();
        }
        this.lstReviews.addAll(lstReviews);
        mAdapter.notifyItemRangeChanged(0, lstReviews.size());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(ProgressBar prgLoadingReply, View view, final int position, final int indexOfReview) {
        switch (view.getId()) {
            case R.id.txtRemoveReply:
                if (CollectionUtils.isNotEmpty(lstReply)) {
                    parentIndexOfReply = indexOfReview;
                    indexOfReplyRemove = position;
                    final Reply reply = lstReply.get(indexOfReview).get(position);
                    DialogUtils.showDialogConfirm(getContext(), DialogUtils.getTitleDialog(2),
                            Utils.getLanguageByResId(R.string.Delete),
                            Utils.getLanguageByResId(R.string.Message_Confirm_Ok),
                            Utils.getLanguageByResId(R.string.Message_Confirm_Cancel),
                            new ColorDialog.OnPositiveListener() {
                                @Override
                                public void onClick(ColorDialog colorDialog) {
                                    colorDialog.dismiss();
                                    mvpPresenter.removeReply(userInfo.getCode(), reply.getId());
                                }
                            }, new ColorDialog.OnNegativeListener() {
                                @Override
                                public void onClick(ColorDialog colorDialog) {
                                    colorDialog.dismiss();
                                }
                            }
                    );
                }
                break;
            case R.id.btnReply:
                rowIndexClick = position;
                currentReviewClick = lstReviews.get(position);
                this.prgLoadingReply = prgLoadingReply;
                prgLoadingReply.setVisibility(View.VISIBLE);
                mvpPresenter.fetchReplyByReviewId(userInfo.getCode(), lstReviews.get(position).getId(), START_PAGE);
                break;
        }
    }


    @Override
    public void initViewLabel(View view) {
        LanguageHelper.getValueByViewId(txtAddReview, txtEmpty);
    }

    @Override
    public void onImageClick(View view, int position, String url, ArrayList<PartnerAlbum> lstModel) {
        position = position >= lstModel.size() ? 0 : position;
        DialogUtils.showDialogAlbum(mFragmentManager, lstModel, position);
    }

    @Override
    public void onClick(View view, final int position) {
        switch (view.getId()) {
            case R.id.txtWriteReply:
                if (Utils.hasLogin()) {
                    if (partner != null) {
                        WriteReplyDialog dialog = WriteReplyDialog.newInstance(lstReviews.get(position), partner.getId());
                        dialog.setOnBackListener(new OnBackFragmentListener() {
                            @Override
                            public void onBack(boolean isBack) {

                            }

                            @Override
                            public void onDismiss(DialogInterface dialog, boolean isBack, boolean isNeedRefresh) {
                                if (!isBack) {
                                    prgLoadingReply.setVisibility(View.VISIBLE);
                                    lrlContentReview.setVisibility(View.GONE);
                                    mvpPresenter.fetchReplyByReviewId(currentReviewClick.getId(), START_PAGE);
                                }
                                if(isNeedRefresh){
                                    mvpPresenter.fetchReplyByReviewId(currentReviewClick.getId(), START_PAGE);
                                }
                                dialog.dismiss();
                            }
                        });
                        DialogUtils.openDialogFragment(mFragmentManager, dialog);
                    }
                } else {
                    DialogUtils.showDialog(getContext(), DialogType.WARNING, DialogUtils.getTitleDialog(2), Utils.getLanguageByResId(R.string.Message_You_Need_Login));
                }
                break;
            case R.id.txtRemoveReview:
                positionClickRemove = position;
                DialogUtils.showDialogConfirm(getContext(), DialogUtils.getTitleDialog(2),
                        Utils.getLanguageByResId(R.string.Delete),
                        Utils.getLanguageByResId(R.string.Message_Confirm_Ok),
                        Utils.getLanguageByResId(R.string.Message_Confirm_Cancel),
                        new ColorDialog.OnPositiveListener() {
                            @Override
                            public void onClick(ColorDialog colorDialog) {
                                colorDialog.dismiss();
                                mvpPresenter.removeReview(userInfo.getCode(), lstReviews.get(position).getId());
                            }
                        }, new ColorDialog.OnNegativeListener() {
                            @Override
                            public void onClick(ColorDialog colorDialog) {
                                colorDialog.dismiss();
                            }
                        }
                );
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        SoftKeyboardManager.hideSoftKeyboard(getContext(), v.getWindowToken(), 0);
        return false;
    }

}
