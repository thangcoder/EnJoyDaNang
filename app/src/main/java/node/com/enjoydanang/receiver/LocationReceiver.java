package node.com.enjoydanang.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;

import org.greenrobot.eventbus.EventBus;

import node.com.enjoydanang.constant.Constant;
import node.com.enjoydanang.constant.Extras;

/**
 * Author: Tavv
 * Created on 08/12/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class LocationReceiver extends BroadcastReceiver {
    private static final String TAG = LocationReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            Bundle bundle = intent.getBundleExtra(Extras.KEY_RECEIVER_LOCATION);
            if (bundle != null) {
                Location currentLocation = bundle.getParcelable(Extras.EXTRAS_RECEIVER_LOCATION);
                if(action.equals(Extras.KEY_RECEIVER_LOCATION_FILTER)){
                    if(currentLocation != null){
                        EventBus.getDefault().post(currentLocation);
                    }else{
                        EventBus.getDefault().post(Constant.LOCATION_NOT_FOUND);
                    }
                }
            }
        }
    }
}
