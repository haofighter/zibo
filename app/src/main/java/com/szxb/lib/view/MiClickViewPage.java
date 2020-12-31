package com.szxb.lib.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class MiClickViewPage extends ViewPager {
    int screenWidth = 0;
    int screenHeight = 0;

    public MiClickViewPage(@NonNull Context context) {
        this(context, null);
    }

    public MiClickViewPage(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);

        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenHeight = metrics.heightPixels;
        screenWidth = metrics.widthPixels;
    }

    float downX = 0;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            downX = ev.getX();
        } else if (ev.getAction() == MotionEvent.ACTION_UP) {
            float moveX = ev.getX() - downX;
            if (downX > screenWidth - 100 && moveX < 10) {
                if (getCurrentItem() + 1 < getAdapter().getCount()) {
                    setCurrentItem(getCurrentItem() + 1);
                    downX = 0;
                    return true;
                }
            } else if (downX < 100 && moveX < 10) {
                if (getCurrentItem() > 0) {
                    setCurrentItem(getCurrentItem() - 1);
                    downX = 0;
                    return true;
                } else {

                }
            }
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item);
    }

    //下一页
    public void nextPage() {
        if (getCurrentItem() < getAdapter().getCount() - 1) {
            setCurrentItem(getCurrentItem() + 1);
        }
    }

    public void beforePage() {
        if (getCurrentItem() > 0) {
            setCurrentItem(getCurrentItem() - 1);
        }
    }


    public void getNowHolder() {
    }
}
