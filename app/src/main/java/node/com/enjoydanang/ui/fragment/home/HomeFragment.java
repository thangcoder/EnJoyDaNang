package node.com.enjoydanang.ui.fragment.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import node.com.enjoydanang.MvpFragment;
import node.com.enjoydanang.R;
import node.com.enjoydanang.api.model.BaseRepository;
import node.com.enjoydanang.model.Banner;
import node.com.enjoydanang.model.Category;
import node.com.enjoydanang.model.MenuItem;
import node.com.enjoydanang.model.Partner;
import node.com.enjoydanang.utils.Utils;
import node.com.enjoydanang.utils.helper.EndlessRecyclerViewScrollListener;
import node.com.enjoydanang.utils.network.NetworkError;
import ss.com.bannerslider.banners.RemoteBanner;
import ss.com.bannerslider.views.BannerSlider;

/**
 * Created by chien on 10/8/17.
 */

public class HomeFragment extends MvpFragment<HomePresenter> implements HomeViewCallbackListener {
    private static final String TAG = HomeFragment.class.getSimpleName();

    @BindView(R.id.rcv_partner)
    RecyclerView rcvPartner;
    @BindView(R.id.grv_menu)
    GridView gridView;
    @BindView(R.id.carouselView)
    BannerSlider bannerSlider;

    private CategoryAdapter mCategoryAdapter;
    private LinearLayoutManager mLayoutManager;
    private final int startPage = 0;
    private int currentPage;

    private List<Partner> lstPartner;
    private List<Category> lstCategories;

    private PartnerAdapter mPartnerAdapter;

    private int countComming = 0;

    @Override
    public void showToast(String desc) {

    }

    @Override
    public void unKnownError() {

    }


    @Override
    protected void init(View view) {
        /**
         * Init Data Product list
         */
        lstPartner = new ArrayList<>();
        mPartnerAdapter = new PartnerAdapter(getContext(), lstPartner);
        rcvPartner.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(getActivity());
        rcvPartner.addItemDecoration(
                new DividerItemDecoration(mMainActivity, DividerItemDecoration.VERTICAL));
        rcvPartner.setLayoutManager(mLayoutManager);
        rcvPartner.setAdapter(mPartnerAdapter);

        lstCategories = new ArrayList<>();
        mCategoryAdapter = new CategoryAdapter(getContext(), lstCategories);
        gridView.setAdapter(mCategoryAdapter);
    }

    @Override
    protected void setEvent(View view) {
        rcvPartner.addOnScrollListener(new LoadMoreProduct(mLayoutManager));
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mvpPresenter = createPresenter();
        mvpPresenter.getPartner(startPage);
        mvpPresenter.getAllCategories();
        mvpPresenter.getBanner();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void bindView(View view) {
        ButterKnife.bind(this, view);
    }

    @Override
    public void getMenuFinish(List<MenuItem> menuList) {

    }

    @Override
    public void getMenuFail() {

    }

    @Override
    public void getCategorysFinish(Category category) {
//        homeAdapter = new CategoryAdapter(mMainActivity, category.getData());
//        gridView.setAdapter(homeAdapter);
    }

    @Override
    public void getCategorysFail() {

    }

    @Override
    public void onGetBannerSuccess(BaseRepository<Banner> data) {
        if (Utils.isNotEmptyContent(data)) {
            List<ss.com.bannerslider.banners.Banner> banners = new ArrayList<>();
            int length = data.getData().size();
            for (int i = 0; i < length; i++) {
                banners.add(new RemoteBanner(data.getData().get(i).getPicture()));
            }
            bannerSlider.setBanners(banners);
        }
    }

    @Override
    public void onGetBannerFailure(NetworkError error) {
        Log.e(TAG, "onGetBannerFailure: " + error.getMessage());
    }

    @Override
    public void onGetPartnerSuccess(BaseRepository<Partner> data) {
        if (Utils.isNotEmptyContent(data)) {
            Log.i(TAG, "onGetPartnerSuccess: " + currentPage);
            List<Partner> partners = data.getData();
            updateItemAdapter(partners);
        }
    }

    @Override
    public void onGetPartnerFailure(NetworkError error) {
        Log.e(TAG, "onGetPartnerFailure: " + error.getMessage());
    }

    @Override
    public void onGetCategorySuccess(BaseRepository<Category> data) {
        if (Utils.isNotEmptyContent(data)) {
            lstCategories.addAll(data.getData());
            mCategoryAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onGetCategoryFailure(NetworkError error) {
        Log.e(TAG, "onGetCategoryFailure: " + error.getMessage());
    }


    @Override
    protected HomePresenter createPresenter() {
        return new HomePresenter(this);
    }

    private void updateItemAdapter(@NonNull List<Partner> partners) {
        int newSize = partners.size();
        int oldSize = partners.size();
        lstPartner.addAll(partners);
        mPartnerAdapter.notifyItemRangeChanged(0, newSize + oldSize);
        mPartnerAdapter.notifyDataSetChanged();
    }

    private class LoadMoreProduct extends EndlessRecyclerViewScrollListener {

        public LoadMoreProduct(LinearLayoutManager layoutManager) {
            super(layoutManager);
        }

        @Override
        public int getFooterViewType(int defaultNoFooterViewType) {
            return 1;
        }

        @Override
        public void onLoadMore(int page, int totalItemsCount) {
            currentPage = page;
            onRetryGetPartner(page);
        }
    }

    private void onRetryGetPartner(int page) {
        mvpPresenter.getPartner(page);
    }
}
