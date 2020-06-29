package com.community.customer.api.user.input;

import java.io.Serializable;

public class CartBody implements Serializable {
    public String id, userID;
    public String goodsID, typeID, typeName;
    public int number;
}

