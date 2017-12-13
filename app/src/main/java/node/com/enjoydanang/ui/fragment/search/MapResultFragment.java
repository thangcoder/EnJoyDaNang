package node.com.enjoydanang.ui.fragment.search;

import android.location.Address;
import android.location.Location;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.commons.collections.CollectionUtils;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.ButterKnife;
import node.com.enjoydanang.MvpFragment;
import node.com.enjoydanang.R;
import node.com.enjoydanang.common.Common;
import node.com.enjoydanang.model.Partner;
import node.com.enjoydanang.service.LocationService;
import node.com.enjoydanang.utils.helper.LocationHelper;

/**
 * Author: Tavv
 * Created on 12/12/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class MapResultFragment extends MvpFragment<SearchPresenter> implements OnMapReadyCallback {
    private static final String TAG = MapResultFragment.class.getSimpleName();

    private static final float INIT_ZOOM_LEVEL = 17f;

    SupportMapFragment mMapFragment;

    private GoogleMap mGoogleMap;

    private LocationHelper mLocationHelper;

    private Location mCurrentLocation;

    private LocationService mLocationService;

    private boolean isMapAlreadyInit;

    public static MapResultFragment getIntance() {
        MapResultFragment fragment = new MapResultFragment();
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
    public int getRootLayoutId() {
        return R.layout.tab_search_map;
    }

    @Override
    public void bindView(View view) {
        ButterKnife.bind(this, view);
    }


    @Override
    public void onResume() {
        super.onResume();
        Common.registerEventBus(this);
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
        Common.unregisterEventBus(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        isMapAlreadyInit = true;
        mGoogleMap = googleMap;
        mLocationHelper.setGoogleMap(mGoogleMap);
        mGoogleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        loadMapView(mLocationService.getLastLocation());
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSearchResultReceive(List<Partner> lstPartners) {
        if (isMapAlreadyInit && mCurrentLocation != null) {
//            double lat = mCurrentLocation.getLatitude();
//            double lng = mCurrentLocation.getLongitude();
//            LatLng latLng = new LatLng(lat, lng);
//            mGoogleMap.addCircle(new CircleOptions()
//                    .center(latLng)
//                    .radius(1000)
//                    .strokeWidth(0f)
//                    .fillColor(0x550000FF));
        }
        if (CollectionUtils.isNotEmpty(lstPartners)) {
            for (Partner partner : lstPartners) {
                double lat = Double.parseDouble(partner.getGeoLat());
                double lng = Double.parseDouble(partner.getGeoLng());
                LatLng point = new LatLng(lat, lng);
                MarkerOptions marker = new MarkerOptions();
                Address address = mLocationHelper.getAddress(lat, lng);
                String titleMarker = mLocationHelper.getFullInfoByAddress(address);
                if (mGoogleMap != null) {
                    marker.position(point).title(titleMarker).draggable(false)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    mGoogleMap.addMarker(marker);
                }
            }
        }
    }
}
