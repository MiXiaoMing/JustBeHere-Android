package com.community.customer.api.other;

import com.community.customer.api.EmptyEntity;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * 与其他相关接口
 */
public interface OtherService {

    /**********  用户反馈  **********/

    @FormUrlEncoded
    @POST("other/feedback")
    Observable<EmptyEntity> feedback(@Field("cid") String cid, @Field("content") String content);
}
