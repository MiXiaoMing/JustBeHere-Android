package com.community.customer.api.mall;

import com.community.customer.api.mall.entity.Goods;
import com.community.customer.api.servers.CarouselEntity;
import com.community.support.common.BaseArrayResult;

import java.io.Serializable;
import java.util.ArrayList;

public class GoodsListEntity extends BaseArrayResult<GoodsListEntity> {
    public Goods goods;
    public ArrayList<GoodsPrice> goodsPrices;
    public float mixPrice, maxPrice;

    public ArrayList<CarouselEntity> carousels;
}
