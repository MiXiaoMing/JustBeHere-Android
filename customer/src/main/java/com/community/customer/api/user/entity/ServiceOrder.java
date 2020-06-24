package com.community.customer.api.user.entity;

public class ServiceOrder {
    public String id, tradeID, deliveryAddressID, serviceCode, serviceName, serviceItems, remind, serviceTime, createTime, updateTime;
    public Float totalPrice, discountPrice, payPrice;

    public String status, content;
    public long remainTime;
}
