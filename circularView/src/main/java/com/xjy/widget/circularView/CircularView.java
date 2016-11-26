package com.xjy.widget.circularView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import java.util.List;

/**
 * User: Tom
 * Date: 2016-11-26
 * Time: 14:10
 * FIXME
 */
public class CircularView extends FrameLayout implements ViewPager.OnPageChangeListener, View.OnClickListener {

    private static ICircularImageLoader sCircularImageLoader;

    private WrapContentHeightViewPager mViewPager;

    private CircularViewAdapter mViewAdapter;

    private OnCircularViewClickListener mOnCircularViewClickListener;

    private boolean mRunning = true;

    private boolean mAutoTurn = true;
    private boolean mAutoTurnThread = true;

    private boolean mIsTouching = false;

    private boolean mReCreateThread = true;

    private int mIntervalTime = 4000;

    public CircularView(Context context) {
        this(context, null);
    }

    public CircularView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_circular_parent, this, true);
        mViewPager = (WrapContentHeightViewPager) findViewById(R.id.viewPager);
        mViewAdapter = new CircularViewAdapter(context);
        mViewPager.setAdapter(mViewAdapter);
        mViewPager.addOnPageChangeListener(this);
        mViewAdapter.setOnItemClickListener(this);
    }

    public void setUrls(List<String> urls) {
        mViewAdapter.setUrls(urls);
        if (urls != null) {
            onPageSelected(0);
            if (urls.size() > 1)
                mViewPager.setCurrentItem(1, false);
        }

    }

    public List<String> getUrls() {
        return mViewAdapter.getUrls();
    }

    public List<String> getTitles() {
        return mViewAdapter.getTitles();
    }

    public void setTitles(List<String> titles) {
        mViewAdapter.setTitles(titles);
        if (mViewAdapter.getTitles() != null) {
            int position = 0;
            if (mViewAdapter.getTitles().size() > 1)
                position++;
            mViewAdapter.getTextView(position).setText(mViewAdapter.getTitles().get(position));
        }
    }

    public static void installImageLoader(ICircularImageLoader iCircularImageLoader) {
        sCircularImageLoader = iCircularImageLoader;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        sCircularImageLoader.onImageLoad(mViewAdapter.getImageView(position), mViewAdapter.getUrls().get(position), null, position);
        if (mViewAdapter.getTitles() != null)
            mViewAdapter.getTextView(position).setText(mViewAdapter.getTitles().get(position));
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == ViewPager.SCROLL_STATE_IDLE) {
            if (mViewPager.getCurrentItem() == 0) {
                mViewPager.setCurrentItem(mViewAdapter.getCount() - 2, false);
            } else if (mViewPager.getCurrentItem() == mViewAdapter.getCount() - 1) {
                mViewPager.setCurrentItem(1, false);
            }
            mIsTouching = false;
        } else {
            mIsTouching = true;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        drawCycle(canvas);
    }

    private void drawCycle(Canvas canvas) {
        canvas.save();
        canvas.translate(getScrollX(), getScrollY());
        int count = 0;
        if (mViewAdapter != null) {
            count = mViewAdapter.getUrls().size();
            if (count > 1) {
                count -= 2;
            }
        }
        int select = mViewPager.getCurrentItem();
        if (select == 1) {
            select = 0;
        } else if (select == mViewAdapter.getCount() - 2) {
            select = count - 1;
        } else {
            select -= 1;
        }
        float density = getContext().getResources().getDisplayMetrics().density;
        int itemWidth = (int) (6 * density);
        int itemHeight = itemWidth / 2;
        int x = (getWidth() - count * itemWidth) / 2;
        int y = getHeight() - itemWidth;
        int minItemHeight = (int) ((float) itemHeight * 0.8F);
        Paint paint = new Paint();
        paint.setAntiAlias(true);

        paint.setStyle(Paint.Style.FILL);
        for (int i = 0; i < count; i++) {
            if (select == i) {
                paint.setColor(0xFFbdbdbd);
                canvas.drawCircle(x + itemWidth * i + itemWidth / 2, y, minItemHeight, paint);
            } else {
                paint.setColor(0xFFe6e6e6);
                canvas.drawCircle(x + itemWidth * i + itemWidth / 2, y, minItemHeight, paint);
            }
        }
        canvas.restore();
    }

    public void notifyDataSetChanged() {
        mViewAdapter.notifyDataSetChanged();
        postInvalidate();
    }

    @Override
    public void onClick(View view) {
        if (mOnCircularViewClickListener != null) {
            mOnCircularViewClickListener.onCircularViewClicked(view, mViewAdapter.getCount() > 1 ? mViewPager.getCurrentItem() - 1 : mViewPager.getCurrentItem());
        }
    }

    public OnCircularViewClickListener getOnCircularViewClickListener() {
        return mOnCircularViewClickListener;
    }

    public void setOnCircularViewClickListener(OnCircularViewClickListener onCircularViewClickListener) {
        mOnCircularViewClickListener = onCircularViewClickListener;
    }

    public boolean isRunning() {
        return mRunning;
    }

    public void setRunning(boolean running) {
        mRunning = running;
    }

    public boolean isAutoTurn() {
        return mAutoTurn;
    }

    public void setAutoTurn(boolean autoTurn) {
        mAutoTurn = autoTurn;
        mAutoTurnThread = mAutoTurn;
    }

    private void start() {
        if (mReCreateThread) {
            mReCreateThread = false;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (mRunning) {
                        while (mAutoTurnThread) {
                            try {
                                Thread.sleep(mIntervalTime);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            if (!mIsTouching)
                                post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, true);
                                    }
                                });
                        }
                    }
                    mReCreateThread = true;
                }
            }).start();
        }
    }

    public int getIntervalTime() {
        return mIntervalTime;
    }

    public void setIntervalTime(int intervalTime) {
        mIntervalTime = intervalTime;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mAutoTurnThread = false;
        mRunning = false;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mViewAdapter.notifyDataSetChanged();
        mAutoTurnThread = mAutoTurn;
        mRunning = true;
        start();
    }

    public void setPageTransformer(boolean reverseDrawingOrder, ViewPager.PageTransformer transformer){
        mViewPager.setPageTransformer(reverseDrawingOrder, transformer);
    }
}