package com.community.customer.api.servers;

import com.appframe.library.network.http.validate.IAfterDeserializeAction;

import java.util.ArrayList;

public class ServerClassify implements IAfterDeserializeAction {
    public String code, name, desc, icon, image;
    public ArrayList<Server> servers;

    @Override
    public void doAfterDeserialize() {
        if (servers == null) {
            servers = new ArrayList<>();
        }
    }
}
