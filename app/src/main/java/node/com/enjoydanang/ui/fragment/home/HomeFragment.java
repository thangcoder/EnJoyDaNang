package node.com.enjoydanang.ui.fragment.home;

import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import node.com.enjoydanang.MvpFragment;
import node.com.enjoydanang.R;
import node.com.enjoydanang.annotation.DialogType;
import node.com.enjoydanang.api.model.Repository;
import node.com.enjoydanang.constant.AppError;
import node.com.enjoydanang.constant.Constant;
import node.com.enjoydanang.framework.FragmentTransitionInfo;
import node.com.enjoydanang.model.Banner;
import node.com.enjoydanang.model.Category;
import node.com.enjoydanang.model.Partner;
import node.com.enjoydanang.model.UserInfo;
import node.com.enjoydanang.ui.activity.main.MainActivity;
import node.com.enjoydanang.ui.fragment.detail.dialog.DetailHomeDialogFragment;
import node.com.enjoydanang.ui.fragment.event.EventDialogFragment;
import node.com.enjoydanang.ui.fragment.home.adapter.CategoryAdapter;
import node.com.enjoydanang.ui.fragment.home.adapter.PartnerAdapter;
import node.com.enjoydanang.utils.DialogUtils;
import node.com.enjoydanang.utils.Utils;
import node.com.enjoydanang.utils.event.OnItemClickListener;
import node.com.enjoydanang.utils.helper.EndlessParentScrollListener;
import node.com.enjoydanang.utils.helper.SeparatorDecoration;

/**
 * Created by chien on 10/8/17.
 */

public class HomeFragment extends MvpFragment<HomePresenter> implements iHomeView, AdapterView.OnItemClickListener,
        OnItemClickListener, BaseSliderView.OnSliderClickListener {
    private static final String TAG = HomeFragment.class.getSimpleName();
    private static final int VERTICAL_ITEM_SPACE = 8;
    private static final int DURATION_SLIDE = 3000;

    @BindView(R.id.rcv_partner)
    RecyclerView rcvPartner;
    @BindView(R.id.grv_menu)
    GridView gridView;

    @BindView(R.id.lrlContentHome)
    LinearLayout lrlContentHome;

    @BindView(R.id.progress_bar)
    ProgressBar prgLoading;

    @BindView(R.id.nestedScroll)
    NestedScrollView nestedScrollView;

    @BindView(R.id.carouselView)
    SliderLayout bannerSlider;
    private CategoryAdapter mCategoryAdapter;

    private List<Partner> lstPartner;
    private List<Banner> listBannerCopy;
    private List<Category> lstCategories = new ArrayList<>();

    private PartnerAdapter mPartnerAdapter;

    private boolean hasLoadmore;

    private int countCategoryClick = 0;

    private LinearLayoutManager mLayoutManager;

    private LoadmorePartner loadmorePartner;

    private UserInfo user;

    private Location mLastLocation;

    private Location firstTimePosition;

    @Override
    public void showToast(String desc) {

    }

    @Override
    public void unKnownError() {

    }

    @Override
    protected void init(View view) {
        user = Utils.getUserInfo();
        lstPartner = new ArrayList<>();
        mPartnerAdapter = new PartnerAdapter(getContext(), lstPartner, this);
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rcvPartner.addItemDecoration(
                new SeparatorDecoration(getContext(), Utils.getColorRes(R.color.material_grey_300), VERTICAL_ITEM_SPACE));
        rcvPartner.setLayoutManager(mLayoutManager);
        rcvPartner.setAdapter(mPartnerAdapter);
        rcvPartner.setHasFixedSize(false);
        rcvPartner.setNestedScrollingEnabled(false);
        mCategoryAdapter = new CategoryAdapter(getContext(), lstCategories);
        gridView.setAdapter(mCategoryAdapter);
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
        loadmorePartner.setCategoryId(-1);
        gridView.setVisibility(View.VISIBLE);
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
    public void onGetPartnerByCategorySuccess(Repository<Partner> data) {
        hideLoading();
        if (!Utils.isNotEmptyContent(data) && !hasLoadmore) {
            mPartnerAdapter.clearAndUpdateData(Collections.EMPTY_LIST);
            rcvPartner.setVisibility(View.GONE);
        } else {
            updateItemAdapter(data.getData());
            rcvPartner.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onGetPartnerByCategoryFailure(AppError error) {
        hideLoading();
        Log.e(TAG, "onGetPartnerByCategoryFailure: " + error.getMessage());
    }


    @Override
    protected HomePresenter createPresenter() {
        return new HomePresenter(this);
    }

    private void updateItemAdapter(@NonNull List<Partner> partners) {
        if (countCategoryClick == 1 && !hasLoadmore) {
            clearFirstTimeData();
            lstPartner.addAll(partners);
            mPartnerAdapter.notifyItemRangeChanged(0, partners.size());
        } else {
            int newSize = partners.size();
            int oldSize = lstPartner.size();
            lstPartner.addAll(partners);
            mPartnerAdapter.notifyItemRangeChanged(0, newSize + oldSize);
        }
        mPartnerAdapter.notifyDataSetChanged();

    }

    private void updateItemNoLoadmore(@NonNull List<Partner> partners) {
        if(listBannerCopy!= null){
            setDataBanner(listBannerCopy);
        }
        int newSize = partners.size();
        if (CollectionUtils.isNotEmpty(lstPartner)) {
            lstPartner.clear();
        }
        lstPartner.addAll(partners);
        mPartnerAdapter.notifyItemRangeChanged(0, newSize);
        mPartnerAdapter.notifyDataSetChanged();
        lrlContentHome.setVisibility(View.VISIBLE);
        gridView.setVisibility(View.VISIBLE);
        bannerSlider.setVisibility(View.VISIBLE);
        prgLoading.setVisibility(View.GONE);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Category category = (Category) parent.getItemAtPosition(position);
        FragmentTransitionInfo transitionInfo = new FragmentTransitionInfo(R.anim.slide_up_in, R.anim.slide_to_left, R.anim.slide_up_in, R.anim.slide_to_left);
        Fragment prev = mFragmentManager.findFragmentByTag(PartnerCategoryFragment.class.getName());
        if (prev == null) {
            mMainActivity.addFragment(PartnerCategoryFragment.newInstance(category.getId(), category.getName(), mLastLocation),
                    R.id.container_fragment, PartnerCategoryFragment.class.getName(),
                    transitionInfo);
        }
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        int bannerId = StringUtils.isNotBlank(slider.getDescription()) ? Integer.parseInt(slider.getDescription()) : -1;
        if (bannerId != -1) {
            DialogUtils.openDialogFragment(mFragmentManager, EventDialogFragment.getInstance(bannerId));
        }
    }


    private class LoadmorePartner extends EndlessParentScrollListener {
        private int categoryId = -1;

        public LoadmorePartner(RecyclerView.LayoutManager layoutManager) {
            super(layoutManager);
        }

        @Override
        public void onLoadMore(int page, int totalItemsCount) {
            hasLoadmore = true;
            onRetryGetPartner(page, categoryId);
        }

        public void setCategoryId(int categoryId) {
            this.categoryId = categoryId;
        }
    }


    private void onRetryGetPartner(int page, int categoryId) {
        if (categoryId != -1) {
            mvpPresenter.getPartnerByCategory(categoryId, page, user.getUserId());
        }
    }

    @Override
    public void onClick(View view, final int position) {
        if (view.getId() == R.id.fabFavorite) {
            if (user.getUserId() != 0) {
                FloatingActionButton fabFavorite = (FloatingActionButton) view;
                mvpPresenter.addFavorite(user.getUserId(), lstPartner.get(position).getId());
                boolean isFavorite = lstPartner.get(position).getFavorite() > 0;
                fabFavorite.setImageResource(isFavorite ? R.drawable.unfollow : R.drawable.follow);
                lstPartner.get(position).setFavorite(isFavorite ? 0 : 1);
            } else {
                DialogUtils.showDialog(getContext(), DialogType.WARNING, DialogUtils.getTitleDialog(2), Utils.getLanguageByResId(R.string.Message_You_Need_Login));
            }

        } else {
            MainActivity activity = (MainActivity) getActivity();
            activity.currentTab = HomeTab.None;
            activity.setShowMenuItem(Constant.SHOW_MENU_BACK);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (CollectionUtils.isNotEmpty(lstPartner)) {
                        Partner partner;
                        try {
                            partner = lstPartner.get(position);
                        } catch (IndexOutOfBoundsException ex) {
                            partner = null;
                        }
                        DetailHomeDialogFragment dialog = DetailHomeDialogFragment.newInstance(partner, false);
                        DialogUtils.openDialogFragment(mFragmentManager, dialog);
                    }
                }
            }, 50);
        }
    }

    @Override
    public void onGetPartnerSuccess(List<Partner> partners) {
        if (CollectionUtils.isEmpty(partners)) {
            rcvPartner.setVisibility(View.GONE);
            return;
        }
        updateItemNoLoadmore(partners);
    }

    @Override
    public void onGetPartnerFailure(AppError error) {
        Log.e(TAG, "onGetPartnerFailure: " + error.getMessage());
    }

    @Override
    public void addFavoriteSuccess() {
    }

    @Override
    public void addFavoriteFailure(AppError error) {
        Log.e(TAG, "onError: " + error.getMessage());
        DialogUtils.showDialog(getContext(), DialogType.WRONG, DialogUtils.getTitleDialog(3), error.getMessage());
    }

    @Override
    public void onFetchAllDataSuccess(List<Partner> partners, List<Banner> banners, List<Category> categories) {
        listBannerCopy = banners;
//        setDataBanner(banners);
        setDataPartner(partners);
        setDataCategory(categories);
        prgLoading.setVisibility(View.GONE);
        lrlContentHome.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFetchFailure(AppError error) {
        Log.e(TAG, "onFetchFailure " + error.getMessage());
        DialogUtils.showDialog(getContext(), DialogType.WRONG, DialogUtils.getTitleDialog(3), error.getMessage());
    }

    private void clearFirstTimeData() {
        if (CollectionUtils.isNotEmpty(lstPartner)) {
            int size = this.lstPartner.size();
            lstPartner.clear();
            mPartnerAdapter.notifyItemRangeChanged(0, size);
        }
    }


    private void setDataBanner(List<Banner> images) {
        HashMap<String, String> sources = new HashMap<>();
        int length = images.size();
        for (int i = 0; i < length; i++) {
//            sources.put(images.get(i).getTitle() + " [Slide " + i + " ]", images.get(i).getPicture());
            sources.put(images.get(i).getId() + "", images.get(i).getPicture());
        }
        for (String id : sources.keySet()) {
            DefaultSliderView textSliderView = new DefaultSliderView(getContext());
            textSliderView
                    .image(sources.get(id))
                    .description(id)
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);
            bannerSlider.addSlider(textSliderView);
        }
        bannerSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        bannerSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        bannerSlider.setCustomAnimation(new DescriptionAnimation());
        bannerSlider.setDuration(DURATION_SLIDE);
    }

    private void setDataCategory(List<Category> categories) {
        lstCategories.addAll(categories);
        mCategoryAdapter.notifyDataSetChanged();
    }

    private void setDataPartner(List<Partner> partners) {
        if (CollectionUtils.isEmpty(partners)) {
            rcvPartner.setVisibility(View.GONE);
            return;
        }
        updateItemNoLoadmore(partners);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageReceive(Object obj) {
        if (obj instanceof String) {
            String msg = (String) obj;
            if (msg.equalsIgnoreCase(Constant.LOCATION_NOT_FOUND)) {
                mvpPresenter.getAllDataHome(user.getUserId(), StringUtils.EMPTY, StringUtils.EMPTY);
            } else {
                mMainActivity.setShowMenuItem(Constant.SHOW_QR_CODE);
                nestedScrollView.scrollTo(0, 0);
            }

        } else if (obj instanceof Location) {
            firstTimePosition = (Location) obj;
            prgLoading.post(new Runnable() {
                public void run() {
                    if (firstTimePosition == null) {
                        mvpPresenter.getAllDataHome(user.getUserId(), StringUtils.EMPTY, StringUtils.EMPTY);
                    } else {
                        String strGeoLat = String.valueOf(firstTimePosition.getLatitude());
                        String strGeoLng = String.valueOf(firstTimePosition.getLongitude());
                        mvpPresenter.getAllDataHome(user.getUserId(), strGeoLat, strGeoLng);
                    }
                }
            });
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getCurrentPosition();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void getCurrentPosition() {
        if (mMainActivity != null) {
            if (mMainActivity.getLocationService() != null) {
                mLastLocation = mMainActivity.getLocationService().getLastLocation();
                if (mLastLocation != null && firstTimePosition != null) {
                    double distanceTo = firstTimePosition.distanceTo(mLastLocation);
                    if (distanceTo > 0) {
                        String strGeoLat = String.valueOf(mLastLocation.getLatitude());
                        String strGeoLng = String.valueOf(mLastLocation.getLongitude());
                        mvpPresenter.getListHome(user.getUserId(), strGeoLat, strGeoLng);
                        firstTimePosition = mLastLocation;
                    }
                } else {
                    mvpPresenter.getListHome(user.getUserId(), "", "");
                }
            }
        }
    }

}
