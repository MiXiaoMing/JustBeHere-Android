package com.community.customer.api;

import android.support.annotation.NonNull;

import com.community.support.utils.AuthUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class CustomHeaderInterceptor implements Interceptor {
    /**
     * 添加header
     */
    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        builder.addHeader("Content-Type", "application/json");
        builder.addHeader("Accept", "application/json");
        builder.addHeader("token", AuthUtil.getAuth());
        return chain.proceed(builder.build());
    }
}