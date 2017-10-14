package node.com.enjoydanang;

import android.app.Activity;
import android.app.Application;

import com.facebook.appevents.AppEventsLogger;
import com.facebook.drawee.backends.pipeline.DraweeConfig;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.kakao.auth.KakaoSDK;

import node.com.enjoydanang.ui.activity.login.KakaoSDKAdapter;
import node.com.enjoydanang.utils.Utils;

import static okhttp3.internal.Internal.instance;

/**
 * Created by chien on 10/8/17.
 */

public class MyAppication extends Application {

    private static volatile MyAppication sInstance = null;
    private static volatile Activity currentActivity = null;

    @Override
    public void onCreate() {
        super.onCreate();
        ImagePipelineConfig frescoConfig = ImagePipelineConfig.newBuilder(getApplicationContext()) .setDownsampleEnabled(true).build();
        DraweeConfig draweeConfig = DraweeConfig.newBuilder()
                .build();
        Fresco.initialize(this, frescoConfig, draweeConfig);
        Utils.init(this);
        sInstance = this;
        AppEventsLogger.activateApp(this);
        KakaoSDK.init(new KakaoSDKAdapter());
    }


    public static Activity getCurrentActivity() {
        return currentActivity;
    }

    public static void setCurrentActivity(Activity currentActivity) {
        MyAppication.currentActivity = currentActivity;
    }

    /**
     * singleton 애플리케이션 객체를 얻는다.
     * @return singleton 애플리케이션 객체
     */
    public static MyAppication getGlobalApplicationContext() {
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


}
