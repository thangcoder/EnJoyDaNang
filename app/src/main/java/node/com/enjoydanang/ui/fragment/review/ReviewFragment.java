package node.com.enjoydanang.ui.fragment.review;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import node.com.enjoydanang.MvpFragment;
import node.com.enjoydanang.R;
import node.com.enjoydanang.annotation.DialogType;
import node.com.enjoydanang.api.model.Repository;
import node.com.enjoydanang.constant.AppError;
import node.com.enjoydanang.model.Partner;
import node.com.enjoydanang.model.PartnerAlbum;
import node.com.enjoydanang.model.Review;
import node.com.enjoydanang.ui.fragment.review.reply.ImagePreviewAdapter;
import node.com.enjoydanang.ui.fragment.review.write.WriteReviewDialog;
import node.com.enjoydanang.utils.DialogUtils;
import node.com.enjoydanang.utils.Utils;
import node.com.enjoydanang.utils.event.OnBackFragmentListener;
import node.com.enjoydanang.utils.event.OnItemClickListener;
import node.com.enjoydanang.utils.helper.EndlessRecyclerViewScrollListener;
import node.com.enjoydanang.utils.helper.LanguageHelper;

/**
 * Author: Tavv
 * Created on 10/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class ReviewFragment extends MvpFragment<ReviewPresenter> implements iReviewView, OnItemClickListener,
        ImagePreviewAdapter.OnImageReviewClickListener {
    private static final String TAG = ReviewFragment.class.getSimpleName();

    @BindView(R.id.rcv_review)
    RecyclerView recyclerView;

    @BindView(R.id.imgPartner2)
    ImageView imgPartner;

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

    private LinearLayoutManager mLayoutManager;

    private Partner partner;

    private List<Review> lstReviews;


    public static ReviewFragment newInstance(Partner partner) {
        ReviewFragment fragment = new ReviewFragment();
        Bundle bundle = new Bundle();
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
        recyclerView.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        lstReviews = new ArrayList<>();
        mAdapter = new ReviewAdapter(lstReviews, getContext(), this, this);
        recyclerView.setAdapter(mAdapter);
        Bundle bundle = getArguments();
        if (bundle != null) {
            partner = (Partner) bundle.getSerializable(TAG);
            if (partner != null) {
                Glide.with(getActivity()).load(partner.getPicture()).crossFade()
                        .skipMemoryCache(false).placeholder(R.drawable.placeholder)
                        .into(imgPartner);
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
//        if (CollectionUtils.isEmpty(models) && currentPage == 0) {
//            lrlContentReview.setVisibility(View.VISIBLE);
//            prgLoading.setVisibility(View.GONE);
//            recyclerView.setVisibility(View.GONE);
//            txtEmpty.setVisibility(View.VISIBLE);
//            return;
//        }
//        updateItems(models);
//        txtEmpty.setVisibility(View.GONE);
//        recyclerView.setVisibility(View.VISIBLE);
//        lrlContentReview.setVisibility(View.VISIBLE);
//        prgLoading.setVisibility(View.GONE);

        updateItems(DummyDataReview.dummyData());
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
    public void onFetchReplyByReview(Repository data) {

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
    public void onClick(View view, int position) {
        if (!Utils.hasLogin()) {
            DialogUtils.showDialog(getContext(), DialogType.WARNING, DialogUtils.getTitleDialog(2), Utils.getLanguageByResId(R.string.Message_You_Need_Login));
        }
    }

    @Override
    public void initViewLabel(View view) {
        super.initViewLabel(view);
        LanguageHelper.getValueByViewId(txtAddReview, txtEmpty);
    }

    @Override
    public void onImageClick(View view, int position, String url, ArrayList<PartnerAlbum> lstModel) {
        DialogUtils.showDialogAlbum(mFragmentManager, lstModel, position);
    }
}
