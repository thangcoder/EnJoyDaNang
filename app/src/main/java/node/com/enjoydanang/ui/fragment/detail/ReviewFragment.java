package node.com.enjoydanang.ui.fragment.detail;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import butterknife.BindView;
import butterknife.ButterKnife;
import node.com.enjoydanang.MvpFragmentWithToolbar;
import node.com.enjoydanang.R;
import node.com.enjoydanang.model.ItemReviewModel;
import node.com.enjoydanang.model.ReviewDetailModel;
import node.com.enjoydanang.utils.network.NetworkError;

/**
 * Author: Tavv
 * Created on 10/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class ReviewFragment extends MvpFragmentWithToolbar<ReviewPresenter> implements iReviewView{

    @BindView(R.id.rcv_review)
    RecyclerView recyclerView;

    private ReviewAdapter mAdapter;

    @BindView(R.id.txtRate)
    TextView txtRate;

    @BindView(R.id.txtCountReviews)
    TextView txtCountReviews;

    @BindView(R.id.imgReviewer)
    ImageView imgReviewer;


    @Override
    protected ReviewPresenter createPresenter() {
        return new ReviewPresenter(this);
    }

    @Override
    protected void init(View view) {
        recyclerView.setHasFixedSize(false);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
    }

    @Override
    protected void setEvent(View view) {

    }

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_review;
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
    public void onFetchReviews(List<ItemReviewModel> models) {
        mAdapter = new ReviewAdapter(this.getContext(), models);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onFetchSuccess(NetworkError error) {

    }

    @Override
    public void onFetchLastReview(ReviewDetailModel reviewDetail) {
        txtRate.setText(String.valueOf(reviewDetail.getRate()));
        txtCountReviews.setText(reviewDetail.getNumberOfRated());
        Glide.with(mMainActivity).load(reviewDetail.getAvatar()).into(imgReviewer);
    }

    @Override
    public void setupActionBar() {

    }
}
