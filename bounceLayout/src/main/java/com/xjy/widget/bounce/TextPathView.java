package com.xjy.widget.bounce;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

/**
 * User: Tom
 * Date: 2016-12-16
 * Time: 15:10
 * FIXME
 */
public class TextPathView extends View{

    private Paint mForePaint;
    private Paint mBackPaint;

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
        String text = "nihao";
        canvas.drawText(text, 0, 0, getBackPaint());
        canvas.drawText(text, 0, 0, getForePaint());
        Path p = new Path();
        TextPaint
    }

    public Paint getForePaint() {
        if (mForePaint == null) {
            mForePaint = new Paint();
            mForePaint.setColor(Color.RED);
        }
        return mForePaint;
    }

    public Paint getBackPaint() {
        if (mBackPaint == null){
            mBackPaint = new Paint();
            mBackPaint.setColor(Color.BLACK);
        }
        return mBackPaint;
    }
}