package com.community.customer.api.user.result;

import com.community.customer.api.servers.ServerPrice;
import com.community.customer.api.user.entity.DeliveryAddress;
import com.community.customer.api.user.entity.Order;
import com.community.customer.api.user.entity.ServiceOrder;
import com.community.support.common.BaseArrayResult;

import java.io.Serializable;
import java.util.ArrayList;

public class ServerOrderListEntity extends BaseArrayResult<ServerOrderListEntity> {
    public Order order;
    public ServiceOrder serviceOrder;
    public DeliveryAddress deliveryAddress;
    public ArrayList<ServerPrice> prices;
}
