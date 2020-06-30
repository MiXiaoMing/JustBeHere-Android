package com.community.customer.order.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appframe.library.component.image.ImageLoader;
import com.community.customer.api.user.GoodsOrderEntity;
import com.community.customer.common.Constants;
import com.community.customer.common.ServerConfig;
import com.community.customer.order.GoodsOrderDetailActivity;
import com.community.support.component.RoundCornerImageView;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;

import cn.wdcloud.acaeva.R;

public class GoodsOrderAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<GoodsOrderEntity> entities = new ArrayList<>();

    public GoodsOrderAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return entities.size();
    }

    @Override
    public Object getItem(int position) {
        return entities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_goods_order, null);
            viewHolder.tvCreateTime = convertView.findViewById(R.id.tvCreateTime);
            viewHolder.tvStatus = convertView.findViewById(R.id.tvStatus);
            viewHolder.tvCount = convertView.findViewById(R.id.tvCount);
            viewHolder.tvPrice = convertView.findViewById(R.id.tvPrice);
            viewHolder.llyIcons = convertView.findViewById(R.id.llyIcons);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvCreateTime.setText(entities.get(position).goodsOrder.createTime);

        String status = entities.get(position).goodsOrder.status;
        viewHolder.tvStatus.setText(Constants.convertGoodsOrderStatus(status));
        viewHolder.tvStatus.setTextColor(Constants.getGoodsStatusColor(status));
        viewHolder.tvCount.setText("共" + entities.get(position).items.size() + "件商品，实付额：");
        viewHolder.tvPrice.setText(entities.get(position).goodsOrder.price + "");
        initIcons(viewHolder.llyIcons, entities.get(position).items);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, GoodsOrderDetailActivity.class);
                intent.putExtra("orderID", entities.get(position).goodsOrder.id);
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    private void initIcons(LinearLayout root, ArrayList<GoodsOrderEntity.Item> items) {
        for (int i = 0; i < items.size(); ++i) {
            if (i > 0) {
                View view = new View(context);
                view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1));
                root.addView(view);
            }
            ImageView imageView = new ImageView(context);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(AutoUtils.getPercentWidthSize(55), AutoUtils.getPercentWidthSize(55));
            imageView.setLayoutParams(layoutParams);
            ImageLoader.normal(context, ServerConfig.file_host + items.get(i).icon, R.drawable.default_image_white, imageView);
            root.addView(imageView);
        }
    }

    public void addAll(ArrayList<GoodsOrderEntity> entities) {
        this.entities.clear();
        this.entities.addAll(entities);
        notifyDataSetChanged();
    }

    private class ViewHolder {
        TextView tvCreateTime, tvStatus, tvCount, tvPrice;
        LinearLayout llyIcons;
    }
}
