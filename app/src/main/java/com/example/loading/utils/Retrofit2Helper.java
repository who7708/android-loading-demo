package com.example.loading.utils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.UUID;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author Chris
 * @version 1.0.0
 * @since 2021/02/20
 */
public class Retrofit2Helper {
    private volatile static Retrofit2Helper retrofit2Helper;
    private Retrofit retrofit;

    private Retrofit2Helper() {
        init();
    }

    public static Retrofit2Helper getInstance() {
        if (retrofit2Helper == null) {
            synchronized (Retrofit2Helper.class) {
                if (retrofit2Helper == null) {
                    retrofit2Helper = new Retrofit2Helper();
                }
            }
        }
        return retrofit2Helper;
    }

    private void init() {
        final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                // 各种超时 timeout，默认都是 10s
                // .connectTimeout(30, TimeUnit.SECONDS)
                // .readTimeout(30, TimeUnit.SECONDS)
                // .writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .addInterceptor(new Interceptor() {
                    @NotNull
                    @Override
                    public Response intercept(@NotNull Chain chain) throws IOException {
                        // Request customization: add request headers
                        Request.Builder requestBuilder = chain.request().newBuilder()
                                .addHeader("t", String.valueOf(System.currentTimeMillis()))
                                .addHeader("x-session-token", uuid());
                        Request request = requestBuilder.build();
                        return chain.proceed(request);
                    }
                })
                // .cache(new Cache(cacheDir, cacheSize))
                .build();

        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("http://192.168.31.10:7001/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }
}