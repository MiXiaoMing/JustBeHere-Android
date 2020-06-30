package com.community.customer.api.other;

import com.community.customer.api.EmptyEntity;
import com.community.customer.api.user.FeedbackEntity;

import org.apache.http.entity.StringEntity;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * 与其他相关接口
 */
public interface OtherService {

    /**********  用户反馈  **********/


    @POST("others/addFeedback")
    Observable<FeedbackEntity> feedback(@Body RequestBody body);
}
