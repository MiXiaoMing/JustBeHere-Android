package com.community.customer.api.mall;

import com.community.customer.api.mall.entity.Goods;
import com.community.customer.api.servers.CarouselEntity;
import com.community.support.common.BaseResult;

import java.io.Serializable;
import java.util.ArrayList;

public class GoodsEntity extends BaseResult<GoodsEntity> implements Serializable {
    public Goods goods;
    public ArrayList<GoodsPrice> goodsPrices;
    public float mixPrice, maxPrice;

    public ArrayList<CarouselEntity> carousels;
}
