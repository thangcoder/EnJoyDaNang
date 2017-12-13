package node.com.enjoydanang.provider;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;

import node.com.enjoydanang.service.ILocationProvider;
import node.com.enjoydanang.utils.event.OnFindLastLocationCallback;

/**
 * Author: Tavv
 * Created on 08/12/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class LocationProvider implements ILocationProvider {
    private static final String TAG = LocationProvider.class.getSimpleName();

    private final LocationListener locationListener;
    private final LocationManager locationManager;
    private final OnFindLastLocationCallback onFindLastLocationCallback;
    private static final int LOCATION_UPDATE_MIN_TIME_GPS = 1000;
    private static final int LOCATION_UPDATE_DISTANCE_GPS = 0;
    private static final int LOCATION_UPDATE_MIN_TIME_NW = 1000;
    private static final int LOCATION_UPDATE_DISTANCE_NW = 0;
    private static final int LOCATION_OUTDATED_WHEN_OLDER_MS = 1000 * 60 * 10;
    private boolean gpsProviderEnabled, networkProviderEnabled;
    private final Context context;

    public LocationProvider(final Context context, LocationListener locationListener, OnFindLastLocationCallback onFindLastLocationCallback) {
        super();
        this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        this.locationListener = locationListener;
        this.onFindLastLocationCallback = onFindLastLocationCallback;
        this.context = context;
        this.gpsProviderEnabled = this.locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        this.networkProviderEnabled = this.locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    public void onPause() {
        if (this.locationListener != null && this.locationManager != null && (this.gpsProviderEnabled || this.networkProviderEnabled)) {
            this.locationManager.removeUpdates(this.locationListener);
        }
    }

    @Override
    public void onResume() {
        if (this.locationManager != null && this.locationListener != null) {

            this.gpsProviderEnabled = this.locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            this.networkProviderEnabled = this.locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            Location lastKnownNWLocation = null;
            Location lastKnownGPSLocation = null;
            if (this.networkProviderEnabled) {
                lastKnownNWLocation = this.locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (lastKnownNWLocation != null && lastKnownNWLocation.getTime() > System.currentTimeMillis() - LOCATION_OUTDATED_WHEN_OLDER_MS) {
                    locationListener.onLocationChanged(lastKnownNWLocation);
                }
                if (locationManager.getProvider(LocationManager.NETWORK_PROVIDER) != null) {
                    this.locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, LOCATION_UPDATE_MIN_TIME_NW, LOCATION_UPDATE_DISTANCE_NW, locationListener);
                }
            }

            if (this.gpsProviderEnabled) {
                lastKnownGPSLocation = this.locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (lastKnownGPSLocation != null && lastKnownGPSLocation.getTime() > System.currentTimeMillis() - LOCATION_OUTDATED_WHEN_OLDER_MS) {
                    locationListener.onLocationChanged(lastKnownGPSLocation);
                }
                if (locationManager.getProvider(LocationManager.GPS_PROVIDER) != null) {
                    this.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_UPDATE_MIN_TIME_GPS, LOCATION_UPDATE_DISTANCE_GPS, locationListener);
                }
            }

            onFindLastLocationCallback.onFound(lastKnownGPSLocation != null ? lastKnownGPSLocation : lastKnownNWLocation);

            if (!this.gpsProviderEnabled || !this.networkProviderEnabled) {
                Log.e(TAG, "GPS is Disable in your device");
            }
        }
    }

}