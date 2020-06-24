package com.community.customer.api.servers;

import com.community.support.common.BaseResult;

import java.io.Serializable;

public class ServerDetailEntity extends BaseResult<ServerDetailEntity> {
    public Server service;
    public ServerDetail serviceDetail;
}
