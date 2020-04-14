package com.szxb.zibo.moudle.maintool;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hao.lib.Util.FileUtils;
import com.szxb.zibo.R;
import com.szxb.zibo.config.zibo.DBManagerZB;
import com.szxb.zibo.record.XdRecord;
import com.szxb.zibo.util.BusToast;
import com.szxb.zibo.util.DateUtil;

import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.Holder> {
    int type = 0;//0 刷卡记录  1 腾讯二维码记录  2 银联记录   3支付宝二维码记录  4交通部二维码记录
    int page = 0;//当前显示的页数
    int maxPage = 0;//显示的最大页数
    int pageShowNum = 5;//显示的最大页数

    List list = new ArrayList();
    Context context;

    public HistoryAdapter(Context context) {
        this.context = context;
    }

    public void nextPage() {
        if (page < maxPage) {
            page++;
        } else {
            BusToast.showToast("已达到最后一页", true);
        }
        notifyDataSetChanged();
    }

    public void beforPage() {
        if (page == 0) {
            BusToast.showToast("已达到第一页", true);
        } else {
            page--;
        }
        notifyDataSetChanged();
    }

    public void setType(int type) throws Exception {
        page = 0;
        this.type = type;
        switch (type) {
            case 0:
                list = DBManagerZB.checkXdRecord("0001", true);//刷卡
                break;
            case 1:
                list = DBManagerZB.checkXdRecord("0001", false);//腾讯
                break;
            case 2:
                list = DBManagerZB.checkUnionRecord();//银联
                break;
            case 3:
                list = DBManagerZB.checkXdRecord("0002", false);//支付宝
                break;
        }
        maxPage = ((List) list).size() / pageShowNum;
        notifyDataSetChanged();
    }

    @NonNull
    @Override

    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.history_layout, null);
        v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder viewHolder, int i) {
        if (i == 0) {
            String center = ((page + 1) * pageShowNum < list.size() ? (page + 1) * pageShowNum : list.size()) + "/" + list.size();
            if (type == 2) {
                center += "\n交易状态";
            }
            viewHolder.setDate("交易标示", "交易金额(元)", "是否上传", "交易时间", "交易状态", center);
        } else {
            Object o = list.get(page * pageShowNum + i - 1);
            if (o instanceof XdRecord) {
                XdRecord card = (XdRecord) o;
                String pay = "0";
                try {
                    pay = FileUtils.fen2Yuan(Integer.parseInt(FileUtils.getSHByte(card.getTradePay()), 16));
                } catch (Exception e) {
                    pay = FileUtils.fen2Yuan(Integer.parseInt(FileUtils.getSHByte(card.getTradePay())));
                }

                String upstate = "";//上车状态
                String inState = card.getInCardStatus();// 1HEX  0x01上车(多票二维码统一传01) 0x02下车 0x03补扣消费 0x00一票
                if (inState.equals("01")) {
                    upstate = "上车";
                } else if (inState.equals("02")) {
                    upstate = "下车";
                } else if (inState.equals("03")) {
                    upstate = "补扣";
                } else if (inState.equals("00")) {
                    upstate = "一票";
                }
                viewHolder.setDate(card.getUseCardnum(), pay, (card.getUpdateFlag().equals("0") ? "未上传" : "已上传") + "|" + upstate, DateUtil.getTimeStr(card.getCreatTime()), card.getStatus() + "|" + (card.getPayType().toLowerCase().equals("fd") ? "正常" : "灰交易"), Long.parseLong(FileUtils.getSHByte(card.getTradeNum()), 16) + "");
            }
        }
    }

    @Override
    public int getItemCount() {
        int count = 1;
        if (list == null || list.size() == 0) {
            count = 1;
        } else if (page < maxPage) {
            count = pageShowNum + 1;
        } else {
            count = list.size() % pageShowNum + 1;
        }
        return count;
    }

    class Holder extends RecyclerView.ViewHolder {
        View itemView;

        public Holder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
        }

        public void setDate(String id, String price, String isupload, String tranTime, String tradeType, String center) {
            ((TextView) itemView.findViewById(R.id.tran_id)).setText(id.toString());
//            try {
//                ((TextView) itemView.findViewById(R.id.tran_price)).setText(FileUtils.fen2Yuan(Integer.parseInt(price)).toString());
//            } catch (Exception e) {
            ((TextView) itemView.findViewById(R.id.tran_price)).setText(price.toString());
//            }
            ((TextView) itemView.findViewById(R.id.isupload)).setText(isupload.toString());
            ((TextView) itemView.findViewById(R.id.tran_time)).setText(tranTime.toString());
            ((TextView) itemView.findViewById(R.id.center_text)).setText(center.toString());
            ((TextView) itemView.findViewById(R.id.trade_status)).setText(tradeType.toString());
        }
    }

}
