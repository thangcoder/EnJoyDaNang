package node.com.enjoydanang.utils.config;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: Tavv
 * Created on 14/11/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class ForceUpdateChecker {
    private static final String TAG = ForceUpdateChecker.class.getSimpleName();

    public static final String KEY_UPDATE_REQUIRED = "force_update_required";
    public static final String KEY_CURRENT_VERSION = "force_update_current_version";
    public static final String KEY_UPDATE_URL = "force_update_store_url";
    public static final String KEY_VERSION_CODE = "current_version_code";

    private OnUpdateNeededListener onUpdateNeededListener;
    private Context context;

    public interface OnUpdateNeededListener {
        void onUpdateNeeded(String updateUrl);
    }

    public static Builder with(@NonNull Context context) {
        return new Builder(context);
    }

    public ForceUpdateChecker(@NonNull Context context,
                              OnUpdateNeededListener onUpdateNeededListener) {
        this.context = context;
        this.onUpdateNeededListener = onUpdateNeededListener;
    }

    public void check() {
        final FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();

        if (remoteConfig.getBoolean(KEY_UPDATE_REQUIRED)) {
            String currentVersion = remoteConfig.getString(KEY_CURRENT_VERSION);
            long newestVersionCode = remoteConfig.getLong(KEY_VERSION_CODE);
            Map<String, String> appInfo = getAppVersion(context);
            String appVersionName = appInfo.get("versionName");
            int appVersionCode = Integer.parseInt(appInfo.get("versionCode"));
            String updateUrl = remoteConfig.getString(KEY_UPDATE_URL);

            if ((!TextUtils.equals(currentVersion, appVersionName) || newestVersionCode > appVersionCode)
                    && onUpdateNeededListener != null) {
                onUpdateNeededListener.onUpdateNeeded(updateUrl);
            }
        }
    }

    private Map<String, String> getAppVersion(Context context) {
        Map<String, String> appInfo = new HashMap<>();
        String versionName = "";
        try {
            versionName = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0)
                    .versionName;
            int versionCode = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0)
                    .versionCode;
            versionName = versionName.replaceAll("[a-zA-Z]|-", "");
            appInfo.put("versionName", versionName);
            appInfo.put("versionCode", String.valueOf(versionCode));
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, e.getMessage());
        }
        return appInfo;
    }

    public static class Builder {

        private Context context;
        private OnUpdateNeededListener onUpdateNeededListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder onUpdateNeeded(OnUpdateNeededListener onUpdateNeededListener) {
            this.onUpdateNeededListener = onUpdateNeededListener;
            return this;
        }

        public ForceUpdateChecker build() {
            return new ForceUpdateChecker(context, onUpdateNeededListener);
        }

        public ForceUpdateChecker check() {
            ForceUpdateChecker forceUpdateChecker = build();
            forceUpdateChecker.check();

            return forceUpdateChecker;
        }
    }
}
