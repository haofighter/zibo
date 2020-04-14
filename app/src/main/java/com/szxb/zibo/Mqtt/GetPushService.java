package com.szxb.zibo.Mqtt;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.*;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.hao.lib.Util.MiLog;
import com.hao.lib.base.BackCall;
import com.szxb.zibo.base.BusApp;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by a'su's on 2018/5/15.
 */

public class GetPushService extends Service {

    final static String serverUri = "tcp://120.220.53.11:10065";
//    final static String serverUri = "tcp://129.204.55.41";
    public final static String subscriptionTopic = "Topic_Trad_QrCode_A";
    public final static int tag = 9012;
    private String clientId = "一般使用设备唯一ID";

    private String userName = "admin";
    private String passWord = "admin";
    private MBinder mBinder = new MBinder();
    private MqttConnectOptions options;

    private static final String TAG = "GetPushService";
    private ScheduledExecutorService scheduler;
    private MqttClient client;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 2) {
                System.out.println("------------连接成功-----------------");
                try {
                    client.subscribe(subscriptionTopic, 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (msg.what == 3) {
                System.out.println("------------连接失败-----------------");
            }
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        // daoSimple = new DaoSimple(App.getInstance());
        clientId = BusApp.getPosManager().getPosSN();

        System.out.println("mqtt设备id---------"
                + clientId);
        init();
        startReconnect();
    }


    private void startReconnect() {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {
                if (!client.isConnected()) {
                    System.out.println("mqtt连接---------设备重连");
                    connect();
                }
            }
        }, 0 * 1000, 10 * 1000, TimeUnit.MILLISECONDS);
    }

    private void init() {
        try {
            //host为主机名，test为clientid即连接MQTT的客户端ID，一般以客户端唯一标识符表示，MemoryPersistence设置clientid的保存形式，默认为以内存保存
            client = new MqttClient(serverUri, clientId,
                    new MemoryPersistence());
            //MQTT的连接设置
            options = new MqttConnectOptions();
            //设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
            options.setCleanSession(true);
            //设置连接的用户名
            options.setUserName(userName);
            //设置连接的密码
            options.setPassword(passWord.toCharArray());
            // 设置超时时间 单位为秒
            options.setConnectionTimeout(10);
            // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
            options.setKeepAliveInterval(20);
            //设置回调
            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    //连接丢失后，一般在这里面进行重连
                    System.out.println("mqtt连接      connectionLost----------");
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    //publish后会执行到这里
                    System.out.println(" mqtt连接    deliveryComplete---------"
                            + token.isComplete());

                }

                @Override
                public void messageArrived(String topicName, final MqttMessage message) {
                    try {
                        Log.i("mqtt 接收数据", "" + new String(message.getPayload()));
                    } catch (Exception e) {
                        MiLog.i("mqtt  错误", "解析错误" + e.getMessage() + "     " + new String(message.getPayload()));
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void sendMessage(XdRecordUpload xdRecordUpload) throws MqttException {
        MqttMessage mqttMessage = new MqttMessage();
        Log.i("mqtt   上送的记录", new Gson().toJson(xdRecordUpload));
        mqttMessage.setPayload(new Gson().toJson(xdRecordUpload).getBytes());
        client.publish(subscriptionTopic, mqttMessage);
    }

    private void connect() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    client.connect(options);
                    Message msg = new Message();
                    msg.what = 2;
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                    Message msg = new Message();
                    msg.what = 3;
                    handler.sendMessage(msg);
                }
            }
        }).start();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            scheduler.shutdown();
            client.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
        GetPushService.this.stopSelf();
    }

    public class MBinder extends Binder implements InterfaceMBinder {
        @Override
        public void sendMessage(XdRecordUpload xdRecordUpload) throws MqttException {
            GetPushService.this.sendMessage(xdRecordUpload);
        }
    }


}
