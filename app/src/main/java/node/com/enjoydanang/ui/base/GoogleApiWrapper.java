package node.com.enjoydanang.ui.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.List;

import node.com.enjoydanang.utils.event.LocationConnectListener;

/**
 * Author: Tavv
 * Created on 16/12/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public abstract class GoogleApiWrapper implements LocationConnectListener {

    enum ConnectionStatus {
        CONNECTED, DISCONNECTED, CONNECTING, SUSPENDED, FAILED
    }

    private final GoogleApiClient mGoogleApiClient;
    private final List<LocationConnectListener> connectionListeners;
    private final Object connectionStatusLock = new Object();
    private ConnectionStatus mConnectionStatus = ConnectionStatus.DISCONNECTED;

    public GoogleApiWrapper(Activity activity) {
        this.connectionListeners = new ArrayList<>();
        this.mGoogleApiClient = buildApiClient(activity);
    }

    protected abstract GoogleApiClient buildApiClient(Context context);

    public GoogleApiClient getGoogleApiClient() {
        return mGoogleApiClient;
    }

    public void setConnectionStatus(ConnectionStatus connectionStatus) {
        synchronized (connectionStatusLock) {
            mConnectionStatus = connectionStatus;
        }
    }

    public ConnectionStatus getConnectionStatus() {
        synchronized (connectionStatusLock) {
            return mConnectionStatus;
        }
    }

    public void connect() {
        mGoogleApiClient.connect();
        setConnectionStatus(ConnectionStatus.CONNECTING);
    }

    public void disconnect() {
        mGoogleApiClient.disconnect();
        setConnectionStatus(ConnectionStatus.DISCONNECTED);
    }

    public boolean addConnectionListener(LocationConnectListener listener) {
        if (!connectionListeners.contains(listener)) {
            return connectionListeners.add(listener);
        }
        return false;
    }

    public int removeConnectionListener(LocationConnectListener listener) {
        int index = connectionListeners.indexOf(listener);
        if (index != -1) {
            connectionListeners.remove(index);
        }
        return index;
    }

    @Override
    public void onConnected(Bundle bundle) {
        setConnectionStatus(ConnectionStatus.CONNECTED);
        for (LocationConnectListener listener : connectionListeners) {
            listener.onConnected(bundle);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        setConnectionStatus(ConnectionStatus.SUSPENDED);
        for (LocationConnectListener listener : connectionListeners) {
            listener.onConnectionSuspended(i);
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        setConnectionStatus(ConnectionStatus.FAILED);
        for (LocationConnectListener listener : connectionListeners) {
            listener.onConnectionFailed(connectionResult);
        }
    }
}