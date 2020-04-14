package com.szxb.zibo.moudle.maintool;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.szxb.zibo.R;

import java.util.ArrayList;
import java.util.List;

public class MainToolAdapter extends RecyclerView.Adapter {
    Context context;
    List<String> stringList = new ArrayList<>();
    int check = 0;

    public int getCheck() {
        return check;
    }

    public void setSelectAdd() {
        check++;
        check = check % stringList.size();
        notifyDataSetChanged();
    }

    public void setSelectRefuse() {
        check--;
        if (check < 0) {
            check = stringList.size()-1;
        } else {
            check = check % stringList.size();
        }
        notifyDataSetChanged();
    }


    public MainToolAdapter(Context context, List<String> stringList) {
        this.context = context;
        this.stringList = stringList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new StringViewHolder(LayoutInflater.from(context).inflate(R.layout.text_view, null));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (i == check % stringList.size()) {
            ((StringViewHolder) viewHolder).itemView.setBackgroundResource(R.color.white_66);
        } else {
            ((StringViewHolder) viewHolder).itemView.setBackgroundResource(R.color.transparent);
        }
        ((StringViewHolder) viewHolder).setText(stringList.get(i));
    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }

    class StringViewHolder extends RecyclerView.ViewHolder {
        View itemView;

        public StringViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
        }

        public void setText(String s) {
            ((TextView) itemView.findViewById(R.id.textview)).setText(s);
        }
    }
}
