package com.xjy.widget.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * User: Tom
 * Date: 2016-11-18
 * Time: 16:27
 * FIXME
 */
public abstract class AbsBaseProvider<M,VH extends MultipleViewHolder>{
    private Context mContext;

    private int mStartNum = 0;

    private int mSpanSize = 1;

    private boolean isKeep = false;

    private RecyclerView.Adapter mAdapter;

    public AbsBaseProvider() {

    }

    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        if (onInflateActionLayout() == 0) {
            VH multipleViewHolder = (VH) new MultipleViewHolder(LayoutInflater.from(getContext()).inflate(onInflateLayout(), parent, false));
            multipleViewHolder.setKeep(isKeep());
            return multipleViewHolder;
        }else{
            VH multipleViewHolder = (VH) new MultipleViewHolder(mContext, onInflateLayout(), onInflateActionLayout());
            multipleViewHolder.setKeep(isKeep());
            return multipleViewHolder;
        }
    }

    public void attachAdapter(RecyclerView.Adapter adapter){
        mAdapter = adapter;
    }

    public abstract int onInflateLayout();

    public abstract void onBindViewHolder(VH viewHolder, int position, M item);

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context context) {
        mContext = context;
    }

    public int getStartNum() {
        return mStartNum;
    }

    public void setStartNum(int startNum) {
        mStartNum = startNum;
    }

    public int getSpanSize() {
        return mSpanSize;
    }

    public void setSpanSize(int spanSize) {
        mSpanSize = spanSize;
    }

    public boolean isKeep() {
        return isKeep;
    }

    public void setKeep(boolean keep) {
        isKeep = keep;
    }

    public int onInflateActionLayout(){
        return 0;
    }

    public void notifyItemChanged(int position){
        if (mAdapter != null){
            mAdapter.notifyItemChanged(position);
        }
    }
}