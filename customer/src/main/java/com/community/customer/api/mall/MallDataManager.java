package com.community.customer.api.mall;

import com.community.customer.api.MobileServerRetrofit;

import io.reactivex.Observable;

/**
 * 商品信息
 */

public class MallDataManager {
    private MallService service;

    public MallDataManager() {
        service = MobileServerRetrofit.getInstance().getRetrofit().create(MallService.class);
    }

    /**********  商品  **********/

    public Observable<GoodsClassifyListEntity> getGoodsClassifyList() {
        return service.getGoodsClassify();
    }

    public Observable<GoodsListEntity> getGoodsList(String classify, int page, int number) {
        return service.getGoodsList(classify, page, number);
    }

    public Observable<GoodsEntity> getGoodsDetail(String code) {
        return service.getGoodsDetail(code);
    }
}
