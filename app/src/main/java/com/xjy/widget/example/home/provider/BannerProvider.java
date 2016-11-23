package com.xjy.widget.example.home.provider;


import com.xjy.widget.adapter.ItemProvider;
import com.xjy.widget.adapter.MultipleViewHolder;
import com.xjy.widget.example.R;

/**
 * User: Tom
 * Date: 2016-11-18
 * Time: 17:31
 * FIXME
 */
public class BannerProvider extends ItemProvider<String> {
    @Override
    public int onInflateLayout() {
        return R.layout.item_home_banner;
    }

    @Override
    public void onBindViewHolder(MultipleViewHolder viewHolder, int position, String item) {
        viewHolder.setText(R.id.textView, item);
    }
}