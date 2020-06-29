package com.community.customer.api.user;

import com.community.customer.api.EmptyEntity;
import com.community.customer.api.user.input.CartBody;
import com.community.customer.api.user.input.DeliveryAddressBody;
import com.community.customer.api.user.input.LoginBody;
import com.community.customer.api.user.input.ServiceOrderBody;
import com.community.customer.api.user.result.ServerOrderEntity;
import com.community.customer.api.user.result.ServerOrderListEntity;
import com.community.support.common.StringResult;
import com.community.support.common.UserInfo;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 与用户相关接口
 */
public interface UserService {

    /**********  登录  **********/

    @POST("login/sendSmsCode")
    Observable<StringResult> sendSms(@Body RequestBody body);

    @POST("login/phoneNumber")
    Observable<StringResult> login(@Body LoginBody body);

    @POST("user/info")
    Observable<UserInfo> getUser();


    @POST("user/updateDevice")
    Observable<EmptyEntity> updateDevice(@Field("cid") String cid, @Field("region") String region);


    @POST("user/updateUserName")
    Observable<EmptyEntity> updateUserName(@Field("userName") String userName);

    /**********  服务地址  **********/

    @POST("user/getAllDeliveryAddress")
    Observable<AddressEntity> getAddressList();

    @POST("user/addDeliveryAddress")
    Observable<EmptyEntity> addAddress(@Body DeliveryAddressBody body);


    @POST("user/updateAddress")
    Observable<EmptyEntity> updateAddress(@Field("id") String id, @Field("contact") String contact, @Field("cellphone") String cellphone, @Field("region") String region, @Field("detail") String detail);


    @POST("user/deleteAddress")
    Observable<EmptyEntity> deleteAddress(@Field("id") String id, @Field("contact") String contact, @Field("cellphone") String cellphone, @Field("region") String region, @Field("detail") String detail);

    /**********  服务地址  **********/

    @POST("service/getTimeList")
    Observable<ServerTimeEntity> getTodayServerTimeList();

    @GET("user/getServerTimeList")
    Observable<ServerTimeEntity> getServerTimeList(@Query("code") String serverCode);

    /**********  服务订单  **********/


    @POST("user/calculateServerPrice")
    Observable<CalculateServerPriceEntity> calculateServerPrice(@Field("serverCode") String serverCode, @Field("serverItems") String serverItems);

    @POST("order/addServiceOrder")
    Observable<AddOrderEntity> addServerOrder(@Body ServiceOrderBody body);

    @POST("order/getAllServiceOrder")
    Observable<ServerOrderListEntity> getServerOrderList();

    @POST("order/getAllUndoneServiceOrder")
    Observable<ServerOrderListEntity> getUndoneServerOrderList();

    @POST("order/getServiceOrder")
    Observable<ServerOrderEntity> getServerOrderDetail(@Body RequestBody body);

    @POST("order/updateServiceOrderStatus")
    Observable<EmptyEntity> changeServerOrderStatus(@Body ServiceOrderBody body);

    /**********  购物车  **********/

    @POST("cart/getAllCountFromCart")
    Observable<CountEntity> getShoppingCartCount();

    @POST("cart/getAllFromCart")
    Observable<CartListEntity> getShoppingCartList();

    @POST("cart/addToCart")
    Observable<CountEntity> addCart(@Body CartBody body);

    @POST("user/deleteCart")
    Observable<CountEntity> deleteCart(@Field("ids") String ids);

    @POST("user/updateCartCount")
    Observable<EmptyEntity> updateCartCount(@Field("cartID") String id, @Field("number") int number);

    /**********  商品订单  **********/

    @POST("order/addGoodsOrder")
    Observable<AddOrderEntity> addGoodsOrder(@Field("addressid") String addressid, @Field("goodsItems") String goodsItems, @Field("remind") String remind);

    @GET("order/getGoodsOrder")
    Observable<GoodsOrderDetailEntity> getGoodsOrderDetail(@Query("orderID") String orderID);


    @POST("user/changeGoodsOrderStatus")
    Observable<EmptyEntity> changeGoodsOrderStatus(@Field("orderid") String orderid, @Field("orderStatus") String orderStatus, @Field("content") String content);

    @POST("order/getGoodsOrderList")
    Observable<GoodsOrderEntity> getGoodsOrderList();

    @POST("order/getAllUndoneGoodsOrder")
    Observable<GoodsOrderEntity> getUndoneGoodsOrderList();

    /**********  会员  **********/


    @POST("user/balancePay")
    Observable<FloatEntity> balancePay(@Field("orderID") String orderID, @Field("orderType") String orderType);
}
