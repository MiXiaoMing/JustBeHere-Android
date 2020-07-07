package com.community.customer.api.user;

import com.community.customer.api.EmptyEntity;
import com.community.customer.api.MobileServerRetrofit;
import com.community.customer.api.mall.entity.Goods;
import com.community.customer.api.servers.ServerPrice;
import com.community.customer.api.user.entity.Cart;
import com.community.customer.api.user.input.CartBody;
import com.community.customer.api.user.input.DeliveryAddressBody;
import com.community.customer.api.user.input.GoodsOrderBody;
import com.community.customer.api.user.input.LoginBody;
import com.community.customer.api.user.input.OrderBody;
import com.community.customer.api.user.input.ServiceOrderBody;
import com.community.customer.api.user.result.ServerOrderEntity;
import com.community.customer.api.user.result.ServerOrderListEntity;
import com.community.support.common.StringResult;
import com.community.support.common.UserInfo;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 服务信息
 */
public class UserDataManager {
    private UserService service;

    public UserDataManager() {
        service = MobileServerRetrofit.getInstance().getRetrofit().create(UserService.class);
    }

    /**********  登录  **********/

    public Observable<StringResult> sendSms(RequestBody cellphone) {
        return service.sendSms(cellphone);
    }

    public Observable<StringResult> login(LoginBody body) {
        return service.login(body);
    }

    public Observable<UserInfo> getUser() {
        return service.getUser();
    }

    public Observable<EmptyEntity> updateDevice(String cid, String region) {
        return service.updateDevice(cid, region);
    }

    public Observable<EmptyEntity> updateUserName(String userName) {
        return service.updateUserName(userName);
    }

    /**********  服务地址  **********/

    public Observable<AddressEntity> getAddressList() {
        return service.getAddressList();
    }

    public Observable<EmptyEntity> address(AddressEntity address, String type) {
        if (type.equals("U")) {
            DeliveryAddressBody body = new DeliveryAddressBody();
            body.id = address.id;
            body.contact = address.contact;
            body.phoneNumber = address.phoneNumber;
            body.region = address.region;
            body.detail = address.detail;

            return service.updateAddress(body);
        } else if (type.equals("D")) {
            return service.deleteAddress(RequestBody.create(MediaType.parse("text/plain"), address.id));
        } else {
            DeliveryAddressBody body = new DeliveryAddressBody();
            body.contact = address.contact;
            body.phoneNumber = address.phoneNumber;
            body.region = address.region;
            body.detail = address.detail;
            return service.addAddress(body);
        }
    }

    /**********  服务时间  **********/

    public Observable<ServerTimeEntity> getTodayServerTimeList() {
        return service.getTodayServerTimeList();
    }

    public Observable<ServerTimeEntity> getServerTimeList(String code) {
        return service.getServerTimeList(code);
    }

    /**********  服务订单  **********/

    public Observable<CalculateServerPriceEntity> calculateServerPrice(String serverCode, String serverItems) {
        return service.calculateServerPrice(serverCode, serverItems);
    }

    public Observable<AddOrderEntity> addServerOrder(ServerOrderConfirm orderConfirm, List<ServerPrice> prices) {
        String items = "[";
        for (ServerPrice price : prices) {
            if (price.buyNumber > 0) {
                items += "{id:\"" + price.id + "\",number:" + price.buyNumber + "}";
            }
        }
        items += "]";

        ServiceOrderBody body = new ServiceOrderBody();
        body.deliveryAddressID = orderConfirm.addressid;
        body.serviceCode = orderConfirm.serverCode;
        body.serviceName = orderConfirm.serverName;
        body.remind = orderConfirm.remind;
        body.serviceItems = items;
        body.serviceTime = orderConfirm.serverTime;
        body.totalPrice = orderConfirm.totalPrice;
        body.discountPrice = orderConfirm.discountPrice;
        body.payPrice = orderConfirm.payPrice;

        return service.addServerOrder(body);
    }

    public Observable<ServerOrderListEntity> getServerOrderList() {
        return service.getServerOrderList();
    }

    public Observable<ServerOrderListEntity> getUndoneServerOrderList() {
        return service.getUndoneServerOrderList();
    }

    public Observable<ServerOrderEntity> getServerOrderDetail(RequestBody orderID) {
        return service.getServerOrderDetail(orderID);
    }

    public Observable<EmptyEntity> changeServerOrderStatus(ServiceOrderBody body) {
        return service.changeServerOrderStatus(body);
    }

    /**********  购物车  **********/

    public Observable<CountEntity> getShoppingCartCount() {
        return service.getShoppingCartCount();
    }

    public Observable<CartListEntity> getShoppingCartList() {
        return service.getShoppingCartList();
    }

    public Observable<CountEntity> addCart(CartBody body) {
        return service.addCart(body);
    }

    public Observable<Cart> deleteCart(String id) {
        return service.deleteCart(RequestBody.create(MediaType.parse("text/plain"), id));
    }

    public Observable<Cart> updateCartCount(CartBody body) {
        return service.updateCartCount(body);
    }

    /**********  商品订单  **********/

    public Observable<AddOrderEntity> addGoodsOrder(GoodsOrderConfirm orderConfirm) {
        String items = "[";
        for (int i = 0; i < orderConfirm.items.size(); ++i) {
            GoodsOrderConfirm.Item item = orderConfirm.items.get(i);
            if (item.number > 0) {
                if (i != 0) {
                    items += ",";
                }
                items += "{code:\"" + item.goodsid + "\",typeName:\"" + item.typeName + "\",typeID:\"" + item.typeid + "\",typePrice:" + item.typePrice + ",number:" + item.number + "}";
            }
        }
        items += "]";

        GoodsOrderBody body = new GoodsOrderBody();
        body.deliveryAddressID = orderConfirm.addressid;
        body.goodsItems = items;
        body.remind = orderConfirm.remind;
        body.price = orderConfirm.price;

        return service.addGoodsOrder(body);
    }

    public Observable<GoodsOrderDetailEntity> getGoodsOrderDetail(String orderID) {
        return service.getGoodsOrderDetail(RequestBody.create(MediaType.parse("text/plain"), orderID));
    }

    public Observable<EmptyEntity> changeGoodsOrderStatus(OrderBody body) {
        return service.changeGoodsOrderStatus(body);
    }

    public Observable<GoodsOrderEntity> getGoodsOrderList() {
        return service.getGoodsOrderList();
    }

    public Observable<GoodsOrderEntity> getUndoneGoodsOrderList() {
        return service.getUndoneGoodsOrderList();
    }

    /**********  会员  **********/

    public Observable<FloatEntity> balancePay(String orderID, String orderType) {
        return service.balancePay(orderID, orderType);
    }
}
