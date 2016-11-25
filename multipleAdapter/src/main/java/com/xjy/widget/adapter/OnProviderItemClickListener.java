package com.xjy.widget.adapter;

import android.view.View;

/**
 * User: Tom
 * Date: 2016-11-16
 * Time: 15:19
 * FIXME
 */
public interface OnProviderItemClickListener<T> {
    void onProviderClick(T provider, MultipleViewHolder viewHolder, View view, int position);
}  