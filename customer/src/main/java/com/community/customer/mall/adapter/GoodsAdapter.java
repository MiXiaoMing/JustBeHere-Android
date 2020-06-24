package com.community.customer.mall.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appframe.library.component.image.ImageLoader;
import com.community.customer.api.mall.Goods;
import com.community.customer.common.Constants;
import com.community.customer.mall.GoodsDetailActivity;
import com.community.support.component.FontTextView;
import com.community.support.component.LoadMoreAdapter;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.Arrays;
import java.util.List;

import cn.wdcloud.acaeva.R;


public class GoodsAdapter extends LoadMoreAdapter<Goods> {
    private Context context;

    public GoodsAdapter(Context context, List<Goods> dataList) {
        super(dataList);
        this.context = context;
    }

    @Override
    public ViewHolder handleCreateViewHolder(@NonNull ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_goods, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void handleBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ViewHolder) {
            ViewHolder holder = (ViewHolder) viewHolder;
            Goods entity = dataList.get(position);
            ImageLoader.normal(context, entity.icon, R.drawable.default_image_white, holder.ivIcon);
            holder.tvTitle.setText(entity.title);
            holder.tvDesc.setText(entity.desc);
            if (entity.prices != null && entity.prices.size() > 0) {
                holder.tvPrice.setVisibility(View.VISIBLE);
                holder.tvPrice.setText("¥ " + entity.mixPrice);
            } else {
                holder.tvPrice.setVisibility(View.GONE);
            }
            initTags(holder.llyTags, entity.tag);
        }
    }

    private void initTags(LinearLayout root, String tagsStr) {
        root.removeAllViews();

        List<String> tags = Arrays.asList(tagsStr.split(","));
        for (int i = 0; i < tags.size(); ++i) {
            if (i > 2) {
                break;
            }
            TextView textView = new FontTextView(context);
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
            textView.setTextSize(11);

            root.addView(textView);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        LinearLayout llyTags;
        TextView tvTitle, tvDesc, tvPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.ivIcon);
            llyTags = itemView.findViewById(R.id.llyTags);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDesc = itemView.findViewById(R.id.tvDesc);
            tvPrice = itemView.findViewById(R.id.tvPrice);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = ViewHolder.this.getAdapterPosition();
                    if (position >= 0) {
                        Intent intent = new Intent(context, GoodsDetailActivity.class);
                        intent.putExtra("goodsid", dataList.get(position).code);
                        context.startActivity(intent);
                    }
                }
            });
        }
    }
}
