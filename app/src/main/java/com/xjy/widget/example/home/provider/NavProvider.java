package com.xjy.widget.example.home.provider;


import android.view.ViewGroup;

import com.xjy.widget.adapter.ItemProvider;
import com.xjy.widget.adapter.MultipleViewHolder;
import com.xjy.widget.example.Model;
import com.xjy.widget.example.R;

/**
 * User: Tom
 * Date: 2016-11-18
 * Time: 17:32
 * FIXME
 */
public class NavProvider extends ItemProvider<Model> {

    @Override
    public MultipleViewHolder onCreateViewHolder(ViewGroup parent) {
        return new MultipleViewHolder(parent, R.layout.item_home_nav);
    }

    @Override
    public void onBindViewHolder(MultipleViewHolder viewHolder, int position, Model item) {
        viewHolder.setText(R.id.textView, item.name);
    }

}