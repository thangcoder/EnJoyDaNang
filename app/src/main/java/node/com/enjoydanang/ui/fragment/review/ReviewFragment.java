package node.com.enjoydanang.ui.fragment.review;

import android.media.Image;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import node.com.enjoydanang.MvpFragment;
import node.com.enjoydanang.MvpFragmentWithToolbar;
import node.com.enjoydanang.R;
import node.com.enjoydanang.api.model.Repository;
import node.com.enjoydanang.framework.FragmentTransitionInfo;
import node.com.enjoydanang.model.Partner;
import node.com.enjoydanang.model.Review;
import node.com.enjoydanang.ui.fragment.detail.dialog.DetailHomeDialogFragment;
import node.com.enjoydanang.ui.fragment.review.write.WriteReviewDialog;
import node.com.enjoydanang.ui.fragment.review.write.WriteReviewFragment;
import node.com.enjoydanang.utils.ImageUtils;
import node.com.enjoydanang.constant.AppError;
import node.com.enjoydanang.utils.event.OnItemClickListener;
import node.com.enjoydanang.utils.helper.EndlessRecyclerViewScrollListener;

/**
 * Author: Tavv
 * Created on 10/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class ReviewFragment extends MvpFragment<ReviewPresenter> implements iReviewView, OnItemClickListener {
    private static final String TAG = ReviewFragment.class.getSimpleName();

    @BindView(R.id.rcv_review)
    RecyclerView recyclerView;

    @BindView(R.id.imgPartner)
    ImageView imgPartner;

    @BindView(R.id.txtEmpty)
    TextView txtEmpty;

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
        mAdapter = new ReviewAdapter(getContext(), lstReviews, this);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mvpPresenter = createPresenter();
        Bundle bundle = getArguments();
        if (bundle != null) {
            partner = (Partner) bundle.getSerializable(TAG);
            if (partner != null) {
                showLoading();
                mvpPresenter.fetchReviewByPartner(partner.getId(), START_PAGE);
                ImageUtils.loadImageNoRadius(getContext(), imgPartner, partner.getPicture());
            }
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
    void onClick(View view){
        if (partner != null) {
            WriteReviewDialog dialog = WriteReviewDialog.newInstance(partner);
            dialog.show(mFragmentManager, TAG);
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
        if (CollectionUtils.isEmpty(models)) {
            recyclerView.setVisibility(View.GONE);
            txtEmpty.setVisibility(View.VISIBLE);
        }
        updateItems(models);
    }

    @Override
    public void onFetchFailure(AppError error) {

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
        Toast.makeText(mMainActivity, "Reply Click", Toast.LENGTH_SHORT).show();
    }

}
