package com.community.customer.api.user.entity;

import java.io.Serializable;

public class Order implements Serializable {
    public String id, userID, tradeID, deliveryAddressID;

    public String status, content;
    public long remainTime;

    public String createTime, updateTime;
}
