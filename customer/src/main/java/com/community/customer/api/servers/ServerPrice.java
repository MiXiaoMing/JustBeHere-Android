package com.community.customer.api.servers;

import java.io.Serializable;

public class ServerPrice implements Serializable {
    public String id, name, unit;
    public int minimum, buyNumber = 0;
    public float price, discount;
}
