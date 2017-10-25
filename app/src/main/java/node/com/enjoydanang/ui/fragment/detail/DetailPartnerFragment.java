package node.com.enjoydanang.ui.fragment.detail;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import node.com.enjoydanang.MvpFragment;
import node.com.enjoydanang.R;
import node.com.enjoydanang.api.model.Repository;
import node.com.enjoydanang.constant.AppError;
import node.com.enjoydanang.model.DetailPartner;
import node.com.enjoydanang.model.PartnerAlbum;
import node.com.enjoydanang.utils.ImageUtils;
import node.com.enjoydanang.utils.ScreenUtils;
import node.com.enjoydanang.utils.Utils;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Author: Tavv
 * Created on 10/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class DetailPartnerFragment extends MvpFragment<DetailPartnerPresenter> implements iDetailPartnerView,
        OnMapReadyCallback, EasyPermissions.PermissionCallbacks {
    private static final String TAG = DetailPartnerFragment.class.getSimpleName();
    private static final int REQUEST_PERMISSION_RESULT = 0x2;
    public static final float INIT_ZOOM_LEVEL = 17.0f;

    @BindView(R.id.txtTitle)
    TextView txtTitle;

    @BindView(R.id.ratingBar)
    RatingBar ratingBar;

    @BindView(R.id.txtContent)
    TextView txtContent;

    @BindView(R.id.imgPartner)
    ImageView imgPartner;

    @BindView(R.id.webview)
    WebView mWebView;

    @BindView(R.id.slider)
    SliderLayout slider;

    @BindView(R.id.mapView)
    MapView mMapView;


    private DetailPartner partner;

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
        mBaseActivity.setTitle(Utils.getString(R.string.Detail_Screen_Title));

    }


    @Override
    protected void setEvent(View view) {

    }

    private void initWebView() {
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        mWebView.setWebViewClient(new WebClient());
        webSettings.setBuiltInZoomControls(true);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mvpPresenter = createPresenter();
        initWebView();
        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
        }

        Bundle bundle = getArguments();
        if (bundle != null) {
            int partnerId = bundle.getInt(TAG);
            mvpPresenter.getDetailPartner(partnerId);
            mvpPresenter.getSlideByPartnerId(partnerId);
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
    public void onFetchDetailPartnerSuccess(Repository<DetailPartner> data) {
        if (Utils.isNotEmptyContent(data)) {
            mMapView.getMapAsync(this);
            DetailPartner detailPartner = data.getData().get(0);
            partner = detailPartner;
            txtTitle.setText(detailPartner.getName());
            ImageUtils.loadImageNoRadius(getContext(), imgPartner, detailPartner.getPicture());
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
        slider.setDuration(3000);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        if (EasyPermissions.hasPermissions(getContext(), Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            loadMapView(partner, googleMap);
        } else {
            requestPermissions();
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Log.i(TAG, "onPermissionsGranted " + requestCode);
        Log.i(TAG, "onPermissionsGranted " + perms);
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.i(TAG, "onPermissionsDenied " + requestCode);
    }

    @AfterPermissionGranted(REQUEST_PERMISSION_RESULT)
    private void requestPermissions() {
        String[] perm = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        EasyPermissions.requestPermissions(this, "The application request permission to showing location", REQUEST_PERMISSION_RESULT, perm);
    }

    private class WebClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return false;

        }
    }

    private void loadVideo(String url) {
        if (StringUtils.isBlank(url)) {
            mWebView.setVisibility(View.GONE);
        } else {
            String frameVideo = Utils.reFormatYoutubeUrl(ScreenUtils.getScreenWidth() / 3, 300, url);
            mWebView.loadData(frameVideo, "text/html", "utf-8");
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
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        mWebView.resumeTimers();
        mWebView.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
        mWebView.onPause();
        mWebView.pauseTimers();
        mMapView.onPause();
    }

    @Override
    public void onStop() {
        slider.stopAutoCycle();
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        mWebView.loadUrl("about:blank");
        mWebView.destroy();
        mWebView = null;
        super.onDestroy();
    }

}
