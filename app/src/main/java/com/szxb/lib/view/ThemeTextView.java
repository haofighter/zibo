package com.szxb.lib.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.szxb.lib.base.MI2App;
import com.szxb.zibo.base.BusApp;

@SuppressLint("AppCompatCustomView")
public class ThemeTextView extends TextView {
    public ThemeTextView(Context context) {
        super(context);
        setTextColor(BusApp.getInstance().getMi2Theme().getTextColorResuoce());
    }

    public ThemeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTextColor(BusApp.getInstance().getMi2Theme().getTextColorResuoce());
    }

    public ThemeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTextColor(BusApp.getInstance().getMi2Theme().getTextColorResuoce());
    }


}
