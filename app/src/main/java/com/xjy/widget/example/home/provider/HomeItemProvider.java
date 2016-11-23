package com.xjy.widget.example.home.provider;


import com.xjy.widget.adapter.ItemProvider;
import com.xjy.widget.adapter.ItemProviderActionHelper;
import com.xjy.widget.adapter.MultipleViewHolder;
import com.xjy.widget.example.Model;
import com.xjy.widget.example.R;

/**
 * User: Tom
 * Date: 2016-11-18
 * Time: 17:33
 * FIXME
 */
public class HomeItemProvider extends ItemProvider<Model> implements ItemProviderActionHelper {
    @Override
    public int onInflateLayout() {
        return R.layout.item_home_item;
    }

    @Override
    public void onBindViewHolder(MultipleViewHolder viewHolder, int position, Model item) {
        viewHolder.setText(R.id.textView, item.name);
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