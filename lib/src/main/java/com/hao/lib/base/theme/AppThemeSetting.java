package com.hao.lib.base.theme;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import com.hao.lib.R;
import com.hao.lib.Util.TypeFaceUtils;
import com.hao.lib.base.MI2App;

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
            return ContextCompat.getDrawable(MI2App.getInstance(), R.color.white);
        }
        return background;
    }

    public void setBackground(Drawable background) {
        this.background = background;
    }

    public Typeface getTypeface() {
        if (typeface == null) {
            typeface = TypeFaceUtils.getHKZHZT(MI2App.getInstance());
        }
        return typeface;
    }

    public void setTypeface(Typeface typeface) {
        this.typeface = typeface;
    }
}
