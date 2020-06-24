package com.community.customer.api.user.result;

import com.community.customer.api.servers.ServerPrice;
import com.community.customer.api.user.entity.DeliveryAddress;
import com.community.customer.api.user.entity.ServiceOrder;
import com.community.support.common.BaseResult;

import java.io.Serializable;
import java.util.ArrayList;

public class ServerOrderEntity extends BaseResult<ServerOrderEntity> implements Serializable {
    public ServiceOrder serviceOrder;
    public DeliveryAddress deliveryAddress;
    public ArrayList<ServerPrice> prices;
}
