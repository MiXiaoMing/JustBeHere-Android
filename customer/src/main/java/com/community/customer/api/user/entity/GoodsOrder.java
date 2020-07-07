package com.community.customer.api.user.entity;

import com.community.support.common.BaseResult;

public class GoodsOrder extends BaseResult<GoodsOrder> {
    public String id, goodsItems, remind;
    public float price;
}
