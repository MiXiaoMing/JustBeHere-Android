package com.community.customer.api.user;

import com.community.support.common.BaseArrayResult;

import java.io.Serializable;
import java.util.ArrayList;

public class ServerTimeEntity extends BaseArrayResult<ServerTimeEntity> {
    public String dayWeek, dayDesc;
    public long dayTime;

    public ArrayList<HourTime> hourTimes;

    public class HourTime implements Serializable {
        public String timeDesc;
        public boolean isTimeActive;
        public int count;
        public long hourTime;
    }
}
