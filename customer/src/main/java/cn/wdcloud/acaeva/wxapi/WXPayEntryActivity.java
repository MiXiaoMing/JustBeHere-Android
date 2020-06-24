package cn.wdcloud.acaeva.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.appframe.utils.logger.Logger;
import com.community.customer.common.Constants;
import com.community.customer.event.PayEvent;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;

import cn.wdcloud.acaeva.R;

/**
 * 微信支付结果页面
 */

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wx);
//        ImageView ivBG = findViewById(R.id.ivBG);
//        BgUtil.loadViewBg(this, R.drawable.splash, ivBG);

        api = WXAPIFactory.createWXAPI(this, Constants.wx_app_id);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp resp) {
        int type = resp.getType();

        if (type == ConstantsAPI.COMMAND_PAY_BY_WX) {
            //微信支付
            switch (resp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    EventBus.getDefault().post(new PayEvent(PayEvent.Event.WX_PAY_SUCCESS));
                    finish();
                    break;
                case BaseResp.ErrCode.ERR_SENT_FAILED:
                    Logger.getLogger().d("用户拒绝支付");
                    EventBus.getDefault().post(new PayEvent(PayEvent.Event.WX_PAY_FAIL));
                    finish();
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    Logger.getLogger().d("用户取消支付");
                    EventBus.getDefault().post(new PayEvent(PayEvent.Event.WX_PAY_CANCEL));
                    finish();
                    break;
                default:
                    fileList();
                    break;
            }
        }
    }
}
