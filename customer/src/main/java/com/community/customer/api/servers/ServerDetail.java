package com.community.customer.api.servers;

import com.appframe.library.network.http.validate.IAfterDeserializeAction;

import java.util.ArrayList;

public class ServerDetail implements IAfterDeserializeAction {
    public String code, classify, note, effect, introduce, refertime, scope,
            tools, assurance, flow, level;
    public ArrayList<Other> others;
    public ArrayList<ServerPrice> prices;


    public ServerDetail(String code, String classify, String note, String effect,
                        String introduce, String refertime,
                        ArrayList<Other> others, String scope, String tools, String assurance,
                        String flow, String level) {
        this.code = code;
        this.classify = classify;
        this.note = note;
        this.effect = effect;
        this.introduce = introduce;
        this.refertime = refertime;
        this.others = others;
        this.scope = scope;
        this.tools = tools;
        this.assurance = assurance;
        this.flow = flow;
        this.level = level;
    }

    @Override
    public void doAfterDeserialize() {
        if (prices == null) {
            prices = new ArrayList<>();
        }

        if (others == null) {
            others = new ArrayList<>();
        }
    }

    public static class Other {
        public String cover, code, name, desc;
    }


}
