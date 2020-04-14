package com.hao.lib.base;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.hao.lib.R;
import com.hao.lib.Util.StatusBarUtil;
import com.hao.lib.help.DrawerHelper;

import java.util.ArrayList;
import java.util.List;

public abstract class MI2Activity extends AppCompatActivity implements DrawerHelper {
    protected String MI2TAG = "MI2Activity";
    public static final String PERMISSION_MI = "com.hao.MI";


    public String getMI2TAG() {
        return MI2TAG;
    }

    /**
     * 添加loading布局
     */
    public View addLoading(View v) {
        if (v != null) {
            v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            loading.addView(v);
        }
        return loading;
    }

    /**
     * 显示加载布局
     */
    public void showLoading() {
        if (loading == null) {
            new NullPointerException("还未加载布局,请在布局加载完成后使用");
            return;
        }
        loading.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏加载布局
     */
    public void dismisLoading() {
        if (loading == null) {
            new NullPointerException("还未加载布局,请在布局加载完成后使用");
            return;
        }
        loading.setVisibility(View.GONE);
    }

    @Override
    public void setContentView(int layoutResID) {
        setContentView(LayoutInflater.from(this).inflate(layoutResID, null));
    }

    RelativeLayout loading;
    DrawerLayout drawer;
    RelativeLayout drawer_content;

    @Override
    public void setContentView(View view) {
        drawer = (DrawerLayout) LayoutInflater.from(this).inflate(R.layout.mi2_activity, null);
        loading = drawer.findViewById(R.id.loading);
        drawer_content = drawer.findViewById(R.id.drawer_content);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
//        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ((RelativeLayout) drawer.findViewById(R.id.content)).addView(view);
        super.setContentView(drawer);
    }


    //LOCK_MODE_UNLOCKED = 0; 可拖拽
    //LOCK_MODE_LOCKED_CLOSED = 1; 关闭并不可手势滑动,可调用方法
    //LOCK_MODE_LOCKED_OPEN = 2; 打开不可手势滑动,可调用方法
    //LOCK_MODE_UNDEFINED = 3;初始状态
    public void setDrawerLockMode(int drawerType) {
        drawer.setDrawerLockMode(drawerType);
    }

    public void closeDrawer() {
        drawer.closeDrawers();
    }

    /**
     * 设置一个侧滑的菜单
     *
     * @param view
     */
    @Override
    public void setDrawerContent(View view) {
        view.measure(0, 0);
        drawer_content.measure(0, 0);
        drawer_content.removeAllViews();
        if (view.getMeasuredWidth() > 0) {
            DrawerLayout.LayoutParams dLayoutParams = new DrawerLayout.LayoutParams(view.getMeasuredWidth(), DrawerLayout.LayoutParams.MATCH_PARENT);
            dLayoutParams.gravity = Gravity.LEFT;
            drawer_content.setLayoutParams(dLayoutParams);
        }
        ViewGroup.LayoutParams dLayoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(dLayoutParams);
        drawer_content.addView(view);
        initDrawView(view);
    }

    public void openDrawer() {
        drawer.openDrawer(Gravity.LEFT);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MI2App.getInstance().removeActivity(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (checkCallingOrSelfPermission(PERMISSION_MI) != PackageManager.PERMISSION_GRANTED) {
            throw new SecurityException("继承此activity需要权限");
        }
        StatusBarUtil.setTranslucent(this);
        super.onCreate(savedInstanceState);
        MI2App.getInstance().addActivity(this);
    }


    public DrawerLayout getParentView() {
        return drawer;
    }

    public View getMainContentView() {
        return drawer.findViewById(R.id.content);
    }

    /**
     * 便捷无参数跳转
     *
     * @param a 需要跳转的activityclass
     */
    public void startActivity(Class<? extends Activity> a) {
        startActivity(new Intent(this, a));
    }


    protected abstract void initDrawView(View view);


    protected void initPromission(String[] promision) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> promission = new ArrayList<>();
            for (int i = 0; i < promision.length; i++) {
                if (checkSelfPermission(promision[i]) == PackageManager.PERMISSION_GRANTED) {

                } else {
                    promission.add(promision[i]);
                }
            }

            String[] str = new String[promission.size()];
            for (int i = 0; i < promission.size(); i++) {
                str[i] = promission.get(i);
            }


            if (str.length != 0) {
                requestPermissions(str, 0);
            }
        }
    }


}
