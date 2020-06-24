package com.community.customer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.view.KeyEvent;

import com.community.support.AutoBaseTitleActivity;
import com.community.support.utils.ToastUtil;

import cn.wdcloud.acaeva.R;

public class SplashActivity extends AutoBaseTitleActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        //登录
//        MobclickAgent.onProfileSignIn("PhoneNum", userInfo.get_loginid());

        //退出
//        MobclickAgent.onProfileSignOff();

//        HashMap<String,String> map = new HashMap<String,String>();
//        map.put("type","book");
//        map.put("quantity","3");
//        MobclickAgent.onEvent(mContext, "purchase", map);

        // 避免从桌面启动程序后，会重新实例化入口类的activity
        if (!this.isTaskRoot()) { // 判断当前activity是不是所在任务栈的根
            Intent intent = getIntent();
            if (intent != null) {
                String action = intent.getAction();
                if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN.equals(action)) {
                    finish();
                    return;
                }
            }
        }
//
//        ImageView ivBG = findViewById(R.id.ivBG);
//        ImageLoader.normalBig(this, R.drawable.splash, R.drawable.splash, ivBG);
    }

    @Override
    protected void onResume() {
        super.onResume();
        alertPermission();

        timer.start();
    }

    private Dialog updateDialog;

    private void alertPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            boolean install = getPackageManager().canRequestPackageInstalls();
            if (!install) {
                if (updateDialog != null) {
                    updateDialog.dismiss();
                    updateDialog = null;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("权限提示：");
                builder.setMessage("倾心需要开启“允许安装应用”权限，请前往设置");
                builder.setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                        SplashActivity.this.startActivityForResult(intent, 10086);
                        dialog.dismiss();
                    }
                });
                builder.setCancelable(false);
                builder.setOnKeyListener(keyListener);
                updateDialog = builder.create();
                updateDialog.show();

                return;
            }
        }
    }

    private CountDownTimer timer = new CountDownTimer(1500, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
        }

        @Override
        public void onFinish() {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }
    };


    // 退出
    private long clickTime = 0;

    private DialogInterface.OnKeyListener keyListener = new DialogInterface.OnKeyListener() {
        @Override
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                long secondTime = System.currentTimeMillis();
                if (secondTime - clickTime < 2000) {
                    finish();
                } else {
                    ToastUtil.show(SplashActivity.this, "再点一次将退出程序");
                    clickTime = secondTime;
                }
                return false;
            }
            return false;
        }
    };
}
