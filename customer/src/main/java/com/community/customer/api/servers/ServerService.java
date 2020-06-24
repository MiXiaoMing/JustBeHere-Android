package com.community.customer.api.servers;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * 与服务相关接口
 */
public interface ServerService {

    @POST("carousel/getMainPageAllCarousel")
    Observable<CarouselEntity> getMainPageCarousel();

    @POST("setting/getMainPageFive")
    Observable<ServiceBlockEntity> getFive();

    @POST("setting/getMainPageTen")
    Observable<ServiceBlockEntity> getTen();

    @POST("setting/getMainPageRecommend")
    Observable<ServiceBlockEntity> getRecommend();

    @POST("setting/getMainPageRecommendServiceClassify")
    Observable<ServiceBlockEntity> getRecommendServiceClassify();


    @POST("service/getServiceClassify")
    Observable<ServerClassifyEntity> getServerClassify(@Body RequestBody code);

    @POST("service/getService")
    Observable<ServerDetailEntity> getServerDetail(@Body RequestBody code);
}
