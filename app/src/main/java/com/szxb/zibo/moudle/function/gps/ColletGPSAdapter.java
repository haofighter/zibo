package com.szxb.zibo.moudle.function.gps;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.szxb.zibo.R;
import com.szxb.zibo.config.zibo.DBManagerZB;

import java.util.ArrayList;
import java.util.List;

public class ColletGPSAdapter extends BaseAdapter {
    Context context;

    public ColletGPSAdapter(Context context) {
        this.context = context;
        colletGpsInfos = DBManagerZB.getCollectStationInfo();
    }

    List<ColletGpsInfo> colletGpsInfos = new ArrayList<>();


    public List<ColletGpsInfo> getDate() {
        return colletGpsInfos;
    }

    @Override
    public void notifyDataSetChanged() {
        colletGpsInfos = DBManagerZB.getCollectStationInfo();
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return colletGpsInfos == null ? 0 : colletGpsInfos.size();
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
            view = LayoutInflater.from(context).inflate(R.layout.collet_gps_item, null);
            viewHolder.stationNo = (TextView) view.findViewById(R.id.station);
            viewHolder.lat = (TextView) view.findViewById(R.id.lat);
            viewHolder.lng = (TextView) view.findViewById(R.id.lng);
            viewHolder.diraction = (TextView) view.findViewById(R.id.diraction);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.stationNo.setText(colletGpsInfos.get(i).getStaionNo() + "");
        viewHolder.lat.setText(colletGpsInfos.get(i).getLat() + "");
        viewHolder.lng.setText(colletGpsInfos.get(i).getLng() + "");
        viewHolder.diraction.setText(colletGpsInfos.get(i).getDiraction() + "");
        return view;
    }

    class ViewHolder {
        TextView stationNo;
        TextView lat;
        TextView lng;
        TextView diraction;
    }
}
