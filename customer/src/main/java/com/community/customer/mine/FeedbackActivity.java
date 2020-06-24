package com.community.customer.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appframe.utils.logger.Logger;
import com.community.customer.api.EmptyEntity;
import com.community.customer.api.other.OtherDataManager;
import com.community.support.AutoBaseTitleActivity;
import com.community.support.component.TypefaceHelper;
import com.community.support.utils.PushUtil;
import com.community.support.utils.ReportUtil;
import com.community.support.utils.ToastUtil;

import cn.wdcloud.acaeva.R;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class FeedbackActivity extends AutoBaseTitleActivity {

    private EditText etRemind;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_feedback);

        LinearLayout llyBack = findViewById(R.id.llyBack);
        etRemind = findViewById(R.id.etRemind);
        etRemind.setTypeface(TypefaceHelper.get(this));
        TextView tvOk = findViewById(R.id.tvOk);

        llyBack.setOnClickListener(clickListener);
        tvOk.setOnClickListener(clickListener);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.llyBack:
                    finish();
                    break;

                case R.id.tvOk:
                    feedback();
                    break;
            }
        }
    };

    private void feedback() {
        Logger.getLogger().d("提交反馈");
        String content = etRemind.getText().toString().trim();

        OtherDataManager dataManager = new OtherDataManager();
        dataManager.feedback(PushUtil.getCID(), content)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<EmptyEntity>() {

                    @Override
                    public void onError(Throwable e) {
                        ReportUtil.reportError(e);
                        Logger.getLogger().e("提交反馈：" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(EmptyEntity result) {
                        if (!result.success) {
                            Logger.getLogger().e("提交反馈，msgCode：" + result.errCode + "/n" + result.message);
                        } else {
                            if (result.data == null) {
                                Logger.getLogger().e("提交反馈, result为空");
                            }
                        }
                        ToastUtil.show(FeedbackActivity.this, "感谢您的支持，我们再接再厉 ！");
                        finish();
                    }
                });
    }
}
