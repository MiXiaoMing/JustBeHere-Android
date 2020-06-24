package com.community.support.component;

import android.content.Context;

import com.appframe.utils.logger.Logger;
import com.community.support.utils.PushUtil;
import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTNotificationMessage;
import com.igexin.sdk.message.GTTransmitMessage;


public class IntentService extends GTIntentService {

    public IntentService() {

    }

    @Override
    public void onReceiveServicePid(Context context, int pid) {
    }

    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage msg) {
        // 透传消息的处理，详看SDK demo
        Logger.getLogger().d("透传消息的处理");
    }

    @Override
    public void onReceiveClientId(Context context, String clientid) {
        Logger.getLogger().d("接收端ID: " + clientid);
        PushUtil.setCID(clientid);
    }

    @Override
    public void onReceiveOnlineState(Context context, boolean online) {
        Logger.getLogger().d("离线上线通知");
    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage cmdMessage) {
        Logger.getLogger().d("各种事件处理回执");
    }

    @Override
    public void onNotificationMessageArrived(Context context, GTNotificationMessage msg) {
    }

    @Override
    public void onNotificationMessageClicked(Context context, GTNotificationMessage msg) {
    }
}
