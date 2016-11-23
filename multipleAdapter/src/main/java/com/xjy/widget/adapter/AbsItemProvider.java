package com.xjy.widget.adapter;

import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Tom
 * Date: 2016-11-18
 * Time: 09:46
 * FIXME
 */
public abstract class AbsItemProvider<M, VH extends MultipleViewHolder> extends AbsBaseProvider<M, VH> implements OnProviderItemClickListener<AbsItemProvider<M, VH>>, OnProviderLongClickListener<AbsItemProvider<M, VH>>, MultipleViewHolder.OnHolderLongClickListener, MultipleViewHolder.OnHolderClickListener{

    private List<M> mDataList;



    private OnProviderItemClickListener<AbsItemProvider<M, VH>> mOnProviderClickListener;

    private OnProviderLongClickListener<AbsItemProvider<M, VH>> mOnProviderLongClickListener;

    private Map<Integer, OnProviderItemClickListener<AbsItemProvider<M, VH>>> mOnMultipleItemClickListenerMap;

    private Map<Integer, OnProviderLongClickListener<AbsItemProvider<M, VH>>> mOnMultipleItemLongClickListenerMap;

    private AbsHeaderFooterProvider mHeaderProvider;
    private AbsHeaderFooterProvider mFooterProvider;

    private Object mHeaderData;

    private Object mFooterData;

    private boolean expand = true;

    public AbsItemProvider() {
        mDataList = new ArrayList<>();
    }


    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        VH multipleViewHolder = super.onCreateViewHolder(parent, viewType);
        multipleViewHolder.setOnHolderClickListener(new MultipleViewHolder.OnHolderClickListener() {
            @Override
            public void onHolderClick(MultipleViewHolder holder, View childView) {
                if (mOnProviderClickListener != null)
                    mOnProviderClickListener.onProviderClick(AbsItemProvider.this, childView, holder.getAdapterPosition() - getStartNum());
            }
        });
        multipleViewHolder.setOnHolderLongClickListener(new MultipleViewHolder.OnHolderLongClickListener() {
            @Override
            public boolean onHolderLongClick(MultipleViewHolder holder, View childView) {
                if (mOnProviderLongClickListener != null)
                    return mOnProviderLongClickListener.onProviderLongClick(AbsItemProvider.this, childView, holder.getAdapterPosition() - getStartNum());
                return false;
            }
        });

        if (mOnMultipleItemClickListenerMap != null){
            for (int id : mOnMultipleItemClickListenerMap.keySet()){
                multipleViewHolder.setClickListener(id, this);
            }
        }

        if (mOnMultipleItemLongClickListenerMap != null){
            for (int id : mOnMultipleItemLongClickListenerMap.keySet()){
                multipleViewHolder.setLongClickListener(id, this);
            }
        }


        return multipleViewHolder;
    }

    @Override
    public void onHolderClick(MultipleViewHolder holder, View childView) {
        if (mOnMultipleItemClickListenerMap != null){
            mOnMultipleItemClickListenerMap.get(childView.getId()).onProviderClick(this, childView, holder.getAdapterPosition() - getStartNum());
        }
    }

    @Override
    public boolean onHolderLongClick(MultipleViewHolder holder, View childView) {
        if (mOnMultipleItemLongClickListenerMap != null){
            return mOnMultipleItemLongClickListenerMap.get(childView.getId()).onProviderLongClick(this, childView, holder.getAdapterPosition() - getStartNum());
        }
        return false;
    }

    @Override
    public void onProviderClick(AbsItemProvider itemProvider, View view, int position) {
        if (mOnMultipleItemClickListenerMap != null) {
            mOnMultipleItemClickListenerMap.get(view.getId()).onProviderClick(AbsItemProvider.this, view, position - getStartNum());
        }
    }

    @Override
    public boolean onProviderLongClick(AbsItemProvider itemProvider, View view, int position) {
        if (mOnMultipleItemLongClickListenerMap != null) {
            return mOnMultipleItemLongClickListenerMap.get(view.getId()).onProviderLongClick(AbsItemProvider.this, view, position - getStartNum());
        }
        return false;
    }


    public OnProviderItemClickListener getOnProviderClickListener() {
        return mOnProviderClickListener;
    }

    public AbsItemProvider setOnProviderClickListener(OnProviderItemClickListener<AbsItemProvider<M, VH>> onProviderClickListener) {
        mOnProviderClickListener = onProviderClickListener;
        return this;
    }

    public OnProviderLongClickListener getOnProviderLongClickListener() {
        return mOnProviderLongClickListener;
    }

    public void setOnProviderLongClickListener(OnProviderLongClickListener<AbsItemProvider<M, VH>> onProviderLongClickListener) {
        mOnProviderLongClickListener = onProviderLongClickListener;
    }

    public AbsItemProvider<M, VH> setOnClickViewListener(int viewId, OnProviderItemClickListener<AbsItemProvider<M, VH>> listener) {
        if (mOnMultipleItemClickListenerMap == null){
            mOnMultipleItemClickListenerMap = new HashMap<>();
        }
        mOnMultipleItemClickListenerMap.put(viewId, listener);
        return this;
    }

    public AbsItemProvider<M, VH> setOnLongClickViewListener(int viewId, OnProviderLongClickListener<AbsItemProvider<M, VH>> listener) {
        if (mOnMultipleItemLongClickListenerMap == null){
            mOnMultipleItemLongClickListenerMap = new HashMap<>();
        }
        mOnMultipleItemLongClickListenerMap.put(viewId, listener);
        return this;
    }

    public Map<Integer, OnProviderItemClickListener<AbsItemProvider<M, VH>>> getOnMultipleItemClickListenerMap() {
        return mOnMultipleItemClickListenerMap;
    }

    public Map<Integer, OnProviderLongClickListener<AbsItemProvider<M, VH>>> getOnMultipleItemLongClickListenerMap() {
        return mOnMultipleItemLongClickListenerMap;
    }

    public AbsItemProvider add(M itemType) {
        mDataList.add(itemType);
        return this;
    }

    public void addAll(List<M> list) {
        mDataList.addAll(list);
    }

    public void clear() {
        mDataList.clear();
    }

    public void remove(int location) {
        mDataList.remove(location);
    }

    public void remove(M itemType) {
        mDataList.remove(itemType);
    }


    /**
     * 当expand为false的时候，size返回0
     * @return
     */
    public int size() {
        if (!isExpand()) {
            return 0;
        }
        return mDataList.size();
    }

    /**
     * 不受expand影响
     * @return
     */
    public int reallySize(){
        return mDataList.size();
    }

    public M get(int position) {
        if (position == -1)
            return null;
        return mDataList.get(position);
    }

    public List<M> getDataList(){
        return mDataList;
    }


    public <H> void registerHeaderProvider(H h, AbsHeaderFooterProvider<H> headerProvider){
        mHeaderData = h;
        mHeaderProvider = headerProvider;
    }

    public <H> void registerFooterProvider(H h, AbsHeaderFooterProvider<H> footerProvider){
        mFooterData = h;
        mFooterProvider = footerProvider;
    }

    public AbsHeaderFooterProvider getHeaderProvider() {
        return mHeaderProvider;
    }

    public AbsHeaderFooterProvider getFooterProvider() {
        return mFooterProvider;
    }

    public Object getHeaderData() {
        return mHeaderData;
    }

    public Object getFooterData() {
        return mFooterData;
    }

    public boolean isExpand() {
        return expand;
    }

    public void setExpand(boolean expand) {
        this.expand = expand;
    }

}