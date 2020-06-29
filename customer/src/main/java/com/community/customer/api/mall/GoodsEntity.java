package com.community.customer.api.mall;

import com.community.customer.api.servers.CarouselEntity;
import com.community.support.common.BaseResult;

import java.io.Serializable;
import java.util.ArrayList;

public class GoodsEntity extends BaseResult<GoodsEntity> implements Serializable {
    public String code, title, desc, icon, tag, content, classify, level;
    public ArrayList<GoodsPrice> prices;
    public float mixPrice, maxPrice;

    public ArrayList<CarouselEntity> carousels;
}
