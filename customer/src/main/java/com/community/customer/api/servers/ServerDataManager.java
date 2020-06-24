package com.community.customer.api.servers;

import com.community.customer.api.MobileServerRetrofit;

import io.reactivex.Observable;
import okhttp3.RequestBody;

/**
 * 服务信息
 */
public class ServerDataManager {
    private ServerService service;

    public ServerDataManager() {
        service = MobileServerRetrofit.getInstance().getRetrofit().create(ServerService.class);
    }

    public Observable<CarouselEntity> getMainPageCarousel() {
        return service.getMainPageCarousel();
    }

    public Observable<ServiceBlockEntity> getFive() {
        return service.getFive();
    }

    public Observable<ServiceBlockEntity> getTen() {
        return service.getTen();
    }

    public Observable<ServiceBlockEntity> getRecommend() {
        return service.getRecommend();
    }

    public Observable<ServiceBlockEntity> getRecommendServiceClassify() {
        return service.getRecommendServiceClassify();
    }


    public Observable<ServerClassifyEntity> getServerClassify(RequestBody classify) {
        return service.getServerClassify(classify);
    }

    public Observable<ServerDetailEntity> getServerDetail(RequestBody code) {
        return service.getServerDetail(code);
    }
}
