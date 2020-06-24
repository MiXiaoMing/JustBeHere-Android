package com.community.customer.mall;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appframe.library.component.image.ImageLoader;
import com.appframe.utils.logger.Logger;
import com.community.customer.common.ServerConfig;
import com.community.customer.api.mall.Goods;
import com.community.customer.api.mall.GoodsEntity;
import com.community.customer.api.mall.MallDataManager;
import com.community.customer.api.servers.CarouselEntity;
import com.community.customer.api.user.CountEntity;
import com.community.customer.api.user.UserDataManager;
import com.community.customer.common.Constants;
import com.community.customer.mine.ShoppingCartActivity;
import com.community.support.AutoBaseTitleActivity;
import com.community.support.component.FontTextView;
import com.community.support.utils.GlideImageLoader;
import com.community.support.utils.ReportUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.youth.banner.Banner;
import com.zhy.autolayout.utils.AutoUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import cn.wdcloud.acaeva.R;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class GoodsDetailActivity extends AutoBaseTitleActivity {
    private TextView tvCartCount;
    private Goods goods;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_goods_detail);

        tvCartCount = findViewById(R.id.tvCartCount);


        goodsDetail();
    }

    private void initView(Goods goods) {
        LinearLayout llyBack = findViewById(R.id.llyBack);

        TextView tvTitle = findViewById(R.id.tvTitle);
        TextView tvDesc = findViewById(R.id.tvDesc);
        LinearLayout llyTags = findViewById(R.id.llyTags);
        TextView tvPrice = findViewById(R.id.tvPrice);
        LinearLayout llyContent = findViewById(R.id.llyContent);

        RelativeLayout rlyCart = findViewById(R.id.rlyCart);
        RelativeLayout rlyPhoneService = findViewById(R.id.rlyPhoneService);

        TextView tvAddCart = findViewById(R.id.tvAddCart);
        TextView tvBuyNow = findViewById(R.id.tvBuyNow);

        tvTitle.setText(goods.title);
        tvDesc.setText(goods.desc);
        if (goods.mixPrice == goods.maxPrice) {
            tvPrice.setText("¥ " + goods.mixPrice);
        } else {
            tvPrice.setText("¥ " + goods.mixPrice + " - " + goods.maxPrice);
        }
        initTags(llyTags, goods.tag);
        initContent(llyContent, goods.content);

       llyBack.setOnClickListener(clickListener);
       rlyCart.setOnClickListener(clickListener);
       rlyPhoneService.setOnClickListener(clickListener);
       tvAddCart.setOnClickListener(clickListener);
       tvBuyNow.setOnClickListener(clickListener);
    }

    private void initTags(LinearLayout root, String tagsStr) {
        root.removeAllViews();

        List<String> tags = Arrays.asList(tagsStr.split(","));
        for (int i = 0; i < tags.size(); ++i) {
            if (i > 2) {
                break;
            }
            TextView textView = new FontTextView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if (i != 0) {
                layoutParams.setMargins(AutoUtils.getPercentWidthSize(5), 0, 0, 0);
            }
            int index = (int)(Math.random() * Constants.tagsColor.size());
            textView.setBackgroundColor(Color.parseColor(Constants.tagsColor.get(index)));
            textView.setPadding(4, 4, 4, 4);
            textView.setLayoutParams(layoutParams);
            textView.setText(tags.get(i));
            textView.setTextColor(Color.WHITE);
            textView.setTextSize(12);

            root.addView(textView);
        }
    }

    private void initContent(LinearLayout root, String content) {
        Type listType = new TypeToken<LinkedList<ImageUnit>>(){}.getType();
        Gson gson = new Gson();
        LinkedList<ImageUnit> imageUnits = gson.fromJson(content, listType);
        for (int i = 0; i < imageUnits.size(); ++i) {
            ImageUnit imageUnit = imageUnits.get(i);
            if (TextUtils.isEmpty(imageUnit.image)) {
                return;
            }

            int imageHeight = 120;
            if (imageUnit.height >= 0) {
                imageHeight = imageUnit.height;
            }
            ImageView imageView = new ImageView(this);
            RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, AutoUtils.getPercentHeightSize(imageHeight));
            layoutParams2.setMargins(0, 0, 0, 0);
            imageView.setLayoutParams(layoutParams2);
            ImageLoader.normal(this, imageUnit.image, R.drawable.default_image_white, imageView);
            root.addView(imageView);
        }
    }

    //轮播图
    private void initCarouselView(ArrayList<CarouselEntity> carousels) {
        if (carousels == null || carousels.size() == 0) {
            Logger.getLogger().e("没有获取到轮播图数据");
            carousels = new ArrayList<>();
            carousels.add(new CarouselEntity("", "", ServerConfig.file_host + "image/carousel_default.jpg"));
        }

        final List<String> images = new ArrayList<>();
        for (int i = 0; i < carousels.size(); i++) {
            images.add(carousels.get(i).path);
        }

        Banner banner = findViewById(R.id.banner);
        banner.setDelayTime(5000).setImages(images).setImageLoader(new GlideImageLoader()).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        shoppingCartCount();
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.llyBack:
                    finish();
                    break;

                case R.id.rlyCart:
                    startActivity(new Intent(GoodsDetailActivity.this, ShoppingCartActivity.class));
                    break;

                case R.id.rlyPhoneService:
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    Uri data = Uri.parse("tel:" + Constants.phone_server_number);
                    intent.setData(data);
                    startActivity(intent);
                    break;

                case R.id.tvAddCart:
                    showSelectDialog("cart");
                    break;

                case R.id.tvBuyNow:
                    showSelectDialog("order");
                    break;
            }
        }
    };

    private void showSelectDialog(String type) {
        if (goods == null) {
            return;
        }
        GoodsSelectDialog dialog = new GoodsSelectDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable("goods", goods);
        bundle.putSerializable("type", type);
        dialog.setArguments(bundle);
        dialog.show(this.getFragmentManager(), "GoodsSelectDialog");
    }

    private void goodsDetail() {
        String goodsID = getIntent().getStringExtra("goodsid");
        Logger.getLogger().d("商品详情, goodsID -> " + goodsID);

        MallDataManager dataManager = new MallDataManager();
        dataManager.getGoodsDetail(goodsID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GoodsEntity>() {

                    @Override
                    public void onError(Throwable e) {
                        ReportUtil.reportError(e);
                        Logger.getLogger().e("商品详情：" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(GoodsEntity result) {
                        if (!result.success) {
                            Logger.getLogger().e("商品详情，msgCode：" + result.errCode + "/n" + result.message);
                            ReportUtil.reportError("商品详情，msgCode：" + result.errCode + "/n" + result.message);
                        } else {
                            if (result.data == null) {
                                Logger.getLogger().e("商品详情, result为空");
                                return;
                            }

                            goods = result.data.getGoods();
                            initView(goods);
                            initCarouselView(goods.carousels);
                        }
                    }
                });
    }

    private void shoppingCartCount() {
        Logger.getLogger().d("获取购物车商品数量");

        UserDataManager dataManager = new UserDataManager();
        dataManager.getShoppingCartCount()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CountEntity>() {

                    @Override
                    public void onError(Throwable e) {
                        ReportUtil.reportError(e);
                        Logger.getLogger().e("获取购物车商品数量：" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(CountEntity result) {
                        if (!result.success) {
                            Logger.getLogger().e("获取购物车商品数量，msgCode：" + result.errCode + "/n" + result.message);
                            ReportUtil.reportError("获取购物车商品数量，msgCode：" + result.errCode + "/n" + result.message);
                        } else {
                            if (result.data == null) {
                                Logger.getLogger().e("获取购物车商品数量, result为空");
                                return;
                            }

                            int count = result.data.getCountInt();
                            if (count > 0) {
                                tvCartCount.setVisibility(View.VISIBLE);
                                tvCartCount.setText(count + "");
                            } else {
                                tvCartCount.setVisibility(View.GONE);
                            }
                        }
                    }
                });
    }

    private class ImageUnit {
        public String image;
        public int height;
    }
}
