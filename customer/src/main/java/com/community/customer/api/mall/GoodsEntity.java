package com.community.customer.api.mall;

import com.community.support.common.BaseResult;

import java.io.Serializable;

public class GoodsEntity extends BaseResult<GoodsEntity> implements Serializable {
    private Goods goods;

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }
}
