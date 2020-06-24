package com.community.customer.api.mall;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 与商品相关接口
 */
public interface MallService {

    /**********  商城  **********/

    @GET("mall/getGoodsClassify")
    Observable<GoodsClassifyListEntity> getGoodsClassify();

    @GET("mall/getGoodsList")
    Observable<GoodsListEntity> getGoodsList(@Query("classify") String classify, @Query("page") int page, @Query("number") int number);

    @GET("mall/getGoods")
    Observable<GoodsEntity> getGoodsDetail(@Query("code") String code);

}
