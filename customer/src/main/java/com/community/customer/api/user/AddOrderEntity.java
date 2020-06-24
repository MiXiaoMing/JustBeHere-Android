package com.community.customer.api.user;

import com.community.support.common.BaseResult;

import java.io.Serializable;

public class AddOrderEntity extends BaseResult<AddOrderEntity> {
    public float payPrice;
    public String id, tradeID;
}
