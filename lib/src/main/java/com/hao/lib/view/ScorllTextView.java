package com.hao.lib.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.animation.AccelerateInterpolator;
import com.hao.lib.R;
import com.hao.lib.Util.SystemUtils;
import com.hao.lib.base.BackCall;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义一个文本展示控件
 * 通过view大小来控制展示的文字数量
 * 并能够识别换行符
 * 并针对大文本进行分页处理
 */
public class ScorllTextView extends View {
    private String textContent = "";
    private float lineSpacingExtra;//行间距
    private float wordSpacingExtra;//字间距
    private float textSize;//文字大小
    private Context context;
    private int lineTextNum;//每行字数
    private int lineNum;//容纳的行数
    private int textColor;//字体颜色
    private Typeface typeface = Typeface.DEFAULT;
    private List<String> textArray = new ArrayList<>();//每一行数据为一个元素  以行数据为单位
    private int show = 0;//当前文本展示的页面
    float textPadingVar = 0;//垂直方向的间隔距离
    float textPadingHor = 0;//横向方向的间隔距离
    float textPadingleft = 0;//文字距离左边距离
    float textPadingright = 0;//文字距离右边距离
    float textPadingtop = 0;//文字距离上边距离
    float textPadingbottom = 0;//文字距离下边距离
    int pageSize = 0;//文字填充的页数

    float offsetHor = 0;//画布横向方向偏移量
    float offsetVar = 0;//画布垂直方向偏移量
    int viewWidth;
    int viewhigh;

    public ScorllTextView(Context context) {
        this(context, null);
    }

    public ScorllTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScorllTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ScorllTextView);
        lineSpacingExtra = array.getDimension(R.styleable.ScorllTextView_lineSpacingExtra, 6);//行间距
        wordSpacingExtra = array.getDimension(R.styleable.ScorllTextView_wordSpaceExtra, 6);//字间距
        textPadingbottom = array.getDimension(R.styleable.ScorllTextView_paddingBottom, 0);//底部距离
        textPadingtop = array.getDimension(R.styleable.ScorllTextView_paddingTop, 0);//顶部距离
        textPadingright = array.getDimension(R.styleable.ScorllTextView_paddingRight, 0);//右边距离
        textPadingleft = array.getDimension(R.styleable.ScorllTextView_paddingLeft, 0);//左边距离
        float pading = array.getDimension(R.styleable.ScorllTextView_padding, 0);//左边距离
        if (pading != 0) {
            textPadingleft = textPadingbottom = textPadingtop = textPadingright = pading;
        }
        textColor = array.getColor(R.styleable.ScorllTextView_textColor, 0xFFFFFFFF);//颜色
        textSize = array.getDimension(R.styleable.ScorllTextView_textSize, SystemUtils.INSTANCE.sp2px(context, 20));//文字大小
        if (scorllTextChangeListener != null) {
            scorllTextChangeListener.pageChange(show);
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewWidth = getMeasuredWidth();
        viewhigh = getMeasuredHeight();
        initViewConfig();
    }


    private void initViewConfig() {
        //绘制文字空间的垂直方向的大小
        float textContentVar = viewhigh - textPadingtop - textPadingbottom;
        //绘制文字空间的横向方向的大小
        float textContentHor = viewWidth - textPadingleft - textPadingright;
        //计算的每行容纳的文字大小
        lineTextNum = (int) (textContentHor / (textSize + wordSpacingExtra));
        //计算没页容纳文字行数
        lineNum = (int) (textContentVar / (textSize + lineSpacingExtra));

        //计算出去边缘距离和文字占用的位置剩余的位置 并计算出每页文字的位置
        //文本垂直方向距离边缘的位置  通过计算一行被填满时所占用的位置，算出空出的位置长度,主要用于文本居中处理
        textPadingVar = (textContentVar - lineNum * (textSize + lineSpacingExtra)) / 2;
        offsetVar = textPadingVar + textPadingtop;
        //文本水平方向距离边缘的位置  通过计算一行被填满时所占用的位置，算出空出的位置长度,主要用于文本居中处理
        textPadingHor = (textContentHor - lineTextNum * (textSize + wordSpacingExtra)) / 2;
        offsetHor = textPadingHor + textPadingleft;
    }

    private void initDate() {
        textArray = new ArrayList<>();
        String[] strs = textContent.split("\n");
        for (int i = 0; i < strs.length; i++) {
            if (strs[i].length() <= lineTextNum) {
                textArray.add(strs[i]);
                continue;
            }
            if (strs[i].length() % lineTextNum == 0) {//刚好整除  此段文字刚好后被整数行容纳
                for (int j = 0; j < strs[i].length() / lineTextNum; j++) {
                    textArray.add(strs[i].substring(j * lineTextNum, (j + 1) * lineTextNum));
                }
            } else {
                int needLineNum = strs[i].length() / lineTextNum + 1;
                for (int k = 0; k < needLineNum; k++) {//获取到每段的字符串 判断能够容纳几行
                    String nowLineText = "";
                    if (k < needLineNum - 1) {
                        nowLineText = strs[i].substring(k * lineTextNum, (k + 1) * lineTextNum);
                    } else {
                        nowLineText = strs[i].substring(k * lineTextNum, strs[i].length());
                    }
                    textArray.add(nowLineText);
                }
            }
        }

        if (textArray.size() % lineNum == 0) {
            pageSize = textArray.size() / lineNum;
        } else {
            pageSize = textArray.size() / lineNum + 1;
        }

    }

    public void setText(String string) {
        textContent = string;
        show = 0;
        invalidate();
    }

    public void appendText(String string) {
        textContent = textContent + string;
        invalidate();
    }

    public void setTextColor(int color) {
        textColor = color;
        invalidate();
    }

    public void setTextColorResuoce(int color) {
        textColor = ContextCompat.getColor(context, color);
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initDate();
        Paint paint = new Paint();
        paint.setTextSize(textSize);
        paint.setFakeBoldText(false);
        paint.setTypeface(typeface);
        paint.setColor(textColor);
        for (int i = 0; i < textArray.size(); i++) {
            float drawTextY = offsetVar + (i % lineNum) * (textSize + lineSpacingExtra) + textSize;//间距的数量比文字行数少一行
            for (int j = 0; j < textArray.get(i).length(); j++) {
                float drawTextX = offsetHor + (textSize + wordSpacingExtra) * j + (int) (i / lineNum) * viewWidth;
                canvas.drawText(textArray.get(i).substring(j, j + 1), drawTextX, drawTextY, paint);
            }
        }
    }

    public void setTypeface(Typeface typeface) {
        this.typeface = typeface;
    }

    //设置文本中当前展示页
    public void setPage(int show) {
        this.show = show;
        offsetVar = textPadingHor + show * viewWidth;
        invalidate();
    }


    float fingerNowX = 0;
    float fingerNowY = 0;
    float moveX = 0;//拖动的距离
    float moveY = 0;

    //手指接触屏幕的时间
    long downTime;
    float s;//手指离开后 以50px的加速度减速后位移的距离
    boolean isNextPage;//用于控制翻页方向

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getPointerCount() == 1) {//只有一根手指
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                s = 0;//重置位移距离
                downTime = System.currentTimeMillis();
                fingerNowX = ev.getX();
                fingerNowY = ev.getY();
                attemptClaimDrag(true);
                return true;
            } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
                moveX = moveX + ev.getX() - fingerNowX;
                moveY = moveY + ev.getY() - fingerNowY;
                fingerNowX = ev.getX();
                fingerNowY = ev.getY();
                if ((moveX > 0 && show == 0) || (moveX < 0 && show == pageSize - 1)) {
                    //如果手指滑动了 并且是第一页向右滑  或者是 最后一页向左滑
                    attemptClaimDrag(false);
                    return false;
                } else {
                    offsetHor = textPadingVar + moveX - show * viewWidth;
                    invalidate();
                    attemptClaimDrag(true);
                    return true;
                }
            } else if (ev.getAction() == MotionEvent.ACTION_UP) {
                final float moveTime = ((float) (System.currentTimeMillis() - downTime)) / 100;
                s = moveX / moveTime / 2 * moveX / 30;
                if (moveX == 0) {
                    s = viewWidth / 4 + 1;
                    if (ev.getX() > viewWidth / 2) {
                        if (show != pageSize - 1) {
                            isNextPage = true;
                        } else {
                            ViewParent viewParent = getParent();
                            ViewParent parent = viewParent.getParent();
                            if (parent instanceof MiClickViewPage) {
                                ((MiClickViewPage) parent).nextPage();
                            }
                            if (scorllTextChangeListener != null) {
                                scorllTextChangeListener.textEnd();
                            }
                        }
                    } else if ((ev.getX() < viewWidth / 2)) {
                        if (show != 0) {
                            isNextPage = false;
                        } else {
                            if (getParent() instanceof MiClickViewPage) {
                                ((MiClickViewPage) getParent()).beforePage();
                            }
                            if (scorllTextChangeListener != null) {
                                scorllTextChangeListener.textStart();
                            }
                        }
                    }
                } else {
                    if (moveX < 0) {
                        isNextPage = true;
                    } else {
                        isNextPage = false;
                    }
                }

                if ((moveX > 0 && show == 0) || (moveX < 0 && show == pageSize - 1)) {
                    //如果手指滑动了 并且是第一页向右滑  或者是 最后一页向左滑
                    if (isNextPage && (moveX < 0 && show == pageSize - 1)) {
                        if (scorllTextChangeListener != null) {
                            scorllTextChangeListener.textEnd();
                        }
                        attemptClaimDrag(false);
                        return false;
                    } else if (!isNextPage && (moveX > 0 && show == 0)) {
                        if (scorllTextChangeListener != null) {
                            scorllTextChangeListener.textStart();
                        }
                        attemptClaimDrag(false);
                        return false;
                    }
                } else {
                    if (show >= 0 && show < pageSize) {
                        setValueAnimal(new BackCall() {
                            @Override
                            public void call(Object o) {
                                if (Math.abs(s) > viewWidth / 4 || Math.abs(moveX) > viewWidth / 4) {
                                    if (isNextPage) {//左移动
                                        offsetHor = moveX - (viewWidth + moveX) * Float.parseFloat(o + "") / 100 - show * viewWidth + textPadingright + textPadingHor;
                                    } else {//右移动
                                        offsetHor = moveX + (viewWidth - moveX) * Float.parseFloat(o + "") / 100 - show * viewWidth + textPadingright + textPadingHor;
                                    }
                                } else {
                                    offsetHor = (1 - Float.parseFloat(o + "") / 100) * moveX - show * viewWidth + textPadingright + textPadingVar;
                                }
                                invalidate();
                                attemptClaimDrag(true);
                                if ((int) o == 100) {//动画执行完成后 重置移动距离
                                    if (Math.abs(s) > viewWidth / 4 || Math.abs(moveX) > viewWidth / 4) {
                                        if (isNextPage) {
                                            show++;
                                        } else {
                                            show--;
                                        }
                                    }
                                    moveX = 0;
                                    moveY = 0;
                                    if (scorllTextChangeListener != null) {
                                        scorllTextChangeListener.pageChange(show);
                                    }
                                }
                            }
                        });
                        return true;
                    } else {
                        moveX = 0;
                        moveY = 0;
                        attemptClaimDrag(false);
                        return false;
                    }
                }
            }
        }
        return super.onTouchEvent(ev);
    }

    private void attemptClaimDrag(Boolean b) {
        ViewParent parent = getParent();
        if (parent != null) {
            // 如果控件有父控件，那么请求父控件不要劫取事件
            // 以便此控件正常处理所有触摸事件
            // 而不是被父控件传入ACTION_CANCEL去截断事件
            parent.requestDisallowInterceptTouchEvent(b);
        }
    }

    public void setValueAnimal(final BackCall backCall) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 100);
        valueAnimator.setDuration(100);
        valueAnimator.setInterpolator(new AccelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                backCall.call((Integer) animation.getAnimatedValue());
            }
        });
        valueAnimator.start();
    }

    ScorllTextChangeListener scorllTextChangeListener;

    public void setTextChangetListener(ScorllTextChangeListener scorllTextChangeListener) {
        this.scorllTextChangeListener = scorllTextChangeListener;
    }

    public interface ScorllTextChangeListener {
        void pageChange(int page);

        void textEnd();

        void textStart();
    }


    public void addPageText() {

    }
}
