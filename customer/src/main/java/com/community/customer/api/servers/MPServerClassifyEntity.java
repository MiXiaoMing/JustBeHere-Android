package com.community.customer.api.servers;

import com.community.support.common.BaseResult;

import java.io.Serializable;
import java.util.ArrayList;

public class MPServerClassifyEntity extends BaseResult<MPServerClassifyEntity> implements Serializable {
    public ArrayList<ServerClassify> serverClassify;
}
