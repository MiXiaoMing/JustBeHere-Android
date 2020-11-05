package com.community.support;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import com.appframe.framework.config.MetaDataConfig;
import com.appframe.library.application.AFApplication;
import com.appframe.library.application.AFMultiDexApplication;
import com.appframe.utils.logger.Logger;
import com.community.support.common.Constants;
import com.community.support.component.IntentService;
import com.community.support.component.PushService;
import com.igexin.sdk.PushManager;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.commonsdk.statistics.common.DeviceConfig;
import com.zhy.autolayout.config.AutoLayoutConifg;

import java.lang.reflect.Method;

public class QXApplication extends AFMultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        Logger.getLogger().setWriteDebugLog(true);

        AutoLayoutConifg.getInstance().useDeviceSize();

        //友盟
        UMConfigure.init(AFApplication.applicationContext, Constants.umeng_app_key, Constants.tag,
                UMConfigure.DEVICE_TYPE_PHONE, "");
        //个推 推送
        PushManager.getInstance().initialize(AFApplication.applicationContext, PushService.class);
        PushManager.getInstance().registerPushIntentService(AFApplication.applicationContext, IntentService.class);

        initData();
    }

    public static boolean checkPermission(Context context, String permission) {
        boolean result = false;
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                Class<?> clazz = Class.forName("android.content.Context");
                Method method = clazz.getMethod("checkSelfPermission", String.class);
                int rest = (Integer) method.invoke(context, permission);
                if (rest == PackageManager.PERMISSION_GRANTED) {
                    result = true;
                } else {
                    result = false;
                }
            } catch (Exception e) {
                result = false;
            }
        } else {
            PackageManager pm = context.getPackageManager();
            if (pm.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                result = true;
            }
        }
        return result;
    }


    public static void getDeviceInfo(Context context) {
        String[] deviceInfo = new String[2];
        try {
            if(context != null){
                deviceInfo[0] = DeviceConfig.getDeviceIdForGeneral(context);
                deviceInfo[1] = DeviceConfig.getMac(context);
            }
            Logger.getLogger().e("{\"device_id\":\"" + deviceInfo[0] + "\",\"mac\":\"" + deviceInfo[1] + "\"}");
        } catch (Exception e){

        }
    }

    private void initData() {
        new MetaDataConfig().initSPStoreName("jbh");
    }
}
