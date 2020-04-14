package com.szxb.zibo.moudle.zibo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.hao.lib.Util.StatusBarUtil;
import com.hao.lib.base.MI2App;
import com.hao.lib.base.Rx.Rx;
import com.hao.lib.base.Rx.RxMessage;
import com.hao.lib.view.RecycleView;
import com.szxb.zibo.R;
import com.szxb.zibo.base.BaseActivity;
import com.szxb.zibo.base.BusApp;
import com.szxb.zibo.config.haikou.ConfigContext;
import com.szxb.zibo.config.zibo.DBManagerZB;
import com.szxb.zibo.config.zibo.InitConfigZB;
import com.szxb.zibo.config.zibo.line.ZBLineInfo;
import com.szxb.zibo.util.BusToast;
import com.yanzhenjie.nohttp.rest.Response;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.szxb.zibo.config.zibo.InitConfigZB.INFO_UP;

public class SelectLineActivity extends BaseActivity implements RxMessage {
    LineAdapter lineAdapter;
    RecycleView recycleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.black));
        super.onCreate(savedInstanceState);
        MI2App.getInstance().addActivity(this);
        setContentView(R.layout.activity_selcect_line);
        recycleView = findViewById(R.id.recycle_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        lineAdapter = new LineAdapter();
        recycleView.setLayoutManager(linearLayoutManager);
        recycleView.setAdapter(lineAdapter);
    }

    @Override
    public void rxDo(Object tag, Object... o) {
        if (o[0] instanceof String) {
            switch ((String) o[0]) {
                case ConfigContext.KEY_BUTTON_BOTTOM_LEFT:
                    lineAdapter.refuseSelect();
                    recycleView.scrollToPosition(lineAdapter.getSelect());
                    break;
                case ConfigContext.KEY_BUTTON_BOTTOM_RIGHT:
                    startActivity(new Intent(this, Main2Activity.class));
                    SelectLineActivity.this.finish();
                    break;
                case ConfigContext.KEY_BUTTON_TOP_LEFT:
                    lineAdapter.addSelect();
                    recycleView.scrollToPosition(lineAdapter.getSelect());
                    break;
                case ConfigContext.KEY_BUTTON_TOP_RIGHT:
                    ZBLineInfo zbLineInfo = lineAdapter.getSelectDate();
                    DBManagerZB.clearAllLine();
                    BusApp.getPosManager().setLineName(zbLineInfo.getRoutename());
                    BusApp.getPosManager().setLineNo(zbLineInfo.getRouteno());
                    BusApp.getPosManager().setBasePrice(0);
                    BusApp.getPosManager().setFarver("00000000000000");
                    BusApp.getPosManager().setLinver("00000000000000");
                    BusToast.showToast("下载线路", true);
                    Executors.newScheduledThreadPool(1).schedule(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Response<JSONObject> response = InitConfigZB.
                                        sendInfoToServer(INFO_UP);
                                if (response.isSucceed()) {
                                    SelectLineActivity.this.finish();
                                }
                            } catch (Exception e) {
                            }
                        }
                    }, 0, TimeUnit.SECONDS);
                    break;
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        Rx.getInstance().setRxMessage(this);
    }

    class LineAdapter extends RecycleView.Adapter<LineHolder> {
        List<ZBLineInfo> lineInfos = DBManagerZB.getAllLineInfo();
        int select = 0;

        @NonNull
        @Override
        public LineHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(BusApp.getInstance()).inflate(R.layout.linelist_item, null);
            view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            return new LineHolder(view);
        }

        public void addSelect() {
            select++;
            if (select >= lineInfos.size()) {
                select = select - lineInfos.size();
            }
            notifyDataSetChanged();
        }

        public void refuseSelect() {
            select--;
            if (select < 0) {
                select = lineInfos.size() - 1;
            }
            notifyDataSetChanged();
        }

        public int getSelect() {
            return select;
        }

        public ZBLineInfo getSelectDate() {
            return lineInfos.get(select);
        }


        @Override
        public void onBindViewHolder(@NonNull LineHolder viewHolder, int i) {
            ((TextView) viewHolder.itemView.findViewById(R.id.textview)).setText(lineInfos.get(i).getRouteno() + "      " + lineInfos.get(i).getRoutename());
            if (i == select) {
                viewHolder.itemView.setBackgroundResource(R.drawable.shape_coner_white_5dp);
                ((TextView) viewHolder.itemView.findViewById(R.id.textview)).setTextColor(ContextCompat.getColor(BusApp.getInstance(), R.color.black));
            } else {
                viewHolder.itemView.setBackgroundResource(R.color.transparent);
                ((TextView) viewHolder.itemView.findViewById(R.id.textview)).setTextColor(ContextCompat.getColor(BusApp.getInstance(), R.color.white));
            }

        }

        @Override
        public int getItemCount() {
            return lineInfos.size();
        }
    }

    class LineHolder extends RecycleView.ViewHolder {
        public LineHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
