package com.szxb.zibo.voice;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

import android.util.Log;

import com.hao.lib.Util.MiLog;
import com.szxb.zibo.R;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


/**
 * 作者: Tangren on 2017-09-05
 * 包名：szxb.com.commonbus.util.sound
 * 邮箱：996489865@qq.com
 * TODO:音源管理
 */

public class SoundPoolUtil {

    public static SoundPool mSoundPlayer = new SoundPool(1,
            AudioManager.STREAM_MUSIC, 5);
    public static SoundPoolUtil soundPlayUtils;
    private static MediaPlayer mediaPlayer;

    private static Context mContext;

    public static int sounds[] = new int[]{
            R.raw.shangban,
            R.raw.xiaban,
            R.raw.erweimamiyaoguoqi,
            R.raw.erweimaguoqi,
            R.raw.erweimageshicuowu,
            R.raw.erweimachongfushiyong,
            R.raw.yuebuzu,
            R.raw.rongjunka,
            R.raw.heimingdanka,
            R.raw.yinhangshanfuka,
            R.raw.qingchongshua,
            R.raw.qingnianshen,
            R.raw.qingchongzhi,
            R.raw.laonianka,
            R.raw.xianluka,
            R.raw.aixinka,
            R.raw.cikayiguoyouxiaoqi,
            R.raw.zanshibunengshiyong,
            R.raw.xueshengka,
            R.raw.shixiaoka,
            R.raw.yuangongka,
            R.raw.sijika,
            R.raw.scan_success,
            R.raw.ec_fee,
            R.raw.ic_invalid,
            R.raw.ec_re_qr_code,
            R.raw.wuxiaoma,
            R.raw.qingshuaxinchongsao,
            R.raw.zanshibunengshiyongcifangshijiaoyi,
            R.raw.xitongyichang,
            R.raw.didi,
            R.raw.caijika,
            R.raw.qiandianka,
            R.raw.jianceka,
            R.raw.chongzhiyuancaozuoshouquanka,
            R.raw.chengxuxiazaishouquanka,
            R.raw.chuzucheshujucaijika,
            R.raw.jiayouka,
            R.raw.wuchangxianxueka,
            R.raw.dang,
            R.raw.dangdang,
            R.raw.cuowu,
            R.raw.qingshangche,
            R.raw.qingxiache,
            R.raw.bupiao,
            R.raw.sijiweishangban,
            R.raw.qingxiacheqingchongzhi,
            R.raw.xueshenkaqingxiache,
            R.raw.laoniankaqingxiache,
            R.raw.aixinkaqingxiache,
            R.raw.rongjunkaqinngxiache,
            R.raw.xueshengkaqingshangche,
            R.raw.laoniankaqingshangche,
            R.raw.aixinkaqingshangche,
            R.raw.rongjunkaqingshangche
    };

    static Queue<Integer> soundQueue = new LinkedList<>();
    static long diractVoiceTime=1500;//语音最低播放时常

    /**
     * 初始化
     *
     * @param context .
     */
    public static SoundPoolUtil init(Context context) {
        if (soundPlayUtils == null) {
            soundPlayUtils = new SoundPoolUtil();
        }
        mContext = context.getApplicationContext();
        for (int sound : sounds) {
            mSoundPlayer.load(mContext, sound, 1);
        }
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    if (!soundQueue.isEmpty() && System.currentTimeMillis() - directTime > diractVoiceTime) {
                        MiLog.i("语音", "线程播报：" + sound(soundQueue.poll()));
                        lastSoundTime = System.currentTimeMillis();
                    }
                } catch (Exception e) {
                    Log.i("错误", "调用声音报错了："+e.getMessage());
                }
            }
        }, 1, diractVoiceTime, TimeUnit.MILLISECONDS);

        return soundPlayUtils;
    }


    static long lastSoundTime;//最后一次播报
    static long directTime;//直接播放的时间


    /**
     * 播放声音
     *
     * @param soundID .
     */
    public synchronized static void play(int soundID) {
        long time = System.currentTimeMillis() - lastSoundTime;
        //判断最后一次添加语音时 语音是否播报完成
        if (time < ((soundQueue.size() + 1) * diractVoiceTime)) {
            soundQueue.add(soundID);
        } else {
            if (soundQueue.isEmpty()) {
                sound(soundID);
            } else {
                soundQueue.add(soundID);
            }
        }
        lastSoundTime = System.currentTimeMillis();
    }


    private static int sound(int soundID) {
        if (mSoundPlayer == null) {
            mSoundPlayer = new SoundPool(1,
                    AudioManager.STREAM_MUSIC, 5);
        }
        int play = mSoundPlayer.play(soundID, 1, 1, 0, 0, 1);
        if (play == 0) {
            if (soundID > 0) {
                playMedia(sounds[soundID - 1]);
            }
        }
        return soundID;
    }


    private static void playMedia(int soundID) {
        try {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }
            mediaPlayer = MediaPlayer.create(mContext, soundID);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setLooping(false);
            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("声音播放异常", "声音异常：" + e.toString());
        }

    }

    public static void release() {
        if (mSoundPlayer != null)
            mSoundPlayer.release();
    }
}
