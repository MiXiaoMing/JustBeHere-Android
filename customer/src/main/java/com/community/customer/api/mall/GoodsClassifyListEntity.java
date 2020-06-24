package com.community.customer.api.mall;

import com.community.support.common.BaseResult;

import java.io.Serializable;
import java.util.ArrayList;

public class GoodsClassifyListEntity extends BaseResult<GoodsClassifyListEntity> implements Serializable {
    private ArrayList<GoodsClassify> goodsClassify;

    public ArrayList<GoodsClassify> getGoodsClassify() {
        return goodsClassify;
    }

    public void setGoodsClassify(ArrayList<GoodsClassify> goodsClassify) {
        this.goodsClassify = goodsClassify;
    }
}
