package com.xjy.widget.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.animation.AnimatorListenerCompat;
import android.support.v4.animation.AnimatorUpdateListenerCompat;
import android.support.v4.animation.ValueAnimatorCompat;
import android.support.v4.view.ViewConfigurationCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

/**
 * User: Tom
 * Date: 2016-11-24
 * Time: 15:25
 * FIXME
 */
public class SlidingLayout extends RelativeLayout implements View.OnClickListener{

    private View mContentView;

    private View mActionView;

    private boolean isOpen = false;

    private int mCanScrollWidth = 0;

    private ViewDragHelper mDragger;

    private OnClickListener mReallyOnClickListener;

    public SlidingLayout(Context context) {
        this(context, null);
    }

    public SlidingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDragger = ViewDragHelper.create(this, 0.1f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return child.equals(mContentView);
            }
            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                Log.d("move", "left:" + left + " dx:" + dx);
                if (Math.abs(left) < mCanScrollWidth * 2){
                    return left;
                }else {
                    return left > 0 ? mCanScrollWidth * 2 : - mCanScrollWidth * 2;
                }

            }

            //手指释放的时候回调
            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel)
            {
                //mAutoBackView手指释放时可以自动回去
                if (releasedChild.equals(mContentView)) {
                    if (isOpen){
                        mDragger.settleCapturedViewAt(0, releasedChild.getTop());
                        isOpen = false;
                    }else {
                        if (releasedChild.getLeft() < 0 && Math.abs(releasedChild.getLeft()) > mCanScrollWidth / 2) {
                            mDragger.settleCapturedViewAt(-mCanScrollWidth, releasedChild.getTop());
                            isOpen = true;
                        } else {
                            mDragger.settleCapturedViewAt(0, releasedChild.getTop());
                            isOpen = false;
                        }
                    }
                    invalidate();
                }
            }
            @Override
            public int getViewHorizontalDragRange(View child) {
                return getMeasuredWidth()-child.getMeasuredWidth();
            }

            @Override
            public int getViewVerticalDragRange(View child) {
                return getMeasuredHeight()-child.getMeasuredHeight();
            }

        });

    }

    @Override
    public void computeScroll() {
        if(mDragger.continueSettling(true)) {
            invalidate();
        }
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (mContentView == null){
            mContentView = getChildAt(0);
            mContentView.setClickable(true);
        }

        if (mActionView == null){
            setActionView(getChildAt(1));
        }
        RelativeLayout.LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        mActionView.setLayoutParams(layoutParams);
        mContentView.bringToFront();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mCanScrollWidth = mActionView.getMeasuredWidth();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event)
    {
        return mDragger.shouldInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        mDragger.processTouchEvent(event);
        return true;
    }

    public View getContentView() {
        return mContentView;
    }

    public void setContentView(View contentView) {
        mContentView = contentView;
    }

    public View getActionView() {
        return mActionView;
    }

    public void setActionView(View actionView) {
        mActionView = actionView;
        RelativeLayout.LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        mActionView.setLayoutParams(layoutParams);
        mContentView.bringToFront();
    }

    public static SlidingLayout build(Context context, int contentLayout, int actionLayout){
        SlidingLayout slidingLayout = new SlidingLayout(context);
        View actionView = LayoutInflater.from(context).inflate(actionLayout, slidingLayout, false);
        View contentView = LayoutInflater.from(context).inflate(contentLayout, slidingLayout, false);
        contentView.setClickable(true);
        slidingLayout.addView(actionView);
        slidingLayout.addView(contentView);
        slidingLayout.setContentView(contentView);
        slidingLayout.setActionView(actionView);
        return slidingLayout;
    }

    @Override
    public void onClick(View view) {
        if (isOpen){
            mContentView.bringToFront();
            isOpen = false;
        }else {
            mReallyOnClickListener.onClick(view);
        }
    }

//    @Override
//    public void setOnClickListener(OnClickListener l) {
//        super.setOnClickListener(this);
//        mReallyOnClickListener = l;
//    }
}