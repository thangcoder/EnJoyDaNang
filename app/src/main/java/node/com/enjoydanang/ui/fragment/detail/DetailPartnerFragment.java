package node.com.enjoydanang.ui.fragment.detail;

import android.Manifest;
import android.app.Activity;
import android.location.Location;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.NestedScrollView;
import android.text.Html;
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
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import node.com.enjoydanang.MvpFragment;
import node.com.enjoydanang.R;
import node.com.enjoydanang.api.model.Repository;
import node.com.enjoydanang.common.Common;
import node.com.enjoydanang.constant.AppError;
import node.com.enjoydanang.model.DetailPartner;
import node.com.enjoydanang.model.Partner;
import node.com.enjoydanang.model.PartnerAlbum;
import node.com.enjoydanang.utils.ImageUtils;
import node.com.enjoydanang.utils.Utils;
import node.com.enjoydanang.utils.event.OnFindLastLocationCallback;
import node.com.enjoydanang.utils.helper.LocationHelper;
import node.com.enjoydanang.utils.widget.CustomMapView;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Author: Tavv
 * Created on 10/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class DetailPartnerFragment extends MvpFragment<DetailPartnerPresenter> implements iDetailPartnerView,
        OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, ActivityCompat.OnRequestPermissionsResultCallback,
        OnFindLastLocationCallback {
    private static final String TAG = DetailPartnerFragment.class.getSimpleName();
    private static final int REQUEST_PERMISSION_RESULT = 0x2;
    private static final float INIT_ZOOM_LEVEL = 17.0f;
    private static final int DURATION_SLIDE = 3000;

    @BindView(R.id.txtTitle)
    TextView txtTitle;

    @BindView(R.id.ratingBar)
    RatingBar ratingBar;

    @BindView(R.id.txtContent)
    TextView txtContent;

    @BindView(R.id.imgPartner)
    SimpleDraweeView imgPartner;

    @BindView(R.id.webview)
    WebView mWebView;

    @BindView(R.id.slider)
    SliderLayout slider;

    @BindView(R.id.mapView)
    CustomMapView mMapView;

    @BindView(R.id.progress_bar)
    ProgressBar prgLoading;

    @BindView(R.id.lrlContentDetail)
    LinearLayout lrlContentDetail;

    @BindView(R.id.scrollDetailPartner)
    NestedScrollView scrollDetailPartner;

    private GoogleMap mGoogleMap;

    private DetailPartner detailPartner;

    private Location mLastLocation;

    private LocationHelper locationHelper;

    private Partner partner;

    public static DetailPartnerFragment newInstance(Partner partner) {
        DetailPartnerFragment fragment = new DetailPartnerFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG, partner);
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
//        initGoogleClient();
        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.setViewParent(scrollDetailPartner);
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
                initWebView();
                locationHelper = new LocationHelper(getActivity(), DetailPartnerFragment.this);
                locationHelper.checkpermission();
                locationHelper.buildGoogleApiClient(DetailPartnerFragment.this, DetailPartnerFragment.this);
                Bundle bundle = getArguments();
                if (bundle != null) {
                    partner = (Partner) bundle.getSerializable(TAG);
                    if (partner != null) {
                        mvpPresenter.getAllDataHome(partner.getId());
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
        Toast.makeText(mMainActivity, desc, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void unKnownError() {

    }


    @Override
    public void onFetchDetailPartnerSuccess(Repository<DetailPartner> data) {
        if (Utils.isNotEmptyContent(data)) {
            mMapView.getMapAsync(this);
            DetailPartner detailPartner = data.getData().get(0);
            this.detailPartner = detailPartner;
            txtTitle.setText(detailPartner.getName());
            ImageUtils.loadImageWithFreso(imgPartner, detailPartner.getPicture());
            if (Build.VERSION.SDK_INT >= 24) {
                txtContent.setText(Html.fromHtml(detailPartner.getDescription(), Html.FROM_HTML_MODE_LEGACY));
            } else {
                txtContent.setText(Html.fromHtml(detailPartner.getDescription()));
            }
            ratingBar.setRating(detailPartner.getStarReview());
            ratingBar.setFocusable(false);

            loadVideo(detailPartner.getVideo());
        }
    }

    @Override
    public void onFetchFailure(AppError appError) {
        Toast.makeText(mMainActivity, appError.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFetchSlideSuccess(List<PartnerAlbum> images) {
        HashMap<String, String> sources = new HashMap<>();
        int length = images.size();
        for (int i = 0; i < length; i++) {
            sources.put(images.get(i).getTitle() + " [Slide " + i + " ]", images.get(i).getPicture());
        }
        for (String name : sources.keySet()) {
            DefaultSliderView textSliderView = new DefaultSliderView(getContext());
            textSliderView
                    .image(sources.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit);
            slider.addSlider(textSliderView);
        }
        slider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        slider.setCustomAnimation(new DescriptionAnimation());
        slider.setDuration(DURATION_SLIDE);
    }

    @Override
    public void onFetchAllData(List<DetailPartner> lstDetailPartner, List<PartnerAlbum> lstAlbum) {
        setDataAlbum(lstAlbum);
        setDataDetail(lstDetailPartner);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        mGoogleMap = googleMap;
        locationHelper.setGoogleMap(mGoogleMap);
        mGoogleMap.getUiSettings().setMapToolbarEnabled(false);
        drawRouteToPartner();
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        prgLoading.setVisibility(View.GONE);
        if (EasyPermissions.hasPermissions(getContext(), Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            loadMapView(detailPartner, googleMap);
        } else {
            locationHelper.checkpermission();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLastLocation = locationHelper.getLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        locationHelper.connectApiClient();
    }

    @Override
    public void onFound(Location location) {
        mLastLocation = location;
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
    }

    private void loadMapView(DetailPartner detailPartner, GoogleMap googleMap) {
        try {
            if (detailPartner != null) {
                double longtitude = Double.parseDouble(StringUtils.trim(detailPartner.getGeoLng()));
                double latitude = Double.parseDouble(StringUtils.trim(detailPartner.getGeoLat()));
                LatLng point = new LatLng(latitude, longtitude);
                MarkerOptions marker = new MarkerOptions();
                marker.position(point).title(detailPartner.getName()).draggable(false)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                CameraPosition cameraPosition = CameraPosition.builder().target(point).zoom(INIT_ZOOM_LEVEL).bearing(0).tilt(45).build();
                googleMap.addMarker(marker);
                googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        } catch (Exception ex) {
            mMapView.setVisibility(View.GONE);
            ex.printStackTrace();
        }
        lrlContentDetail.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mMapView != null) {
            mMapView.onResume();
        }
        if (mWebView != null) {
            mWebView.resumeTimers();
            mWebView.onResume();
        }
        if (locationHelper != null) {
            locationHelper.checkPlayServices();
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        if (mWebView != null) {
            mWebView.onPause();
            mWebView.pauseTimers();
        }
        if (mMapView != null) {
            mMapView.onPause();
        }


    }

    @Override
    public void onStop() {
        if(slider != null){
            slider.stopAutoCycle();
        }
        super.onStop();
        if(mMapView != null){
            mMapView.onStop();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if(mMapView != null){
            mGoogleMap.clear();
            mMapView.onLowMemory();
            System.gc();
        }
    }

    @Override
    public void onDestroy() {
        if(mWebView != null){
            mWebView.loadUrl("about:blank");
            mWebView.destroy();
            mWebView = null;
        }
        if(mGoogleMap != null){
            mGoogleMap.clear();
        }
        mMapView = null;
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void drawRouteToPartner() {
        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                double longtitude = Double.parseDouble(StringUtils.trim(detailPartner.getGeoLng()));
                double latitude = Double.parseDouble(StringUtils.trim(detailPartner.getGeoLat()));
                LatLng partnerPoint = new LatLng(latitude, longtitude);
                LatLng currentPoint = getCurrentLocation();
                if (currentPoint != null) {
                    startIntentMapsView(partnerPoint, currentPoint);
                }
                return true;
            }
        });
        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
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
        mLastLocation = locationHelper.getLocation();
        if (mLastLocation != null) {
            double latitude = mLastLocation.getLatitude();
            double longitude = mLastLocation.getLongitude();
            return new LatLng(latitude, longitude);
        } else {
            showToast("Couldn't get the location. Make sure location is enabled on the device");
            return null;
        }
    }

    // Permission check functions
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // redirects to utils
        locationHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);

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
        HashMap<String, String> sources = new HashMap<>();
        int length = images.size();
        for (int i = 0; i < length; i++) {
            sources.put(images.get(i).getTitle() + " [Slide " + i + " ]", images.get(i).getPicture());
        }
        for (String name : sources.keySet()) {
            DefaultSliderView textSliderView = new DefaultSliderView(getContext());
            textSliderView
                    .image(sources.get(name))
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
            mMapView.getMapAsync(this);
            DetailPartner detailPartner = lstDetailPartner.get(0);
            this.detailPartner = detailPartner;
            txtTitle.setText(detailPartner.getName());
            ImageUtils.loadImageWithFreso(imgPartner, detailPartner.getPicture());
            if (Build.VERSION.SDK_INT >= 24) {
                txtContent.setText(Html.fromHtml(detailPartner.getDescription(), Html.FROM_HTML_MODE_LEGACY));
            } else {
                txtContent.setText(Html.fromHtml(detailPartner.getDescription()));
            }
            ratingBar.setRating(detailPartner.getStarReview());
            ratingBar.setFocusable(false);

            loadVideo(detailPartner.getVideo());
        }

    }

}
