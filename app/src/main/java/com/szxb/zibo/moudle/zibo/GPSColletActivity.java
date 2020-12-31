package com.szxb.zibo.moudle.zibo;

import android.os.Bundle;
import android.os.Environment;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.szxb.lib.Util.FileUtils;
import com.szxb.zibo.R;
import com.szxb.zibo.base.BaseActivity;
import com.szxb.zibo.config.haikou.ConfigContext;
import com.szxb.zibo.config.zibo.DBManagerZB;
import com.szxb.zibo.moudle.function.gps.ColletGPSAdapter;
import com.szxb.zibo.moudle.function.gps.ColletGpsInfo;
import com.szxb.zibo.moudle.function.gps.OperationAdapter;
import com.szxb.zibo.moudle.function.location.GPSEvent;
import com.szxb.zibo.util.BusToast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class GPSColletActivity extends BaseActivity {
    ListView operation_list;
    ListView list_view;
    ColletGPSAdapter colletGPSAdapter;
    TextView nowj;
    TextView noww;
    TextView near1_j;
    TextView near1_w;
    TextView near1_len;
    TextView near1_name;
    TextView now_diraction;
    ScheduledFuture scheduledFuture;

    int diraction = 0;//0 上行 1 下行

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gps_collect);


        initView();
        initData();
    }

    private void initView() {
        operation_list = findViewById(R.id.operation_list);
        list_view = findViewById(R.id.station_list);
        nowj = findViewById(R.id.now_gpsj);
        noww = findViewById(R.id.now_gpsw);
        near1_j = findViewById(R.id.near1_gpsj);
        near1_w = findViewById(R.id.near1_gpsw);
        near1_len = findViewById(R.id.near1_len);
        near1_name = findViewById(R.id.near1_name);
        now_diraction = findViewById(R.id.now_diraction);

        now_diraction.setText(diraction == 0 ? "上行" : "下行");
        colletGPSAdapter = new ColletGPSAdapter(this);
        list_view.setAdapter(colletGPSAdapter);
        initOperation();
    }


    private void initOperation() {
        OperationAdapter operationAdapter = new OperationAdapter(this);
        List<String> stringList = new ArrayList<>();
        stringList.add("采集");
        stringList.add("删除");
        stringList.add("导出");
        stringList.add("切换方向");
        stringList.add("清除所有");
        operationAdapter.addDate(stringList);
        operation_list.setAdapter(operationAdapter);
    }


    protected void initData() {
        scheduledFuture = Executors.newScheduledThreadPool(1).scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if (GPSEvent.bdLocation != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            nowj.setText("当前的站点   经度：" + GPSEvent.bdLocation.getLatitude() + "    ");
                            noww.setText("纬度：" + GPSEvent.bdLocation.getLongitude() + "");
                        }
                    });
                }
            }
        }, 1, 1, TimeUnit.SECONDS);
    }


    @Override
    public void rxDo(Object tag, Object... o) {
        super.rxDo(tag, o);
        if (tag instanceof String) {
            if (o.length > 0) {
                switch ((String) o[0]) {
                    case ConfigContext.KEY_BUTTON_BOTTOM_LEFT:
                        ((OperationAdapter) operation_list.getAdapter()).setSelectAdd();
                        break;
                    case ConfigContext.KEY_BUTTON_BOTTOM_RIGHT:
                        if (scheduledFuture != null) {
                            scheduledFuture.cancel(true);
                        }
                        finish();
                        break;
                    case ConfigContext.KEY_BUTTON_TOP_LEFT:
                        ((OperationAdapter) operation_list.getAdapter()).setSelectReduce();
                        break;
                    case ConfigContext.KEY_BUTTON_TOP_RIGHT:
                        switch (((OperationAdapter) operation_list.getAdapter()).getSelect()) {
                            case 0://采集
                                DBManagerZB.addStationInfo(
                                        new ColletGpsInfo(GPSEvent.bdLocation.getLatitude(),
                                                GPSEvent.bdLocation.getLongitude(),
                                                (long) DBManagerZB.selectColletNumForDiraction(diraction).size() + 1,
                                                diraction
                                        ));
                                BusToast.showToast("采集成功", true);
                                colletGPSAdapter.notifyDataSetChanged();
                                list_view.smoothScrollToPosition(colletGPSAdapter.getCount());
                                break;
                            case 1://删除
                                List<ColletGpsInfo> colletGpsInfos = ((ColletGPSAdapter) list_view.getAdapter()).getDate();
                                if (colletGpsInfos.size() == 0) {
                                    return;
                                }
                                DBManagerZB.deleteStation(colletGpsInfos.get(colletGpsInfos.size() - 1));
                                colletGPSAdapter.notifyDataSetChanged();
                                break;
                            case 2://导出
                                List<ColletGpsInfo> collectStationInfo = DBManagerZB.getCollectStationInfo();
                                String collect = new Gson().toJson(collectStationInfo);
                                try {
                                    String path = Environment.getExternalStorageDirectory().toString() + "/station/" + System.currentTimeMillis() + ".txt";
                                    FileUtils.byteToFile(collect.getBytes(), new File(path));
                                    FileUtils.copyDir(
                                            Environment.getExternalStorageDirectory().toString() + "/station",
                                            "/storage/sdcard1/station");
                                    BusToast.showToast("导出成功", true);
                                } catch (Exception e) {
                                    BusToast.showToast("导出失败", false);
                                }

                                break;
                            case 3://上下行切换
                                diraction = (diraction + 1) % 2;
                                now_diraction.setText(diraction == 0 ? "上行" : "下行");
                                break;
                            case 4://清除记录
                                DBManagerZB.clearStation();
                                colletGPSAdapter.notifyDataSetChanged();
                                break;
                        }
                        break;
                }
            }
        }
    }
}
