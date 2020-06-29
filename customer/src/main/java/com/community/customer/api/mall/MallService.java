package com.community.customer.api.mall;

import com.community.customer.api.mall.input.GoodsListBody;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 与商品相关接口
 */
public interface MallService {

    /**********  商城  **********/

    @POST("goods/getAllGoodsClassify")
    Observable<GoodsClassifyListEntity> getGoodsClassify();

    @POST("goods/getGoodsList")
    Observable<GoodsListEntity> getGoodsList(@Body GoodsListBody body);

    @POST("goods/getGoods")
    Observable<GoodsEntity> getGoodsDetail(@Body RequestBody body);

}
