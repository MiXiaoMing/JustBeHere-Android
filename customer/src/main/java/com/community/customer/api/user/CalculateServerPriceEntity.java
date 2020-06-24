package com.community.customer.api.user;

import com.community.support.common.BaseResult;

import java.io.Serializable;

public class CalculateServerPriceEntity extends BaseResult<CalculateServerPriceEntity> implements Serializable {
    private float totalPrice, discountPrice;

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public float getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(float discountPrice) {
        this.discountPrice = discountPrice;
    }
}
