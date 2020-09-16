package com.szxb.zibo.moudle.init;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hao.lib.Util.FileUtils;
import com.hao.lib.Util.MiLog;
import com.hao.lib.base.Rx.Rx;
import com.hao.lib.base.Rx.RxMessage;
import com.lilei.tool.tool.IToolInterface;
import com.szxb.zibo.BuildConfig;
import com.szxb.zibo.R;
import com.szxb.zibo.base.BusApp;
import com.szxb.zibo.cmd.DoCmd;
import com.szxb.zibo.cmd.comThread;
import com.szxb.zibo.cmd.devCmd;
import com.szxb.zibo.config.haikou.ConfigContext;
import com.szxb.zibo.config.zibo.DBManagerZB;
import com.szxb.zibo.config.zibo.InitConfigZB;
import com.szxb.zibo.config.zibo.line.PraseLine;
import com.szxb.zibo.manager.PosManager;
import com.szxb.zibo.moudle.function.location.GPSEvent;
import com.szxb.zibo.moudle.zibo.Main2Activity;
import com.szxb.zibo.moudle.zibo.SelectLineActivity;
import com.szxb.zibo.record.AppParamInfo;
import com.szxb.zibo.record.XdRecord;
import com.szxb.zibo.runTool.RunSettiing;
import com.szxb.zibo.util.BusToast;
import com.szxb.zibo.util.DateUtil;
import com.szxb.zibo.util.MMKVManager;
import com.tencent.mmkv.MMKV;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function5;
import io.reactivex.functions.Function6;
import io.reactivex.functions.Function7;
import io.reactivex.functions.Function8;
import io.reactivex.schedulers.Schedulers;

import static com.szxb.zibo.config.zibo.InitConfigZB.uploadFile;
import static com.szxb.zibo.util.DateUtil.setK21Time;
import static java.lang.System.arraycopy;

public class InitActiivty extends AppCompatActivity implements RxMessage {
    EditText update_info;
    AnimationDrawable drawable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("流程", "init");
        BusApp.getInstance().addActivity(this);
        setContentView(R.layout.activity);
        BusApp.getInstance().setHandler(handler);
        GPSEvent.GPSEventOpen();
//        if (BusApp.getPosManager().getNeedIntallApk()) {
//            BusApp.getInstance().installApk();
//        }
//        new Thread(new Runnable() {
////            @Override
////            public void run() {
////                uploadFile(FileUtils.readFile(new File(Environment.getExternalStorageDirectory().toString() + "/log.zip")), "zip");
////
////            }
//        }).start();

        PraseLine.prasePub(FileUtils.readAssetsFileTobyte("newkey.pub", this));
        checkedConfig();

    }

    private void checkedConfig() {
        try {
            AppParamInfo appParamInfo = DBManagerZB.checkAppParamInfo();
            MiLog.i("参数配置", "检查运行参数 开机检查：" + new Gson().toJson(DBManagerZB.checkAppParamInfo()));
            if (!appParamInfo.checked()) {//如果当前的运行配置缺少参数  检查保存的
                String string = (String) MMKVManager.getInstance().get("appRunInfo", String.class);
                PosManager posManager = new Gson().fromJson(string, PosManager.class);
                if (posManager != null) {
                    posManager.setLinver("00000000000000");
                    BusApp.setManager(posManager);
                    MiLog.i("流程", "获取并设置备份数据");
                }
            }
        } catch (Exception e) {
            MiLog.i("错误", "开机检查配置报错    " + e.getMessage());
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if ((msg.obj instanceof String) && msg.what == 99) {
                MiLog.i("流程", (String) msg.obj);
                update_info.append(msg.obj + "\n");
            }
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        initView();
        initRx();
        startInit();
    }

    private void startInit() {
//        PraseLine.praseLine(new File("/storage/sdcard0/20190816141533 (1).far"));

        Disposable subscribe = Observable.zip(InitConfigZB.uninstall(), InitConfigZB.initBin(), InitConfigZB.updateKeyBroad(), InitConfigZB.initK21Thread(), InitConfigZB.initUnionParam(), InitConfigZB.sendPosInfo(),
                InitConfigZB.initInstallApk(), InitConfigZB.initLine(), new Function8<Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Object>() {
                    @Override
                    public Object apply(Boolean aBoolean, Boolean aBoolean2, Boolean aBoolean3, Boolean aBoolean4, Boolean a, Boolean aBoolean6, Boolean aBoolean7, Boolean aBoolean8) throws Exception {
                        MiLog.clear(3);
                        return true;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object aBoolean) throws Exception {
                        if ((boolean) aBoolean) {
                            BusToast.showToast("初始化成功", true);
                        }
                        startActivity(new Intent(InitActiivty.this, Main2Activity.class));
                        finish();
                    }

                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.i("错误", throwable.getMessage());

                    }
                });

    }


    @Override
    protected void onPause() {
        super.onPause();
    }


    private void resetPSAM() {
        try {
            devCmd psamDate = DoCmd.resetPSAM();//重置PSAM／
            if (psamDate != null) {
                byte[] psamInfo = new byte[psamDate.getnRecvLen()];
                arraycopy(psamDate.getDataBuf(), 0, psamInfo, 0, psamInfo.length);

                int i = 0;
                //选择卡槽
                byte[] Slot = new byte[1];
                arraycopy(psamDate.getDataBuf(), i, Slot, 0, Slot.length);
                i += Slot.length;
                String slot = FileUtils.bytesToHexString(Slot);

                //终端号
                byte[] PosID = new byte[6];
                arraycopy(psamDate.getDataBuf(), i, PosID, 0, PosID.length);
                i += PosID.length;
                String posID = FileUtils.bytesToHexString(PosID);
                BusApp.getPosManager().setPsamNo(posID);
                Log.i("psamid", posID);

                //PSAM卡号
                byte[] SerialNum = new byte[10];
                arraycopy(psamDate.getDataBuf(), i, SerialNum, 0, SerialNum.length);
                i += SerialNum.length;
                String serialNum = FileUtils.bytesToHexString(SerialNum);
                Log.i("psam卡号", serialNum);

                //密钥索引
                byte[] Key_index = new byte[1];
                arraycopy(psamDate.getDataBuf(), i, Key_index, 0, Key_index.length);
                String key_index = FileUtils.bytesToHexString(Key_index);

                //选择卡槽
                byte[] Slot1 = new byte[1];
                arraycopy(psamDate.getDataBuf(), i, Slot1, 0, Slot1.length);
                i += Slot1.length;
                String slot1 = FileUtils.bytesToHexString(Slot1);

                //终端号
                byte[] PosID1 = new byte[6];
                arraycopy(psamDate.getDataBuf(), i, PosID1, 0, PosID1.length);
                i += PosID1.length;
                String posID1 = FileUtils.bytesToHexString(PosID1);
                Log.i("psamid", posID);

                //PSAM卡号
                byte[] SerialNum1 = new byte[10];
                arraycopy(psamDate.getDataBuf(), i, SerialNum1, 0, SerialNum1.length);
                i += SerialNum1.length;
                String serialNum1 = FileUtils.bytesToHexString(SerialNum1);
                Log.i("psam卡号", serialNum1);

                //密钥索引
                byte[] Key_index1 = new byte[1];
                arraycopy(psamDate.getDataBuf(), i, Key_index1, 0, Key_index1.length);
                String key_index1 = FileUtils.bytesToHexString(Key_index1);
            } else {
                BusToast.showToast("PSAM卡重置数据获取失败", false);
            }
        } catch (Exception e) {
            Log.i("错误", "PSAM卡重置数据获取失败");
        }
    }

    private void initView() {
        try {
            ImageView progress = findViewById(R.id.progress);
            drawable = (AnimationDrawable) progress.getBackground();
            drawable.start();
            ((TextView) findViewById(R.id.tip_info)).setText(String.format("温馨提示:\n\t\t\t\t%1$s", ConfigContext.tip()));
            update_info = findViewById(R.id.update_info);
            update_info.setText("正在进行初始化\n");
        } catch (Exception e) {
            Log.i("错误", "initview报错" + e.getMessage());
        }
    }

    private void initRx() {
        Rx.getInstance().setRxMessage(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Rx.getInstance().remove(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (drawable != null) {
            drawable.stop();
        }
    }

    @Override
    public void rxDo(Object tag, Object... o) {
//        if (tag instanceof String) {
////            switch ((String) tag) {
////                case "selectLine":
////                    startActivity(new Intent(InitActiivty.this, SelectLineActivity.class));
////                    finish();
////                    break;
////            }
////        }
    }

    comThread comThread;

    public static void init21Time() {
        try {
            devCmd devCmd = DoCmd.getTime();
            if (devCmd == null) {
                Log.i("错误", "k21重置失败");
                return;
            }
            byte[] time = new byte[devCmd.getnRecvLen()];
            arraycopy(devCmd.getDataBuf(), 0, time, 0, time.length);
            String timeStr = FileUtils.bytesToHexString(time);

            int yearInt = FileUtils.hexStringToInt(timeStr.substring(0, 4));
            int monthInt = FileUtils.hexStringToInt(timeStr.substring(4, 6));
            int dayInt = FileUtils.hexStringToInt(timeStr.substring(6, 8));
            int hourInt = FileUtils.hexStringToInt(timeStr.substring(8, 10));
            int minInt = FileUtils.hexStringToInt(timeStr.substring(10, 12));
            int secondInt = FileUtils.hexStringToInt(timeStr.substring(12, 14));


            if (yearInt < 2019 && System.currentTimeMillis() < DateUtil.ymdhmsStrToLong("20190610000000")) {
                RunSettiing.getInstance().retTime(true);
            } else if (yearInt < 2019) {
                setK21Time();
            } else if (yearInt >= 2019 && System.currentTimeMillis() < DateUtil.ymdhmsStrToLong("20190610000000")) {
                try {
                    IToolInterface iToolInterface = BusApp.getInstance().getmService();
                    if (iToolInterface != null) {
                        iToolInterface.setDateTime(yearInt, monthInt, dayInt, hourInt, minInt, secondInt);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            Log.i("错误", "重置k21时间" + e.getMessage());
        }
    }
}
