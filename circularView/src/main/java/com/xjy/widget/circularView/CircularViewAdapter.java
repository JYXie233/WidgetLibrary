package com.xjy.widget.circularView;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Tom
 * Date: 2016-11-26
 * Time: 14:11
 * FIXME
 */
public class CircularViewAdapter extends PagerAdapter implements View.OnClickListener {

    private List<String> mUrls;
    private List<String> mTitles;
    private List<CircularChildView> mCircularViews;
    private Context mContext;

    public CircularViewAdapter(Context context) {
        mContext = context;
        mUrls = new ArrayList<>();
        mCircularViews = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mUrls.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (mCircularViews.get(position).getParent() == null)
            container.addView(mCircularViews.get(position));
        if (mTitles == null) {
            mCircularViews.get(position).textView.setVisibility(View.GONE);
        }
        return mCircularViews.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        container.removeView(mCircularViews.get(position));
    }

    public void setUrls(List<String> urls) {
        mUrls = new ArrayList<>();
        if (urls != null && urls.size() > 1) {
            mUrls.add(0, urls.get(urls.size() - 1));
            mUrls.addAll(urls);
            mUrls.add(urls.get(0));
        }
        int count = mUrls.size();
        mCircularViews.clear();
        while (count > 0) {
            CircularChildView circularView = new CircularChildView(mContext);
            circularView.setOnClickListener(this);
            mCircularViews.add(circularView);
            count--;
        }
    }

    public List<String> getUrls() {
        return mUrls;
    }

    public ImageView getImageView(int position) {
        return mCircularViews.get(position).imageView;
    }

    public TextView getTextView(int position) {
        return mCircularViews.get(position).textView;
    }

    public List<String> getTitles() {
        return mTitles;
    }

    public void setTitles(List<String> titles) {
        mTitles = titles;
        if (mTitles != null && mTitles.size() > 1) {
            mTitles.add(0, mTitles.get(mTitles.size() - 1));
            mTitles.add(mTitles.get(1));
        }

    }

    @Override
    public void onClick(View view) {
        if (onItemClickListener != null) {
            onItemClickListener.onClick(view);
        }
    }

    private View.OnClickListener onItemClickListener;

    public void setOnItemClickListener(View.OnClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}