package com.xjy.widget.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

/**
 * User: Tom
 * Date: 2016-11-16
 * Time: 15:10
 * FIXME
 */
public class MultipleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

    private SparseArray<View> mViews;

    private OnHolderClickListener mOnHolderClickListener;
    private OnHolderLongClickListener mOnHolderLongClickListener;

    private boolean isKeep = false;

    public MultipleViewHolder(Context context, int contentLayout, int actionLayout){
        this(SlidingLayout.build(context, contentLayout, actionLayout));
    }

    public MultipleViewHolder(View itemView) {
        super(itemView);
        if (itemView instanceof SlidingLayout){
            SlidingLayout layout = (SlidingLayout) itemView;
            layout.getContentView().setOnClickListener(this);
            layout.getContentView().setOnLongClickListener(this);
//            this.itemView.setOnClickListener(this);
//            this.itemView.setOnLongClickListener(this);
        }else {
            this.itemView.setOnClickListener(this);
            this.itemView.setOnLongClickListener(this);
        }
        mViews = new SparseArray<View>();
    }

    private <T extends View> T findViewById(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public MultipleViewHolder setText(int viewId, String value) {
        TextView view = findViewById(viewId);
        view.setText(value);
        return this;
    }

    public MultipleViewHolder setBackground(int viewId, int resId) {
        View view = findViewById(viewId);
        view.setBackgroundResource(resId);
        return this;
    }

    public MultipleViewHolder setClickListener(int viewId, final OnHolderClickListener onHolderClickListener) {
        View view = findViewById(viewId);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onHolderClickListener.onHolderClick(MultipleViewHolder.this, view);
            }
        });
        return this;
    }

    public MultipleViewHolder setLongClickListener(int viewId, final OnHolderLongClickListener onLongClickListener) {
        View view = findViewById(viewId);
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return onLongClickListener.onHolderLongClick(MultipleViewHolder.this, view);
            }
        });
        return this;
    }

    @Override
    public void onClick(View view) {
        if (mOnHolderClickListener != null){
            mOnHolderClickListener.onHolderClick(this, view);
        }
    }

    @Override
    public boolean onLongClick(View view) {
        if (mOnHolderLongClickListener != null){
            return mOnHolderLongClickListener.onHolderLongClick(this, view);
        }
        return false;
    }

    public OnHolderClickListener getOnHolderClickListener() {
        return mOnHolderClickListener;
    }

    public void setOnHolderClickListener(OnHolderClickListener onHolderClickListener) {
        mOnHolderClickListener = onHolderClickListener;
    }

    public OnHolderLongClickListener getOnHolderLongClickListener() {
        return mOnHolderLongClickListener;
    }

    public void setOnHolderLongClickListener(OnHolderLongClickListener onHolderLongClickListener) {
        mOnHolderLongClickListener = onHolderLongClickListener;
    }

    public interface OnHolderClickListener{
        void onHolderClick(MultipleViewHolder holder, View childView);
    }

    public interface OnHolderLongClickListener{
        boolean onHolderLongClick(MultipleViewHolder holder, View childView);
    }

    public boolean isKeep() {
        return isKeep;
    }

    public void setKeep(boolean keep) {
        isKeep = keep;
    }
}