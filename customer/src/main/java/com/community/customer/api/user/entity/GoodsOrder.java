package com.community.customer.api.user.entity;

import com.community.support.common.BaseResult;

public class GoodsOrder extends BaseResult<GoodsOrder> {
    public String id, userID, tradeID, deliveryAddressID, goodsItems, remind;
    public float price;

    public String status, content;
    public long remainTime;

    public String createTime, updateTime;
}
