package com.community.customer.api.other;

import com.community.customer.api.EmptyEntity;
import com.community.customer.api.MobileServerRetrofit;

import io.reactivex.Observable;

/**
 * 其他接口
 */
public class OtherDataManager {
    private OtherService service;

    public OtherDataManager() {
        service = MobileServerRetrofit.getInstance().getRetrofit().create(OtherService.class);
    }

    /**********  用户反馈  **********/

    public Observable<EmptyEntity> feedback(String cid, String content) {
        return service.feedback(cid, content);
    }
}
