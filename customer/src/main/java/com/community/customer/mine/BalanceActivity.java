package com.community.customer.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.community.customer.LoginActivity;
import com.community.customer.order.ServerOrderSelectActivity;
import com.community.support.AutoBaseTitleActivity;
import com.community.support.utils.UserInfoUtil;

import cn.wdcloud.acaeva.R;

public class BalanceActivity extends AutoBaseTitleActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_balance);

        LinearLayout llyBack = findViewById(R.id.llyBack);
        TextView tvDeposit = findViewById(R.id.tvDeposit);

        llyBack.setOnClickListener(clickListener);
        tvDeposit.setOnClickListener(clickListener);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.llyBack:
                    finish();
                    break;

                case R.id.tvDeposit:
                    if (!UserInfoUtil.isLogin()) {
                        startActivity(new Intent(BalanceActivity.this, LoginActivity.class));
                    } else {
                        startActivity(new Intent(BalanceActivity.this, DepositActivity.class));
                    }
                    break;
            }
        }
    };
}
