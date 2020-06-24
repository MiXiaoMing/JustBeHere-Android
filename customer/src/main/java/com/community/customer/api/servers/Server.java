package com.community.customer.api.servers;

public class Server {
    public String code, name, shortdesc, desc, linkdesc, icon, bigicon, cover, classify, level;

    public Server(String code, String name, String shortdesc, String desc,
                  String linkdesc, String icon, String bigicon, String cover,
                  String classify, String level) {
        this.code = code;
        this.name = name;
        this.shortdesc = shortdesc;
        this.desc = desc;
        this.linkdesc = linkdesc;
        this.icon = icon;
        this.bigicon = bigicon;
        this.cover = cover;
        this.classify = classify;
        this.level = level;
    }
}
