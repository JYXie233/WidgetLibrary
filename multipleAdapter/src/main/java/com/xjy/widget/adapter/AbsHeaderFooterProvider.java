package com.xjy.widget.adapter;

import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Tom
 * Date: 2016-11-18
 * Time: 16:04
 * FIXME
 */
public abstract class AbsHeaderFooterProvider<M> extends AbsBaseProvider<M, MultipleViewHolder> implements OnProviderItemClickListener, OnProviderLongClickListener, MultipleViewHolder.OnHolderLongClickListener, MultipleViewHolder.OnHolderClickListener{
    private SparseIntArray mPositionSection;

    private HashMap<Integer, M> mHeaderData;
    private HashMap<Integer, M> mFooterData;

    private OnProviderItemClickListener mOnProviderClickListener;

    private OnProviderLongClickListener mOnProviderLongClickListener;

    private Map<Integer, OnProviderItemClickListener> mOnMultipleItemClickListenerMap;

    private Map<Integer, OnProviderLongClickListener> mOnMultipleItemLongClickListenerMap;

    public AbsHeaderFooterProvider() {

        mPositionSection = new SparseIntArray();
        mHeaderData = new HashMap<>();
        mFooterData = new HashMap<>();
    }

    @Override
    public MultipleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MultipleViewHolder holder = super.onCreateViewHolder(parent, viewType);
        holder.setOnHolderClickListener(new MultipleViewHolder.OnHolderClickListener() {
            @Override
            public void onHolderClick(MultipleViewHolder holder, View childView) {
                if (getOnProviderClickListener() != null) {
                    int section = (int) holder.itemView.getTag(R.id.jy_section);
                    getOnProviderClickListener().onProviderClick(holder, childView, section);
                }
            }
        });

        holder.setOnHolderLongClickListener(new MultipleViewHolder.OnHolderLongClickListener() {
            @Override
            public boolean onHolderLongClick(MultipleViewHolder holder, View childView) {
                if (getOnProviderLongClickListener() != null) {
                    int section = (int) holder.itemView.getTag(R.id.jy_section);
                    return getOnProviderLongClickListener().onProviderLongClick(childView, section);
                }
                return false;
            }
        });

        if (getOnMultipleItemClickListenerMap() != null){
            for (int id : getOnMultipleItemClickListenerMap().keySet()){
                holder.setClickListener(id, this);
            }
        }

        if (getOnMultipleItemClickListenerMap() != null){
            for (int id : getOnMultipleItemClickListenerMap().keySet()){
                holder.setLongClickListener(id, this);
            }
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(MultipleViewHolder viewHolder, int position, M item) {
        viewHolder.itemView.setTag(R.id.jy_section, position);
        onBindHeaderFooterHolder(viewHolder, position, item);
    }

    public abstract void onBindHeaderFooterHolder(MultipleViewHolder viewHolder, int section, M item);

    @Override
    public void onProviderClick(MultipleViewHolder holder, View view, int position) {
        if (getOnMultipleItemClickListenerMap() != null) {
            int section = (int) holder.itemView.getTag(R.id.jy_section);
            getOnMultipleItemClickListenerMap().get(view.getId()).onProviderClick(holder, view, section);
        }
    }

    @Override
    public boolean onProviderLongClick(View view, int position) {
        if (getOnMultipleItemLongClickListenerMap() != null) {
            return getOnMultipleItemLongClickListenerMap().get(view.getId()).onProviderLongClick(view, getSection(position));
        }
        return false;
    }

    @Override
    public void onHolderClick(MultipleViewHolder holder, View childView) {
        if (mOnMultipleItemClickListenerMap != null){
            mOnMultipleItemClickListenerMap.get(childView.getId()).onProviderClick(holder, childView, holder.getAdapterPosition() - getStartNum());
        }
    }

    @Override
    public boolean onHolderLongClick(MultipleViewHolder holder, View childView) {
        if (mOnMultipleItemLongClickListenerMap != null){
            return mOnMultipleItemLongClickListenerMap.get(childView.getId()).onProviderLongClick(childView, holder.getAdapterPosition() - getStartNum());
        }
        return false;
    }

    public void put(int position, int section){
        mPositionSection.put(position, section);
    }

    public void putHeader(M m, int section){
        mHeaderData.put(section, m);
    }

    public void putFooter(M m, int section){
        mFooterData.put(section, m);
    }

    public int getSection(int position){
        return mPositionSection.get(position);
    }

    public M getHeaderData(int section){
        return mHeaderData.get(section);
    }

    public M getFooterData(int section){
        return mFooterData.get(section);
    }

    public Map<Integer, OnProviderItemClickListener> getOnMultipleItemClickListenerMap() {
        if (mOnMultipleItemClickListenerMap == null){
            mOnMultipleItemClickListenerMap = new HashMap<>();
        }
        return mOnMultipleItemClickListenerMap;
    }

    public void setOnMultipleItemClickListenerMap(Map<Integer, OnProviderItemClickListener> onMultipleItemClickListenerMap) {
        mOnMultipleItemClickListenerMap = onMultipleItemClickListenerMap;
    }

    public Map<Integer, OnProviderLongClickListener> getOnMultipleItemLongClickListenerMap() {
        if (mOnMultipleItemLongClickListenerMap== null){
            mOnMultipleItemLongClickListenerMap = new HashMap<>();
        }
        return mOnMultipleItemLongClickListenerMap;
    }

    public void setOnMultipleItemLongClickListenerMap(Map<Integer, OnProviderLongClickListener> onMultipleItemLongClickListenerMap) {
        mOnMultipleItemLongClickListenerMap = onMultipleItemLongClickListenerMap;
    }

    public OnProviderItemClickListener getOnProviderClickListener() {
        return mOnProviderClickListener;
    }

    public void setOnProviderClickListener(OnProviderItemClickListener onProviderClickListener) {
        mOnProviderClickListener = onProviderClickListener;
    }

    public OnProviderLongClickListener getOnProviderLongClickListener() {
        return mOnProviderLongClickListener;
    }

    public void setOnProviderLongClickListener(OnProviderLongClickListener onProviderLongClickListener) {
        mOnProviderLongClickListener = onProviderLongClickListener;
    }
}