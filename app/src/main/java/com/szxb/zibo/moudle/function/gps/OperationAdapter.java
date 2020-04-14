package com.szxb.zibo.moudle.function.gps;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.szxb.zibo.R;

import java.util.ArrayList;
import java.util.List;

public class OperationAdapter extends BaseAdapter {
    Context context;
    int select;


    public OperationAdapter(Context context) {
        this.context = context;
    }

    public void setSelectAdd() {
        select = (select + 1) % operations.size();
        notifyDataSetChanged();
    }

    public int getSelect() {
        return select;
    }

    public void setSelectReduce() {
        select = (select - 1) % operations.size();
        if (select == -1) {
            select = operations.size() - 1;
        }
        notifyDataSetChanged();
    }

    List<String> operations = new ArrayList<>();

    public void addDate(List<String> operations) {
        this.operations = operations;
    }


    @Override
    public int getCount() {
        return operations.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.text, null);
            viewHolder.operation = (TextView) view.findViewById(R.id.operation);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.operation.setText(operations.get(i));
        if (select == i) {
            viewHolder.operation.setBackgroundResource(R.drawable.shape_orange);
        } else {
            viewHolder.operation.setBackgroundResource(R.drawable.shape_white);
        }
        return view;
    }

    class ViewHolder {
        TextView operation;
    }
}
