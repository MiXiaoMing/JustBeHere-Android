package com.community.customer.api.user;

import com.community.support.common.BaseArrayResult;

import java.io.Serializable;
import java.util.ArrayList;

public class GoodsOrderEntity extends BaseArrayResult<GoodsOrderEntity> implements Serializable {
    public String id, tradeid, remind, status, createTime, cancelTime, content;
    public int itemsCount;
    public Float price;
    public String addressid, region, contact, cellphone;
    public long payTime;
    public ArrayList<Item> items;

    public static class Item implements Serializable {
        public String id, title, icon, typeName;
        public int number;
        public float typePrice;
    }
}
