package com.xjy.widget.bounce;

import android.content.Context;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Scroller;

/**
 * User: Tom
 * Date: 2016-11-23
 * Time: 14:27
 * FIXME
 */
public abstract class CanRefreshLayout extends ViewGroup {

    public static String TAG = CanRefreshLayout.class.getSimpleName();

    //  默认刷新时间
    private static final int DEFAULT_DURATION = 300;

    //  自动刷新时间
    private static final int DEFAULT_AUTO_DURATION = 100;
    //    默认摩擦系数
    private static final float DEFAULT_FRICTION = 0.5f;
    //   刷新完成时，默认平滑滚动单位距离  除CLASSIC外有效
    private static final int DEFAULT_SMOOTH_LENGTH = 50;
    //   刷新完成时，默认平滑滚动单位时间 除CLASSIC外有效
    private static final int DEFAULT_SMOOTH_DURATION = 3;


    //  通过触摸判断滑动方向
    private static byte NO_SCROLL = 0;
    private static byte NO_SCROLL_UP = 1;
    private static byte NO_SCROLL_DOWN = 2;


    //  头部
    protected View mHeaderView;
    //  底部
    protected View mFooterView;
    //    内容
    protected View mContentView;
    //    头部高度
    protected int mHeaderHeight;
    //    底部高度
    protected int mFooterHeight;

    private boolean isSetHeaderHeight;
    private boolean isSetFooterHeight;


    //    摩擦系数
    private float mFriction = DEFAULT_FRICTION;
    //    是否可下拉
    private boolean mRefreshEnabled = true;
    //    是否可上拉
    private boolean mLoadMoreEnabled = true;
    //   下拉监听
    protected OnRefreshListener mOnRefreshListener;
    //    上拉监听
    protected OnLoadMoreListener mOnLoadMoreListener;

    //    刷新时间
    private int mDuration = DEFAULT_DURATION;
    //    平滑滚动单位距离  除CLASSIC外有效
    private int mSmoothLength = DEFAULT_SMOOTH_LENGTH;
    //    平滑滚动单位时间 除CLASSIC外有效
    private int mSmoothDuration = DEFAULT_SMOOTH_DURATION;


    //  不可滑动view的滑动方向
    private int isUpOrDown = NO_SCROLL;
    //  判断y轴方向的存储值
    float directionX;
    //   判断x轴方向存储值
    float directionY;
    //   下拉偏移
    private int mHeadOffY;
    //    上拉偏移
    private int mFootOffY;
    //    内容偏移
    private int mContentOffY;
    //  最后一次触摸的位置
    private float lastY;
    //  偏移
    private int currentOffSetY;
    //  触摸移动的位置
    private int offsetSum;
    //    触摸移动的位置之和
    private int scrollSum;
    //  一个缓存值
    private int tempY;

    private boolean isRefreshingOrLoadMoreing = false;

    private View mKeepView;

    private Scroller mScroller = new Scroller(getContext());

    private boolean isNoMoreData = false;


    public CanRefreshLayout(Context context) {
        this(context, null);
    }

    public CanRefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CanRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    /**
     * 自己设置高度时不使用自动获取高度
     *
     * @param mHeaderHeight
     */
    public void setHeaderHeight(int mHeaderHeight) {
        this.mHeaderHeight = mHeaderHeight;
        isSetHeaderHeight = true;
    }

    /**
     * 自己设置高度时不使用自动获取高度
     *
     * @param mFooterHeight
     */
    public void setFooterHeight(int mFooterHeight) {
        this.mFooterHeight = mFooterHeight;
        isSetFooterHeight = true;
    }

    /**
     * 设置是否可下拉刷新
     *
     * @param enable
     */
    public void setRefreshEnabled(boolean enable) {
        this.mRefreshEnabled = enable;
    }


    /**
     * 设置是否可上拉加载
     *
     * @param enable
     */
    public void setLoadMoreEnabled(boolean enable) {
        this.mLoadMoreEnabled = enable;
    }


    /**
     * 设置刷新监听
     *
     * @param mOnRefreshListener
     */
    public void setOnRefreshListener(@NonNull OnRefreshListener mOnRefreshListener) {
        this.mOnRefreshListener = mOnRefreshListener;
    }


    /**
     * 设置加载更多监听
     *
     * @param mOnLoadMoreListener
     */
    public void setOnLoadMoreListener(@NonNull OnLoadMoreListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;
    }


    /**
     * 设置摩擦系数
     *
     * @param mFriction
     */
    public void setFriction(@FloatRange(from = 0.0, to = 1.0) float mFriction) {

        this.mFriction = mFriction;

    }

    /**
     * 设置默认刷新时间
     *
     * @param mDuration
     */
    public void setDuration(int mDuration) {
        this.mDuration = mDuration;
    }

    /**
     * 设置滑动单位距离
     *
     * @param mSmoothLength
     */
    public void setSmoothLength(int mSmoothLength) {
        this.mSmoothLength = mSmoothLength;
    }

    /**
     * 设置滑动单位时间
     *
     * @param mSmoothDuration
     */
    public void setSmoothDuration(int mSmoothDuration) {
        this.mSmoothDuration = mSmoothDuration;
    }


    /**
     * 通过id得到相应的view
     */
    @Override
    protected void onFinishInflate() {
        final int childCount = getChildCount();

        if (childCount > 0) {
            mContentView = getChildAt(0);
        }

        if (mContentView == null) {
            throw new IllegalStateException("error");
        }
        super.onFinishInflate();
        onInflateHeaderFooter();
    }


    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        childLayout();
    }


    /**
     * 设置上拉下拉中间view的位置
     */
    private void childLayout() {

        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();

        if (mHeaderView != null) {
            MarginLayoutParams lp = (MarginLayoutParams) mHeaderView.getLayoutParams();
            final int left = paddingLeft + lp.leftMargin;
            final int top = paddingTop + lp.topMargin - mHeaderHeight + mHeadOffY;
            final int right = left + mHeaderView.getMeasuredWidth();
            final int bottom = top + mHeaderView.getMeasuredHeight();
            mHeaderView.layout(left, top, right, bottom);
        }

        if (mFooterView != null) {
            MarginLayoutParams lp = (MarginLayoutParams) mFooterView.getLayoutParams();
            final int left = paddingLeft + lp.leftMargin;
            final int top = getMeasuredHeight() + paddingTop + lp.topMargin - mFootOffY;
            final int right = left + mFooterView.getMeasuredWidth();
            final int bottom = top + mFooterView.getMeasuredHeight();
            mFooterView.layout(left, top, right, bottom);
        }

        if (mContentView != null) {
            MarginLayoutParams lp = (MarginLayoutParams) mContentView.getLayoutParams();
            final int left = paddingLeft + lp.leftMargin;
            final int top = paddingTop + lp.topMargin + mContentOffY;
            final int right = left + mContentView.getMeasuredWidth();
            final int bottom = top + mContentView.getMeasuredHeight();
            mContentView.layout(left, top, right, bottom);
        }

        if (getKeepView() != null){
            MarginLayoutParams lp = (MarginLayoutParams) mKeepView.getLayoutParams();
            final int left = paddingLeft + lp.leftMargin;
            final int top = paddingTop + lp.topMargin + mContentOffY;
            final int right = left + mKeepView.getMeasuredWidth();
            final int bottom = top + mKeepView.getMeasuredHeight();
            mKeepView.layout(left, top, right, bottom);
        }

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (mHeaderView != null) {
            measureChildWithMargins(mHeaderView, widthMeasureSpec, 0, heightMeasureSpec, 0);
            MarginLayoutParams lp = (MarginLayoutParams) mHeaderView.getLayoutParams();

            if (!isSetHeaderHeight) {
                mHeaderHeight = mHeaderView.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
            }
        }

        if (mFooterView != null) {
            measureChildWithMargins(mFooterView, widthMeasureSpec, 0, heightMeasureSpec, 0);
            MarginLayoutParams lp = (MarginLayoutParams) mFooterView.getLayoutParams();

            if (!isSetFooterHeight) {
                mFooterHeight = mFooterView.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
            }
        }

        if (mContentView != null) {
            measureChildWithMargins(mContentView, widthMeasureSpec, 0, heightMeasureSpec, 0);
        }

        if (getKeepView() != null){
            measureChildWithMargins(mKeepView, widthMeasureSpec, 0, heightMeasureSpec, 0);
        }


    }

    private View getKeepView(){
        if (mKeepView == null){
            mKeepView = findViewWithTag("Keep it");
        }
        return mKeepView;
    }


    /**
     * 能否刷新
     *
     * @return
     */
    private boolean canRefresh() {
        return mRefreshEnabled && mHeaderView != null && !canChildScrollUp() && !isRefreshingOrLoadMoreing;
    }

    /**
     * 能否加载更多
     *
     * @return
     */
    private boolean canLoadMore() {
        return mLoadMoreEnabled && mFooterView != null && !canChildScrollDown() && !isRefreshingOrLoadMoreing;
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        return super.dispatchTouchEvent(e);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                directionY = e.getY();
                directionX = e.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                if (directionY <= 0 || directionX <= 0) {
                    break;
                }

                float eventY = e.getY();
                float eventX = e.getX();

                float offY = eventY - directionY;
                float offX = eventX - directionX;

                directionY = eventY;
                directionX = eventX;

                boolean moved = Math.abs(offY) > Math.abs(offX);

                if (offY > 0 && moved && canRefresh()) {
                    isUpOrDown = NO_SCROLL_UP;
                } else if (offY < 0 && moved && canLoadMore()) {
                    isUpOrDown = NO_SCROLL_DOWN;
                } else {
                    isUpOrDown = NO_SCROLL;
                }

                if (isUpOrDown == NO_SCROLL_DOWN || isUpOrDown == NO_SCROLL_UP) {
                    return true;
                }
                break;
        }

        return super.onInterceptTouchEvent(e);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
//     当是不可滑动的view里进入
        if (!canChildScrollDown() && !canChildScrollUp()) {
            if (isUpOrDown == NO_SCROLL_UP) {
                if (canRefresh()) {
                    return touch(e, true);
                }
            } else if (isUpOrDown == NO_SCROLL_DOWN) {
                if (canLoadMore()) {
                    return touch(e, false);
                }
            } else {
                switch (e.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        directionY = e.getY();
                        directionX = e.getX();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (directionY <= 0 || directionX <= 0) {
                            break;
                        }

                        float eventY = e.getY();
                        float eventX = e.getX();

                        float offY = eventY - directionY;
                        float offX = eventX - directionX;

                        directionY = eventY;
                        directionX = eventX;

                        boolean moved = Math.abs(offY) > Math.abs(offX);

                        if (offY > 0 && moved && canRefresh()) {
                            isUpOrDown = NO_SCROLL_UP;
                        } else if (offY < 0 && moved && canLoadMore()) {
                            isUpOrDown = NO_SCROLL_DOWN;
                        } else {
                            isUpOrDown = NO_SCROLL;
                        }
                        break;
                }
                return true;
            }

        } else {
            if (canRefresh()) {
                return touch(e, true);
            } else if (canLoadMore()) {
                return touch(e, false);
            }
        }
        return super.onTouchEvent(e);
    }


    /**
     * 触摸滑动处理
     *
     * @param e
     * @param isHead
     * @return
     */
    private boolean touch(MotionEvent e, boolean isHead) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastY = e.getY();
                return true;
            case MotionEvent.ACTION_MOVE:
                if (lastY > 0) {
                    currentOffSetY = (int) (e.getY() - lastY);
                    offsetSum += currentOffSetY;
                }
                lastY = e.getY();

                boolean isCanMove;
                if (isHead) {
                    isCanMove = offsetSum > 0;
                } else {
                    isCanMove = offsetSum < 0;
                }

                if (isCanMove) {
                    float ratio = getRatio();
                    if (ratio < 0) {
                        ratio = 0;
                    }
                    int scrollNum = -((int) (currentOffSetY * ratio));
                    scrollSum += scrollNum;
                    if (isHead) {
                        smoothMove(true, true, scrollNum, scrollSum);
                        if (Math.abs(scrollSum) > mHeaderHeight) {
                            getHeaderInterface().onPrepare();
                        }
                        getHeaderInterface().onPositionChange(Math.abs(scrollSum) / (float) mHeaderHeight);
                    } else {
                        smoothMove(false, true, scrollNum, scrollSum);
                        if (Math.abs(scrollSum) > mFooterHeight) {
                            getFooterInterface().onPrepare();
                        }
                        getFooterInterface().onPositionChange(Math.abs(scrollSum) / (float) mFooterHeight);
                        if (isNoMoreData){
                            getFooterInterface().onNoMoreData();
                        }
                    }
                }
                return true;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (Math.abs(scrollSum) > 3) {
                    if (isHead) {
                        if (Math.abs(scrollSum) > mHeaderHeight) {
                            smoothMove(true, false, -mHeaderHeight, mHeaderHeight);
                            getHeaderInterface().onRelease();
                            refreshing();
                        } else {
                            smoothMove(true, false, 0, 0);
                        }
                    } else {
                        if (Math.abs(scrollSum) > mFooterHeight && !isNoMoreData) {
                            smoothMove(false, false, mContentView.getMeasuredHeight() - getMeasuredHeight() + mFooterHeight, mFooterHeight);
                            getFooterInterface().onRelease();
                            loadingMore();
                        } else {
                            smoothMove(false, false, mContentView.getMeasuredHeight() - getMeasuredHeight(), 0);
                            if (isNoMoreData){
                                getFooterInterface().onNoMoreData();
                            }
                        }
                    }
                }
                resetParameter();
                break;
        }
        return super.onTouchEvent(e);
    }

    /**
     * 滑动距离越大比率越小，越难拖动
     *
     * @return
     */
    private float getRatio() {
        return 1 - (Math.abs(offsetSum) / (float) getMeasuredHeight()) - 0.3f * mFriction;
    }


    /**
     * 重置参数
     */
    private void resetParameter() {

        directionX = 0;
        directionY = 0;
        isUpOrDown = NO_SCROLL;
        lastY = 0;
        offsetSum = 0;
        scrollSum = 0;

    }


    /**
     * * 滚动布局的方法
     *
     * @param isHeader
     * @param isMove      手指在移动还是已经抬起
     * @param moveScrollY
     * @param moveY
     */
    private void smoothMove(boolean isHeader, boolean isMove, int moveScrollY, int moveY) {
        if (isHeader) {
            if (isMove) {
                smoothScrollBy(0, moveScrollY);
            } else {
                smoothScrollTo(0, moveScrollY);
            }
        } else {
            if (isMove) {
                smoothScrollBy(0, moveScrollY);
            } else {
                smoothScrollTo(0, moveScrollY);
            }

        }


    }

    /**
     * 调用此方法滚动到目标位置
     *
     * @param fx
     * @param fy
     */
    public void smoothScrollTo(int fx, int fy) {
        int dx = fx - mScroller.getFinalX();
        int dy = fy - mScroller.getFinalY();
        smoothScrollBy(dx, dy);
    }

    /**
     * 调用此方法设置滚动的相对偏移
     *
     * @param dx
     * @param dy
     */
    public void smoothScrollBy(int dx, int dy) {
        mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(), dx, dy);
        invalidate();
    }


    /**
     * 通过设置偏移滚动到目标位置
     *
     * @param isHeader
     * @param isMove
     * @param moveY
     */
    private void layoutMove(boolean isHeader, boolean isMove, int moveY) {

        if (isMove) {
//            if (isHeader) {
//                if (mHeadStyle == UPPER) {
//                    mHeadOffY = moveY;
//                } else if (mHeadStyle == LOWER) {
//                    mHeadOffY = mHeaderHeight;
//                    mContentOffY = moveY;
//                } else if (mHeadStyle == MID) {
//                    int offY = moveY / 2 + mHeaderHeight / 2;
//                    mHeadOffY = offY;
//                    mContentOffY = moveY;
//                }
//            } else {
//                if (mFootStyle == UPPER) {
//                    mFootOffY = moveY;
//                } else if (mFootStyle == LOWER) {
//                    mFootOffY = mFooterHeight;
//                    mContentOffY = -moveY;
//                } else if (mFootStyle == MID) {
//                    int offY = moveY / 2 + mFooterHeight / 2;
//                    mFootOffY = offY;
//                    mContentOffY = -moveY;
//                }
//            }
        } else {
            if (isHeader) {
                layoutMoveSmooth(isHeader, moveY, mHeaderHeight);
            } else {
                layoutMoveSmooth(isHeader, moveY, mFooterHeight);
            }
        }

        requestLayout();
    }

    private void layoutMoveSmooth(boolean isHeader, int moveY, int mHeight) {

        if (moveY == mHeight) {

            tempY = Math.abs(scrollSum);
            layoutSmoothMove(isHeader, moveY);

        } else if (moveY == 0) {

            tempY = mHeight;
            layoutSmoothMove(isHeader, moveY);


        }

    }


    private void layoutSmoothMove(final boolean isHeader, final int moveY) {

        tempY -= mSmoothLength;

        if (tempY <= moveY) {

            layoutMove(isHeader, true, moveY);
            return;
        }


        layoutMove(isHeader, true, tempY);


        postDelayed(new Runnable() {
            @Override
            public void run() {
                layoutSmoothMove(isHeader, moveY);
            }
        }, mSmoothDuration);


    }


    /**
     * 刷新完成
     */
    public void refreshComplete() {
        isRefreshingOrLoadMoreing = false;
        getHeaderInterface().onComplete();
        postDelayed(new Runnable() {
            @Override
            public void run() {
                smoothMove(true, false, 0, 0);
                getHeaderInterface().onReset();
            }
        }, mDuration);
    }

    public void closeHeader(){
        smoothMove(true, false, 0, 0);
        getHeaderInterface().onComplete();
        getHeaderInterface().onReset();
    }

    public void closeFooter(){
        smoothMove(false, false, mContentView.getMeasuredHeight() - getMeasuredHeight(), 0);
        getFooterInterface().onComplete();
        getFooterInterface().onReset();
    }

    /**
     * 加载更多完成
     */
    public void loadMoreComplete() {
        isRefreshingOrLoadMoreing = false;
        getFooterInterface().onComplete();
        postDelayed(new Runnable() {
            @Override
            public void run() {
                smoothMove(false, false, mContentView.getMeasuredHeight() - getMeasuredHeight(), 0);
                getFooterInterface().onReset();
            }
        }, mDuration);

    }

    /**
     * 自动刷新
     */
    public void autoRefresh() {

        if (mHeaderView != null) {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    smoothMove(true, false, -mHeaderHeight, -mHeaderHeight);
                    getHeaderInterface().onRelease();
                    refreshing();
                }
            }, DEFAULT_AUTO_DURATION);


        }


    }


    private void refreshing() {
        if (mOnRefreshListener != null) {
            isRefreshingOrLoadMoreing = true;
            mOnRefreshListener.onRefresh();
            setNoMoreData(false);
        }

    }

    private void loadingMore() {
        if (mOnLoadMoreListener != null) {
            isRefreshingOrLoadMoreing = true;
            mOnLoadMoreListener.onLoadMore();
        }

    }


    @Override
    public void computeScroll() {

        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }

        super.computeScroll();
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p != null && p instanceof LayoutParams;
    }

    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new ViewGroup.LayoutParams(getContext(), attrs);
    }


    public static class LayoutParams extends MarginLayoutParams {

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        @SuppressWarnings({"unused"})
        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }


    private CanRefresh getHeaderInterface() {
        return (CanRefresh) mHeaderView;
    }

    private CanRefresh getFooterInterface() {
        return (CanRefresh) mFooterView;
    }


    /**
     * 是否能下拉
     *
     * @return
     */
    protected boolean canChildScrollUp() {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (mContentView instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) mContentView;
                return absListView.getChildCount() > 0
                        && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0)
                        .getTop() < absListView.getPaddingTop());
            } else {
                return ViewCompat.canScrollVertically(mContentView, -1) || mContentView.getScrollY() > 0;
            }
        } else {
            return ViewCompat.canScrollVertically(mContentView, -1);
        }
    }


    /**
     * 是否能上拉
     *
     * @return
     */
    protected boolean canChildScrollDown() {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (mContentView instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) mContentView;
                return absListView.getChildCount() > 0
                        && (absListView.getLastVisiblePosition() < absListView.getChildCount() - 1
                        || absListView.getChildAt(absListView.getChildCount() - 1).getBottom() > absListView.getPaddingBottom());
            } else {
                return ViewCompat.canScrollVertically(mContentView, 1) || mContentView.getScrollY() < 0;
            }
        } else {
            return ViewCompat.canScrollVertically(mContentView, 1);
        }
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public interface OnRefreshListener {
        void onRefresh();
    }

    public View getFooterView() {
        return mFooterView;
    }

    public void setFooterView(View footerView) {
        mFooterView = footerView;
        getFooterInterface().setIsHeaderOrFooter(false);
        addView(mFooterView);
        mFooterView.bringToFront();
    }

    public View getHeaderView() {
        return mHeaderView;
    }

    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        getHeaderInterface().setIsHeaderOrFooter(true);
        addView(mHeaderView);
        mHeaderView.bringToFront();
    }

    public View getContentView() {
        return mContentView;
    }

    public void setContentView(View contentView) {
        mContentView = contentView;

    }

    public boolean isNoMoreData() {
        return isNoMoreData;
    }

    public void setNoMoreData(boolean noMoreData) {
        isNoMoreData = noMoreData;
    }

    public abstract void onInflateHeaderFooter();
}