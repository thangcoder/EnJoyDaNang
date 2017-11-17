package node.com.enjoydanang.utils.config;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.util.HashMap;
import java.util.Map;

import node.com.enjoydanang.BuildConfig;
import node.com.enjoydanang.constant.Constant;

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
        FirebaseRemoteConfigSettings configSettings =
                new FirebaseRemoteConfigSettings.Builder()
                        .setDeveloperModeEnabled(BuildConfig.DEBUG_MODE)
                        .build();

        final FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        // set in-app defaults
        Map<String, Object> remoteConfigDefaults = new HashMap<String, Object>();
        remoteConfigDefaults.put(ForceUpdateChecker.KEY_UPDATE_REQUIRED, false);
        remoteConfigDefaults.put(ForceUpdateChecker.KEY_CURRENT_VERSION, BuildConfig.VERSION_NAME);
        remoteConfigDefaults.put(ForceUpdateChecker.KEY_VERSION_CODE, BuildConfig.VERSION_CODE);

        remoteConfigDefaults.put(ForceUpdateChecker.KEY_UPDATE_URL, BuildConfig.urlStore);

        long cacheExpiration = Constant.FETCH_UPDATE_TIME;
        mFirebaseRemoteConfig.setDefaults(remoteConfigDefaults);
        if (mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }
        mFirebaseRemoteConfig.fetch(cacheExpiration)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            mFirebaseRemoteConfig.activateFetched();
                        }
                    }
                });

        FirebaseRemoteConfig.getInstance().setConfigSettings(configSettings);

    }
}
