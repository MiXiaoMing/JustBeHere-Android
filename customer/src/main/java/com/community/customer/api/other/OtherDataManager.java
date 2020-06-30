package com.community.customer.api.other;

import com.community.customer.api.EmptyEntity;
import com.community.customer.api.MobileServerRetrofit;
import com.community.customer.api.user.FeedbackEntity;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 其他接口
 */
public class OtherDataManager {
    private OtherService service;

    public OtherDataManager() {
        service = MobileServerRetrofit.getInstance().getRetrofit().create(OtherService.class);
    }

    /**********  用户反馈  **********/

    public Observable<FeedbackEntity> feedback(String content) {
        return service.feedback(RequestBody.create(MediaType.parse("text/plain"), content));
    }
}
