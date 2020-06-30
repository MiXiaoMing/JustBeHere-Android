package com.community.customer.api.user.entity;

import com.community.support.common.BaseResult;

public class Cart extends BaseResult<Cart> {
    public String id, userID, goodsID, typeID, typeName;
    public int number;
}
