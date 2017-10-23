package node.com.enjoydanang.ui.fragment.detail;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import node.com.enjoydanang.MvpFragmentWithToolbar;
import node.com.enjoydanang.R;
import node.com.enjoydanang.api.model.Repository;
import node.com.enjoydanang.constant.AppError;
import node.com.enjoydanang.model.DetailPartner;
import node.com.enjoydanang.utils.ImageUtils;
import node.com.enjoydanang.utils.Utils;

/**
 * Author: Tavv
 * Created on 10/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class DetailPartnerFragment extends MvpFragmentWithToolbar<DetailPartnerPresenter> implements iDetailPartnerView {
    private static final String TAG = DetailPartnerFragment.class.getSimpleName();

    @BindView(R.id.txtTitle)
    TextView txtTitle;

    @BindView(R.id.ratingBar)
    RatingBar ratingBar;

    @BindView(R.id.txtContent)
    TextView txtContent;

    @BindView(R.id.imgPartner)
    ImageView imgPartner;

    public static DetailPartnerFragment newInstance(int partnerId) {
        DetailPartnerFragment fragment = new DetailPartnerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TAG, partnerId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected DetailPartnerPresenter createPresenter() {
        return new DetailPartnerPresenter(this);
    }

    @Override
    protected void init(View view) {

    }

    @Override
    protected void setEvent(View view) {

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mvpPresenter = createPresenter();
        Bundle bundle = getArguments();
        if (bundle != null) {
            int partnerId = bundle.getInt(TAG);
            mvpPresenter.getDetailPartner(partnerId);
        }
    }

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_detail;
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
    public void setupActionBar() {

    }

    @Override
    public void onFetchDetailPartnerSuccess(Repository<DetailPartner> data) {
        if (Utils.isNotEmptyContent(data)) {
            DetailPartner detailPartner = data.getData().get(0);
            txtTitle.setText(detailPartner.getName());
            ImageUtils.loadImageNoRadius(getContext(), imgPartner, detailPartner.getPicture());
            if (Build.VERSION.SDK_INT >= 24) {
                txtContent.setText(Html.fromHtml(detailPartner.getDescription(), Html.FROM_HTML_MODE_LEGACY));
            } else {
                txtContent.setText(Html.fromHtml(detailPartner.getDescription()));
            }
            ratingBar.setRating(detailPartner.getStarReview());


        }
    }

    @Override
    public void onFetchFailure(AppError appError) {

    }
}
