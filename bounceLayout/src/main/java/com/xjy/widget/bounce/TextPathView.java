package com.xjy.widget.bounce;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.List;


/**
 * User: Tom
 * Date: 2016-12-16
 * Time: 15:10
 * FIXME
 */
public class TextPathView extends View {

    private static final String TAG = "TextPathView";

    private Paint mForePaint;

    private Paint mTextPaint;

    private String text;

    private List<Path> mPaths;

    private boolean mIsRunning = false;

    private float value = 0f;

    private float[] mCurrentPosition = new float[2];

    public TextPathView(Context context) {
        super(context);
    }

    public TextPathView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextPathView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Path path : mPaths) {
            getTextPaint().setAlpha((int) (value * 255));
            getForePaint().setAlpha((int) ((1 - value) * 255));
            canvas.drawPath(path, getTextPaint());
            final PathMeasure pathMeasure = new PathMeasure(path, true);
            pathMeasure.getPosTan(value * pathMeasure.getLength(), mCurrentPosition, null);
            canvas.drawCircle(mCurrentPosition[0], mCurrentPosition[1], 6, getForePaint());
        }



    }

    public Paint getTextPaint() {
        if (mTextPaint == null) {
            mTextPaint = new Paint();
            mTextPaint.setColor(Color.BLACK);
            mTextPaint.setAntiAlias(true);
            mTextPaint.setTextSize(100f);
        }
        return mTextPaint;
    }

    public Paint getForePaint() {
        if (mForePaint == null) {
            mForePaint = new Paint();
            mForePaint.setAntiAlias(true);
            mForePaint.setColor(Color.BLACK);
            mForePaint.setTextSize(100f);
        }
        return mForePaint;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        mPaths = new ArrayList<>();
        float start = 0f;
        Rect rect = new Rect();
        for (char c : text.toCharArray()){
            Path path = getPath(String.valueOf(c), start);
            getForePaint().getTextBounds(String.valueOf(c), 0, 1, rect);
            start += rect.width() + 10;
            mPaths.add(path);
        }
        startPathAnim();
    }

    private Path getPath(String text, float start){
        Path path = new Path();
        getForePaint().getTextPath(text, 0, text.length(), start, getForePaint().getFontSpacing(), path);
        path.close();
        return path;
    }

    // 开启路径动画
    public void startPathAnim() {

        // 0 － getLength()
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1.0f);
        valueAnimator.setDuration(1000);
        valueAnimator.setRepeatCount(Integer.MAX_VALUE);
        valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
        // 减速插值器
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                TextPathView.this.value = value;
                postInvalidate();
            }
        });
        valueAnimator.start();


    }
}