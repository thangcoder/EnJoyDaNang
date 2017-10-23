package node.com.enjoydanang.ui.fragment.home;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import node.com.enjoydanang.MvpFragment;
import node.com.enjoydanang.MvpFragmentWithToolbar;
import node.com.enjoydanang.R;
import node.com.enjoydanang.api.model.Repository;
import node.com.enjoydanang.constant.AppError;
import node.com.enjoydanang.constant.Constant;
import node.com.enjoydanang.framework.FragmentTransitionInfo;
import node.com.enjoydanang.model.Banner;
import node.com.enjoydanang.model.Category;
import node.com.enjoydanang.model.ExchangeRate;
import node.com.enjoydanang.model.Partner;
import node.com.enjoydanang.model.Weather;
import node.com.enjoydanang.ui.fragment.detail.DetailHomeFragment;
import node.com.enjoydanang.ui.fragment.home.adapter.CategoryAdapter;
import node.com.enjoydanang.ui.fragment.home.adapter.PartnerAdapter;
import node.com.enjoydanang.ui.fragment.home.adapter.ViewPagerAdapter;
import node.com.enjoydanang.ui.fragment.home.adapter.WeatherAdapter;
import node.com.enjoydanang.utils.ImageUtils;
import node.com.enjoydanang.utils.Utils;
import node.com.enjoydanang.utils.event.OnItemClickListener;
import node.com.enjoydanang.utils.helper.EndlessParentScrollListener;
import node.com.enjoydanang.utils.helper.FragmentHelper;
import ss.com.bannerslider.banners.RemoteBanner;
import ss.com.bannerslider.views.BannerSlider;

import static node.com.enjoydanang.R.id.fabFavorite;

/**
 * Created by chien on 10/8/17.
 */

public class HomeFragment extends MvpFragment<HomePresenter> implements iHomeView, AdapterView.OnItemClickListener, OnItemClickListener {
    private static final String TAG = HomeFragment.class.getSimpleName();

    public enum TypeGetPartner {
        ALL_PARTNER, PARTNER_BY_CATEGORY
    }

    @BindView(R.id.rcv_partner)
    RecyclerView rcvPartner;
    @BindView(R.id.grv_menu)
    GridView gridView;
    @BindView(R.id.rcv_weather)
    RecyclerView rcvWeather;
    @BindView(R.id.txtExchangeRate)
    TextView txtExchangeRate;
    @BindView(R.id.txtNameExchange)
    TextView txtNameExchange;

    @BindView(R.id.imgVNRate)
    ImageView imgVNRate;

    @BindView(R.id.imgUSRate)
    ImageView imgUSRate;

    @BindView(R.id.nestedScroll)
    NestedScrollView nestedScrollView;

    @BindView(R.id.carouselView)
    BannerSlider bannerSlider;


    private CategoryAdapter mCategoryAdapter;
    private final int startPage = 0;
    private int currentPage;

    private List<Partner> lstPartner;
    private List<Category> lstCategories;

    private PartnerAdapter mPartnerAdapter;
    private boolean hasLoadmore;

    private LinearLayoutManager mLayoutManager;

    private LoadmorePartner loadmorePartner;

    private final int defaultCustomerId = 0;

    @Override
    public void showToast(String desc) {

    }

    @Override
    public void unKnownError() {

    }


    @Override
    protected void init(View view) {
        /**
         * Init Data
         */
        mBaseActivity.getToolbar().setTitle(Utils.getString(R.string.Home_Screen_Title));
        mBaseActivity.setSupportActionBar(mBaseActivity.getToolbar());
        lstPartner = new ArrayList<>();
        mPartnerAdapter = new PartnerAdapter(getContext(), lstPartner, this);
        rcvPartner.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rcvPartner.addItemDecoration(
                new DividerItemDecoration(mMainActivity, DividerItemDecoration.VERTICAL));
        rcvPartner.setLayoutManager(mLayoutManager);
        rcvPartner.setAdapter(mPartnerAdapter);
        rcvPartner.setNestedScrollingEnabled(false);

        lstCategories = new ArrayList<>();
        mCategoryAdapter = new CategoryAdapter(getContext(), lstCategories);
        gridView.setAdapter(mCategoryAdapter);

        rcvWeather.setLayoutManager(new LinearLayoutManager(mMainActivity, LinearLayoutManager.HORIZONTAL, false));
        bannerSlider.setLoopSlides(true);
    }

    @Override
    protected void setEvent(View view) {
        loadmorePartner = new LoadmorePartner(mLayoutManager);
        gridView.setOnItemClickListener(this);
        nestedScrollView.setOnScrollChangeListener(loadmorePartner);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mvpPresenter = createPresenter();
        loadmorePartner.setTypeGetPartner(TypeGetPartner.ALL_PARTNER);
        mvpPresenter.getListHome(defaultCustomerId);
        mvpPresenter.getAllCategories();
        mvpPresenter.getBanner();
        mvpPresenter.getWeather();
        mvpPresenter.getExchangeRate();
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
    public void onGetBannerSuccess(List<Banner> data) {
        List<ss.com.bannerslider.banners.Banner> banners = new ArrayList<>();
        int length = data.size();
        for (int i = 0; i < length; i++) {
            banners.add(new RemoteBanner(data.get(i).getPicture()));
        }
        bannerSlider.setBanners(banners);
    }

    @Override
    public void onGetBannerFailure(AppError error) {
        Log.e(TAG, "onGetBannerFailure: " + error.getMessage());
    }

    @Override
    public void onGetPartnerSuccess(List<Partner> partners) {
        updateItemAdapter(partners);
    }

    @Override
    public void onGetPartnerFailure(AppError error) {
        Log.e(TAG, "onGetPartnerFailure: " + error.getMessage());
    }

    @Override
    public void onGetCategorySuccess(List<Category> data) {
        lstCategories.addAll(data);
        mCategoryAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGetCategoryFailure(AppError error) {
        Log.e(TAG, "onGetCategoryFailure: " + error.getMessage());
    }

    @Override
    public void onGetPartnerByCategorySuccess(Repository<Partner> data) {
        if (!Utils.isNotEmptyContent(data) && !hasLoadmore) {
            mPartnerAdapter.clearAndUpdateData(Collections.EMPTY_LIST);
            rcvPartner.setVisibility(View.GONE);
        } else {
            mPartnerAdapter.clearAndUpdateData(data.getData());
            rcvPartner.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onGetPartnerByCategoryFailure(AppError error) {
        Log.e(TAG, "onGetPartnerByCategoryFailure: " + error.getMessage());
    }

    @Override
    public void onFetchWeatherSuccess(List<Weather> lstWeathers) {
        WeatherAdapter mWeatherAdapter = new WeatherAdapter(lstWeathers, getContext());
        rcvWeather.setAdapter(mWeatherAdapter);
    }

    @Override
    public void onFetchWeatherFailure(AppError error) {
        Log.e(TAG, "onGetPartnerByCategoryFailure: " + error.getMessage());
    }

    @Override
    public void onFetchExchangeRateSuccess(List<ExchangeRate> lstExchanges) {
        //Only one item return
        ExchangeRate exchangeRate = lstExchanges.get(0);
        String strExchange = String.format(Constant.EXCHANGE_RATE_FORMAT, exchangeRate.getRate());
        txtNameExchange.setText(exchangeRate.getName());
        ImageUtils.loadImageNoRadius(getContext(), imgUSRate, exchangeRate.getFlagEN());
        ImageUtils.loadImageNoRadius(getContext(), imgVNRate, exchangeRate.getFlagVN());
        txtExchangeRate.setText(strExchange);
    }

    @Override
    public void onFetchExchangeRateFailure(AppError error) {
        Log.e(TAG, "onGetPartnerByCategoryFailure: " + error.getMessage());
    }


    @Override
    protected HomePresenter createPresenter() {
        return new HomePresenter(this);
    }

    private void updateItemAdapter(@NonNull List<Partner> partners) {
        int newSize = partners.size();
        int oldSize = lstPartner.size();
        lstPartner.addAll(partners);
        mPartnerAdapter.notifyItemRangeChanged(0, newSize + oldSize);
        mPartnerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Category category = (Category) parent.getItemAtPosition(position);
        if (category != null) {
            showLoading();
            for (int i = 0; i < gridView.getCount(); i++) {
                View v = gridView.getChildAt(i);
                if (i == position) {
                    v.setBackgroundResource(R.drawable.border_item_selected);
                } else {
                    v.setBackgroundResource(R.drawable.border_item_default);
                }
            }
            loadmorePartner.setTypeGetPartner(TypeGetPartner.PARTNER_BY_CATEGORY);
            loadmorePartner.setCategoryId(category.getId());
            hasLoadmore = false;
            mvpPresenter.getPartnerByCategory(category.getId(), startPage);
        }
    }

    private class LoadmorePartner extends EndlessParentScrollListener {
        private TypeGetPartner typeGetPartner;
        private int categoryId = -1;

        public LoadmorePartner(RecyclerView.LayoutManager layoutManager) {
            super(layoutManager);
        }

        @Override
        public void onLoadMore(int page, int totalItemsCount) {
            currentPage = page;
            hasLoadmore = true;
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
        if (type == TypeGetPartner.PARTNER_BY_CATEGORY && categoryId != -1) {
            mvpPresenter.getPartnerByCategory(categoryId, page);
        }
    }

    @Override
    public void onClick(View view, int position) {
        if (view.getId() == fabFavorite) {
            FloatingActionButton fabFavorite = (FloatingActionButton) view;
            //TODO : call apply favorite
        } else {
            FragmentTransitionInfo transitionInfo = new FragmentTransitionInfo(R.anim.slide_up_in, 0, 0, 0);
            Bundle bundle = new Bundle();
            bundle.putInt(DetailHomeFragment.class.getSimpleName(), lstPartner.get(position).getId());
            mBaseActivity.replaceFragment(R.id.container_fragment, DetailHomeFragment.class.getName(), true, bundle, transitionInfo);
        }
    }

}
