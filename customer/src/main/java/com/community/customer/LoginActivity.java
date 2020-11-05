package com.community.customer;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appframe.utils.logger.Logger;
import com.community.customer.api.CustomObserver;
import com.community.customer.api.user.LoginEntity;
import com.community.customer.api.user.UserDataManager;
import com.community.customer.api.user.input.LoginBody;
import com.community.customer.other.AgreementActivity;
import com.community.support.AutoBaseTitleActivity;
import com.community.support.common.StringResult;
import com.community.support.common.UserInfo;
import com.community.support.component.TypefaceHelper;
import com.community.support.utils.AuthUtil;
import com.community.support.utils.PhoneFormatCheckUtils;
import com.community.support.utils.PushUtil;
import com.community.support.utils.ReportUtil;
import com.community.support.utils.ToastUtil;
import com.community.support.utils.UserInfoUtil;
import com.umeng.analytics.MobclickAgent;

import cn.wdcloud.acaeva.R;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class LoginActivity extends AutoBaseTitleActivity {
    private EditText etAccount, etSmsCode;
    private TextView tvSmsCode;
    private UserDataManager dataManager = new UserDataManager();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        LinearLayout llyBack = findViewById(R.id.llyBack);

        etAccount = findViewById(R.id.etAccount);
        etSmsCode = findViewById(R.id.etSmsCode);

        etAccount.setTypeface(TypefaceHelper.get(this));
        etSmsCode.setTypeface(TypefaceHelper.get(this));

        tvSmsCode = findViewById(R.id.tvSmsCode);
        TextView tvLogin = findViewById(R.id.tvLogin);

        findViewById(R.id.tvUserAgree).setOnClickListener(clickListener);
        findViewById(R.id.tvPrivacy).setOnClickListener(clickListener);

        llyBack.setOnClickListener(clickListener);
        tvSmsCode.setOnClickListener(clickListener);
        tvLogin.setOnClickListener(clickListener);
    }

    private void sendSms() {
        Logger.getLogger().d("发送验证码");

        tvSmsCode.setEnabled(false);

        String cellphone = etAccount.getText().toString().trim();

        if (TextUtils.isEmpty(cellphone)) {
            ToastUtil.show(LoginActivity.this, "请输入手机号");
            return;
        } else if (!PhoneFormatCheckUtils.isPhoneLegal(cellphone)) {
            ToastUtil.show(LoginActivity.this, "请输入正确的手机号");
            return;
        }

        countDownTimer.start();

        dataManager.sendSms(RequestBody.create(MediaType.parse("text/plain"), cellphone))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<StringResult>() {
                    @Override
                    public void onError(Throwable e) {
                        ReportUtil.reportError(e);
                        tvSmsCode.setEnabled(true);
                        Logger.getLogger().e("发送验证码：" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(StringResult result) {
                        if (!result.success) {
                            Logger.getLogger().e("发送验证码，msgCode：" + result.errCode + "/n" + result.message);
                            tvSmsCode.setEnabled(true);
                        } else {
                            if (result.data == null) {
                                Logger.getLogger().e("发送验证码, result为空");
                                tvSmsCode.setEnabled(true);
                                return;
                            }

                            etSmsCode.setText(result.data);
                        }
                    }
                });
    }

    private void login() {
        Logger.getLogger().d("用户登录");
        String cellphone = etAccount.getText().toString().trim();
        String smsCode = etSmsCode.getText().toString().trim();

        if (TextUtils.isEmpty(cellphone)) {
            ToastUtil.show(LoginActivity.this, "请输入手机号");
            return;
        } else if (!PhoneFormatCheckUtils.isPhoneLegal(cellphone)) {
            ToastUtil.show(LoginActivity.this, "请输入正确的手机号");
            return;
        } else if (TextUtils.isEmpty(smsCode)) {
            ToastUtil.show(LoginActivity.this, "请输入验证码");
            return;
        }


        // TODO: 2020/6/23 这里获取 CID
//        PushUtil.getCID()

        LoginBody body = new LoginBody();
        body.phoneNumber = cellphone;
        body.smsCode = smsCode;

        dataManager.login(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomObserver<StringResult>() {

                    @Override
                    public void onError(String message) {

                    }

                    @Override
                    public void onSuccess(StringResult result) {
                        AuthUtil.saveAuth(result.data);
                        MobclickAgent.onProfileSignIn("Login_PhoneNum", UserInfoUtil.getCellphone());

                        getUser();
                    }
                });
    }

    private void getUser() {
        dataManager.getUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomObserver<UserInfo>() {

                    @Override
                    public void onError(String message) {

                    }

                    @Override
                    public void onSuccess(UserInfo result) {
                        UserInfoUtil.setUserID(result.data.id);
                        UserInfoUtil.setUserName(result.data.name);
                        UserInfoUtil.setUserType(result.data.type);
                        UserInfoUtil.setCellphone(result.data.phoneNumber);
                        finish();
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (TextUtils.isEmpty(UserInfoUtil.getUserID())) {
            AuthUtil.saveAuth("");
        }
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.llyBack:
                    finish();
                    break;

                case R.id.tvSmsCode:
                    sendSms();
                    break;

                case R.id.tvLogin:
                    login();
                    break;

                case R.id.tvUserAgree: {
                    Intent intent = new Intent(LoginActivity.this, AgreementActivity.class);
                    intent.putExtra("title", "用户协议与免责条款");
                    intent.putExtra("url", "https://otherh5.ycaiyun.com/xieyi/xy-1.html\n");
                    startActivity(intent);
                }
                    break;

                case R.id.tvPrivacy: {
                    Intent intent = new Intent(LoginActivity.this, AgreementActivity.class);
                    intent.putExtra("title", "隐私政策");
                    intent.putExtra("url", "https://otherh5.ycaiyun.com/xieyi/xy-2.html");
                    startActivity(intent);
                }
                    break;
        }
    }};

    private CountDownTimer countDownTimer = new CountDownTimer(60 * 1000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            tvSmsCode.setText(millisUntilFinished / 1000 + "s");
        }

        @Override
        public void onFinish() {
            tvSmsCode.setEnabled(true);
            tvSmsCode.setText("获取验证码");
        }
    };
}
