package com.community.customer.api.user;

import com.community.customer.api.mall.GoodsPrice;
import com.community.customer.api.mall.entity.Goods;
import com.community.customer.api.user.entity.Cart;
import com.community.support.common.BaseArrayResult;
import com.community.support.common.BaseResult;

import java.io.Serializable;
import java.util.ArrayList;

public class CartListEntity extends BaseArrayResult<CartListEntity> {
    public Goods goods;
    public ArrayList<GoodsPrice> goodsPrices;
    public Cart cart;

    public boolean isSelect = false;
}
