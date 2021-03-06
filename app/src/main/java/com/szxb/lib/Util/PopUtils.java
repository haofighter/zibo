package com.szxb.lib.Util;

import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;

import com.szxb.lib.base.MI2App;
import com.szxb.zibo.R;
import com.szxb.zibo.base.BusApp;

public class PopUtils {

    private static class PopUtilHelp {
        public static PopUtils popUtils = new PopUtils();
    }

    public static PopUtils getInstance() {
        return PopUtilHelp.popUtils;
    }

    PopupWindow popupWindow;

    /**
     * 显示一个空间长度与关联控件相同的pop
     *
     * @param v
     * @param addView
     */
    public PopUtils createPop(View v, View addView) {
        popupWindow = new PopupWindow(BusApp.getInstance().getApplication());

        popupWindow.setContentView(addView);
        popupWindow.setTouchable(true);
        popupWindow.setWidth(v.getWidth());

        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupWindow.setBackgroundDrawable(BusApp.getInstance().getApplication().getResources().getDrawable(
                R.color.red));
//        // 设置好参数之后再show

        return this;
    }

    public void showAsViewDown(View v) {
        popupWindow.showAsDropDown(v);
    }


    public void showDown(View v) {
        popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
    }
}
