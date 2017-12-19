package node.com.enjoydanang;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.drawee.backends.pipeline.DraweeConfig;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.kakao.auth.KakaoSDK;

import org.json.JSONObject;

import io.fabric.sdk.android.Fabric;
import node.com.enjoydanang.model.UserInfo;
import node.com.enjoydanang.receiver.LanguageReceiver;
import node.com.enjoydanang.ui.activity.login.KakaoSDKAdapter;
import node.com.enjoydanang.utils.SharedPrefsUtils;
import node.com.enjoydanang.utils.Utils;
import node.com.enjoydanang.utils.config.AppUpdateConfiguration;
import node.com.enjoydanang.utils.helper.DomainHelper;
import node.com.enjoydanang.utils.helper.LanguageHelper;

/**
 * Created by chien on 10/8/17.
 */

public class GlobalApplication extends MultiDexApplication{
    private static final String TAG = GlobalApplication.class.getSimpleName();
    private static volatile GlobalApplication sInstance = null;
    private static volatile Activity currentActivity = null;
    public JSONObject jsLanguage;
    private static UserInfo userInfo;
    private String strLanguage;
    private boolean hasSessionLogin;
    private BroadcastReceiver mBroadcastReceiver = null;
    private boolean hasClickedUpdate;

    @Override
    public void onCreate() {
        super.onCreate();
        ImagePipelineConfig frescoConfig = ImagePipelineConfig.newBuilder(getApplicationContext()).setDownsampleEnabled(true).build();
        DraweeConfig draweeConfig = DraweeConfig.newBuilder()
                .build();
        Fresco.initialize(this, frescoConfig, draweeConfig);
        sInstance = this;
        AppEventsLogger.activateApp(this);
        KakaoSDK.init(new KakaoSDKAdapter());
        if(!BuildConfig.DEBUG_MODE){
            final Fabric fabric = new Fabric.Builder(this)
                    .kits(new Crashlytics())
                    .debuggable(BuildConfig.DEBUG_MODE)
                    .build();
            Fabric.with(fabric);
        }
        new AppUpdateConfiguration().configFirebaseUpdate();
        SharedPrefsUtils.setContext(this);
        hasSessionLogin = Utils.hasSessionLogin();
//        setUpLangReceiver();
//        checkLanguage();
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
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
            throw new IllegalStateException("this application does not inherit node.com.enjoydanang.GlobalApplication");
        return sInstance;
    }

    /**
     * 애플리케이션 종료시 singleton 어플리케이션 객체 초기화한다.
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
        sInstance = null;
        if(mBroadcastReceiver != null){
            unregisterReceiver(mBroadcastReceiver);
        }
    }

    public JSONObject getJsLanguage() {
        return jsLanguage;
    }

    public void setJsLanguage(JSONObject jsLanguage) {
        this.jsLanguage = jsLanguage;
    }

    public static UserInfo getUserInfo() {
        return userInfo;
    }

    public static void setUserInfo(UserInfo userInfo) {
        GlobalApplication.userInfo = userInfo;
    }

    private void checkLanguage(){
        strLanguage = LanguageHelper.getSystemLanguage();
        if(strLanguage.equalsIgnoreCase("vi")){
            new DomainHelper(DomainHelper.DomainType.VN);
        }else{
            new DomainHelper(DomainHelper.DomainType.KR);
        }
    }

    public boolean isHasSessionLogin() {
        return hasSessionLogin;
    }

    public void setHasSessionLogin(boolean hasSessionLogin) {
        this.hasSessionLogin = hasSessionLogin;
    }


    public BroadcastReceiver setUpLangReceiver() {
        if (mBroadcastReceiver == null) {
            mBroadcastReceiver = new LanguageReceiver();
            IntentFilter filter = new IntentFilter(Intent.ACTION_LOCALE_CHANGED);
            registerReceiver(mBroadcastReceiver, filter);
        }
        return mBroadcastReceiver;
    }

    public String getStrLanguage() {
        return strLanguage;
    }

    public void setStrLanguage(String strLanguage) {
        this.strLanguage = strLanguage;
    }

    public boolean isHasClickedUpdate() {
        return hasClickedUpdate;
    }

    public void setHasClickedUpdate(boolean hasClickedUpdate) {
        this.hasClickedUpdate = hasClickedUpdate;
    }

}
