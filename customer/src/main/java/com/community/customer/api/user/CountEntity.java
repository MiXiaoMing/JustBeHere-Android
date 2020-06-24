package com.community.customer.api.user;

import com.community.support.common.BaseResult;

import java.io.Serializable;

public class CountEntity extends BaseResult<CountEntity> implements Serializable {
    private String count;

    public int getCountInt() {
        return Integer.valueOf(count);
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
