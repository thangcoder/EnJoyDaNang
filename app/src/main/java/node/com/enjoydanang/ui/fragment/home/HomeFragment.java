package node.com.enjoydanang.ui.fragment.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
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
import node.com.enjoydanang.constant.AppError;
import ss.com.bannerslider.banners.RemoteBanner;
import ss.com.bannerslider.views.BannerSlider;

/**
 * Created by chien on 10/8/17.
 */

public class HomeFragment extends MvpFragment<HomePresenter> implements HomeViewCallbackListener, AdapterView.OnItemClickListener {
    private static final String TAG = HomeFragment.class.getSimpleName();

    public enum TypeGetPartner {
        ALL_PARTNER, PARTNER_BY_CATEGORY
    }

    @BindView(R.id.rcv_partner)
    RecyclerView rcvPartner;
    @BindView(R.id.grv_menu)
    GridView gridView;
    @BindView(R.id.carouselView)
    BannerSlider bannerSlider;
    @BindView(R.id.empty_view)
    TextView txtEmptyView;

    private CategoryAdapter mCategoryAdapter;
    private LinearLayoutManager mLayoutManager;
    private final int startPage = 0;
    private int currentPage;

    private List<Partner> lstPartner;
    private List<Category> lstCategories;

    private PartnerAdapter mPartnerAdapter;
    private LoadMorePartner loadMorePartner;

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
        loadMorePartner = new LoadMorePartner(mLayoutManager);

    }

    @Override
    protected void setEvent(View view) {
        rcvPartner.addOnScrollListener(loadMorePartner);
        gridView.setOnItemClickListener(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mvpPresenter = createPresenter();
        loadMorePartner.setTypeGetPartner(TypeGetPartner.ALL_PARTNER);
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
    public void onGetBannerFailure(AppError error) {
        Log.e(TAG, "onGetBannerFailure: " + error.getMessage());
    }

    @Override
    public void onGetPartnerSuccess(BaseRepository<Partner> data) {
        if (Utils.isNotEmptyContent(data)) {
            List<Partner> partners = data.getData();
            updateItemAdapter(partners);
        }
    }

    @Override
    public void onGetPartnerFailure(AppError error) {
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
    public void onGetCategoryFailure(AppError error) {
        Log.e(TAG, "onGetCategoryFailure: " + error.getMessage());
    }

    @Override
    public void onGetPartnerByCategorySuccess(BaseRepository<Partner> data) {
        if (Utils.isNotEmptyContent(data)) {
            mPartnerAdapter.clearAndUpdateData(data.getData());
            rcvPartner.setVisibility(View.VISIBLE);
            txtEmptyView.setVisibility(View.GONE);
        }else{
            mPartnerAdapter.clearAndUpdateData(Collections.EMPTY_LIST);
            rcvPartner.setVisibility(View.GONE);
            txtEmptyView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onGetPartnerByCategoryFailure(AppError error) {

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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Category category = (Category) parent.getItemAtPosition(position);
        if (category != null) {
            showLoading();
            loadMorePartner.setTypeGetPartner(TypeGetPartner.PARTNER_BY_CATEGORY);
            loadMorePartner.setCategoryId(category.getId());
            mvpPresenter.getPartnerByCategory(category.getId(), startPage);
        }
    }

    private class LoadMorePartner extends EndlessRecyclerViewScrollListener {

        private TypeGetPartner typeGetPartner;
        private int categoryId = 0;

        public LoadMorePartner(LinearLayoutManager layoutManager) {
            super(layoutManager);
        }

        @Override
        public int getFooterViewType(int defaultNoFooterViewType) {
            return 1;
        }

        @Override
        public void onLoadMore(int page, int totalItemsCount) {
            currentPage = page;
            onRetryGetPartner(page, categoryId, typeGetPartner);
        }

        public void setTypeGetPartner(TypeGetPartner typeGetPartner) {
            this.typeGetPartner = typeGetPartner;
        }

        public void setCategoryId(int categoryId) {
            this.categoryId = categoryId;
        }
    }

    private void onRetryGetPartner(int page, int categoryId, TypeGetPartner type) {
        if (type == TypeGetPartner.ALL_PARTNER) {
            mvpPresenter.getPartner(page);
        } else if (type == TypeGetPartner.PARTNER_BY_CATEGORY) {
            mvpPresenter.getPartnerByCategory(categoryId, page);
        }

    }


}
