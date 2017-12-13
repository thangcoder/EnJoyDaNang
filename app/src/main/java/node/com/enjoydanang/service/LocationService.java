package node.com.enjoydanang.service;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import node.com.enjoydanang.GlobalApplication;
import node.com.enjoydanang.constant.Extras;
import node.com.enjoydanang.provider.LocationProvider;
import node.com.enjoydanang.utils.event.OnFindLastLocationCallback;

/**
 * Author: Tavv
 * Created on 08/12/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class LocationService extends Service implements LocationListener, OnFindLastLocationCallback {

    public Location mLastLocation;

    public LocationProvider mLocationProvider;

    private LocalBroadcastManager mLocalBroadcastManager;

    private final IBinder mBinder = new LocalBinder();

    public boolean isEnableLocation;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if (mLocalBroadcastManager == null) {
            mLocalBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
        }
        mLocationProvider = new LocationProvider(GlobalApplication.getGlobalApplicationContext(), this, this);
        mLocationProvider.onResume();
        return mBinder;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLocationProvider != null) {
            mLocationProvider.onPause();
        }
    }


    public Location getLastLocation() {
        return mLastLocation;
    }

    public void setLastLocation(Location mLastLocation) {
        this.mLastLocation = mLastLocation;
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        updateOnChangeLocationBroadcast(mLastLocation);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void updateOnChangeLocationBroadcast(Location location) {
        Intent intent = new Intent(Extras.KEY_RECEIVER_LOCATION_ON_FOUND_FILTER);
        Bundle bundle = new Bundle();
        bundle.putParcelable(Extras.EXTRAS_RECEIVER_LOCATION, location);
        intent.putExtra(Extras.KEY_RECEIVER_LOCATION, bundle);
        mLocalBroadcastManager.sendBroadcast(intent);
    }

    private void updateOnFoundLocationBroadcast(Location location) {
        Intent intent = new Intent(Extras.KEY_RECEIVER_LOCATION_FILTER);
        Bundle bundle = new Bundle();
        bundle.putParcelable(Extras.EXTRAS_RECEIVER_LOCATION, location);
        intent.putExtra(Extras.KEY_RECEIVER_LOCATION, bundle);
        mLocalBroadcastManager.sendBroadcast(intent);
    }

    @Override
    public void onFound(Location location) {
        mLastLocation = location;
        updateOnFoundLocationBroadcast(mLastLocation);
    }


    public class LocalBinder extends Binder {

        public LocationService getService() {
            return LocationService.this;
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mLocationProvider.onPause();
        return super.onUnbind(intent);
    }

}
