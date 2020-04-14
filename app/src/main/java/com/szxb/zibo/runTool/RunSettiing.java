package com.szxb.zibo.runTool;

import android.text.TextUtils;
import android.util.Log;
import com.google.gson.Gson;
import com.hao.lib.net.OkHttpManager;
import com.lilei.tool.tool.IToolInterface;

import com.szxb.zibo.base.BusApp;
import com.szxb.zibo.net.NetUrl;
import com.szxb.zibo.net.bean.TimeSettingBean;
import com.szxb.zibo.util.BusToast;

import okhttp3.Call;
import okhttp3.Callback;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.szxb.zibo.util.DateUtil.setK21Time;


//用于校准机器的相关设置
public class RunSettiing {
    private static class RunSettingHelp {
        public static RunSettiing runSettiing = new RunSettiing();
    }

    public static RunSettiing getInstance() {
        return RunSettingHelp.runSettiing;
    }

    //校准时间
    public void retTime(final boolean isTip) {
        new OkHttpManager().setNetType(OkHttpManager.NetType.Post)
                .setUrl(NetUrl.REG_TIME_URL)
                .setNetBack(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                    }

                    @Override
                    public void onResponse(Call call, okhttp3.Response response) {
                        try {
                            String st = response.body().string();
                            TimeSettingBean timeSettingBean = new Gson().fromJson(st, TimeSettingBean.class);
                            if (TextUtils.equals("0000", timeSettingBean.getRescode())) {
                                String time = timeSettingBean.getDate();
                                setTime(time, isTip);
                            }
                        } catch (Exception e) {
                            Log.i("", "");
                        }
                    }
                });
    }

    /**
     * 校准时间
     *
     * @param time .
     */
    public static void setTime(String time, boolean isTip) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("zh", "CN"));
            Date date = format.parse(time);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.get(Calendar.YEAR);
            calendar.get(Calendar.MONTH);
            calendar.get(Calendar.DATE);
            calendar.get(Calendar.HOUR);
            calendar.get(Calendar.MINUTE);
            calendar.get(Calendar.SECOND);

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int min = calendar.get(Calendar.MINUTE);
            int second = calendar.get(Calendar.SECOND);

            IToolInterface iToolInterface = BusApp.getInstance().getmService();
            if (iToolInterface != null) {
                iToolInterface.setDateTime(year, month, day, hour, min, second);
                setK21Time();
                if (isTip) {
                    BusToast.showToast("校准成功", true);
                }
            } else {
                if (isTip) {
                    BusToast.showToast("校准失败[NULL]", true);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            BusToast.showToast("校准失败\n" + e.toString(), false);
        }
    }


    public void installApk(){

    }
}
