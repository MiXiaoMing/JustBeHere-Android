package com.community.customer.api.user;

import com.community.support.common.BaseArrayResult;
import com.community.support.common.BaseResult;

import java.io.Serializable;
import java.util.ArrayList;

public class CartListEntity extends BaseArrayResult<CartListEntity> {
    public String id, goodsid, title, icon, typeID, typeName;
    public float typePrice;
    public int number;
    public boolean isSelect = false;
}
