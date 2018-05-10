package com.hezo.zhangtong.dropdownmenu.widget;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hezo.zhangtong.dropdownmenu.R;

import java.util.List;

public class ConstellationAdapter extends BaseAdapter {

    private Context context;
    private List<String> list;
    private int checkItemPosition = 0;

    public ConstellationAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    public void setCheckItemPosition(int position) {
        checkItemPosition = position;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public String getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_constellation_layout, null);
            viewHolder = new ViewHolder();
            viewHolder.tv = convertView.findViewById(R.id.tv);
            convertView.setTag(viewHolder);
        }
        fillValue(position, viewHolder);

        return convertView;
    }

    private void fillValue(int position, ViewHolder viewHolder) {
        viewHolder.tv.setText(list.get(position));
        if (checkItemPosition != -1) {
            if (checkItemPosition == position) {
                viewHolder.tv.setTextColor(context.getResources().getColor(R.color.drop_drop_selected));
                viewHolder.tv.setBackgroundResource(R.drawable.check_bg);
            } else {
                viewHolder.tv.setTextColor(context.getResources().getColor(R.color.drop_drop_unselected));
                viewHolder.tv.setBackgroundResource(R.drawable.uncheck_bg);
            }
        }

    }

    static class ViewHolder {
        TextView tv;
    }


}
