package com.community.customer.api.user.input;

import java.io.Serializable;

public class ServiceOrderBody implements Serializable {
    public String deliveryAddressID, serviceCode, serviceName, remind, serviceItems;
    public long serviceTime;
    public float totalPrice, discountPrice, payPrice;

    public String id;
    public String status, content;
}
