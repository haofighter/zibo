package com.hao.lib.base.Rx;

import android.os.Looper;
import android.util.Log;

import com.hao.lib.Util.MiLog;
import com.hao.lib.Util.ThreadUtils;
import com.hao.lib.base.MI2App;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Rx {

    private Rx() {
    }

    public static Rx getInstance() {
        return RxHelp.rx;
    }

    private static class RxHelp {
        static final Rx rx = new Rx();
    }

    BlockingQueue<RxMessage> queue = new LinkedBlockingQueue<>(10000);
    ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
            5, 5,
            1, TimeUnit.SECONDS,
            new ArrayBlockingQueue<Runnable>(10, false),
            new ThreadPoolExecutor.CallerRunsPolicy()
    );
    Object tag;
    Object[] o;
    boolean ismain;


    public void sendMessage(Object showTag, Object... o1) {
        tag = showTag;
        o = o1;
        if(o1.length!=0&&o1[0] instanceof Boolean) {
            ismain = (boolean) o1[0];
        }else{
            ismain=true;
        }
        Log.i("rx", "showTag=" + showTag);
        doRx();
    }


    public void doRx() {
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                Log.i("rx", "showTag=" + tag);
                if (queue.size() == 0) {
                    MiLog.i("rx", "queue空");
                    return;
                }
                if (tag == null || o == null) {
                    MiLog.i("rx", "消息空");
                    return;
                }
                Log.i("rx", "showTag1=" + tag);
                for (final RxMessage rx : queue) {
                    try {
                        if (ismain) {
                            MI2App.getInstance().getNowActivitie().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        rx.rxDo(tag, o);
                                    } catch (Exception e) {
                                        MiLog.i("rx报错了", "tag=" + tag);
                                    }
                                }
                            });
                        } else {
                            rx.rxDo(tag, o);
                        }
                    } catch (Exception e) {
                        MiLog.i("rx报错了", "直接" + "tag=" + tag);
                    }
                }
            }
        });

    }

    public void addRxMessage(RxMessage rxMessage) {
        queue.offer(rxMessage);
    }

    public void setRxMessage(RxMessage rxMessage) {
        queue.clear();
        queue.offer(rxMessage);
    }

    public void remove(RxMessage rxMessage) {
        queue.remove(rxMessage);
    }

    public void removeAll() {
        queue.clear();
    }

}
