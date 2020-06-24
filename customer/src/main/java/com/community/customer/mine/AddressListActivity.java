package com.community.customer.mine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import cn.wdcloud.acaeva.R;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import com.appframe.utils.logger.Logger;
import com.community.customer.api.user.AddressEntity;
import com.community.customer.api.user.UserDataManager;
import com.community.customer.mine.adpater.AddressAdapter;
import com.community.customer.mine.adpater.AddressEditAdapter;
import com.community.support.AutoBaseTitleActivity;
import com.community.support.utils.ReportUtil;

import java.util.ArrayList;


public class AddressListActivity extends AutoBaseTitleActivity {
    private AddressAdapter addressAdapter;
    private AddressEditAdapter addressEditAdapter;

    private String type, selectID; //edit: 编辑UI
    private ArrayList<AddressEntity> addressArrayList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_address_list);

        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        String selectID = intent.getStringExtra("id");

        ListView lvAddressList = findViewById(R.id.lvAddressList);
        if (!TextUtils.isEmpty(type) && type.equals("edit")) {
            addressEditAdapter = new AddressEditAdapter(this, selectID);
            lvAddressList.setAdapter(addressEditAdapter);
        } else {
            addressAdapter = new AddressAdapter(this);
            lvAddressList.setAdapter(addressAdapter);
        }

        LinearLayout llyBack = findViewById(R.id.llyBack);
        TextView tvAdd = findViewById(R.id.tvAdd);

        llyBack.setOnClickListener(clickListener);
        tvAdd.setOnClickListener(clickListener);
    }

    @Override
    protected void onResume() {
        super.onResume();

        selectID = "";
        getAddressList();
    }

    private void getAddressList() {
        Logger.getLogger().d("获取服务地址列表");
        UserDataManager dataManager = new UserDataManager();

        dataManager.getAddressList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AddressEntity>() {

                    @Override
                    public void onError(Throwable e) {
                        ReportUtil.reportError(e);
                        Logger.getLogger().e("获取服务地址列表：" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(AddressEntity result) {
                        if (!result.success) {
                            Logger.getLogger().e("获取服务地址列表，msgCode：" + result.errCode + "/n" + result.message);
                        } else {
                            if (result.data == null) {
                                Logger.getLogger().e("获取服务地址列表, result为空");
                                return;
                            }

                            addressArrayList = result.data;

                            if (!TextUtils.isEmpty(type) && type.equals("edit")) {
                                addressEditAdapter.addAll(addressArrayList);
                            } else {
                                addressAdapter.addAll(addressArrayList);
                            }
                        }
                    }
                });
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.llyBack:
                    finish();
                    break;

                case R.id.tvAdd:
                    Intent intent = new Intent(AddressListActivity.this, AddressSettingActivity.class);
                    intent.putExtra("type", "A");
                    startActivity(intent);
                    break;
            }
        }
    };

    public void setSelect(String id) {
        selectID = id;
    }

    @Override
    public void finish() {
        boolean isSetResult = false;
        for (AddressEntity address : addressArrayList) {
            if (address.id.equals(selectID)) {
                Intent intent = new Intent();
                intent.putExtra("address", address);
                setResult(Activity.RESULT_OK, intent);
                isSetResult = true;
                break;
            }
        }

        if (!isSetResult) {
            setResult(Activity.RESULT_OK, new Intent());
        }

        super.finish();

    }
}
