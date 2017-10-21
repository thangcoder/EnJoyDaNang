package node.com.enjoydanang;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import com.facebook.appevents.AppEventsLogger;
import com.facebook.drawee.backends.pipeline.DraweeConfig;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.kakao.auth.KakaoSDK;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import node.com.enjoydanang.ui.activity.login.KakaoSDKAdapter;

import static com.kakao.util.helper.Utility.getPackageInfo;

/**
 * Created by chien on 10/8/17.
 */

public class GlobalApplication extends Application {
    private static final String TAG = GlobalApplication.class.getSimpleName();
    private static volatile GlobalApplication sInstance = null;
    private static volatile Activity currentActivity = null;
    public JSONObject jsLanguage;

    @Override
    public void onCreate() {
        super.onCreate();
        ImagePipelineConfig frescoConfig = ImagePipelineConfig.newBuilder(getApplicationContext()) .setDownsampleEnabled(true).build();
        DraweeConfig draweeConfig = DraweeConfig.newBuilder()
                .build();
        Fresco.initialize(this, frescoConfig, draweeConfig);
        sInstance = this;
        AppEventsLogger.activateApp(this);
        KakaoSDK.init(new KakaoSDKAdapter());
    }


    public static Activity getCurrentActivity() {
        return currentActivity;
    }

    public static void setCurrentActivity(Activity currentActivity) {
        GlobalApplication.currentActivity = currentActivity;
    }

    /**
     * singleton 애플리케이션 객체를 얻는다.
     * @return singleton 애플리케이션 객체
     */
    public static GlobalApplication getGlobalApplicationContext() {
        if(sInstance == null)
            throw new IllegalStateException("this application does not inherit com.kakao.GlobalApplication");
        return sInstance;
    }

    /**
     * 애플리케이션 종료시 singleton 어플리케이션 객체 초기화한다.
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
        sInstance = null;
    }

    public static String getKeyHash(final Context context) {
        PackageInfo packageInfo = getPackageInfo(context, PackageManager.GET_SIGNATURES);
        if (packageInfo == null)
            return null;

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                return Base64.encodeToString(md.digest(), Base64.NO_WRAP);
            } catch (NoSuchAlgorithmException e) {
                Log.w(TAG, "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
        return null;
    }

    public JSONObject getJsLanguage() {
        return jsLanguage;
    }

    public void setJsLanguage(JSONObject jsLanguage) {
        this.jsLanguage = jsLanguage;
    }
}
