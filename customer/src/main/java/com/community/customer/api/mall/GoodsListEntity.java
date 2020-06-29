package com.community.customer.api.mall;

import com.community.customer.api.servers.CarouselEntity;
import com.community.support.common.BaseArrayResult;

import java.io.Serializable;
import java.util.ArrayList;

public class GoodsListEntity extends BaseArrayResult<GoodsListEntity> {
    public String code, title, desc, icon, tag, content, classify, level;
    public ArrayList<GoodsPrice> prices;
    public float mixPrice, maxPrice;

    public ArrayList<CarouselEntity> carousels;
}
