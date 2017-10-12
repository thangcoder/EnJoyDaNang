package node.com.enjoydanang;

import android.app.Application;

import com.facebook.appevents.AppEventsLogger;
import com.facebook.drawee.backends.pipeline.DraweeConfig;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;

import node.com.enjoydanang.utils.Utils;

/**
 * Created by chien on 10/8/17.
 */

public class MyAppication extends Application {
    private static MyAppication sInstance;

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
    }


    public static MyAppication getInstance() {
        return sInstance;
    }


}
