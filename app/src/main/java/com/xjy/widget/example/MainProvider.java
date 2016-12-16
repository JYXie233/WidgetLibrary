package com.xjy.widget.example;


import android.view.ViewGroup;

import com.xjy.widget.adapter.AbsItemProvider;
import com.xjy.widget.adapter.ItemProvider;
import com.xjy.widget.adapter.ItemProviderActionHelper;
import com.xjy.widget.adapter.MultipleViewHolder;

/**
 * User: Tom
 * Date: 2016-11-16
 * Time: 15:33
 * FIXME
 */
public class MainProvider extends AbsItemProvider<Model, MainViewHolder> implements ItemProviderActionHelper {

    public MainProvider() {
        setSpanSize(2);
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent) {
        return new MainViewHolder(parent.getContext(), R.layout.item_main, R.layout.item_action_view);
    }

    @Override
    public void onBindViewHolder(MainViewHolder viewHolder, int position, Model item) {
        viewHolder.setText(R.id.textView, item.name);
    }

    @Override
    public int onInflateActionLayout() {
        return R.layout.item_action_view;
    }

    @Override
    public boolean isItemCanSwipe(int position) {
        return false;
    }

    @Override
    public boolean isItemCanMove(int position) {
        return true;
    }

    @Override
    public boolean onItemSwipe(int position) {
        return false;
    }

    @Override
    public boolean onItemMove(int oldPosition, int newPosition) {
        return false;
    }
}