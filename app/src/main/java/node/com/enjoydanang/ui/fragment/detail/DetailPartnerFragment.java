package node.com.enjoydanang.ui.fragment.detail;

import android.app.Activity;
import android.app.Dialog;
import android.location.Location;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.facebook.AccessToken;
import com.facebook.FacebookDialog;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.maps.model.LatLng;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import node.com.enjoydanang.MvpFragment;
import node.com.enjoydanang.R;
import node.com.enjoydanang.annotation.DialogType;
import node.com.enjoydanang.common.Common;
import node.com.enjoydanang.constant.AppError;
import node.com.enjoydanang.constant.Constant;
import node.com.enjoydanang.model.DetailPartner;
import node.com.enjoydanang.model.Partner;
import node.com.enjoydanang.model.PartnerAlbum;
import node.com.enjoydanang.model.Share;
import node.com.enjoydanang.model.UserInfo;
import node.com.enjoydanang.ui.fragment.detail.dialog.DetailHomeDialogFragment;
import node.com.enjoydanang.ui.fragment.share.ShareDialogFragment;
import node.com.enjoydanang.utils.DialogUtils;
import node.com.enjoydanang.utils.ImageUtils;
import node.com.enjoydanang.utils.Utils;
import node.com.enjoydanang.utils.ZaloUtils;
import node.com.enjoydanang.utils.event.OnItemClickListener;
import node.com.enjoydanang.utils.event.OnShareZaloListener;
import node.com.enjoydanang.utils.helper.LanguageHelper;
import node.com.enjoydanang.utils.helper.LocationHelper;

/**
 * Author: Tavv
 * Created on 10/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class DetailPartnerFragment extends MvpFragment<DetailPartnerPresenter> implements iDetailPartnerView,
        OnItemClickListener, OnShareZaloListener {
    private static final String TAG = DetailPartnerFragment.class.getSimpleName();
    private static final String KEY_OPEN_FROM_NEARBY = "open_from_nearby";

    private static final int DURATION_SLIDE = 3000;

    private static final int IMAGE_PARTNER_WIDTH = 400;
    private static final int IMAGE_PARTNER_HEIGHT = 250;

    @BindView(R.id.txtTitle)
    TextView txtTitle;

    @BindView(R.id.ratingBar)
    RatingBar ratingBar;

    @BindView(R.id.txtContent)
    WebView txtContent;

    @BindView(R.id.imgPartner)
    SimpleDraweeView imgPartner;

    @BindView(R.id.webview)
    WebView mWebView;

    @BindView(R.id.slider)
    SliderLayout slider;

    @BindView(R.id.mapView)
    SimpleDraweeView imgMapView;

    @BindView(R.id.progress_bar)
    ProgressBar prgLoading;

    @BindView(R.id.lrlContentDetail)
    LinearLayout lrlContentDetail;

    @BindView(R.id.scrollDetailPartner)
    NestedScrollView scrollDetailPartner;

    private Location mLastLocation;

    private LocationHelper mLocationHelper;

    @BindView(R.id.rcvPartnerAround)
    RecyclerView rcvPartnerAround;

    @BindView(R.id.txtNearPlaces)
    TextView txtNearPlaces;

    @BindView(R.id.txtDistance)
    TextView txtDistance;

    @BindView(R.id.txtDiscount)
    TextView txtDiscount;

    private Partner partner;

    private List<Partner> lstPartnerAround;

    private boolean isLocationNull;

    private boolean isHideListPartnerAround;

    private ZaloUtils zaloUtils;

    private Share share;

    private ShareDialogFragment bottomShareDialog;

    public static DetailPartnerFragment newInstance(Partner partner, boolean isOpenFromNearby) {
        DetailPartnerFragment fragment = new DetailPartnerFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(TAG, partner);
        bundle.putBoolean(KEY_OPEN_FROM_NEARBY, isOpenFromNearby);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected DetailPartnerPresenter createPresenter() {
        return new DetailPartnerPresenter(this);
    }

    @Override
    protected void init(View view) {
        mBaseActivity.setTitle(Utils.getLanguageByResId(R.string.Tab_Detail));
        zaloUtils = new ZaloUtils();
        zaloUtils.setLoginZaLoListener(this);
        if (mMainActivity != null) {
            if (mMainActivity.getLocationService() != null) {
                mLastLocation = mMainActivity.getLocationService().getLastLocation();
            }
            mLocationHelper = mMainActivity.mLocationHelper;
        }
    }


    @Override
    protected void setEvent(View view) {

    }

    private void initWebView() {
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        mWebView.setWebViewClient(new WebClient(getActivity()));
        webSettings.setBuiltInZoomControls(true);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mvpPresenter = createPresenter();
        prgLoading.post(new Runnable() {
            @Override
            public void run() {
                Bundle bundle = getArguments();
                if (bundle != null) {
                    partner = (Partner) bundle.getParcelable(TAG);
                    isLocationNull = mLastLocation == null;
                    if (partner != null) {
                        initWebView();
                        if (isLocationNull) {
                            mvpPresenter.getAllDataHome(partner.getId());
                        } else {
                            String geoLat = String.valueOf(mLastLocation.getLatitude());
                            String geoLng = String.valueOf(mLastLocation.getLongitude());
                            mvpPresenter.getAllDataNearBy(partner.getId(), geoLat, geoLng);
                        }
                    }
                }
            }
        });
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
        Toast.makeText(getContext(), desc, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void unKnownError() {

    }


    @Override
    public void onFetchFailure(AppError appError) {
        DetailHomeDialogFragment fragment = (DetailHomeDialogFragment) getParentFragment();
        if (fragment != null) {
            fragment.countGetResultFailed += 1;
            if (fragment.countGetResultFailed == 1) {
                Log.e(TAG, "onFetchFail " + appError.getMessage());
            }
        }
    }


    @Override
    public void onFetchAllData(List<DetailPartner> lstDetailPartner, List<PartnerAlbum> lstAlbum) {
        setDataAlbum(lstAlbum);
        setDataDetail(lstDetailPartner);

    }

    @Override
    public void onFetchListPartnerAround(List<Partner> lstPartnerAround) {
        if (!isHideListPartnerAround) {
            initListPartnerAround(lstPartnerAround);
        } else {
            rcvPartnerAround.setVisibility(View.GONE);
            txtNearPlaces.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view, int position) {
        if (CollectionUtils.isNotEmpty(lstPartnerAround)) {
            DetailHomeDialogFragment dialog = DetailHomeDialogFragment.newInstance(lstPartnerAround.get(position), true);
            DialogUtils.reOpenDialogFragment(mFragmentManager, dialog);
        }
    }

    @Override
    public void onShareSuccess() {
        if (bottomShareDialog != null) {
            bottomShareDialog.dismiss();
            String title = Utils.getString(R.string.share_title_success);
            title = String.format(title, "Zalo");
            DialogUtils.showDialog(getContext(), DialogType.SUCCESS, title, Utils.getString(R.string.share_content));
        }
    }

    @Override
    public void onShareFailure(String message) {

    }

    private class WebClient extends WebViewClient {
        private Activity activity;

        public WebClient(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            Common.onSslError(activity, view, handler, error);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return false;

        }

    }

    private void loadVideo(String url) {
        if (StringUtils.isBlank(url)) {
            mWebView.setVisibility(View.GONE);
        } else {
            String videoId = url.substring(url.lastIndexOf("/") + 1);
            if (StringUtils.isNotBlank(videoId)) {
                String frameVideo = Utils.getIframeVideoPlay(videoId, 300);
                mWebView.loadData(frameVideo, "text/html", "utf-8");
            } else {
                mWebView.setVisibility(View.GONE);
            }
        }
        prgLoading.setVisibility(View.GONE);
        lrlContentDetail.setVisibility(View.VISIBLE);
    }

    private void loadMapView(DetailPartner detailPartner) {
        try {
            if (detailPartner != null && mLocationHelper != null) {
                double longtitude = Double.parseDouble(StringUtils.trim(detailPartner.getGeoLng()));
                double latitude = Double.parseDouble(StringUtils.trim(detailPartner.getGeoLat()));
                String strImage = mLocationHelper.getUrlThumbnailLocation(longtitude, latitude);
                Uri uri = Uri.parse(strImage);
                if (StringUtils.isNotBlank(strImage)) {
                    ImageUtils.loadImageWithFresoURI(imgMapView, uri);
                }
            }
        } catch (Exception ex) {
            imgMapView.setVisibility(View.GONE);
            ex.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mWebView != null) {
            mWebView.resumeTimers();
            mWebView.onResume();
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        if (mWebView != null) {
            mWebView.onPause();
            mWebView.pauseTimers();
        }
    }

    @Override
    public void onStop() {
        if (slider != null) {
            slider.stopAutoCycle();
        }
        super.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            mWebView.loadUrl("about:blank");
            mWebView.destroy();
            mWebView = null;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void drawRouteToPartner(final DetailPartner detailPartner) {
        imgMapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double longtitude = Double.parseDouble(StringUtils.trim(detailPartner.getGeoLng()));
                double latitude = Double.parseDouble(StringUtils.trim(detailPartner.getGeoLat()));
                LatLng partnerPoint = new LatLng(latitude, longtitude);
                LatLng currentPoint = getCurrentLocation();
                if (currentPoint != null) {
                    startIntentMapsView(partnerPoint, currentPoint);
                }
            }
        });
    }

    private void startIntentMapsView(LatLng partnerLocation, LatLng currentLocation) {
        if (partnerLocation != null && currentLocation != null) {
            String url = Utils.getUriMapsDirection(currentLocation.latitude, currentLocation.longitude, partnerLocation.latitude, partnerLocation.longitude);
            Utils.startIntentMap(getContext(), url);
        }
    }


    private LatLng getCurrentLocation() {
        retryGetLastLocation();
        if (mLastLocation != null) {
            double latitude = mLastLocation.getLatitude();
            double longitude = mLastLocation.getLongitude();
            return new LatLng(latitude, longitude);
        } else {
            showToast(Utils.getLanguageByResId(R.string.Map_Location_NotFound));
            return null;
        }
    }

    private void retryGetLastLocation() {
        if (mLastLocation == null && mMainActivity.getLocationService() != null) {
            mLastLocation = mMainActivity.getLocationService().getLastLocation();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (mWebView != null) {
                mWebView.resumeTimers();
                mWebView.onResume();
            }
        } else {
            if (mWebView != null) {
                mWebView.pauseTimers();
                mWebView.onPause();
            }
        }
    }

    private void setDataAlbum(List<PartnerAlbum> images) {
        for (PartnerAlbum banner : images) {
            DefaultSliderView textSliderView = new DefaultSliderView(getContext());
            textSliderView
                    .image(banner.getPicture())
                    .description(String.valueOf(banner.getId()))
                    .setScaleType(BaseSliderView.ScaleType.Fit);

            slider.addSlider(textSliderView);
        }
        slider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        slider.setCustomAnimation(new DescriptionAnimation());
        slider.setDuration(DURATION_SLIDE);
    }

    private void setDataDetail(List<DetailPartner> lstDetailPartner) {
        if (CollectionUtils.isNotEmpty(lstDetailPartner)) {
            DetailPartner detailPartner = lstDetailPartner.get(0);
            isHideListPartnerAround = checkGeoPartnerEmpty(detailPartner);
            if (!checkGeoPartnerEmpty(detailPartner)) {
                UserInfo user = Utils.getUserInfo();
                mvpPresenter.getListPartnerAround(user.getUserId(), detailPartner.getGeoLat(), detailPartner.getGeoLng());
            }
            if (detailPartner != null) {
                if (StringUtils.isNotBlank(detailPartner.getDistance()) &&
                        !StringUtils.equals(detailPartner.getDistance().trim(), "km")
                        && detailPartner.isDisplayDistance()) {
                    String distance = detailPartner.getDistance();
                    txtDistance.setText(distance);
                    txtDistance.setVisibility(View.VISIBLE);
                } else {
                    txtDistance.setVisibility(View.GONE);
                }
                if (detailPartner.getDiscount() != 0) {
                    String strDiscount = String.format(Locale.getDefault(), Constant.DISCOUNT_TEMPLATE, detailPartner.getDiscount(), "%");
                    txtDiscount.setText(strDiscount);
                    txtDiscount.setVisibility(View.VISIBLE);
                }
            }
            txtTitle.setText(detailPartner.getName());
            ImageUtils.loadImageWithScaleFreso(imgPartner, detailPartner.getPicture(), IMAGE_PARTNER_WIDTH, IMAGE_PARTNER_HEIGHT);
            txtContent.loadDataWithBaseURL(null, detailPartner.getDescription(), "text/html", "utf-8", null);
            ratingBar.setRating(detailPartner.getStarReview());
            ratingBar.setFocusable(false);
            drawRouteToPartner(detailPartner);
            loadMapView(detailPartner);
            loadVideo(detailPartner.getVideo());
        }
    }

    private void initListPartnerAround(List<Partner> lstPartnerAround) {
        if (CollectionUtils.isNotEmpty(lstPartnerAround)) {
            this.lstPartnerAround = lstPartnerAround;
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
                    layoutManager.getOrientation());
            rcvPartnerAround.setNestedScrollingEnabled(false);
            rcvPartnerAround.setLayoutManager(layoutManager);
            rcvPartnerAround.addItemDecoration(dividerItemDecoration);
            PartnerAroundAdapter partnerAroundAdapter = new PartnerAroundAdapter(lstPartnerAround, this);
            rcvPartnerAround.setAdapter(partnerAroundAdapter);
        } else {
            rcvPartnerAround.setVisibility(View.GONE);
        }
    }

    @Override
    public void initViewLabel(View view) {
        super.initViewLabel(view);
        LanguageHelper.getValueByViewId(txtNearPlaces);
    }

    private boolean checkGeoPartnerEmpty(DetailPartner partner) {
        return partner != null && (StringUtils.isEmpty(partner.getGeoLat()) || StringUtils.isEmpty(partner.getGeoLng()));
    }

    @OnClick(R.id.btnShare)
    public void onButtonClick(View view) {
        if (partner != null) {
            String urlShare = Constant.URL_HOST_IMAGE + partner.getShareUrl();
            if (StringUtils.isNotBlank(urlShare)) {
                share = new Share(partner.getName(), urlShare, "");
                bottomShareDialog = DialogUtils.showSheetShareDialog(mMainActivity, share);

            }
        }
    }
}
