package com.szxb.lib.base.theme;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

import androidx.annotation.ColorRes;
import androidx.core.content.ContextCompat;

import com.szxb.lib.Util.TypeFaceUtils;
import com.szxb.lib.base.MI2App;
import com.szxb.zibo.R;
import com.szxb.zibo.base.BusApp;

public class AppThemeSetting {
    int textColorResuoce;
    Drawable background;
    Typeface typeface;


    public int getTextColorResuoce() {
        if (textColorResuoce == 0) {
            return R.color.white;
        }
        return textColorResuoce;
    }

    public void setTextColorResuoce(@ColorRes int textColorResuoce) {
        this.textColorResuoce = textColorResuoce;
    }

    public Drawable getBackground() {
        if (background == null) {
            return ContextCompat.getDrawable(BusApp.getInstance().getApplication(), R.color.white);
        }
        return background;
    }

    public void setBackground(Drawable background) {
        this.background = background;
    }

    public Typeface getTypeface() {
        if (typeface == null) {
            typeface = TypeFaceUtils.getHKZHZT(BusApp.getInstance().getApplication());
        }
        return typeface;
    }

    public void setTypeface(Typeface typeface) {
        this.typeface = typeface;
    }
}
