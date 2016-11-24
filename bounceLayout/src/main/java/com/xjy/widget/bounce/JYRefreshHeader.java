package com.xjy.widget.bounce;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * User: Tom
 * Date: 2016-11-24
 * Time: 11:16
 * FIXME
 */
public class JYRefreshHeader extends FrameLayout implements CanRefresh{

    private String mLastUpdateTime = "未刷新";

    private ProgressBar mProgressBar;

    private TextView mStatusTextView;

    private TextView mTimeTextView;

    private ImageView mArrow;

    private boolean mIsHeader;

    public JYRefreshHeader(Context context) {
        super(context);
        setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        LayoutInflater.from(context).inflate(R.layout.jy_refresh_header, this, true);
        mArrow = (ImageView) findViewById(R.id.arrow);
        mStatusTextView = (TextView) findViewById(R.id.status_text_view);
        mTimeTextView = (TextView) findViewById(R.id.time_text_view);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    @Override
    public void onReset() {
        mStatusTextView.setText(mIsHeader ? "下拉刷新" : "加载更多");
        mTimeTextView.setText(mLastUpdateTime);
        mTimeTextView.setVisibility(mIsHeader ? VISIBLE : GONE);
        mProgressBar.setVisibility(GONE);
        mArrow.setVisibility(mIsHeader ? VISIBLE : GONE);
    }

    @Override
    public void onPrepare() {
        mStatusTextView.setText(mIsHeader ? "松开刷新":"松开加载更多");
        mTimeTextView.setText(mLastUpdateTime);
        mProgressBar.setVisibility(GONE);
        mArrow.setVisibility(mIsHeader ? VISIBLE : GONE);
        mTimeTextView.setVisibility(mIsHeader ? VISIBLE : GONE);
    }

    @Override
    public void onRelease() {
        mStatusTextView.setText(mIsHeader ? "刷新中":"加载中");
        mTimeTextView.setText(mLastUpdateTime);
        mProgressBar.setVisibility(VISIBLE);
        mArrow.setVisibility(GONE);
    }

    @Override
    public void onComplete() {
        mStatusTextView.setText(mIsHeader ? "刷新成功":"加载成功");
        mTimeTextView.setText(mLastUpdateTime);
        mProgressBar.setVisibility(GONE);
        mArrow.setVisibility(GONE);
        mTimeTextView.setVisibility(mIsHeader ? VISIBLE : GONE);
        mLastUpdateTime = DateUtils.convertTimeToFormat(System.currentTimeMillis());
    }

    @Override
    public void onPositionChange(float currentPercent) {
        mArrow.clearAnimation();
        mArrow.animate().rotation(currentPercent > 1.0 ? 180 : 0).start();

        mStatusTextView.setText(mIsHeader ? currentPercent > 1.0 ? "松开刷新":"下拉刷新" : "松开加载更多");
    }

    @Override
    public void setIsHeaderOrFooter(boolean isHeader) {
        mIsHeader = isHeader;
    }
}