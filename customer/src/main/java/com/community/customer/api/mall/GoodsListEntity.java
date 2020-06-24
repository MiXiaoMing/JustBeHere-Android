package com.community.customer.api.mall;

import com.community.support.common.BaseResult;

import java.io.Serializable;
import java.util.ArrayList;

public class GoodsListEntity extends BaseResult<GoodsListEntity> implements Serializable {
    private ArrayList<Goods> goodsList;


    public ArrayList<Goods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(ArrayList<Goods> goodsList) {
        this.goodsList = goodsList;
    }
}
