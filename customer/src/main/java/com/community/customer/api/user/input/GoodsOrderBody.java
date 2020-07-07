package com.community.customer.api.user.input;

import java.io.Serializable;

public class GoodsOrderBody implements Serializable {
    public String deliveryAddressID, goodsItems, remind;
    public float price;
}

