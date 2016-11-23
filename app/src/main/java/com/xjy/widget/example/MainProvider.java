package com.xjy.widget.example;


import com.xjy.widget.adapter.ItemProvider;
import com.xjy.widget.adapter.ItemProviderActionHelper;
import com.xjy.widget.adapter.MultipleViewHolder;

/**
 * User: Tom
 * Date: 2016-11-16
 * Time: 15:33
 * FIXME
 */
public class MainProvider extends ItemProvider<Model> implements ItemProviderActionHelper {

    public MainProvider() {
        setSpanSize(2);
    }

    @Override
    public int onInflateLayout() {
        return R.layout.item_2;
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