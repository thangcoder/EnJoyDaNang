package node.com.enjoydanang.ui.fragment.detail;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

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

public class ReviewFragment extends MvpFragmentWithToolbar<ReviewPresenter> implements iReviewView {

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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mvpPresenter = createPresenter();
        mvpPresenter.doDummyData();
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
        mAdapter = new ReviewAdapter(getContext(), models);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onFetchSuccess(NetworkError error) {
        Toast.makeText(mMainActivity, error.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFetchLastReview(ReviewDetailModel reviewDetail) {
        if (reviewDetail != null) {
            String templateCountReviews = "from %d reviews";
            String numOfReviwed = String.format(Locale.getDefault(), templateCountReviews, reviewDetail.getNumberOfRated());
            txtRate.setText(String.valueOf(reviewDetail.getRate()));
            txtCountReviews.setText(numOfReviwed);
            ImageUtils.loadImageNoRadius(getContext(), imgReviewer, reviewDetail.getAvatar());
        }
    }

    @Override
    public void setupActionBar() {

    }
}
