package com.xjy.widget.adapter;


import android.view.View;

/**
 * User: Tom
 * Date: 2016-11-16
 * Time: 15:28
 * FIXME
 */
public interface OnProviderLongClickListener<T> {
    boolean onProviderLongClick(T provider, View view, int position);
}  