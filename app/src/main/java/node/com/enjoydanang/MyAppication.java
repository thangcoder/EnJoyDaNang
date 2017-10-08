package node.com.enjoydanang;

import android.app.Application;

import com.facebook.common.logging.FLog;
import com.facebook.drawee.backends.pipeline.DraweeConfig;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.listener.RequestListener;
import com.facebook.imagepipeline.listener.RequestLoggingListener;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by chien on 10/8/17.
 */

public class MyAppication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ImagePipelineConfig frescoConfig = ImagePipelineConfig.newBuilder(getApplicationContext()) .setDownsampleEnabled(true).build();
        DraweeConfig draweeConfig = DraweeConfig.newBuilder()
                .build();
        Fresco.initialize(this, frescoConfig, draweeConfig);
    }
}
