package com.xjy.widget.example;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.xjy.widget.adapter.MultipleViewHolder;

/**
 * User: Tom
 * Date: 2016-12-13
 * Time: 16:24
 * FIXME
 */
public class MainViewHolder extends MultipleViewHolder{

    public MainViewHolder(ViewGroup parent, int layoutId) {
        super(parent, layoutId);
    }

    public MainViewHolder(Context context, int contentLayout, int actionLayout) {
        super(context, contentLayout, actionLayout);
    }

    public MainViewHolder(View itemView) {
        super(itemView);
    }
}