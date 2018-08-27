package node.com.enjoydanang.ui.fragment.home;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.maps.model.LatLng;
import com.zing.zalo.zalosdk.oauth.OauthResponse;
import com.zing.zalo.zalosdk.oauth.ValidateOAuthCodeCallback;
import com.zing.zalo.zalosdk.oauth.ZaloSDK;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import node.com.enjoydanang.MvpFragment;
import node.com.enjoydanang.R;
import node.com.enjoydanang.annotation.DialogType;
import node.com.enjoydanang.constant.AppError;
import node.com.enjoydanang.constant.Constant;
import node.com.enjoydanang.framework.FragmentTransitionInfo;
import node.com.enjoydanang.model.Banner;
import node.com.enjoydanang.model.Category;
import node.com.enjoydanang.model.Partner;
import node.com.enjoydanang.model.PostZalo;
import node.com.enjoydanang.model.UserInfo;
import node.com.enjoydanang.ui.activity.main.MainActivity;
import node.com.enjoydanang.ui.fragment.detail.dialog.DetailHomeDialogFragment;
import node.com.enjoydanang.ui.fragment.event.EventDialogFragment;
import node.com.enjoydanang.ui.fragment.home.adapter.CategoryAdapter;
import node.com.enjoydanang.ui.fragment.home.adapter.PartnerAdapter;
import node.com.enjoydanang.utils.DialogUtils;
import node.com.enjoydanang.utils.Utils;
import node.com.enjoydanang.utils.ZaloUtils;
import node.com.enjoydanang.utils.event.OnItemClickListener;
import node.com.enjoydanang.utils.event.OnLoginZaloListener;
import node.com.enjoydanang.utils.helper.LocationHelper;

/**
 * Author: Tavv
 * Created on 30/10/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public class HomeFragment extends MvpFragment<HomePresenter> implements iHomeView, AdapterView.OnItemClickListener,
        OnItemClickListener, BaseSliderView.OnSliderClickListener, OnLoginZaloListener {
    private static final String TAG = HomeFragment.class.getSimpleName();
    private static final int VERTICAL_ITEM_SPACE = 8;
    private static final int DURATION_SLIDE = 3000;
    private static final float DISTANCE_TO_RELOAD = 0.5f;// ~500 meters

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

    private List<Partner> lstPartner;

    private PartnerAdapter mPartnerAdapter;

    private UserInfo user;

    private Location mLastLocation;

    private Location firstTimePosition;

    private CategoryAdapter mCategoryAdapter;

    private List<Category> lstCategories;

    private List<Banner> lstBanners;

    private ShareDialog shareDialog;

    private PostZalo postZalo;

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
        shareDialog = new ShareDialog(this);
        mPartnerAdapter = new PartnerAdapter(getContext(), lstPartner, this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rcvPartner.addItemDecoration(Utils.getDividerDecoration(mLayoutManager.getOrientation()));
        rcvPartner.setLayoutManager(mLayoutManager);
        rcvPartner.setNestedScrollingEnabled(false);
        rcvPartner.setAdapter(mPartnerAdapter);
        lstCategories = new ArrayList<>();
        mCategoryAdapter = new CategoryAdapter(getContext(), lstCategories);
        gridView.setAdapter(mCategoryAdapter);
    }

    @Override
    protected void setEvent(View view) {
        gridView.setOnItemClickListener(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mvpPresenter = createPresenter();
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
    protected HomePresenter createPresenter() {
        return new HomePresenter(this);
    }

    private void updateItemNoLoadmore(@NonNull List<Partner> partners) {
        if (CollectionUtils.isNotEmpty(lstPartner)) {
            lstPartner.clear();
        }
        lstPartner.addAll(partners);
        mPartnerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Category category = (Category) parent.getItemAtPosition(position);
        FragmentTransitionInfo transitionInfo = new FragmentTransitionInfo(R.anim.slide_up_in, R.anim.slide_to_left, R.anim.slide_up_in, R.anim.slide_to_left);
        Fragment prev = null;
        mLastLocation = mLastLocation == null ? firstTimePosition : mLastLocation;
        if (StringUtils.isBlank(category.getOriginalUrl())) {
            prev = mFragmentManager.findFragmentByTag(PartnerCategoryFragment.class.getName());
            if (prev == null) {
                mMainActivity.addFragment(PartnerCategoryFragment.newInstance(category.getId(), category.getName(), mLastLocation),
                        R.id.container_fragment, PartnerCategoryFragment.class.getName(),
                        transitionInfo);
            }
        } else {
            DialogUtils.openDialogFragment(mFragmentManager, PartnerContentWebViewFragment.newInstance(category, mLastLocation));
        }
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        int bannerId = StringUtils.isNotBlank(slider.getDescription()) ? Integer.parseInt(slider.getDescription()) : -1;
        if (bannerId != -1) {
            DialogUtils.openDialogFragment(mFragmentManager, EventDialogFragment.getInstance(bannerId));
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
        } else if (view.getId() == R.id.btnShare) {
            String urlShare = lstPartner.get(position).getShareUrl();
            if (StringUtils.isNotBlank(urlShare)) {
                urlShare = Constant.URL_HOST_IMAGE + urlShare;
                postZalo = new PostZalo(lstPartner.get(position).getName(), urlShare, StringUtils.EMPTY);
                DialogUtils.showPopupShare(getContext(), shareDialog, getActivity(), urlShare, lstPartner.get(position).getName());
//                showPopupShare(urlShare, lstPartner.get(position).getName());
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
        if (CollectionUtils.isNotEmpty(lstPartner)) {
            lstPartner.clear();
        }
        lstPartner.addAll(partners);
        mPartnerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGetPartnerFailure(AppError error) {
    }

    @Override
    public void addFavoriteSuccess() {
    }

    @Override
    public void addFavoriteFailure(AppError error) {
        DialogUtils.showDialog(getContext(), DialogType.WRONG, DialogUtils.getTitleDialog(3), error.getMessage());
    }

    @Override
    public void onFetchAllDataSuccess(List<Partner> partners, List<Banner> banners, List<Category> categories) {
        setDataBanner(banners);
        setDataPartner(partners);
        setDataCategory(categories);
        prgLoading.setVisibility(View.GONE);
        lrlContentHome.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFetchFailure(AppError error) {
        if (CollectionUtils.isEmpty(lstBanners) && CollectionUtils.isEmpty(lstCategories) && CollectionUtils.isEmpty(lstPartner)) {
            DialogUtils.showDialog(getContext(), DialogType.WRONG, DialogUtils.getTitleDialog(3), error.getMessage());
        }
    }


    private void setDataBanner(List<Banner> images) {
        if (CollectionUtils.isEmpty(lstBanners) && CollectionUtils.isNotEmpty(images)) {
            lstBanners = images;
            for (Banner banner : images) {
                DefaultSliderView textSliderView = new DefaultSliderView(getContext());
                textSliderView
                        .image(banner.getPicture())
                        .description(String.valueOf(banner.getId()))
                        .setScaleType(BaseSliderView.ScaleType.Fit)
                        .setOnSliderClickListener(this);
                bannerSlider.addSlider(textSliderView);
            }
            bannerSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
            bannerSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            bannerSlider.setCustomAnimation(new DescriptionAnimation());
            bannerSlider.setDuration(DURATION_SLIDE);
        }
    }

    private void setDataCategory(List<Category> categories) {
        if (CollectionUtils.isEmpty(lstCategories) && CollectionUtils.isNotEmpty(categories)) {
            lstCategories.addAll(categories);
            mCategoryAdapter.notifyDataSetChanged();
        }
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
        boolean needReload = false;
        if (obj instanceof String) {
            String msg = (String) obj;
            if (msg.equalsIgnoreCase(Constant.LOCATION_NOT_FOUND)) {
                mvpPresenter.getAllDataHome(user.getUserId(), StringUtils.EMPTY, StringUtils.EMPTY);
            } else {
                mMainActivity.setShowMenuItem(Constant.SHOW_QR_CODE);
                nestedScrollView.scrollTo(0, 0);
            }

        } else if (obj instanceof Location) {
            final Location newLocation = (Location) obj;
            if (firstTimePosition == null) {
                firstTimePosition = newLocation;
                needReload = true;
            } else {
                LatLng startPoint = new LatLng(firstTimePosition.getLatitude(), firstTimePosition.getLongitude());
                LatLng endPoint = new LatLng(newLocation.getLatitude(), newLocation.getLongitude());
                LocationHelper locationHelper = mMainActivity.mLocationHelper;
                if (locationHelper != null) {
                    double value = locationHelper.calculationByDistance(startPoint, endPoint);
                    if (value > DISTANCE_TO_RELOAD) {
                        firstTimePosition = newLocation;
                        needReload = true;
                    } else {
                        needReload = false;
                    }
                }
            }
            if (needReload) {
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

    public void scrollToTop() {
        if (nestedScrollView != null && mMainActivity != null) {
            mMainActivity.setShowMenuItem(Constant.SHOW_QR_CODE);
            nestedScrollView.scrollTo(0, 0);
        }
    }

    private void showPopupShare(final String url, final String title) {
        if (StringUtils.isBlank(url)) return;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.popup_share, null);
        dialogBuilder.setView(dialogView);

        ImageView imgShareKakao = (ImageView) dialogView.findViewById(R.id.img_share_kakao);
        ImageView imgShareFb = (ImageView) dialogView.findViewById(R.id.img_share_fb);
        ImageView imgShareZalo = (ImageView) dialogView.findViewById(R.id.img_share_zalo);

        final AlertDialog alertDialog = dialogBuilder.create();

        alertDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setCancelable(true);

        imgShareKakao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Share Kakaotalk : " + url, Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        });
        imgShareFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                if (ShareDialog.canShow(ShareLinkContent.class)) {
                    ShareLinkContent content = new ShareLinkContent.Builder()
                            .setContentUrl(Uri.parse(url))
                            .build();
                    shareDialog.show(content);
                }
            }
        });

        imgShareZalo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                postZalo = new PostZalo(title, url, StringUtils.EMPTY);
                ZaloSDK.Instance.isAuthenticate(new ValidateOAuthCodeCallback() {
                    @Override
                    public void onValidateComplete(boolean isValidated, int errorCode, long userId, String oauthCode) {
                        if (isValidated) {
                            if (postZalo != null) {
                                ZaloUtils.postToWall(getContext(), postZalo.getUrlShare(), postZalo.getTitle());
                            }
                        } else {
                            //TODO: Do Login Zalo before share
                        }
                    }
                });
            }
        });
        alertDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ZaloSDK.Instance.onActivityResult(getActivity(), requestCode, resultCode, data);
    }

    @Override
    public void onLoginSuccess(OauthResponse response) {
        if (postZalo != null) {
            ZaloUtils.postToWall(getContext(), postZalo.getUrlShare(), postZalo.getTitle());
        }
    }

    @Override
    public void onLoginFailed(String message) {

    }
}
