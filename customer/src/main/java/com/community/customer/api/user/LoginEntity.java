package com.community.customer.api.user;

import com.community.support.common.BaseResult;
import com.community.support.common.UserInfo;

import java.io.Serializable;

public class LoginEntity extends BaseResult<LoginEntity> implements Serializable {
    public String token;
    public UserInfo userInfo;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
