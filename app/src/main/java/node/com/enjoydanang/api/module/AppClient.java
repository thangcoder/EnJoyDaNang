package node.com.enjoydanang.api.module;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import node.com.enjoydanang.BuildConfig;
import node.com.enjoydanang.api.model.NullOnEmptyConverterFactory;
import node.com.enjoydanang.constant.Constant;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Author: Tavv
 * Created on 01/10/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public class AppClient {

    public static Retrofit retrofit = null;

    private static OkHttpClient httpClient;

    private static final int TIMEOUT_MINUTES = 3;

    public static Retrofit getClient() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        httpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(TIMEOUT_MINUTES, TimeUnit.MINUTES)
                .readTimeout(TIMEOUT_MINUTES, TimeUnit.MINUTES)
                .retryOnConnectionFailure(true)
                .build();
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Constant.URL_HOST)
                    .client(httpClient)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(new NullOnEmptyConverterFactory())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }


    public static Retrofit setNewBaseUrl(String url) {
        if(httpClient == null){
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient = new OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .connectTimeout(TIMEOUT_MINUTES, TimeUnit.SECONDS)
                    .readTimeout(TIMEOUT_MINUTES, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .build();
        }
        if (httpClient != null) {
            return new Retrofit.Builder()
                      .baseUrl(url)
                      .client(httpClient)
                      .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                      .addConverterFactory(new NullOnEmptyConverterFactory())
                      .addConverterFactory(GsonConverterFactory.create())
                      .build();
        }
        return null;
    }

    public static OkHttpClient getUnsafeOkHttpClient() {

        try {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            OkHttpClient okHttpClient = builder
                    .addNetworkInterceptor(new HeaderInterceptor())
                    .addInterceptor(interceptor)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .build();
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
