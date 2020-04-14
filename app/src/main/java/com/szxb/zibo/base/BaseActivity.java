package com.szxb.zibo.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;

import com.hao.lib.base.MI2App;
import com.hao.lib.base.Rx.Rx;
import com.hao.lib.base.Rx.RxMessage;

import com.szxb.zibo.R;

public class BaseActivity extends AppCompatActivity implements RxMessage {

    protected TranslateAnimation mShowAnimation;
    protected TranslateAnimation mHiddenAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_base);
        MI2App.getInstance().addActivity(this);
        Log.i("流程", "base");

    }

    @Override
    protected void onResume() {
        initRx();
        super.onResume();
    }

    @Override
    public void setContentView(int layoutResID) {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_base, null);
        View layoutContent = LayoutInflater.from(this).inflate(layoutResID, null);
        RelativeLayout content = view.findViewById(R.id.content);
        content.addView(layoutContent);

        setContentView(view);
        initAnimation();

    }


    public void initRx() {
        Rx.getInstance().setRxMessage(this);
    }


    @Override
    public void rxDo(Object tag, Object... o) {
        if (tag instanceof String) {
            if (tag.equals("net")) {
                findViewById(R.id.net).setVisibility((boolean) o[0] ? View.GONE : View.VISIBLE);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Rx.getInstance().removeAll();
    }

    //菜单初始化动画
    private void initAnimation() {
        mShowAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mShowAnimation.setDuration(300);

        mHiddenAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f);
        mHiddenAnimation.setDuration(300);
    }

    public void setViewAnimal(final View view, boolean isshow) {
        if (isshow) {
            view.setVisibility(View.VISIBLE);
            mShowAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    view.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            view.startAnimation(mShowAnimation);
        } else {
            mHiddenAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    view.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            view.startAnimation(mHiddenAnimation);
        }
    }
}
