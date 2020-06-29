package com.community.customer.api.mall;

import com.community.customer.api.MobileServerRetrofit;
import com.community.customer.api.mall.input.GoodsListBody;

import io.reactivex.Observable;
import okhttp3.RequestBody;

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

    public Observable<GoodsListEntity> getGoodsList(GoodsListBody body) {
        return service.getGoodsList(body);
    }

    public Observable<GoodsEntity> getGoodsDetail(RequestBody body) {
        return service.getGoodsDetail(body);
    }
}
