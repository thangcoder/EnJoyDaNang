package android.com.enjoydanang.api.module;

import android.text.TextUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HeaderInterceptor
        implements Interceptor {
    //    Context context;
//    public HeaderInterceptor(Context context){
//        this.context=context;
//    }
    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
            request = request.newBuilder()
//                    .addHeader("Authorization", "Bearer " + ArubaitoGlobal.token)
                    .addHeader("Connection", "close")
                    .build();

        Response response = chain.proceed(request);

        return response;
    }
}