package node.com.enjoydanang.ui.fragment.search;

import android.graphics.Bitmap;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import node.com.enjoydanang.MvpFragment;
import node.com.enjoydanang.R;
import node.com.enjoydanang.model.Partner;
import node.com.enjoydanang.service.LocationService;
import node.com.enjoydanang.utils.BitmapUtil;
import node.com.enjoydanang.utils.JsonUtils;
import node.com.enjoydanang.utils.Utils;
import node.com.enjoydanang.utils.event.OnFetchSearchResult;
import node.com.enjoydanang.utils.helper.LocationHelper;

/**
 * Author: Tavv
 * Created on 12/12/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class MapResultFragment extends MvpFragment<SearchPresenter> implements OnMapReadyCallback {
    private static final String TAG = MapResultFragment.class.getSimpleName();

    private static final float INIT_ZOOM_LEVEL = 14f;

    SupportMapFragment mMapFragment;

    private GoogleMap mGoogleMap;

    private LocationHelper mLocationHelper;

    private Location mCurrentLocation;

    private LocationService mLocationService;

    private boolean isMapAlreadyInit;

    private List<Partner> lstPartner;

    private static final int DEFAULT_RADIUS = 1000; // 1 kilometer

    private static final int DEFAULT_MARKER_ICON_SIZE = 70; // 1 kilometer

    private OnFetchSearchResult mOnFetchSearchResult;

    public static String arrayPartner;

    public static MapResultFragment getIntance(OnFetchSearchResult onFetchSearchResult, ArrayList<Partner> data) {
        MapResultFragment fragment = new MapResultFragment();
        fragment.setFetchSearchResult(onFetchSearchResult);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(TAG, data);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static MapResultFragment getIntance(OnFetchSearchResult onFetchSearchResult, String data) {
        MapResultFragment fragment = new MapResultFragment();
        fragment.setFetchSearchResult(onFetchSearchResult);
        arrayPartner = data;
        return fragment;
    }

    @Override
    protected SearchPresenter createPresenter() {
        return null;
    }

    @Override
    protected void init(View view) {
        mMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapView);
        if (mMainActivity != null) {
            mLocationHelper = mMainActivity.mLocationHelper;
            mLocationService = mMainActivity.getLocationService();
            if (mLocationService != null) {
                mCurrentLocation = mLocationService.getLastLocation();
            }
        }
        try {
            if (mMapFragment != null) {
                mMapFragment.getMapAsync(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void setEvent(View view) {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (StringUtils.isNotBlank(arrayPartner)) {
            lstPartner = JsonUtils.convertJsonObjectToList(arrayPartner, Partner[].class);
        }
    }

    @Override
    public int getRootLayoutId() {
        return R.layout.tab_search_map;
    }

    @Override
    public void bindView(View view) {
        ButterKnife.bind(this, view);
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        isMapAlreadyInit = true;
        mGoogleMap = googleMap;
        mLocationHelper.setGoogleMap(mGoogleMap);
        mGoogleMap.getUiSettings().setMapToolbarEnabled(false);
        mGoogleMap.setMyLocationEnabled(true);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        loadMapView(mLocationService.getLastLocation());
        drawNearPlace();
//        drawMap();
    }

    private void drawNearPlace() {
        Bundle bundle = getArguments();
        Bitmap btmMarker = BitmapUtil.getBitmapFromDrawable(getContext(), R.drawable.marker_thumb, DEFAULT_MARKER_ICON_SIZE, DEFAULT_MARKER_ICON_SIZE);
        if (bundle != null) {
            lstPartner = bundle.getParcelableArrayList(TAG);
            if (isMapAlreadyInit && mCurrentLocation != null) {
                double lat = mCurrentLocation.getLatitude();
                double lng = mCurrentLocation.getLongitude();
                LatLng latLng = new LatLng(lat, lng);
                mGoogleMap.addCircle(new CircleOptions()
                        .center(latLng)
                        .radius(DEFAULT_RADIUS)
                        .strokeWidth(0f)
                        .fillColor(Utils.getColorRes(R.color.color_circle_fill_map)));
            }
            if (CollectionUtils.isNotEmpty(lstPartner)) {
                for (Partner partner : lstPartner) {
                    double lat = Double.parseDouble(partner.getGeoLat());
                    double lng = Double.parseDouble(partner.getGeoLng());
                    LatLng point = new LatLng(lat, lng);
                    MarkerOptions marker = new MarkerOptions();
                    if (mGoogleMap != null) {
                        BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(btmMarker);
                        marker.position(point).title(partner.getName()).draggable(false)
                                .icon(icon);
                        mGoogleMap.addMarker(marker);
                    }
                }
            }
        }
        mOnFetchSearchResult.onFetchCompleted(TAG);
    }


    private void drawMap() {
        Bitmap btmMarker = BitmapUtil.getBitmapFromDrawable(getContext(), R.drawable.marker_thumb, 50, 50);
        if (btmMarker != null) {
            if (isMapAlreadyInit && mCurrentLocation != null) {
                double lat = mCurrentLocation.getLatitude();
                double lng = mCurrentLocation.getLongitude();
                LatLng latLng = new LatLng(lat, lng);
                mGoogleMap.addCircle(new CircleOptions()
                        .center(latLng)
                        .radius(DEFAULT_RADIUS)
                        .strokeWidth(0f)
                        .fillColor(Utils.getColorRes(R.color.color_circle_fill_map)));
            }
            if (CollectionUtils.isNotEmpty(lstPartner)) {
                for (Partner partner : lstPartner) {
                    double lat = Double.parseDouble(partner.getGeoLat());
                    double lng = Double.parseDouble(partner.getGeoLng());
                    LatLng point = new LatLng(lat, lng);
                    MarkerOptions marker = new MarkerOptions();
                    if (mGoogleMap != null) {
                        BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(btmMarker);
                        marker.position(point).title(partner.getName()).draggable(false)
                                .icon(icon);
                        mGoogleMap.addMarker(marker);
                    }
                }
            }
        }
        mOnFetchSearchResult.onFetchCompleted(TAG);
    }


    private void loadMapView(Location currentLocation) {
        if (currentLocation != null) {
            LatLng point = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            MarkerOptions marker = new MarkerOptions();
            Address address = mLocationHelper.getAddress(currentLocation.getLatitude(), currentLocation.getLongitude());
            String titleMarker = mLocationHelper.getFullInfoByAddress(address);
            if (mGoogleMap != null) {
                marker.position(point).title(titleMarker).draggable(false)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                CameraPosition cameraPosition = CameraPosition.builder().target(point).zoom(INIT_ZOOM_LEVEL).bearing(0).tilt(45).build();
                mGoogleMap.addMarker(marker);
                mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        }
    }

    public void setFetchSearchResult(OnFetchSearchResult onFetchSearchResult) {
        mOnFetchSearchResult = onFetchSearchResult;
    }

}
