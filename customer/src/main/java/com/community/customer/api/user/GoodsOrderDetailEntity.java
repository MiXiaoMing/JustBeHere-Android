package com.community.customer.api.user;

import com.appframe.library.network.http.validate.IAfterDeserializeAction;
import com.community.customer.api.user.entity.DeliveryAddress;
import com.community.customer.api.user.entity.GoodsOrder;
import com.community.support.common.BaseResult;

import java.io.Serializable;
import java.util.ArrayList;

public class GoodsOrderDetailEntity extends BaseResult<GoodsOrderDetailEntity> implements IAfterDeserializeAction {
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
        if (goodsOrder == null) {
            goodsOrder = new GoodsOrder();
        }

        if (deliveryAddress == null) {
            deliveryAddress = new DeliveryAddress();
        }
    }
}
