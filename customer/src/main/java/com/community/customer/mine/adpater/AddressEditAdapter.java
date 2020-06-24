package com.community.customer.mine.adpater;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.wdcloud.acaeva.R;

import com.community.customer.api.user.AddressEntity;
import com.community.customer.mine.AddressListActivity;
import com.community.customer.mine.AddressSettingActivity;

import java.io.Serializable;
import java.util.ArrayList;


public class AddressEditAdapter extends BaseAdapter {
    private AddressListActivity activity;
    private ArrayList<AddressEntity> entities = new ArrayList<>();

    private String selectID;

    public AddressEditAdapter(AddressListActivity activity, String selectID) {
        this.activity = activity;
        this.selectID = selectID;
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
            convertView = LayoutInflater.from(activity).inflate(R.layout.item_address_edit, null);
            viewHolder.tvName = convertView.findViewById(R.id.tvName);
            viewHolder.tvRegion = convertView.findViewById(R.id.tvRegion);
            viewHolder.tvCellphone = convertView.findViewById(R.id.tvCellphone);
            viewHolder.rlyAddress = convertView.findViewById(R.id.rlyAddress);
            viewHolder.ivSelect = convertView.findViewById(R.id.ivSelect);
            viewHolder.rlyEdit = convertView.findViewById(R.id.rlyEdit);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (!TextUtils.isEmpty(selectID) && entities.get(position).id.equals(selectID)) {
            viewHolder.ivSelect.setImageResource(R.drawable.icon_selected);
        } else {
            viewHolder.ivSelect.setImageResource(R.drawable.icon_unselected);
        }

        viewHolder.tvName.setText(entities.get(position).contact);
        viewHolder.tvRegion.setText(entities.get(position).region);
        viewHolder.tvCellphone.setText(entities.get(position).cellphone);

        viewHolder.ivSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectID = entities.get(position).id;
                notifyDataSetChanged();
                activity.setSelect(selectID);
                activity.finish();
            }
        });

        viewHolder.rlyAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectID = entities.get(position).id;
                notifyDataSetChanged();
                activity.setSelect(selectID);
                activity.finish();
            }
        });

        viewHolder.rlyEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, AddressSettingActivity.class);
                intent.putExtra("type", "U");
                intent.putExtra("address", (Serializable)entities.get(position));
                activity.startActivity(intent);
            }
        });

        return convertView;
    }

    public void addAll(ArrayList<AddressEntity> entities) {
        this.entities.clear();
        this.entities.addAll(entities);
        boolean isSelect = false;
        for (AddressEntity address : this.entities) {
            if (address.id.equals(selectID)) {
                activity.setSelect(selectID);
                isSelect = true;
            }
        }
        if (!isSelect) {
            activity.setSelect("");
        }
        notifyDataSetChanged();
    }

    private class ViewHolder {
        ImageView ivSelect;
        TextView tvName, tvRegion, tvCellphone;
        RelativeLayout rlyAddress, rlyEdit;
    }
}
