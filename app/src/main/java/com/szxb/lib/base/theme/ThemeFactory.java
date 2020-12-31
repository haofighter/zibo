package com.szxb.lib.base.theme;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;

import androidx.annotation.ColorRes;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.szxb.lib.base.MI2App;
import com.szxb.zibo.base.BusApp;

public class ThemeFactory {

    AppThemeSetting appThemeSetting = new AppThemeSetting();

    private ThemeFactory() {
    }

    public AppThemeSetting getTheme() {
        return appThemeSetting;
    }

    static class ThemeFactoryHelp {
        public final static ThemeFactory themeFactory = new ThemeFactory();
    }

    public static ThemeFactory getInstance() {
        return ThemeFactoryHelp.themeFactory;
    }

    public ThemeFactory createTheme() {
        if (appThemeSetting == null) {
            appThemeSetting = new AppThemeSetting();
        }
        return this;
    }


    public ThemeFactory setTextColor(int colorResource) {
        appThemeSetting.setTextColorResuoce(colorResource);
        return this;
    }

    public ThemeFactory setTextColorResource(Context context, @ColorRes int colorResource) {
        setTextColor(ContextCompat.getColor(context, colorResource));
        return this;
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public ThemeFactory setBackBitmap(int resouce) {
        appThemeSetting.setBackground(ContextCompat.getDrawable(BusApp.getInstance().getApplication(), resouce));
        return this;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public ThemeFactory setWindowsBackground(Drawable resouce) {
        appThemeSetting.setBackground(resouce);
        return this;
    }

    public ThemeFactory setBackBitmap(Bitmap bitmap) {
        appThemeSetting.setBackground(new BitmapDrawable(bitmap));
        return this;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void load() {
        if (appThemeSetting.background != null) {
            BusApp.getInstance().setWindowsBackground(appThemeSetting.background);
        }
    }
}
