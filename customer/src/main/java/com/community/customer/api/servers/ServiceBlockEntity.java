package com.community.customer.api.servers;

import com.community.customer.api.mall.Goods;
import com.community.customer.api.mall.GoodsClassify;
import com.community.customer.api.other.Other;
import com.community.support.common.BaseArrayResult;

public class ServiceBlockEntity extends BaseArrayResult<ServiceBlockEntity> {
    public String classify;

    public ServerClassify serviceClassify;
    public Server service;
    public GoodsClassify goodsClassify;
    public Goods goods;
    public Other other;
}
