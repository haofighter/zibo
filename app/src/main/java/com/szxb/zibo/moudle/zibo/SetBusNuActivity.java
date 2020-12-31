package com.szxb.zibo.moudle.zibo;

import android.os.Bundle;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.szxb.java8583.module.manager.BusllPosManage;
import com.szxb.lib.base.Rx.RxMessage;
import com.szxb.zibo.R;
import com.szxb.zibo.base.BusApp;

import com.szxb.zibo.base.BaseActivity;
import com.szxb.zibo.config.haikou.ConfigContext;
import com.szxb.zibo.util.BusToast;

import java.util.ArrayList;
import java.util.List;

public class SetBusNuActivity extends BaseActivity implements RxMessage {
    List<TextView> textViews = new ArrayList<>();
    int nowCheck;//当前选中
    boolean isChange = true;//是否为修改状态

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_bus_nu);
        TextView tv_1 = findViewById(R.id.tv_1);
        TextView tv_2 = findViewById(R.id.tv_2);
        TextView tv_3 = findViewById(R.id.tv_3);
        TextView tv_4 = findViewById(R.id.tv_4);
        TextView tv_5 = findViewById(R.id.tv_5);
        TextView tv_6 = findViewById(R.id.tv_6);
        TextView un_1 = findViewById(R.id.un_1);
        TextView un_2 = findViewById(R.id.un_2);
        TextView un_3 = findViewById(R.id.un_3);
        TextView un_4 = findViewById(R.id.un_4);
        TextView un_5 = findViewById(R.id.un_5);
        TextView un_6 = findViewById(R.id.un_6);
        TextView un_7 = findViewById(R.id.un_7);
        TextView un_8 = findViewById(R.id.un_8);
        textViews.add(tv_1);
        textViews.add(tv_2);
        textViews.add(tv_3);
        textViews.add(tv_4);
        textViews.add(tv_5);
        textViews.add(tv_6);
//        textViews.add(un_1);
//        textViews.add(un_2);
//        textViews.add(un_3);
//        textViews.add(un_4);
//        textViews.add(un_5);
//        textViews.add(un_6);
//        textViews.add(un_7);
//        textViews.add(un_8);
        selectTv(nowCheck);

        initDate();
    }

    private void initDate() {
        String busNo = BusApp.getPosManager().getBusNo();
        String unionPos = BusllPosManage.getPosManager().getPosSn();
       if(unionPos==null||unionPos.length()<7){
           unionPos="00000000";
       }
        String date = busNo + unionPos;
        for (int i = 0; i < date.length(); i++) {
            try {
                textViews.get(i).setText(date.substring(i, i + 1));
            } catch (Exception e) {

            }
        }
    }

    @Override
    public void rxDo(Object tag, Object... objects) {
        if (tag instanceof String) {
            switch ((String) objects[0]) {
                case ConfigContext.KEY_BUTTON_BOTTOM_LEFT:
                    if (!isChange) {
                        nowCheck--;
                        if (nowCheck < 0) {
                            nowCheck = textViews.size() - 1;
                        }
                        selectTv(nowCheck);
                    } else {
                        changeNum(nowCheck, false);
                    }
                    break;
                case ConfigContext.KEY_BUTTON_BOTTOM_RIGHT:
                    finish();
                    break;
                case ConfigContext.KEY_BUTTON_TOP_LEFT:
                    if (!isChange) {
                        nowCheck++;
                        selectTv(nowCheck);
                    } else {
                        changeNum(nowCheck, true);
                    }
                    break;
                case ConfigContext.KEY_BUTTON_TOP_RIGHT:
                    if (isChange) {
                        if (nowCheck % textViews.size() == textViews.size() - 1) {
                            isChange = !isChange;
                        } else {
                            nowCheck++;
                        }
                    } else {
                        if (nowCheck % textViews.size() == textViews.size() - 1) {
                            String busNo = getBusNo();
                            BusApp.getPosManager().setBusNo(busNo);
                            BusToast.showToast("设置车号：" + busNo,true);
                            finish();
                        } else {
                            isChange = !isChange;
                        }
                    }
                    selectTv(nowCheck);
                    break;
            }
        }
    }


    public void selectTv(int index) {
        index = index % textViews.size();
        for (int i = 0; i < textViews.size(); i++) {
            textViews.get(i).setBackgroundResource(R.color.transparent);
            textViews.get(i).setTextColor(ContextCompat.getColor(this, R.color.white));
            textViews.get(i).setTextSize(25);
        }
        if (index == 0) {
            isChange = true;
        }
        TextView textView = textViews.get(index);
        if (isChange) {
            textView.setTextColor(ContextCompat.getColor(this, R.color.black));
            textView.setBackgroundResource(R.color.white);
            textView.setTextSize(40);
        } else {
            textView.setBackgroundResource(R.color.transparent);
            textView.setTextColor(ContextCompat.getColor(this, R.color.white));
            textView.setTextSize(40);
        }
    }

    public void changeNum(int index, boolean isAdd) {
        index = index % textViews.size();
        TextView textView = textViews.get(index);
        int num = Integer.parseInt(textView.getText().toString());
        if (isAdd) {
            num++;
        } else {
            num--;
            if (num < 0) {
                num = 9;
            }
        }
        textView.setText(num % 10 + "");
    }


    public String getBusNo() {
        String nu = "";
        for (int i = 0; i < 6; i++) {
            nu += textViews.get(i).getText().toString();
        }
        return nu;
    }

    public String getUnionPos() {
        String nu = "";
        for (int i = 6; i < textViews.size(); i++) {
            nu += textViews.get(i).getText().toString();
        }
        return nu;
    }
}
