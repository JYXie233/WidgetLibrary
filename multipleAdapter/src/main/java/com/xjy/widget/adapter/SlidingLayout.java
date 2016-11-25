package com.xjy.widget.adapter;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * User: Tom
 * Date: 2016-11-24
 * Time: 15:25
 * FIXME
 */
public class SlidingLayout extends RelativeLayout implements View.OnClickListener {

    private View mContentView;

    private View mActionView;

    private boolean isOpen = false;

    private int mCanScrollWidth = 0;

    private ViewDragHelper mDragger;

    private OnClickListener mContentViewOnClickListener;

    private SlidingLayoutListener mSlidingLayoutListener;

    public SlidingLayout(Context context) {
        this(context, null);
    }

    public SlidingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDragger = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return child.equals(mContentView);
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                Log.d("move", "left:" + left + " dx:" + dx);
                if (Math.abs(left) < mCanScrollWidth * 2) {
                    return left;
                } else {
                    return left > 0 ? mCanScrollWidth * 2 : -mCanScrollWidth * 2;
                }

            }

            //手指释放的时候回调
            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                //mAutoBackView手指释放时可以自动回去
                if (releasedChild.equals(mContentView)) {

                    if (releasedChild.getLeft() < 0 && Math.abs(releasedChild.getLeft()) > mCanScrollWidth / 2) {
                        mDragger.settleCapturedViewAt(-mCanScrollWidth, releasedChild.getTop());
                        isOpen = true;
                        if (mSlidingLayoutListener != null)
                            mSlidingLayoutListener.onActionLayoutOpen();
                    } else {
                        mDragger.settleCapturedViewAt(0, releasedChild.getTop());
                        isOpen = false;
                        if (mSlidingLayoutListener != null)
                            mSlidingLayoutListener.onActionLayoutClose();
                    }

                    invalidate();
                }
            }

            @Override
            public int getViewHorizontalDragRange(View child) {
//                return getMeasuredWidth()-child.getMeasuredWidth();
                return child == mContentView ? child.getWidth() : 0;
            }


            @Override
            public int getViewVerticalDragRange(View child) {
//                return getMeasuredHeight()-child.getMeasuredHeight();
                return child == mContentView ? child.getHeight() : 0;
            }

        });

    }

    @Override
    public void computeScroll() {
        if (mDragger.continueSettling(true)) {
            invalidate();
        }
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (mContentView == null) {
            mContentView = getChildAt(0);
            mContentView.setClickable(true);
            mContentView.setOnClickListener(this);
        }

        if (mActionView == null) {
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
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return mDragger.shouldInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
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


    @Override
    public void onClick(View view) {
        if (isOpen) {
            closeActionLayout();
        } else {
            if (mContentViewOnClickListener != null)
                mContentViewOnClickListener.onClick(view);
        }
    }

    public OnClickListener getContentViewOnClickListener() {
        return mContentViewOnClickListener;
    }

    public void setContentViewOnClickListener(OnClickListener contentViewOnClickListener) {
        mContentViewOnClickListener = contentViewOnClickListener;
        getContentView().setOnClickListener(this);
    }

    public void closeActionLayout() {
        toggleActionLayout(false);
    }

    public void openActionLayout() {
        toggleActionLayout(true);
    }

    public void toggleActionLayout() {
        toggleActionLayout(!isOpen);
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void toggleActionLayout(boolean open) {
        mDragger.smoothSlideViewTo(mContentView, open ? mCanScrollWidth : 0, mContentView.getTop());
        ViewCompat.postInvalidateOnAnimation(this);
        isOpen = !isOpen;
        if (mSlidingLayoutListener != null) {
            if (isOpen) {
                mSlidingLayoutListener.onActionLayoutOpen();
            } else {
                mSlidingLayoutListener.onActionLayoutClose();
            }

        }
    }

    public SlidingLayoutListener getSlidingLayoutListener() {
        return mSlidingLayoutListener;
    }

    public void setSlidingLayoutListener(SlidingLayoutListener slidingLayoutListener) {
        mSlidingLayoutListener = slidingLayoutListener;
    }

    public static SlidingLayout build(Context context, int contentLayout, int actionLayout) {
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


}