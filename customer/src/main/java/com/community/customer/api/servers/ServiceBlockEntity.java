package com.community.customer.api.servers;

import com.community.customer.api.mall.GoodsClassifyListEntity;
import com.community.customer.api.mall.GoodsListEntity;
import com.community.customer.api.other.Other;
import com.community.support.common.BaseArrayResult;

public class ServiceBlockEntity extends BaseArrayResult<ServiceBlockEntity> {
    public String classify;

    public ServerClassify serviceClassify;
    public Server service;
    public GoodsClassifyListEntity goodsClassify;
    public GoodsListEntity goods;
    public Other other;
}
