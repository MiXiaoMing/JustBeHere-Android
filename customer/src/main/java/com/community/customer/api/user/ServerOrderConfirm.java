package com.community.customer.api.user;

import java.io.Serializable;

public class ServerOrderConfirm implements Serializable {
    public String addressid, contact, region, cellphone, serverCode, serverName, remind;
    public Float totalPrice, discountPrice, payPrice;
    public long serverTime;

    public ServerOrderConfirm() {

    }
}
