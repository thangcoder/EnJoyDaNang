package node.com.enjoydanang.utils;

import node.com.enjoydanang.LogApp;
import android.content.Context;
import android.net.ConnectivityManager;


/**
 * Created by Welcome on 4/6/2016.
 */
public class ConnectionUltils {
    //    public static boolean isConnected(Context context) {
//        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        if (connectivity != null) {
//            NetworkInfo[] info = connectivity.getAllNetworkInfo();
//            if (info != null)
//                for (int i = 0; i < info.length; i++)
//                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
//                        return true;
//                    }
//
//        }
//        return false;
//    }
    private final static String TAG = "NetworkChecker";

    public static boolean isConnected(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean val = false;
        LogApp.log(TAG, "Checking for Mobile Internet Network");
        final android.net.NetworkInfo mobile = connectivity
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobile.isAvailable() && mobile.isConnected()) {
            LogApp.log(TAG, "Found Mobile Internet Network");
            val = true;
        } else {
            LogApp.log(TAG, "Mobile Internet Network not Found");
        }
        LogApp.log(TAG, "Checking for WI-FI Network");
        final android.net.NetworkInfo wifi = connectivity
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifi.isAvailable() && wifi.isConnected()) {
            LogApp.log(TAG, "Found WI-FI Network");
            val = true;
        } else {
            LogApp.log(TAG, "WI-FI Network not Found");
        }
        return val;
    }
    public boolean ischeck(Context context){
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        //For 3G check
        boolean is3g = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                .isConnectedOrConnecting();
        //For WiFi Check
        boolean isWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .isConnectedOrConnecting();

        System.out.println(is3g + " net " + isWifi);

        if (!is3g && !isWifi)
        {
            return false;
        }
        else
        {
            return true;
        }
    }


}
