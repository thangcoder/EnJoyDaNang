package node.com.enjoydanang.utils.config;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.util.HashMap;
import java.util.Map;

import node.com.enjoydanang.BuildConfig;

/**
 * Author: Tavv
 * Created on 14/11/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class AppUpdateConfiguration {
    private static final String TAG = AppUpdateConfiguration.class.getSimpleName();

    public AppUpdateConfiguration(){}

    public void configFirebaseUpdate() {
        final FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        // set in-app defaults
        Map<String, Object> remoteConfigDefaults = new HashMap<String, Object>();
        remoteConfigDefaults.put(ForceUpdateChecker.KEY_UPDATE_REQUIRED, false);
        remoteConfigDefaults.put(ForceUpdateChecker.KEY_CURRENT_VERSION, BuildConfig.VERSION_NAME);

        remoteConfigDefaults.put(ForceUpdateChecker.KEY_UPDATE_URL,
                BuildConfig.urlStore);

        firebaseRemoteConfig.setDefaults(remoteConfigDefaults);
        firebaseRemoteConfig.fetch() // fetch every minutes
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "remote config is fetched.");
                            firebaseRemoteConfig.activateFetched();
                        }
                    }
                });
    }
}
