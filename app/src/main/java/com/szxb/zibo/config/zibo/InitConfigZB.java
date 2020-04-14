package com.szxb.zibo.config.zibo;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Environment;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.hao.lib.Util.DataUtils;
import com.hao.lib.Util.FileUtils;
import com.hao.lib.Util.MiLog;
import com.hao.lib.Util.SystemUtils;
import com.hao.lib.base.Rx.Rx;
import com.szxb.java8583.core.Iso8583Message;
import com.szxb.java8583.module.SignIn;
import com.szxb.java8583.module.manager.BusllPosManage;
import com.szxb.jni.SerialCom;
import com.szxb.jni.Ymodem;
import com.szxb.zibo.BuildConfig;
import com.szxb.zibo.base.BusApp;
import com.szxb.zibo.cmd.DoCmd;
import com.szxb.zibo.cmd.comThread;
import com.szxb.zibo.cmd.devCmd;
import com.szxb.zibo.config.zibo.RequestDate.PosDownLoadReseponseDate;
import com.szxb.zibo.config.zibo.RequestDate.PosInfoDate;
import com.szxb.zibo.config.zibo.RequestDate.RequestParam;
import com.szxb.zibo.config.zibo.line.PraseLine;
import com.szxb.zibo.config.zibo.line.StationName;
import com.szxb.zibo.moudle.function.location.GPSEntity;
import com.szxb.zibo.moudle.function.location.GPSEvent;
import com.szxb.zibo.moudle.function.location.GPSToBaidu;
import com.szxb.zibo.moudle.function.unionpay.UnionPay;
import com.szxb.zibo.moudle.function.unionpay.config.UnionConfig;
import com.szxb.zibo.moudle.function.unionpay.config.UnionPayManager;
import com.szxb.zibo.net.JsonRequest;
import com.szxb.zibo.net.NetUrl;
import com.szxb.zibo.net.bean.HeartBean;
import com.szxb.zibo.record.AppParamInfo;
import com.szxb.zibo.record.XdRecord;
import com.szxb.zibo.runTool.RunSettiing;
import com.szxb.zibo.util.ZipUtils;
import com.szxb.zibo.util.apkmanage.AppUtil;
import com.szxb.zibo.util.BusToast;
import com.szxb.zibo.util.DateUtil;
import com.szxb.zibo.util.Util;
import com.szxb.zibo.util.sp.CommonSharedPreferences;
import com.yanzhenjie.nohttp.FileBinary;
import com.yanzhenjie.nohttp.Headers;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.OnUploadListener;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.download.DownloadListener;
import com.yanzhenjie.nohttp.download.DownloadQueue;
import com.yanzhenjie.nohttp.download.DownloadRequest;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.SyncRequestExecutor;

import org.csource.common.NameValuePair;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;

import static java.lang.System.arraycopy;

public class InitConfigZB {
    private static final String HEART = "1";//心跳
    public static final String INFO_UP = "4";//信息上报

    public static Observable<Boolean> uninstall() {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void subscribe(ObservableEmitter<Boolean> subscriber) throws Exception {
                boolean backStatus = false;
                Message message = new Message();
                message.what = 99;
                try {

                    if (AppUtil.getAppProcessName("com.szxb.buspay")) {
                        AppUtil.unInstallAPk("com.szxb.buspay");
                        message.obj = "卸载单票成功";
                    } else if (AppUtil.getAppProcessName("com.szxb.zbgj")) {
                        AppUtil.unInstallAPk("com.szxb.zbgj");
                        message.obj = "卸载多票成功";
                    } else {
                        AppUtil.unInstallAPk("com.szxb.buspay");
                        message.obj = "未发现其他版本";
                        BusApp.getInstance().getHandler().sendMessage(message);
                        subscriber.onNext(true);
                    }
                } catch (Exception e) {
                    message.obj = "卸载出错";
                    subscriber.onNext(false);
                }
                subscriber.onNext(backStatus);
            }
        });

    }


    //加载bin文件
    public static Observable<Boolean> initBin() {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> subscriber) throws Exception {
                boolean backStatus = false;
                Message message = new Message();
                message.what = 99;
                try {
                    String binName = BuildConfig.BIN_NAME;
                    message.obj = binName;
                    if (!TextUtils.equals(BusApp.getPosManager().getLastVersion(), binName)) {
                        MiLog.i("流程", "开始更新");
                        AssetManager ass = BusApp.getInstance().getAssets();
                        int k = Ymodem.ymodemUpdate(ass, binName);
                        if (k == 0) {
                            backStatus = true;
                            BusApp.getPosManager().setLastVersion(binName);
                            SerialCom.DevRest();//重启k21
                            BusToast.showToast("固件 更新成功", true);
                            message.obj = "bin更新成功\n版本" + message.obj;
                            backStatus = true;
                        } else {
                            MiLog.i("流程", "更新失败");
                            message.obj = "bin更新失败\n版本" + message.obj;
                        }
                    } else {
                        message.obj = "bin无需更新\n版本" + message.obj;
                    }


                } catch (Exception e) {
                    message.obj = "bin更新出错\n版本" + message.obj;
                    subscriber.onNext(false);
                }
                BusApp.getInstance().getHandler().sendMessage(message);
                subscriber.onNext(backStatus);
            }
        });
    }

    public static ObservableSource<Boolean> initK21Thread() {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> subscriber) {
                try {
                    Message message = new Message();
                    message.what = 99;
                    Thread thread = comThread.getInstance();
                    if (!thread.isAlive()) {
                        thread.start();
                    }
                    message.obj = "刷卡线程启动成功";
                    BusApp.getInstance().getHandler().sendMessage(message);
                    MiLog.i("流程", "线程开启");
                    resetPSAM();
                    subscriber.onNext(true);
                } catch (Exception e) {
                    subscriber.onNext(true);
                }
            }
        });
    }

    //初始化 PSAM
    private static void resetPSAM() {
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

            } else {
                BusToast.showToast("PSAM卡重置数据获取失败", false);
            }
        } catch (Exception e) {
            MiLog.i("错误", "PSAM卡重置数据获取失败");
        }
    }

    public static Observable<Boolean> sendPosInfo() {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> subscriber) throws Exception {
                try {
                    boolean backStatus = false;
                    Message message = new Message();
                    message.what = 99;
                    message.obj = "发送机具信息";
                    MiLog.i("流程", "发送机具信息");
                    Response<JSONObject> execute = sendInfoToServer(INFO_UP);
                    if (execute.isSucceed()) {
                        String result = execute.get().toJSONString();
                        backStatus = true;
                    }

                    message.obj = message.obj + (backStatus ? "成功" : "失败");
                    BusApp.getInstance().getHandler().sendMessage(message);
                    subscriber.onNext(backStatus);
                } catch (Exception e) {
                    subscriber.onNext(false);
                }
            }
        });
    }

    public static void downLoadFile(Result result, final String taskNo) {
        try {
            MiLog.i("流程", "下载任务：" + taskNo);
            String url = result.getTrans_data().getRequest_content().getHttpUrl();
            if (url == null) {
                return;
            }
            String path = Environment.getExternalStorageDirectory().getAbsolutePath();
            String[] str = url.split("\\.");
            String tag = str[str.length - 1];//取标示符
            final String[] urls = url.split("." + tag);//通过标示字符来获取到版本
            url = urls[0] + "." + tag;//当前文件的下载地址
            String linVer;
            if (tag.equals("apk")) {
                linVer = "zibo." + tag;//保存的文件地址
            } else {
                linVer = urls[1] + "." + tag;//保存的文件地址
            }

            try {
                String oldPath = FileUtils.searchFile(path, tag);
                while (!oldPath.equals("")) {
                    MiLog.i("删除文件", oldPath);
                    File file = new File(oldPath);
                    file.delete();
                    oldPath = FileUtils.searchFile(path, tag);
                }
            } catch (Exception e) {

            }


            MiLog.i("下载", "下载地址：" + url + "         文件保存名称：" + linVer);
            //120.220.53.11:10091/group1/M00/00/01/wKgGK11SOwqANjR3AABHlLDTP1I929.lin
            DownloadQueue downloadQueue = NoHttp.newDownloadQueue();
            DownloadRequest downloadRequest = NoHttp.createDownloadRequest(url, path, linVer, true, true);
            int what = 0;
            if (linVer.contains("apk")) {
                what = 1;
            } else if (linVer.contains("csn")) {
                what = 2;
            }
            downloadQueue.add(what, downloadRequest, new DownloadListener() {
                @Override
                public void onDownloadError(int what, Exception exception) {
                    exception.printStackTrace();
                    Log.i("下载", "onDownloadError:" + exception.getMessage());
                    downLoadResponse("-1", taskNo);
                    BusApp.downProgress = -1;
                }

                @Override
                public void onStart(int what, boolean isResume, long rangeSize, Headers responseHeaders, long allCount) {
                    Log.i("下载", "onStart");
                }

                @Override
                public void onProgress(int what, int progress, long fileCount, long speed) {
                    Log.i("下载", "onProgress:" + progress + "   what：" + what);
                    if (what == 1) {
                        BusApp.downProgress = progress;
                    }
                }

                @Override
                public void onFinish(int what, final String filePath) {
                    if (filePath.endsWith("lin")) {//线路
                        MiLog.i("流程", "线路：" + filePath + "    " + (urls[1].replace("/", "")));
                        BusToast.showToast("线路下载成功", true);
                        BusApp.getPosManager().setLinver(urls[1].replace("/", ""));
                        PraseLine.praseAllLine(new File(filePath));
                        Rx.getInstance().sendMessage("selectLine");
                    } else if (filePath.endsWith("far")) {//票价
                        MiLog.i("流程", "票价：" + filePath);
                        BusApp.getPosManager().setFarver(urls[1].replace("/", ""));
                        PraseLine.praseLine(new File(filePath));
                    } else if (filePath.endsWith("pub")) {//密钥
                        MiLog.i("流程", "密钥：" + filePath);
                        BusApp.getPosManager().setPub_ver(urls[1].replace("/", ""));
                        PraseLine.prasePub(new File(filePath));
                    } else if (filePath.endsWith("csn")) {//黑名单
                        MiLog.i("流程", "黑名单：" + filePath);
                        String blackVer = urls[1].replace("/", "");
                        if (blackVer.split("_").length > 1) {
                            blackVer = blackVer.split("_")[1];
                        }
                        BusApp.getPosManager().setCsnVer(blackVer);
                        Executors.newScheduledThreadPool(1).schedule(new Runnable() {
                            @Override
                            public void run() {
                                PraseLine.praseCsn(new File(filePath));
                            }
                        }, 0, TimeUnit.SECONDS);
                    } else if (filePath.endsWith("apk")) {//下载APK
                        try {
                            MiLog.i("流程", "下载APK：" + filePath);
                            MiLog.i("请求", "下载安装:" + "正在安装：" + filePath);
                            BusApp.getPosManager().setIntallApkPath(filePath);
                            BusApp.getPosManager().setNeedIntallApk(true);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    PackageManager packageManager = BusApp.getInstance().getPackageManager();
                                    Intent it = packageManager.getLaunchIntentForPackage("com.szxb.installapk");
                                    BusApp.getInstance().startActivity(it);
                                }
                            }).start();
                        } catch (Exception e) {
                            MiLog.i("请求", "下载安装:" + "安装出错" + e.getMessage());
                        }
                    }
                    Executors.newSingleThreadExecutor().submit(new Runnable() {
                        @Override
                        public void run() {
                            downLoadResponse("1", taskNo);//确认下载任务
                            try {
                                sendInfoToServer(INFO_UP);
                            } catch (Exception e) {
                            }

                        }
                    });
                }

                @Override
                public void onCancel(int what) {
                    Log.i("下载", "onCancel");
                }
            });
        } catch (
                Exception e) {
            MiLog.i("错误", "下载错误：" + e.getMessage());
        }

    }

    public static void sendHeart() {
        try {
            Response<JSONObject> execute = sendInfoToServer(HEART);
            if (execute.isSucceed()) {
                String resultStr = execute.get().toJSONString();
                Result result = new Gson().fromJson(resultStr, Result.class);
                if (result.getResult_code().equals("0")) {
                    if (result.getTrans_data().getRequest_code().equals("ums_info")) {//银联参数
                        String union = result.getTrans_data().getRequest_content().getUms_tenant_no() + "-"
                                + result.getTrans_data().getRequest_content().getKek() + "-"
                                + result.getTrans_data().getRequest_content().getUms_terminal_no();
                        CommonSharedPreferences.put("union", union);
                        BusApp.getPosManager().setUms_key_ver(result.getTrans_data().getRequest_content().getMd5());
                        MiLog.i("流程", "银联参数：" + union);
                        regeistUnion(union);
                        downLoadResponse("1", result.getTrans_data().getTask_no());//确认下载任务
                        sendInfoToServer(INFO_UP);
                    } else if (result.getTrans_data().getRequest_code().equals("up_log")) {//银联参数

                        FileUtils.copyDir(
                                "data/data/" + BusApp.getInstance().getPackageName(),
                                Environment.getExternalStorageDirectory().toString() + "/log/"
                        );

                        ZipUtils.doCompress(Environment.getExternalStorageDirectory().toString() + "/log/", Environment.getExternalStorageDirectory().toString() + "/log.zip");
                        String[] path = uploadFile(FileUtils.readFile(new File(Environment.getExternalStorageDirectory().toString() + "/log.zip")), "zip");

                        if (path != null && path.length >= 2) {
                            PosDownLoadReseponseDate.PosUploadInfoDate posUploadInfoDate = new PosDownLoadReseponseDate.PosUploadInfoDate();
                            posUploadInfoDate.setFile_name(DateUtil.getCurrentDate2() + ".zip");
                            posUploadInfoDate.setGroup(path[0]);
                            posUploadInfoDate.setPath(path[1]);
                            posUploadInfoDate.setProtocol("fastdfs");
                            posUploadInfoDate.setIp_port("139.9.113.219:22000");
                            Response<JSONObject> response = downLoadResponse("1", result.getTrans_data().getTask_no(), posUploadInfoDate);
                            MiLog.i("请求", "日志任务确认返回=" + response.get().toString());
                        } else {
                            downLoadResponse("-1", result.getTrans_data().getTask_no(), "");
                        }
                        sendInfoToServer(INFO_UP);
                    } else {
                        downLoadFile(result, result.getTrans_data().getTask_no());
                    }
                } else {
                    Log.i("请求", "心跳失败");
                }
            } else {
                Log.i("请求", "心跳失败");
            }
        } catch (Exception e) {
            MiLog.i("心跳", "心跳上送失败");
        }
    }

    //上报信息  1 心跳  4 上报信息  99
    public static Response<JSONObject> sendInfoToServer(String type) throws Exception {
        JsonRequest macRequest = new JsonRequest(NetUrl.SEND_INFO_SERVER);
        macRequest.setConnectTimeout(3000);
        macRequest.setReadTimeout(3000);
        PosInfoDate posInfoDate = new PosInfoDate();
        if (type.equals(INFO_UP)) {
            posInfoDate.setLevel_no(BusApp.getPosManager().getLineNo());
            posInfoDate.setBase_price(BusApp.getPosManager().getBasePrice() + "");
            posInfoDate.setBus_no(BusApp.getPosManager().getBusNo().toString());
            posInfoDate.setCert_ver("0");
            posInfoDate.setCsn_ver(BusApp.getPosManager().getCsnVer());
            posInfoDate.setCurrent_time(DateUtil.getCurrentDate2());
            posInfoDate.setDriver_no(BusApp.getPosManager().getDriverNo().toString());
            posInfoDate.setFar_ver(BusApp.getPosManager().getFarver());
            posInfoDate.setLib_ver(BusApp.getPosManager().getLib_ver());
            posInfoDate.setLine_no(BusApp.getPosManager().getLineNo().toString());
            posInfoDate.setLin_ver(BusApp.getPosManager().getLinver());
            posInfoDate.setLocation_info(GPSEvent.bdLocation == null ? "0" : (GPSEvent.bdLocation.getLongitude() + "," + GPSEvent.bdLocation.getLatitude()));
            posInfoDate.setMax_pos_sn(BusApp.getPosManager().getmchTrxIdNow() + "");
            posInfoDate.setPsamInf(BusApp.getPosManager().getM1psam() + "," + BusApp.getPosManager().getCpupsam());
            posInfoDate.setPsam_no(BusApp.getPosManager().getPsamNo());
            posInfoDate.setPub_ver(BusApp.getPosManager().getPub_ver());
            posInfoDate.setSoftwar_ver(BusApp.getInstance().getPakageVersion());
            posInfoDate.setUnion_com_no(BusllPosManage.getPosManager().getMchId());
            posInfoDate.setUnion_term_no(BusllPosManage.getPosManager().getPosSn());
            posInfoDate.setUn_upload_num(DBManagerZB.checkUnUp() + "");
            posInfoDate.setUsr_ver(BusApp.getPosManager().getUsrver());
            posInfoDate.setUms_key_ver(BusApp.getPosManager().getUms_key_ver());
            Log.i("线路版本", BusApp.getPosManager().getLinver());
            MiLog.i("请求", "心跳信息上报");
        } else if (type.equals(HEART)) {
            MiLog.i("请求", "心跳上送");
            posInfoDate.setSoft_ver(BusApp.getInstance().getPakageVersion());
        }

        macRequest.setDefineRequestBodyForJson(RequestParam.getRequestParam(posInfoDate, type));
        MiLog.i("请求", "数据：" + RequestParam.getRequestParam(posInfoDate, type));
        Response<JSONObject> execute = SyncRequestExecutor.INSTANCE.execute(macRequest);
        MiLog.i("请求", "返回：" + execute.get().toString());
        if (execute.isSucceed()) {
            if (!type.equals(HEART)) {
                MiLog.i("流程", "请求 重新发送心跳");
                sendHeart();
            } else {
                String result = execute.get().toJSONString();
                HeartBean heartBean = new Gson().fromJson(result, HeartBean.class);
                if (heartBean.getTimestamp() - System.currentTimeMillis() > 30 * 1000) {
                    Date date = new Date(heartBean.getTimestamp());
                    RunSettiing.getInstance().setTime(DateUtil.getCurrentDate(date), false);
                }
            }
        }
        return execute;
    }

    public static Response<JSONObject> downLoadResponse(String status, String taskno) {
        return downLoadResponse(status, taskno, null);
    }

    //下载任务确认
    public static Response<JSONObject> downLoadResponse(String status, String taskno, Object content) {
        JsonRequest macRequest = new JsonRequest(NetUrl.SEND_INFO_SERVER);
        PosDownLoadReseponseDate posDownLoadReseponseDate = new PosDownLoadReseponseDate();
        posDownLoadReseponseDate.setStatus(status);
        posDownLoadReseponseDate.setTask_no(taskno);
        if (content != null) {
            posDownLoadReseponseDate.setContent(content);
        }
        String date = RequestParam.getRequestParam(posDownLoadReseponseDate, "5");
        MiLog.i("流程", "请求 任务确认：" + taskno + "   status=" + status + "   " + date);
        macRequest.setDefineRequestBodyForJson(date);
        return SyncRequestExecutor.INSTANCE.execute(macRequest);
    }

    //初始化银联参数
    public static Observable<Boolean> initUnionParam() {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> subscriber) throws Exception {
                UnionPayManager unionPayManager = new UnionPayManager();
                BusllPosManage.init(unionPayManager);
                String unionStr = (String) CommonSharedPreferences.get("union", "");
                if (unionStr.equals("") && !BuildConfig.isTest) {
                    subscriber.onNext(false);
                    return;
                }

                if (!BusApp.getInstance().getNetWorkState()) {
                    BusToast.showToast("网络不可用", false);
                    subscriber.onNext(false);
                    return;
                }

                MiLog.i("银联参数", unionStr);
                try {
                    regeistUnion(unionStr);
                    MiLog.i("流程", "银联签到");
                    subscriber.onNext(true);
                } catch (Exception e) {
                    Log.i("流程", "银联参数设置错误");
                    subscriber.onNext(false);
                }
            }
        });
    }

    private static void regeistUnion(String unionStr) {
        if (unionStr != null && !unionStr.equals("")) {
            String[] unionParam = unionStr.split("-");
            BusllPosManage.getPosManager().setMachId(unionParam[0]);
            BusllPosManage.getPosManager().setKey(unionParam[1]);
            BusllPosManage.getPosManager().setPosSn(unionParam[2]);
        } else {
            return;
        }

//        //淄博返回商户
//        BusllPosManage.getPosManager().setMachId("438370341112007");
//        BusllPosManage.getPosManager().setKey("2C4F497C20FB2FB6758651E5750DAD9B");
//        BusllPosManage.getPosManager().setPosSn("63461748");

        if (!BusApp.getPosManager().getLineType().endsWith("P")) {
            BusllPosManage.getPosManager().setTradeSeq();
            Iso8583Message message = SignIn.getInstance().message(BusllPosManage.getPosManager().getTradeSeq());
            UnionPay.getInstance().exeSSL(UnionConfig.SIGN, message.getBytes(), true);
        }

    }

    //将超过5000条的数据 转换成文件进行储存
    public static Observable<Boolean> clearDateBase() {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> subscriber) throws Exception {
                try {
                    if (DBManagerZB.checkUploadRecodeNum() > 5000) {
                        List<XdRecord> scanRecordEntities = DBManagerZB.checkUploadRecod();
                        String scanRecord = new Gson().toJson(scanRecordEntities);
                        FileUtils.saveStrToFile(scanRecord, new File(Environment.getExternalStorageDirectory() + "/NewRecord/scan_" + DateUtil.getCurrentDate2() + "_record.txt"));
                        DBManagerZB.deleteScanRecord(scanRecordEntities);
                    }

                    File file = new File(Environment.getExternalStorageDirectory() + "/NewRecord");
                    List<File> remove = new ArrayList<>();
                    if (file.isDirectory()) {
                        File[] files = file.listFiles();
                        for (int i = 0; i < files.length; i++) {
                            String[] strs = files[i].getName().split("_");
                            long date = Long.parseLong(strs[1]);//yyyyMMddHHmmss  转为int  20190621000000
                            long now = Long.parseLong(DateUtil.getCurrentDate2());//       20190721000000
                            if (now - date > 100000000) {
                                remove.add(files[i]);
                            }
                        }
                    }
                    for (int i = 0; i < remove.size(); i++) {
                        Log.i("文件清理", remove.get(i).getName());
                        remove.get(i).delete();
                    }
                    subscriber.onNext(true);
                } catch (Exception e) {
                    subscriber.onNext(false);
                    Log.i("错误", "clearDateBase(RecordUpload.java:207) 数据清除错误" + e.getMessage());
                }
            }
        });
    }


    //将超过5000条的数据 转换成文件进行储存
    public static Observable<Boolean> initLine() {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> subscriber) throws Exception {
                AppParamInfo appParamInfo = DBManagerZB.checkAppParamInfo();
                if (appParamInfo == null || appParamInfo.getLinVer().equals("00000000000000")) {
                    byte[] bytes = FileUtils.readAssetsFileTobyte("20190723000001_EFD9.lin", BusApp.getInstance());
                    PraseLine.praseAllLineByte(bytes);
                    subscriber.onNext(true);
                }
                subscriber.onNext(false);
            }
        });
    }


    public static Observable<Boolean> initInstallApk() {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> subscriber) throws Exception {
                String now = BuildConfig.InstallApk;
                String old = BusApp.getPosManager().getInstallApk();
                if (!now.equals(old)) {
                    try {
                        byte[] bytes = FileUtils.readAssetsFileTobyte(BuildConfig.InstallApk, BusApp.getInstance());
                        if (FileUtils.byteToFile(bytes, new File(Environment.getExternalStorageDirectory() + "/install.apk"))) {
                            MiLog.i("安装", "apk保存成功");
                            BusApp.getInstance().getmService().apkInstall(Environment.getExternalStorageDirectory() + "/install.apk");
                            MiLog.i("安装", "apk安装成功");
                            BusApp.getPosManager().setInstallApk(BuildConfig.InstallApk);
                            subscriber.onNext(true);
                        } else {
                            subscriber.onNext(false);
                        }
                    } catch (Exception e) {
                        subscriber.onNext(false);
                        MiLog.i("异常", "获取安装apk异常");
                    }
                } else {
                    subscriber.onNext(true);
                }
            }
        });
    }


    public static void addstation() {
        try {
            if (BusApp.getPosManager().getLineType().equals("O")) {
                return;
            }


            List<StationName> stationNames = DBManagerZB.checkStation(BusApp.getPosManager().getDirection());

            int nowStation = BusApp.getPosManager().getStationID();
            if (nowStation > stationNames.size()) {
                nowStation = nowStation % stationNames.size();
            }

            Log.i("站点循环", BusApp.getPosManager().getStationID() + "       " + BusApp.getPosManager().getDirection());
            StationName stationName = stationNames.get(nowStation);
            //坐标转换
            GPSEntity gpsEntity = new GPSEntity();
            double[] add = GPSToBaidu.wgs2bd(Util.get6Double(stationName.getLat()), Util.get6Double(stationName.getLon()));
            gpsEntity.setLatitude(add[0]);
            gpsEntity.setLongitude(add[1]);

            MiLog.i("转换的站点坐标", "stationName：lat=" + stationName.getLat() + "  lon=" + stationName.getLon());
            MiLog.i("转换完成的站点坐标", "stationName：lat=" + add[0] + "  lon=" + add[1]);

            GPSEvent.Calculation(gpsEntity);
        } catch (Exception e) {
            MiLog.i("切换站错误", e.getMessage());
        }
    }


    public static void refuseStation() {
        try {
            if (BusApp.getPosManager().getLineType().equals("O")) {
                return;
            }
            int nowStation = BusApp.getPosManager().getStationID();
            List<StationName> stationNames = DBManagerZB.checkStation(BusApp.getPosManager().getDirection());
            Log.i("站点循环", BusApp.getPosManager().getStationID() + "       " + BusApp.getPosManager().getDirection());
            nowStation--;
            nowStation--;
            if (nowStation < 0) {
                nowStation = 0;
            }
            StationName stationName = stationNames.get(nowStation);
            //坐标转换
            GPSEntity gpsEntity = new GPSEntity();
            double[] add = GPSToBaidu.wgs2bd(Util.get6Double(stationName.getLat()), Util.get6Double(stationName.getLon()));
            gpsEntity.setLatitude(add[0]);
            gpsEntity.setLongitude(add[1]);

//            gpsEntity.setLatitude(36.8041744259);
//            gpsEntity.setLongitude(118.0584396479);

            MiLog.i("转换的站点坐标", "stationName：lat=" + stationName.getLat() + "  lon=" + stationName.getLon());
//            MiLog.i("转换完成的站点坐标", "stationName：lat=" + add[0] + "  lon=" + add[1]);

            GPSEvent.Calculation(gpsEntity);
        } catch (Exception e) {
            MiLog.i("切换站错误", e.getMessage());
        }
    }


//    public static void uploadeFile(File file, String url) {
//        RequestQueue requestQueue = NoHttp.newRequestQueue();
//        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.POST);
//        FileBinary binary = new FileBinary(file, "log");
//        binary.setUploadListener(5, new OnUploadListener() {
//            @Override
//            public void onStart(int what) {
//
//            }
//
//            @Override
//            public void onCancel(int what) {
//
//            }
//
//            @Override
//            public void onProgress(int what, int progress) {
//
//            }
//
//            @Override
//            public void onFinish(int what) {
//
//            }
//
//            @Override
//            public void onError(int what, Exception exception) {
//
//            }
//        });
//        requestQueue.add(4, request, new OnResponseListener<String>() {
//            @Override
//            public void onStart(int what) {
//
//            }
//
//            @Override
//            public void onSucceed(int what, Response<String> response) {
//
//            }
//
//            @Override
//            public void onFailed(int what, Response<String> response) {
//
//            }
//
//            @Override
//            public void onFinish(int what) {
//
//            }
//        });
//    }


    public static String[] uploadFile(byte[] file, String extName) {
        try {

            //network_timeout = 30
            //charset = UTF-8
            //http.tracker_http_port = 8080
            //http.anti_steal_token = no
            //http.secret_key = FastDFS1234567890
            //
            //tracker_server = 10.0.11.243:22122
            //tracker_server = 10.0.11.244:22122
            //
            //connection_pool.enabled = true
            //connection_pool.max_count_per_entry = 500
            //connection_pool.max_idle_time = 3600
            //connection_pool.max_wait_time_in_ms = 1000
            String config = "connect_timeout_in_seconds=5\n" +
                    "network_timeout = 30\n" +
                    "charset = UTF-8\n" +
                    "connect_timeout = 2\n" +
                    "http.anti_steal_token = no\n" +
                    "http.tracker_http_port = 8080\n" +
                    "\n" +
//                    "http.secret_key = FastDFS1234567890\n" +
//                    "tracker_server = 139.9.113.219:10065\n" +
                    "tracker_server = 139.9.113.219:22000\n" +
                    "\n" +
                    "connection_pool.max_count_per_entry = 500\n" +
                    "connection_pool.enabled = true\n" +
                    "connection_pool.max_idle_time = 3600\n" +
                    "connection_pool.max_wait_time_in_ms = 1000";


            FileUtils.saveStrToFile(config, new File(Environment.getExternalStorageDirectory().toString() + "/config.pro"));
            // 初始化文件资源
            ClientGlobal.init(Environment.getExternalStorageDirectory().toString() + "/config.pro");
            // 链接FastDFS服务器，创建tracker和Stroage
            TrackerClient trackerClient = new TrackerClient(ClientGlobal.g_tracker_group);

            TrackerServer trackerServer = trackerClient.getConnection();
            NameValuePair nvp[] = new NameValuePair[]{new NameValuePair("zip", DateUtil.getCurrentDate2()),};

            StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);

            StorageClient storageClient = new StorageClient(trackerServer, storageServer);

//          System.out.println(fileIds.length);
//          System.out.println("组名：" + fileIds[0]);
//          System.out.println("路径: " + fileIds[1]);

            return storageClient.upload_file(file, extName, nvp);
        } catch (Exception e) {

            MiLog.i("上传日志失败", e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}