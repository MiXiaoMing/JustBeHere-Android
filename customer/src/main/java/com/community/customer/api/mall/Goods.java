package com.community.customer.api.mall;

import com.community.customer.api.servers.CarouselEntity;

import java.io.Serializable;
import java.util.ArrayList;

public class Goods implements Serializable {
    public String code, title, desc, icon, tag, content, classify, level;
    public ArrayList<GoodsPrice> prices;
    public float mixPrice, maxPrice;

    public ArrayList<CarouselEntity> carousels;

    public Goods(String code, String title, String icon, String tag, String classify, String level,
                 String content, String desc, ArrayList<GoodsPrice> prices, float mixPrice, float maxPrice,
                 ArrayList<CarouselEntity> carousels) {
        this.code = code;
        this.title = title;
        this.icon = icon;
        this.tag = tag;
        this.classify = classify;
        this.level = level;
        this.desc = desc;
        this.content = content;
        this.prices = prices;
        this.carousels = carousels;
        this.mixPrice = mixPrice;
        this.maxPrice = maxPrice;
    }
}
