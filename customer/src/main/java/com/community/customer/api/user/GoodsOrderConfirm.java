package com.community.customer.api.user;

import java.io.Serializable;
import java.util.ArrayList;

public class GoodsOrderConfirm implements Serializable {
    public String addressid, contact, region, cellphone, remind;
    public Float price;
    public ArrayList<Item> items = new ArrayList<>();

    public GoodsOrderConfirm() {

    }

    public void addItem(String goodsid, String title, String icon, String typeid, String typeName, float typePrice, int number) {
        Item item = new Item(goodsid, title, icon, typeid, typeName, typePrice, number);
        items.add(item);
    }

    public class Item implements Serializable{
        public String goodsid, title, icon;
        public String typeid, typeName;
        public float typePrice;
        public int number;

        public Item(String goodsid, String title, String icon, String typeid, String typeName, float typePrice, int number) {
            this.goodsid = goodsid;
            this.title = title;
            this.icon = icon;
            this.typeid = typeid;
            this.typeName = typeName;
            this.typePrice = typePrice;
            this.number = number;
        }
    }
}
