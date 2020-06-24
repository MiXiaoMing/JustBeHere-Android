package com.community.customer.api.mall;

import java.io.Serializable;

public class GoodsPrice implements Serializable {
    public String id, name;
    public float price, discountRatio;

    public GoodsPrice(String id, String name, float price, float discountRatio) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.discountRatio = discountRatio;
    }
}
