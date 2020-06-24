package com.community.customer.api.user;

import com.community.support.common.BaseResult;

import java.io.Serializable;

public class FloatEntity extends BaseResult<FloatEntity> implements Serializable {
    private float balance;

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }
}
