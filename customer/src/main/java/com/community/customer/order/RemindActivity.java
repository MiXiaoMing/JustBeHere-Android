package com.community.customer.order;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.community.support.AutoBaseTitleActivity;
import com.community.support.component.TypefaceHelper;

import cn.wdcloud.acaeva.R;

public class RemindActivity extends AutoBaseTitleActivity {

    private EditText etRemind;
    private boolean exitType = false; //false:不更新  true:更新

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_remind);

        LinearLayout llyBack = findViewById(R.id.llyBack);
        etRemind = findViewById(R.id.etRemind);
        etRemind.setTypeface(TypefaceHelper.get(this));
        TextView tvOk = findViewById(R.id.tvOk);

        String content = getIntent().getStringExtra("content");
        if (!TextUtils.isEmpty(content)) {
            etRemind.setText(content);
        }

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
                    exitType = true;
                    finish();
                    break;
            }
        }
    };

    @Override
    public void finish() {
        if (exitType) {
            Intent intent = new Intent();
            intent.putExtra("remind", etRemind.getText().toString().trim());
            setResult(Activity.RESULT_OK, intent);
        }

        super.finish();
    }
}
