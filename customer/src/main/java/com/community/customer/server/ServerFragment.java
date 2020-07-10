package com.community.customer.server;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.wdcloud.acaeva.R;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import com.appframe.library.component.image.ImageLoader;
import com.appframe.utils.logger.Logger;
import com.community.customer.common.ServerConfig;
import com.community.customer.api.servers.CarouselEntity;
import com.community.customer.api.servers.ServerDataManager;
import com.community.customer.api.servers.ServiceBlockEntity;
import com.community.customer.common.Constants;
import com.community.support.BaseFragment;
import com.community.support.component.FontTextView;
import com.community.support.utils.BgUtil;
import com.community.support.utils.GlideImageLoader;
import com.community.support.utils.ReportUtil;
import com.community.support.utils.ToastUtil;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

public class ServerFragment extends BaseFragment {
    private View view;

    private ServerDataManager serverDataManager = new ServerDataManager();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_server, null);

            initCarousel();
            initFive();    //五大项
            initTen();    //十小项
            initRecommend();    //推荐
            initRecommendServiceClassify();    //三大类
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }

        return view;
    }

    private void initCarousel() {
        serverDataManager.getMainPageCarousel()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CarouselEntity>() {
                    @Override
                    public void onError(Throwable e) {
                        ReportUtil.reportError(e);
                        Logger.getLogger().e("获取首页数据错误：" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(CarouselEntity result) {
                        if (!result.success) {
                            Logger.getLogger().e("首页数据获取失败，msgCode：" + result.errCode + "/n" + result.message);
                        } else {
                            if (result.data == null) {
                                Logger.getLogger().e("服务列表数据获取失败, result为空");
                                return;
                            }

                            initCarouselView(result.data);
                        }
                    }
                });
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
            images.add(ServerConfig.file_host + carousels.get(i).path);
        }

        Banner banner = view.findViewById(R.id.banner);
        final ArrayList<CarouselEntity> finalCarousels = carousels;
        banner.setDelayTime(5000).setImages(images).setImageLoader(new GlideImageLoader()).setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                CarouselEntity carousel = finalCarousels.get(position);
                ServerCommonView.toClass(ServerFragment.this.getActivity(), carousel.classify, carousel.code);
            }
        }).start();
    }

    private void initFive() {
        serverDataManager.getFive()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ServiceBlockEntity>() {
                    @Override
                    public void onError(Throwable e) {
                        ReportUtil.reportError(e);
                        Logger.getLogger().e("获取首页项数据：" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ServiceBlockEntity result) {
                        if (!result.success) {
                            Logger.getLogger().e("首页数据获取失败，msgCode：" + result.errCode + "/n" + result.message);
                        } else {
                            if (result.data == null) {
                                Logger.getLogger().e("获取首页项数据, result为空");
                                return;
                            }

                            initFiveUnit(result.data);
                        }
                    }
                });
    }

    private void initTen() {
        serverDataManager.getTen()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ServiceBlockEntity>() {
                    @Override
                    public void onError(Throwable e) {
                        ReportUtil.reportError(e);
                        Logger.getLogger().e("获取首页项数据：" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ServiceBlockEntity result) {
                        if (!result.success) {
                            Logger.getLogger().e("首页数据获取失败，msgCode：" + result.errCode + "/n" + result.message);
                        } else {
                            if (result.data == null) {
                                Logger.getLogger().e("获取首页项数据, result为空");
                                return;
                            }

                            initTenUnits(result.data);
                        }
                    }
                });
    }

    private void initRecommend() {
        serverDataManager.getRecommend()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ServiceBlockEntity>() {
                    @Override
                    public void onError(Throwable e) {
                        ReportUtil.reportError(e);
                        Logger.getLogger().e("获取首页项数据：" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ServiceBlockEntity result) {
                        if (!result.success) {
                            Logger.getLogger().e("首页数据获取失败，msgCode：" + result.errCode + "/n" + result.message);
                        } else {
                            if (result.data == null) {
                                Logger.getLogger().e("获取首页项数据, result为空");
                                return;
                            }

                            initRecommends(result.data);
                        }
                    }
                });
    }

    private void initRecommendServiceClassify() {
        serverDataManager.getRecommendServiceClassify()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ServiceBlockEntity>() {
                    @Override
                    public void onError(Throwable e) {
                        ReportUtil.reportError(e);
                        Logger.getLogger().e("获取首页项数据：" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ServiceBlockEntity result) {
                        if (!result.success) {
                            Logger.getLogger().e("首页数据获取失败，msgCode：" + result.errCode + "/n" + result.message);
                        } else {
                            if (result.data == null) {
                                Logger.getLogger().e("获取首页项数据, result为空");
                                return;
                            }

                            initServerClassifyView(result.data);
                        }
                    }
                });
    }

    //五大项
    private void initFiveUnit(final List<ServiceBlockEntity> units) {
        LinearLayout llyFiveClassify = view.findViewById(R.id.llyFiveClass);
        if (units == null || units.size() == 0) {
            llyFiveClassify.setVisibility(View.GONE);
        } else {
            llyFiveClassify.setVisibility(View.VISIBLE);

            for (int i = 0; i < units.size(); ++i) {
                LinearLayout linearLayout = new LinearLayout(this.getActivity());
                linearLayout.setGravity(Gravity.CENTER_HORIZONTAL);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));
                final int finalI = i;
                linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        handleServerBlockClick(units.get(finalI));
                    }
                });

                String icon, name;
                if (units.get(i).classify.equals(Constants.data_server_classify)) {
                    icon = units.get(i).serviceClassify.icon;
                    name = units.get(i).serviceClassify.name;
                } else if (units.get(i).classify.equals(Constants.data_server)) {
                    icon = units.get(i).service.icon;
                    name = units.get(i).service.name;
                } else {
                    Logger.getLogger().e("数据错误：" + units.get(i).classify);
                    llyFiveClassify.setVisibility(View.GONE);
                    continue;
                }

                ImageView imageView = new ImageView(this.getActivity());
                imageView.setLayoutParams(new LinearLayout.LayoutParams(AutoUtils.getPercentHeightSize(35), AutoUtils.getPercentHeightSize(35)));
                BgUtil.loadImageCircle(this.getActivity(), ServerConfig.file_host + icon, R.drawable.default_image_white, imageView);
                linearLayout.addView(imageView);

                TextView textView = new FontTextView(this.getActivity());
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, AutoUtils.getPercentHeightSize(10), 0, 0);
                textView.setLayoutParams(layoutParams);
                textView.setText(name);
                textView.setTextColor(Color.parseColor("#222222"));
                textView.setTextSize(12);
                linearLayout.addView(textView);

                llyFiveClassify.addView(linearLayout);
            }
        }
    }

    //十小项
    private void initTenUnits(final List<ServiceBlockEntity> units) {
        LinearLayout llyTenServer_One = view.findViewById(R.id.llyTenServer_One);
        LinearLayout llyTenServer_Two = view.findViewById(R.id.llyTenServer_Two);
        if (units == null || units.size() == 0) {
            llyTenServer_One.setVisibility(View.GONE);
            llyTenServer_Two.setVisibility(View.GONE);
        } else {
            llyTenServer_One.setVisibility(View.VISIBLE);
            llyTenServer_Two.setVisibility(View.GONE);

            for (int i = 0; i < units.size() && i < 5; ++i) {
                RelativeLayout relativeLayout = new RelativeLayout(this.getActivity());
                relativeLayout.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));
                final int finalI = i;
                relativeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        handleServerBlockClick(units.get(finalI));
                    }
                });

                String icon, name;
                if (units.get(i).classify.equals(Constants.data_server_classify)) {
                    icon = units.get(i).serviceClassify.icon;
                    name = units.get(i).serviceClassify.name;
                } else if (units.get(i).classify.equals(Constants.data_server)) {
                    icon = units.get(i).service.icon;
                    name = units.get(i).service.name;
                } else if (units.get(i).classify.equals(Constants.data_goods_classify)) {
                    icon = units.get(i).goodsClassify.icon;
                    name = units.get(i).goodsClassify.name;
                } else if (units.get(i).classify.equals(Constants.data_goods)) {
                    icon = units.get(i).goods.goods.icon;
                    name = units.get(i).goods.goods.title;
                } else if (units.get(i).classify.equals(Constants.data_others)) {
                    icon = units.get(i).other.icon;
                    name = units.get(i).other.name;
                } else {
                    Logger.getLogger().e("数据错误：" + units.get(i).classify);
                    llyTenServer_One.setVisibility(View.GONE);
                    continue;
                }

                ImageView imageView = new ImageView(this.getActivity());
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(AutoUtils.getPercentHeightSize(23), AutoUtils.getPercentHeightSize(23));
                layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                imageView.setLayoutParams(layoutParams);
                BgUtil.loadImageCenterInside(this.getActivity(), ServerConfig.file_host + icon, R.drawable.default_image_white, imageView);
                relativeLayout.addView(imageView);

                TextView textView = new FontTextView(this.getActivity());
                RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams2.addRule(RelativeLayout.CENTER_HORIZONTAL);
                layoutParams2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                textView.setLayoutParams(layoutParams2);
                textView.setText(name);
                textView.setTextColor(Color.parseColor("#222222"));
                textView.setTextSize(12);
                relativeLayout.addView(textView);

                llyTenServer_One.addView(relativeLayout);
            }

            if (units.size() > 5) {
                llyTenServer_Two.setVisibility(View.VISIBLE);
                for (int i = 5; i < units.size(); ++i) {
                    RelativeLayout relativeLayout = new RelativeLayout(this.getActivity());
                    relativeLayout.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));
                    final int finalI = i;
                    relativeLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            handleServerBlockClick(units.get(finalI));
                        }
                    });

                    String icon, name;
                    if (units.get(i).classify.equals(Constants.data_server_classify)) {
                        icon = units.get(i).serviceClassify.icon;
                        name = units.get(i).serviceClassify.name;
                    } else if (units.get(i).classify.equals(Constants.data_server)) {
                        icon = units.get(i).service.icon;
                        name = units.get(i).service.name;
                    } else if (units.get(i).classify.equals(Constants.data_goods_classify)) {
                        icon = units.get(i).goodsClassify.icon;
                        name = units.get(i).goodsClassify.name;
                    } else if (units.get(i).classify.equals(Constants.data_goods)) {
                        icon = units.get(i).goods.goods.icon;
                        name = units.get(i).goods.goods.title;
                    } else if (units.get(i).classify.equals(Constants.data_others)) {
                        icon = units.get(i).other.icon;
                        name = units.get(i).other.name;
                    } else {
                        Logger.getLogger().e("数据错误：" + units.get(i).classify);
                        continue;
                    }

                    ImageView imageView = new ImageView(this.getActivity());
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(AutoUtils.getPercentHeightSize(23), AutoUtils.getPercentHeightSize(23));
                    layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                    imageView.setLayoutParams(layoutParams);
                    BgUtil.loadImageCenterInside(this.getActivity(), ServerConfig.file_host + icon, R.drawable.default_image_white, imageView);
                    relativeLayout.addView(imageView);

                    TextView textView = new FontTextView(this.getActivity());
                    RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams2.addRule(RelativeLayout.CENTER_HORIZONTAL);
                    layoutParams2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    textView.setLayoutParams(layoutParams2);
                    textView.setText(name);
                    textView.setTextColor(Color.parseColor("#222222"));
                    textView.setTextSize(12);
                    relativeLayout.addView(textView);

                    llyTenServer_Two.addView(relativeLayout);
                }
            }
        }
    }

    //热门推荐
    private void initRecommends(final List<ServiceBlockEntity> units) {
        LinearLayout llyRecommend = view.findViewById(R.id.llyRecommend);
        if (units == null || units.size() == 0) {
            llyRecommend.setVisibility(View.GONE);
        } else {
            llyRecommend.setVisibility(View.VISIBLE);

            LinearLayout linearLayout = new LinearLayout(this.getActivity());
            for (int i = 0; i < units.size(); ++i) {
                String name, shortdesc, bigicon;
                if (units.get(i).classify.equals(Constants.data_server_classify)) {
                    name = units.get(i).serviceClassify.name;
                    shortdesc = units.get(i).serviceClassify.desc;
                    bigicon = units.get(i).serviceClassify.icon;
                } else if (units.get(i).classify.equals(Constants.data_server)) {
                    name = units.get(i).service.name;
                    shortdesc = units.get(i).service.shortdesc;
                    bigicon = units.get(i).service.bigicon;
                } else {
                    Logger.getLogger().e("数据错误：" + units.get(i).classify);
                    llyRecommend.setVisibility(View.GONE);
                    continue;
                }

                if (i == 0) {
                    linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                    linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, AutoUtils.getPercentHeightSize(140), 1));
                    llyRecommend.addView(linearLayout);

                    LinearLayout linearLayout1 = new LinearLayout(this.getActivity());
                    linearLayout1.setOrientation(LinearLayout.VERTICAL);
                    linearLayout1.setGravity(Gravity.CENTER);
                    linearLayout1.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));
                    linearLayout.addView(linearLayout1);
                    final int finalI = i;
                    linearLayout1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            handleServerBlockClick(units.get(finalI));
                        }
                    });

                    TextView textView = new FontTextView(this.getActivity());
                    textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    textView.setText(name);
                    textView.setTextColor(getResources().getColor(R.color.blue_bg));
                    textView.setTextSize(16);
                    linearLayout1.addView(textView);

                    TextView textView1 = new FontTextView(this.getActivity());
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(0, AutoUtils.getPercentHeightSize(6), 0, 0);
                    textView1.setLayoutParams(layoutParams);
                    textView1.setText(shortdesc);
                    textView1.setTextColor(Color.parseColor("#b5b2ad"));
                    textView1.setTextSize(11);
                    linearLayout1.addView(textView1);

                    ImageView imageView = new ImageView(this.getActivity());
                    RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(AutoUtils.getPercentHeightSize(120), AutoUtils.getPercentHeightSize(80));
                    layoutParams2.setMargins(0, AutoUtils.getPercentHeightSize(4), 0, 0);
                    imageView.setLayoutParams(layoutParams2);
                    ImageLoader.normal(this.getActivity(), ServerConfig.file_host + bigicon, R.drawable.default_image_white, imageView);
                    linearLayout1.addView(imageView);
                } else if (i == 1) {
                    View view = new View(this.getActivity());
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(AutoUtils.getPercentHeightSize(1), ViewGroup.LayoutParams.MATCH_PARENT);
                    view.setLayoutParams(layoutParams);
                    view.setBackgroundColor(Color.parseColor("#f2f2f2"));
                    linearLayout.addView(view);

                    LinearLayout linearLayout1 = new LinearLayout(this.getActivity());
                    linearLayout1.setOrientation(LinearLayout.VERTICAL);
                    linearLayout1.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));
                    linearLayout.addView(linearLayout1);

                    linearLayout = linearLayout1;

                    serverTableItem(linearLayout, units.get(i));
                } else if (i == 2) {
                    View view = new View(this.getActivity());
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, AutoUtils.getPercentHeightSize(1));
                    view.setLayoutParams(layoutParams);
                    view.setBackgroundColor(Color.parseColor("#f2f2f2"));
                    linearLayout.addView(view);

                    serverTableItem(linearLayout, units.get(i));
                } else {
                    if (i % 2 == 1) {
                        View view = new View(this.getActivity());
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, AutoUtils.getPercentHeightSize(1));
                        view.setLayoutParams(layoutParams);
                        view.setBackgroundColor(Color.parseColor("#f2f2f2"));
                        llyRecommend.addView(view);

                        LinearLayout linearLayout1 = new LinearLayout(this.getActivity());
                        linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
                        linearLayout1.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, AutoUtils.getPercentHeightSize(70)));
                        llyRecommend.addView(linearLayout1);

                        linearLayout = linearLayout1;
                    } else {
                        View view = new View(this.getActivity());
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(AutoUtils.getPercentHeightSize(1), ViewGroup.LayoutParams.MATCH_PARENT);
                        view.setLayoutParams(layoutParams);
                        view.setBackgroundColor(Color.parseColor("#f2f2f2"));
                        linearLayout.addView(view);
                    }
                    serverTableItem(linearLayout, units.get(i));
                }
            }
        }
    }

    //三大分类
    private void initServerClassifyView(List<ServiceBlockEntity> entities) {
        LinearLayout llyRoot = view.findViewById(R.id.llyRoot);
        if (entities != null && entities.size() > 0) {
            for (int i = 0; i < entities.size(); ++i) {
                ServerCommonView.serverClassify(this.getActivity(), llyRoot, entities.get(i).serviceClassify);
            }
        }
    }

    private void serverTableItem(LinearLayout root, final ServiceBlockEntity blockEntity) {
        final String classify = blockEntity.classify;
        final String code, name, shortdesc, bigicon;
        if (classify.equals(Constants.data_server_classify)) {
            code = blockEntity.serviceClassify.code;
            name = blockEntity.serviceClassify.name;
            shortdesc = blockEntity.serviceClassify.desc;
            bigicon = blockEntity.serviceClassify.icon;
        } else if (classify.equals(Constants.data_server)) {
            code = blockEntity.service.code;
            name = blockEntity.service.name;
            shortdesc = blockEntity.service.shortdesc;
            bigicon = blockEntity.service.bigicon;
        } else {
            ToastUtil.show(this.getActivity(), "数据错误：" + classify);
            return;
        }

        RelativeLayout relativeLayout = new RelativeLayout(this.getActivity());
        relativeLayout.setGravity(Gravity.CENTER_VERTICAL);
        relativeLayout.setPadding(AutoUtils.getPercentHeightSize(10), 0, AutoUtils.getPercentHeightSize(10), 0);
        relativeLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1));
        root.addView(relativeLayout);

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ServerCommonView.toClass(ServerFragment.this.getActivity(), classify, code);
            }
        });

        LinearLayout linearLayout2 = new LinearLayout(this.getActivity());
        RelativeLayout.LayoutParams layoutParams4 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams4.setMargins(0, AutoUtils.getPercentHeightSize(10), 0, 0);
        linearLayout2.setLayoutParams(layoutParams4);
        linearLayout2.setOrientation(LinearLayout.VERTICAL);
        relativeLayout.addView(linearLayout2);

        TextView textView = new FontTextView(this.getActivity());
        textView.setText(name);
        textView.setSingleLine();
        textView.setMaxEms(7);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setTextColor(Color.parseColor("#222222"));
        textView.setTextSize(16);
        linearLayout2.addView(textView);

        TextView textView1 = new FontTextView(this.getActivity());
        LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams3.setMargins(0, AutoUtils.getPercentHeightSize(8), 0, 0);
        textView1.setLayoutParams(layoutParams3);
        textView1.setText(shortdesc);
        textView1.setTextColor(Color.parseColor("#b5b2ad"));
        textView1.setTextSize(11);
        linearLayout2.addView(textView1);

        ImageView imageView = new ImageView(this.getActivity());
        RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(AutoUtils.getPercentHeightSize(45), AutoUtils.getPercentHeightSize(45));
        layoutParams2.setMargins(0, AutoUtils.getPercentHeightSize(4), 0, 0);
        layoutParams2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        imageView.setLayoutParams(layoutParams2);
        BgUtil.loadImageCircle(this.getActivity(), ServerConfig.file_host + bigicon, R.drawable.default_image_white, imageView);
        relativeLayout.addView(imageView);
    }

    private void handleServerBlockClick(ServiceBlockEntity blockEntity) {
        String classify = blockEntity.classify;
        String code = null;
        if (classify.equals(Constants.data_server_classify)) {
            code = blockEntity.serviceClassify.code;
        } else if (classify.equals(Constants.data_server)) {
            code = blockEntity.service.code;
        } else {
            ToastUtil.show(this.getActivity(), "数据错误：" + classify);
        }
        ServerCommonView.toClass(this.getActivity(), classify, code);
    }
}
