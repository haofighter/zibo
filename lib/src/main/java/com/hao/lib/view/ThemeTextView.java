package com.hao.lib.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import com.hao.lib.base.MI2App;

@SuppressLint("AppCompatCustomView")
public class ThemeTextView extends TextView {
    public ThemeTextView(Context context) {
        super(context);
        setTextColor(MI2App.getInstance().getMi2Theme().getTextColorResuoce());
    }

    public ThemeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTextColor(MI2App.getInstance().getMi2Theme().getTextColorResuoce());
    }

    public ThemeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTextColor(MI2App.getInstance().getMi2Theme().getTextColorResuoce());
    }


}