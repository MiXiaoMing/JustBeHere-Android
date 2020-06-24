package com.community.support;

import android.content.Context;
import android.support.annotation.Keep;
import android.util.Log;

import com.appframe.library.application.AFMultiDexApplication;
import com.taobao.sophix.PatchStatus;
import com.taobao.sophix.SophixApplication;
import com.taobao.sophix.SophixEntry;
import com.taobao.sophix.SophixManager;
import com.taobao.sophix.listener.PatchLoadStatusListener;

public class SophixStubApplication extends SophixApplication {

    @Keep
    @SophixEntry(AFMultiDexApplication.class)
    static class RealApplicationStub {
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        initSophix();
    }

    private void initSophix() {
        String appVersion = "0.0.0";
        try {
            appVersion = this.getPackageManager()
                    .getPackageInfo(this.getPackageName(), 0)
                    .versionName;
        } catch (Exception e) {
            Log.i("SophixStubApplication","appVersion获取失败");
        }

        final SophixManager instance = SophixManager.getInstance();
        instance.setContext(this)
                .setAppVersion(appVersion)
                .setSecretMetaData("25781646", "c6e0e16387140b5f71eadccbe068b25c", "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCs52gCz9/39KxVh8KNUqjg6DSZJGb5/FFkXNC7/YX4aVL1g3evvb9OlmGcR9vuAw11kFAB6OuwVbT/byENx2ABZcqLT25oAqeYK9KSUz3iPFTDHsnpWeAee4D0A8+7To3gLd72Yaeh9ToHtMRs4/lg02UYCXsi2iMiu2iafmjHx8oWXrpwTxKnqw+4Pzgw2WSZDP4paLcMUv562b48wAKSufwGi3gDRKcYzZju+llKPDDs9FMmrVK4GVXMflqke5KJImtluZlENs6SYBNLpMI2BcC9wWGZ3izoOOGwwmYCr6+tG6iRFGI8UgsPHNszH245nMu/jDqUV5wOMkFukAUdAgMBAAECggEAeKaFAqlRbLGT657vGDRc2YvO4CnxnXD+zpyhnvxalqvQErepNb2fF9SByk6uXdhNdQK9gYeppwM3aKKAGyBLks4X1rwXm4Z2sg2qQybI4OwebYU3eZiYdkzR//QE6Xv4Lj1z/tLH6ZC37JasFhvCcVkxiuNaxEgZCmxhoqSgsv5GGy0p6C0OtBIIOQ066atXBc0UzHkACaHiMZGp4c/atg1oXGBgYrYYIUlTpTozNnFoQk8YWiwVWOaZjyPs7oOLAvuOlinMvW7+MtEGUb9KNvMxpyA8oicyTceMNep8j1f/1TkYTu2o5Q7h/e56O6HRuJ2ZcSFiVzHzsieAII2B9QKBgQDTtr305PTqa3wiLYNGvTL1pQvJNqUcyR+mNaxYd2cArOtAKXAUx+VSRveYm6f7z6RxDbBlWUx0ZB4+vu1CC5Ym6BsKFFzchwLcF1Gu86DpEL3u1Dv4vRU/fyYeM6z9ILK65Aq9SKGdzcTHYQ0zzqn4/y2hk7fUePmyVT9ysEdu7wKBgQDREmWrAhXAOAOqT170rT79lSWVplvWCK4UtirPCxgUVQ+EQqwP/Wfs0ZqXL+0/Mim5MJRIqUCaxDV5YHyOib7WHa5NZXIPdt26xVtKUInRk2sBXT+Max/+0fljXoDzBj+fCOOFtUAULo6ocbIfUoXOvLJidzq/y0fodOvJQznMswKBgHwyofD+N6clcubh6DchWrm8HtPlWKswWX650eb79NLynfc/1954/dBsjWdbOQqB3yfrLeXmovFMommQu8KY5Xbhhqs9EtTT5xuSGnsVtPnEtodavevHkKV1Q7lH+11rRy5YBOzRpO3b/mowRvxzakcF+5DkBV2BsompdNUlEGLFAoGBAMSLUPyEh0Ie5oMy/odyXqcxkRWwQOFjY5Og9XmSal6i4GVr8ClQBmUBXx0GNziTxdmMq7nrMwrLraGfbTjqkXbkjDnCRon3KtcsNXgVYpqcxZmQue8dvgRmDNa3vu24UBMH3iySidzvL7l2uQbtI1GI3wHmKFqplb4YPJkGE841AoGBAKAuAEiWawXXe5fYTJW5lxfIImWR4Hv+4/LjO5tWxW1/ChEBVKlJBGv56/3sR1XflQ/fnDmWxPvQnSR+/PQusCh4mf236qNoE9wysvTS1t4igfcBPHV4MRumEb0l7rU0GbAwm47i3cZ6XrHG2MLKkOkXXjEeqHpiizp62qukNQ5+")
                .setEnableDebug(false)
                .setAesKey("GrainRain870620A")
                .setEnableFullLog()
                .setPatchLoadStatusStub(new PatchLoadStatusListener() {
                    @Override
                    public void onLoad(final int mode, final int code, final String info, final int handlePatchVersion) {
                        if (code == PatchStatus.CODE_LOAD_SUCCESS) {
                            Log.i("SophixStubApplication", "sophix加载布丁成功!");
                        } else if (code == PatchStatus.CODE_LOAD_RELAUNCH) {
                            // 如果需要在后台重启，建议此处用SharePreference保存状态。
                            Log.i("SophixStubApplication","sophix预加载布丁成功. 重启应用生效.");
                            SophixManager.getInstance().killProcessSafely();
                        }
                    }
                }).initialize();
    }
}
