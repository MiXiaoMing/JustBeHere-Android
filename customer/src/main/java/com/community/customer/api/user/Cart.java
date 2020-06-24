package com.community.customer.api.user;

import java.io.Serializable;

public class Cart implements Serializable {
    public String id, goodsid, title, icon, typeID, typeName;
    public float typePrice;
    public int number;
    public boolean isSelect = false;

    public Cart() {

    }
}
