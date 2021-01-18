package com.szxb.zibo.moudle.init;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.lilei.tool.tool.IToolInterface;
import com.szxb.java8583.module.manager.BusllPosManage;
import com.szxb.lib.Util.FileUtils;
import com.szxb.lib.Util.MiLog;
import com.szxb.lib.base.Rx.Rx;
import com.szxb.lib.base.Rx.RxMessage;
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
import com.szxb.zibo.moudle.function.card.CardInfoEntity;
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
import com.yanzhenjie.nohttp.InitializationConfig;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.URLConnectionNetworkExecutor;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function5;
import io.reactivex.functions.Function6;
import io.reactivex.functions.Function7;
import io.reactivex.functions.Function8;
import io.reactivex.functions.Function9;
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
        //初始化网络请求
        NoHttp.initialize(
                InitializationConfig.newBuilder(getApplication())
                        .networkExecutor(new URLConnectionNetworkExecutor())
                        .connectionTimeout(15 * 1000)
                        .build());
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

//        PraseLine.prasePub(FileUtils.readAssetsFileTobyte("newkey.pub", this));

//        CardInfoEntity cardInfoEntity = new CardInfoEntity();
//        cardInfoEntity.putDate(FileUtils.hexStringToBytes("000000202012091710520400e651acb808000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000425500001005290354ee9f0f7010103e820181024204a1024000000000000005518102410000000007080007080020117000130b0000000000000000000000000286e0000d791ffff286e000009f609f6286e0000d791ffff286e000009f609f6000803620600b4000b05020018e718e7000803620600b4000b05020018e718e700000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000044e010e00c82020120906394718e70025872550010048670004054601012c"));
//        CardInfoEntity cardInfoEntity1 = new CardInfoEntity();
//        cardInfoEntity1.putDate(FileUtils.hexStringToBytes("000000202012091730380400e651acb808000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000425500001005290354ee9f0f7010103e820181024204a1024000000000000005518102410000000007080007080020117000130b0000000000000000000000000286e0000d791ffff286e000009f609f6286e0000d791ffff286e000009f609f600080363090000000b05020018e718e700080363090000000b05020018e718e700000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001041b001000b42020120917100718e7c8259525500100375500040004950100"));
//        CardInfoEntity cardInfoEntity2 = new CardInfoEntity();
//        cardInfoEntity2.putDate(FileUtils.hexStringToBytes("000000202012091730410400e651acb808000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000425500001005290354ee9f0f7010103e820181024204a1024000000000000005518102410000000007080007080020117000130b0000000000000000000000000746d00008b92ffff746d000009f609f6746d00008b92ffff746d000009f609f6000803640600b4000b05020018e718e7000803640600b4000b05020018e718e700000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000041b001800b42020120917301718e700259525500100529900040495010100"));
//        xdRecord.praseDate("0300011F010601DDFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF303430303036FFFFFFFF303031353436130000000000000000FFFFFFFF5136423941315432313831363034323537002009545700000000000000000000000000040509000020093E979F60192120201101144616C61D00002C0100002C01000000000000000000000000000000002550000000000031000101000000000000000000000000000000000000000000000000000000000000000000000000000000000000008800414145424174654D754C4C586A34484B314561687637614A3656485942796E773853415262445372734F56317445746F31365644626E6E6E6B4A5A6E53553143434D4853496E614C552F63555673454D49695176334F39516C716C414A676F7549435241575632546B5A3975714B4A75352F5963493575354234724E677A496B6A31447753413D3D00000000000000000000000000000000000000000000000000000000000000000000000000001F71");
//        Log.i("刷卡记录4", "卡类型："+xdRecord.getMainCardType() + xdRecord.getChildCardType() + "         交易时间：" + xdRecord.getTradeTime() + "      方向：" + xdRecord.getDirection()+"      刷卡站点："+xdRecord.getStationNum());


//            MiLog.i("刷卡", FileUtils.bytesToHexString(bytes));

//            bytes = FileUtils.hexStringToBytes("0000002020112416591908007256b2d02011788080024d54009d0060869b7256b2d0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001493090100006833903664530ffffffff02010310493090100006833920200917208009170000000001564500453000010100390000000000003009413106163674202011241659160900004131061636740200fc000000003700000030000021a620201124165916453013664530ffffffff68fbb68de8f827027d01010000000000000001b802453013664530ffffffff453000fc0d000034303939393800004131061636752020112416591600000000ba453013664530ffffffff453000fc0530303430393939380000413106163674202011241659160000003000002840002100140000000000000000000000000000000000000000000021a60000002020112416591908007256b2d02011788080024d54009d0060869b7256b2d0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001493090100006833903664530ffffffff02010310493090100006833920200917208009170000000001564500453000010100390000000000003009413106163674202011241659160900004131061636740200fc000000003700000030000021a620201124165916453013664530ffffffff68fbb68de8f827027d01010000000000000001b802453013664530ffffffff453000fc0d000034303939393800004131061636752020112416591600000000ba453013664530ffffffff453000fc0530303430393939380000413106163674202011241659160000003000002840002100140000000000000000000000000000000000000000000021a6");
//            解析卡 如果00 则表示解析成功


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
        Disposable subscribe = Observable.zip(InitConfigZB.uninstall(), InitConfigZB.initBin(), InitConfigZB.updateKeyBroad(), InitConfigZB.initK21Thread(),InitConfigZB.checkConfig(), InitConfigZB.initUnionParam(), InitConfigZB.sendPosInfo(),
                InitConfigZB.initInstallApk(), InitConfigZB.initLine(), new Function9<Boolean,Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Object>() {
                    @Override
                    public Object apply(Boolean aBoolean, Boolean aBoolean2, Boolean aBoolean3, Boolean aBoolean4, Boolean a, Boolean aBoolean6, Boolean aBoolean7, Boolean aBoolean8,Boolean aBoolean9) throws Exception {
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


//    private void resetPSAM() {
//        try {
//            devCmd psamDate = DoCmd.resetPSAM();//重置PSAM／
//            if (psamDate != null) {
//                byte[] psamInfo = new byte[psamDate.getnRecvLen()];
//                arraycopy(psamDate.getDataBuf(), 0, psamInfo, 0, psamInfo.length);
//
//                int i = 0;
//                //选择卡槽
//                byte[] Slot = new byte[1];
//                arraycopy(psamDate.getDataBuf(), i, Slot, 0, Slot.length);
//                i += Slot.length;
//                String slot = FileUtils.bytesToHexString(Slot);
//
//                //终端号
//                byte[] PosID = new byte[6];
//                arraycopy(psamDate.getDataBuf(), i, PosID, 0, PosID.length);
//                i += PosID.length;
//                String posID = FileUtils.bytesToHexString(PosID);
//                BusApp.getPosManager().setPsamNo(posID);
//                Log.i("psamid", posID);
//
//                //PSAM卡号
//                byte[] SerialNum = new byte[10];
//                arraycopy(psamDate.getDataBuf(), i, SerialNum, 0, SerialNum.length);
//                i += SerialNum.length;
//                String serialNum = FileUtils.bytesToHexString(SerialNum);
//                Log.i("psam卡号", serialNum);
//
//                //密钥索引
//                byte[] Key_index = new byte[1];
//                arraycopy(psamDate.getDataBuf(), i, Key_index, 0, Key_index.length);
//                String key_index = FileUtils.bytesToHexString(Key_index);
//
//                //选择卡槽
//                byte[] Slot1 = new byte[1];
//                arraycopy(psamDate.getDataBuf(), i, Slot1, 0, Slot1.length);
//                i += Slot1.length;
//                String slot1 = FileUtils.bytesToHexString(Slot1);
//
//                //终端号
//                byte[] PosID1 = new byte[6];
//                arraycopy(psamDate.getDataBuf(), i, PosID1, 0, PosID1.length);
//                i += PosID1.length;
//                String posID1 = FileUtils.bytesToHexString(PosID1);
//                Log.i("psamid", posID);
//
//                //PSAM卡号
//                byte[] SerialNum1 = new byte[10];
//                arraycopy(psamDate.getDataBuf(), i, SerialNum1, 0, SerialNum1.length);
//                i += SerialNum1.length;
//                String serialNum1 = FileUtils.bytesToHexString(SerialNum1);
//                Log.i("psam卡号", serialNum1);
//
//                //密钥索引
//                byte[] Key_index1 = new byte[1];
//                arraycopy(psamDate.getDataBuf(), i, Key_index1, 0, Key_index1.length);
//                String key_index1 = FileUtils.bytesToHexString(Key_index1);
//            } else {
//                BusToast.showToast("PSAM卡重置数据获取失败", false);
//            }
//        } catch (Exception e) {
//            Log.i("错误", "PSAM卡重置数据获取失败");
//        }
//    }

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
