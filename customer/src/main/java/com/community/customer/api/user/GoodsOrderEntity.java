package com.community.customer.api.user;

import com.community.customer.api.user.entity.DeliveryAddress;
import com.community.customer.api.user.entity.GoodsOrder;
import com.community.support.common.BaseArrayResult;

import java.io.Serializable;
import java.util.ArrayList;

public class GoodsOrderEntity extends BaseArrayResult<GoodsOrderEntity> {
    public GoodsOrder goodsOrder;
    public DeliveryAddress deliveryAddress;

    public ArrayList<Item> items;

    public static class Item implements Serializable {
        public String code, typeID, typeName, icon, title;
        public int number;
        public float typePrice;
    }

    @Override
    public void doAfterDeserialize() {
        if (data == null) {
            data = new ArrayList<>();
        }

        if (goodsOrder == null) {
            goodsOrder = new GoodsOrder();
        }

        if (deliveryAddress == null) {
            deliveryAddress = new DeliveryAddress();
        }
    }
}
