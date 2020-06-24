package com.community.customer.event;

import java.io.Serializable;

/**
 * 支付事件
 */

public class PayEvent implements Serializable {

    private Event event;

    public PayEvent(Event event) {
        this.event = event;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }


    public enum Event {
        START_PAY,
        WX_PAY_SUCCESS,
        WX_PAY_FAIL,
        WX_PAY_CANCEL,
    }
}
