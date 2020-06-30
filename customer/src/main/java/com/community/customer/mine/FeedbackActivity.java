package com.community.customer.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appframe.utils.logger.Logger;
import com.community.customer.api.CustomObserver;
import com.community.customer.api.EmptyEntity;
import com.community.customer.api.other.OtherDataManager;
import com.community.customer.api.user.FeedbackEntity;
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

        new OtherDataManager().feedback(content)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomObserver<FeedbackEntity>() {

                    @Override
                    public void onError(String message) {

                    }

                    @Override
                    public void onSuccess(FeedbackEntity result) {
                        ToastUtil.show(FeedbackActivity.this, "感谢您的支持，我们再接再厉 ！");
                        finish();
                    }
                });
    }
}
