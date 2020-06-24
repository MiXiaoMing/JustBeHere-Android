package com.community.customer.api.servers;

import com.community.support.common.BaseArrayResult;
import com.community.support.common.BaseResult;

import java.io.Serializable;
import java.util.ArrayList;

public class CarouselEntity extends BaseArrayResult<CarouselEntity> {
    public String classify, code, path;

    public CarouselEntity(String classify, String code, String path) {
        this.classify = classify;
        this.code = code;
        this.path = path;
    }
}
