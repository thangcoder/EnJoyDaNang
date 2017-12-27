package node.com.enjoydanang.ui.fragment.search;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import node.com.enjoydanang.MvpFragment;
import node.com.enjoydanang.R;
import node.com.enjoydanang.annotation.DialogType;
import node.com.enjoydanang.constant.AppError;
import node.com.enjoydanang.model.InfoWindow;
import node.com.enjoydanang.model.Partner;
import node.com.enjoydanang.model.UserInfo;
import node.com.enjoydanang.service.LocationService;
import node.com.enjoydanang.ui.fragment.detail.dialog.DetailHomeDialogFragment;
import node.com.enjoydanang.utils.BitmapUtil;
import node.com.enjoydanang.utils.DialogUtils;
import node.com.enjoydanang.utils.LocationUtils;
import node.com.enjoydanang.utils.Utils;
import node.com.enjoydanang.utils.event.OnBackFragmentListener;
import node.com.enjoydanang.utils.event.OnItemClickListener;
import node.com.enjoydanang.utils.helper.LanguageHelper;
import node.com.enjoydanang.utils.helper.LocationHelper;
import node.com.enjoydanang.utils.helper.SoftKeyboardManager;

/**
 * Author: Tavv
 * Created on 14/12/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public class MapFragment extends MvpFragment<SearchPresenter> implements iSearchView,
        SearchView.OnQueryTextListener, OnItemClickListener, OnMapReadyCallback,
        GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMarkerClickListener,
        OnBackFragmentListener, GoogleMap.OnInfoWindowCloseListener {
    private static final String TAG = MapFragment.class.getSimpleName();

    private static final float INIT_ZOOM_LEVEL = 14f;

    private SupportMapFragment mMapFragment;

    private GoogleMap mGoogleMap;

    private LocationHelper mLocationHelper;

    private LocationService mLocationService;

    private boolean isMapAlreadyInit;

    private List<Partner> lstPartnerResultSearch;

    private List<Partner> lstPartnerNearPlace;

    private static final int DEFAULT_RADIUS = 1000; // 1 kilometer

    private static final int DEFAULT_MARKER_ICON_SIZE = 50; // 1 kilometer

    private static final int DELAY_INTERVAL = 1000;

    @BindView(R.id.search)
    SearchView searchView;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.frame)
    FrameLayout frame;

    @BindView(R.id.lnlSearch)
    RelativeLayout lnlSearch;

    @BindView(R.id.rlrContent)
    LinearLayout rlrContent;

    @BindView(R.id.lrlInfoPartner)
    LinearLayout lrlInfoPartner;

    @BindView(R.id.rllPartnerPlaces)
    LinearLayout rllPartnerPlaces;

    @BindView(R.id.rcvSearchResult)
    RecyclerView rcvSearchResult;

    @BindView(R.id.rcvPartnerNearPlace)
    RecyclerView rcvPartnerNearPlace;

    @BindView(R.id.txtSearching)
    TextView txtSearching;

    @BindView(R.id.txtEmpty)
    TextView txtEmpty;

    @BindView(R.id.txtPartnerName)
    TextView txtPartnerName;

    @BindView(R.id.txtDistance)
    TextView txtDistance;

    private SearchResultAdapter mSearchQueryAdapter;

    private UserInfo userInfo;

    private Location currentLocation;

    private static final int DEFAULT_DISTANCE = 1;

    private boolean isLocationNotFound;

    private String myCurrentAddress;

    private Marker currentMarker;

    private boolean isResultSearchQueryVisible;

    private List<Marker> lstMarkers;

    private boolean isDistanceTextClick;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mvpPresenter = createPresenter();
    }

    private void fetchNearPlace() {
        if (currentLocation != null) {
            String geoLat = String.valueOf(currentLocation.getLatitude());
            String geoLng = String.valueOf(currentLocation.getLongitude());
            mvpPresenter.searchPlaceByCurrentLocation(userInfo.getUserId(), DEFAULT_DISTANCE, geoLat, geoLng);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (StringUtils.isNotEmpty(query)) {
            showResultContainer(true);
            mvpPresenter.searchWithTitle(query);
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (StringUtils.isNotEmpty(newText)) {
            mvpPresenter.searchWithTitle(newText);
            showResultContainer(true);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            if(mSearchQueryAdapter != null){
                mSearchQueryAdapter.clearAllItem();
                Utils.hideSoftKeyboard(getActivity());
                showResultContainer(false);                
            }
            progressBar.setVisibility(View.GONE);
        }
        return false;
    }

    private void showResultContainer(final boolean isShow) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isResultSearchQueryVisible = isShow;
                rcvSearchResult.setVisibility(isShow ? View.VISIBLE : View.GONE);
                rlrContent.setVisibility(isShow ? View.GONE : View.VISIBLE);
            }
        }, DELAY_INTERVAL);
    }

    @Override
    public void showToast(String desc) {

    }

    @Override
    public void unKnownError() {

    }

    @Override
    public void onClick(View view, int position) {
        ViewParent parent = view.getParent();
        DetailHomeDialogFragment dialog = null;
        if (parent.equals(rcvSearchResult)) {
            if (CollectionUtils.isNotEmpty(lstPartnerResultSearch)) {
                dialog = DetailHomeDialogFragment.newInstance(lstPartnerResultSearch.get(position), false);
            }
        } else {
            Partner partner = null;
            if (CollectionUtils.isNotEmpty(lstPartnerNearPlace)) {
                partner = lstPartnerNearPlace.get(position);
            }
            if (view.getId() == R.id.txtDistance) {
                isDistanceTextClick = true;
                if (CollectionUtils.isNotEmpty(lstMarkers)) {
                    for (Marker marker : lstMarkers) {
                        InfoWindow infoWindow = (InfoWindow) marker.getTag();
                        if (infoWindow != null && partner != null) {
                            if (infoWindow.getPartnerId() == partner.getId()) {
                                marker.showInfoWindow();
                                moveCameraWithGeo(partner.getGeoLat(), partner.getGeoLng());
                                setInfoOnMarkerClick(marker);
                                break;
                            }
                        }
                    }
                }
            } else {
                dialog = DetailHomeDialogFragment.newInstance(partner, false);
            }
        }
        if (dialog != null) {
            DialogUtils.openDialogFragment(mFragmentManager, dialog);
        }
    }

    @Override
    public void OnQuerySearchResult(List<Partner> lstPartner) {
        isResultSearchQueryVisible = true;
        mSearchQueryAdapter.updateResult(lstPartner);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onResultPlaceByRange(List<Partner> lstPartner) {
        if (CollectionUtils.isNotEmpty(lstPartner)) {
            lstPartnerNearPlace = lstPartner;
            mvpPresenter.getAddressByGeoLocation(lstPartner);
        } else {
            rcvPartnerNearPlace.setVisibility(View.GONE);
            txtEmpty.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onGetLocationAddress(List<String> lstAddress) {
        for (int i = 0; i < lstAddress.size(); i++) {
            lstPartnerNearPlace.get(i).setLocationAddress(lstAddress.get(i));
        }
        SearchPartnerResultAdapter mSearchNearResultAdapter = new SearchPartnerResultAdapter(lstPartnerNearPlace, getContext(), this);

        rcvPartnerNearPlace.setAdapter(mSearchNearResultAdapter);
        drawNearPlace();
    }

    @Override
    public void onError(AppError error) {
        DialogUtils.showDialog(getContext(), DialogType.WRONG, DialogUtils.getTitleDialog(3), error.getMessage());
    }

    @Override
    protected SearchPresenter createPresenter() {
        return new SearchPresenter(this);
    }

    @Override
    protected void init(View view) {
        hideProgress(false);
        enableSearchView(searchView, false);
        mMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapView);
        setLayoutWeight(rllPartnerPlaces, 0.5f);
        if (LocationUtils.isGpsEnabled() && LocationUtils.isLocationEnabled()) {
            userInfo = Utils.getUserInfo();
            getComponentFromMain();
            initRecyclerView();
            initRecyclerViewPartnerNear();
            try {
                if (mMapFragment != null) {
                    mMapFragment.getMapAsync(this);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            hideProgress(true);
            enableSearchView(searchView, true);
            txtEmpty.setVisibility(View.VISIBLE);
            rcvPartnerNearPlace.setVisibility(View.GONE);
            DialogUtils.showDialog(getContext(), DialogType.INFO, Utils.getLanguageByResId(R.string.Permisstion_Title),
                    Utils.getLanguageByResId(R.string.Map_Location_NotFound));
        }
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rcvSearchResult.setLayoutManager(layoutManager);
        lstPartnerResultSearch = new ArrayList<>();
        mSearchQueryAdapter = new SearchResultAdapter(getContext(), lstPartnerResultSearch, this);
        rcvSearchResult.setNestedScrollingEnabled(false);
        rcvSearchResult.setAdapter(mSearchQueryAdapter);
    }

    private void initRecyclerViewPartnerNear() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
                layoutManager.getOrientation());
        rcvPartnerNearPlace.setLayoutManager(layoutManager);
        rcvPartnerNearPlace.setNestedScrollingEnabled(false);
        rcvPartnerNearPlace.addItemDecoration(dividerItemDecoration);
    }

    @Override
    protected void setEvent(View view) {
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_search_v2;
    }

    @Override
    public void bindView(View view) {
        ButterKnife.bind(this, view);
        customSearchView();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        isMapAlreadyInit = true;
        mGoogleMap = googleMap;
        if(mLocationHelper != null && googleMap != null){
            mLocationHelper.setGoogleMap(googleMap);
        }
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.setMyLocationEnabled(true);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (LocationUtils.isGpsEnabled() && LocationUtils.isLocationEnabled() && !isLocationNotFound) {
            loadMapView(mLocationService.getLastLocation());
            fetchNearPlace();
        } else {
            hideProgress(true);
            enableSearchView(searchView, true);
            txtEmpty.setVisibility(View.VISIBLE);
            rcvPartnerNearPlace.setVisibility(View.GONE);
        }
    }

    private void customSearchView() {
        EditText searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(Utils.getColorRes(R.color.color_title_category));
        searchEditText.setHintTextColor(Utils.getColorRes(R.color.material_grey_200));
        try {
            Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
            f.setAccessible(true);
            f.set(searchEditText, R.drawable.cursor);// set textCursorDrawable to null
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initViewLabel(View view) {
        super.initViewLabel(view);
        LanguageHelper.getValueByViewId(searchView, txtSearching, txtPartnerName, txtEmpty);
        String defaultDistance = Utils.getLanguageByResId(R.string.Map_Distance);
        if (StringUtils.isNotBlank(defaultDistance)) {
            txtDistance.setText(defaultDistance.concat(": -.-"));
        }
    }

    private void getComponentFromMain() {
        if (mMainActivity != null) {
            if (mMainActivity.getLocationService() != null) {
                mLocationService = mMainActivity.getLocationService();
                currentLocation = mMainActivity.getLocationService().getLastLocation();
                isLocationNotFound = currentLocation == null;
            } else {
                isLocationNotFound = true;
                DialogUtils.showDialog(getContext(), DialogType.INFO, Utils.getLanguageByResId(R.string.Permisstion_Title),
                        Utils.getLanguageByResId(R.string.Map_Location_NotFound));
            }
            mLocationHelper = mMainActivity.mLocationHelper;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mMapFragment != null) {
            mMapFragment.onStop();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mMapFragment != null && mGoogleMap != null) {
            mMapFragment.onLowMemory();
            mGoogleMap.clear();
            System.gc();
        }
    }

    @Override
    public void onDestroyView() {
        if (mGoogleMap != null) {
            mGoogleMap.clear();
        }
        if (mMapFragment != null) {
            mMapFragment.onDestroy();
            mMapFragment = null;
        }
        System.gc();
        super.onDestroyView();
    }

    private void drawNearPlace() {
        Bitmap btmMarker = BitmapUtil.getBitmapFromDrawable(getContext(), R.drawable.marker_thumb, DEFAULT_MARKER_ICON_SIZE, DEFAULT_MARKER_ICON_SIZE);
        if (isMapAlreadyInit && currentLocation != null) {
            double lat = currentLocation.getLatitude();
            double lng = currentLocation.getLongitude();
            LatLng latLng = new LatLng(lat, lng);
            mGoogleMap.addCircle(new CircleOptions()
                    .center(latLng)
                    .radius(DEFAULT_RADIUS)
                    .strokeWidth(0f)
                    .fillColor(Utils.getColorRes(R.color.color_circle_fill_map)));
        }
        if (CollectionUtils.isNotEmpty(lstPartnerNearPlace)) {
            lstMarkers = new ArrayList<>();
            for (Partner partner : lstPartnerNearPlace) {
                double lat = Double.parseDouble(partner.getGeoLat());
                double lng = Double.parseDouble(partner.getGeoLng());
                LatLng point = new LatLng(lat, lng);
                MarkerOptions markerOptions = new MarkerOptions();
                CustomInfoWindowGoogleMap infoWindowGoogleMap = new CustomInfoWindowGoogleMap(getContext());
                if (mGoogleMap != null) {
                    mGoogleMap.setInfoWindowAdapter(infoWindowGoogleMap);
                    BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(btmMarker);
                    markerOptions.position(point).title(partner.getName()).draggable(false)
                            .icon(icon);
                    addMarkerInfo(partner, markerOptions, mGoogleMap, lstMarkers);
                }
            }
            mGoogleMap.setOnMarkerClickListener(this);
            mGoogleMap.setOnInfoWindowClickListener(this);
            mGoogleMap.setOnInfoWindowCloseListener(this);
        }
        hideProgress(true);
        enableSearchView(searchView, true);
    }

    private void addMarkerInfo(Partner partner, MarkerOptions markerOptions, GoogleMap googleMap, List<Marker> lstMarkers) {
        if (partner != null && markerOptions != null && googleMap != null) {
            InfoWindow infoWindow = new InfoWindow(partner.getId(), partner.getName(),
                    partner.getLocationAddress(), partner.getPicture(),
                    partner.getDistance(), partner.getCategoryName());
            Marker marker = googleMap.addMarker(markerOptions);
            marker.setTag(infoWindow);
            lstMarkers.add(marker);
        }
    }


    private void loadMapView(Location currentLocation) {
        if (currentLocation != null) {
            LatLng point = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            Address address = mLocationHelper.getAddress(currentLocation.getLatitude(), currentLocation.getLongitude());
            String titleMarker = mLocationHelper.getFullInfoAddress(address);
            if (currentMarker != null) {
                currentMarker.remove();
            }
            myCurrentAddress = titleMarker;
            if (mGoogleMap != null) {
                markerOptions.position(point).title(titleMarker).draggable(false)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                CameraPosition cameraPosition = CameraPosition.builder().target(point).zoom(INIT_ZOOM_LEVEL).bearing(0).tilt(45).build();
                currentMarker = mGoogleMap.addMarker(markerOptions);
                mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        }
    }

    private void hideProgress(boolean hide) {
        if (hide) {
            progressBar.setVisibility(View.GONE);
            txtSearching.setVisibility(View.GONE);
            frame.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.VISIBLE);
            txtSearching.setVisibility(View.VISIBLE);
            frame.setVisibility(View.GONE);
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        InfoWindow infoWindow = (InfoWindow) marker.getTag();
        if (CollectionUtils.isNotEmpty(lstPartnerNearPlace) && infoWindow != null) {
            lrlInfoPartner.setVisibility(View.VISIBLE);
            setLayoutWeight(rllPartnerPlaces, 0.4f);
            Partner result = null;
            for (Partner partner : lstPartnerNearPlace) {
                if (partner.getId() == infoWindow.getPartnerId()) {
                    result = partner;
                    break;
                }
            }
            if (result != null) {
                DetailHomeDialogFragment dialog = DetailHomeDialogFragment.newInstance(result, false);
                DialogUtils.openDialogFragment(mFragmentManager, dialog);
            }
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return setInfoOnMarkerClick(marker);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (lnlSearch != null) {
            SoftKeyboardManager.hideSoftKeyboard(getContext(), lnlSearch.getWindowToken(), 0);
        }
        if (mLocationService != null) {
            loadMapView(mLocationService.getLastLocation());
        }
    }

    @Override
    public void onBack(boolean isBack) {
        if (isBack && isResultSearchQueryVisible) {
            searchView.setQuery("", false);
            searchView.clearFocus();
            showResultContainer(false);
            isResultSearchQueryVisible = false;
        } else {
            isResultSearchQueryVisible = true;
        }
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface, boolean isBack) {

    }

    public boolean isResultSearchQueryVisible() {
        return isResultSearchQueryVisible;
    }

    private void enableSearchView(View view, boolean enabled) {
        view.setEnabled(enabled);
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                enableSearchView(child, enabled);
            }
        }
    }

    private void moveCameraWithGeo(String geoLat, String geoLng) {
        if (StringUtils.isNotBlank(geoLat)
                && StringUtils.isNotBlank(geoLng)
                && mGoogleMap != null) {
            double latitude = Double.parseDouble(geoLat);
            double longitude = Double.parseDouble(geoLng);
            final LatLng maposition = new LatLng(latitude, longitude);
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(maposition));
            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(INIT_ZOOM_LEVEL));
        }
    }

    private boolean setInfoOnMarkerClick(Marker marker) {
        InfoWindow infoWindow = (InfoWindow) marker.getTag();
        if (infoWindow != null) {
            lrlInfoPartner.setVisibility(View.VISIBLE);
            String strCategory = infoWindow.getCategory().replaceAll("\\s+", " ");
            txtDistance.setText(infoWindow.getDistance() + " - " + strCategory);
            txtPartnerName.setText(infoWindow.getPartnerName());
            setLayoutWeight(rllPartnerPlaces, 0.4f);
            return false;
        } else {
            txtDistance.setText(Utils.getLanguageByResId(R.string.Map_My_Current_Position));
            txtPartnerName.setText(myCurrentAddress);
            return true;
        }
    }

    private void setLayoutWeight(LinearLayout relativeLayout, float weight) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, 0, weight);
        relativeLayout.setLayoutParams(layoutParams);
    }

    @Override
    public void onInfoWindowClose(Marker marker) {
        lrlInfoPartner.setVisibility(View.GONE);
        setLayoutWeight(rllPartnerPlaces, 0.5f);
    }
}
