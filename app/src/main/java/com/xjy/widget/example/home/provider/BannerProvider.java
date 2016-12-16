package com.xjy.widget.example.home.provider;


import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.xjy.widget.adapter.ItemProvider;
import com.xjy.widget.adapter.MultipleViewHolder;
import com.xjy.widget.circularView.CircularView;
import com.xjy.widget.circularView.OnCircularViewClickListener;
import com.xjy.widget.example.MainActivity;
import com.xjy.widget.example.R;
import com.xjy.widget.example.animation.ZoomOutPageTransformer;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Tom
 * Date: 2016-11-18
 * Time: 17:31
 * FIXME
 */
public class BannerProvider extends ItemProvider<String> {

    @Override
    public MultipleViewHolder onCreateViewHolder(ViewGroup parent) {
        return new MultipleViewHolder(parent, R.layout.item_home_banner);
    }

    @Override
    public void onBindViewHolder(MultipleViewHolder viewHolder, int position, String item) {
        CircularView circularView = viewHolder.findViewById(R.id.circularView);
        circularView.setPageTransformer(true, new ZoomOutPageTransformer());
        List<String> url = new ArrayList<>();
        url.add("https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1480142597&di=ec90aa4f0b282e9ed9f40d2b404c8049&src=http://www.hongyiart.com/upload/image/20151216/20151216111822_30596.jpg");
        url.add("http://img1.imgtn.bdimg.com/it/u=1089582262,166446285&fm=21&gp=0.jpg");
        url.add("http://e.hiphotos.baidu.com/baike/pic/item/500fd9f9d72a6059d1248e1c2a34349b033bba84.jpg");
        circularView.setUrls(url);
        circularView.setTitles(url);
        circularView.notifyDataSetChanged();
        circularView.setOnCircularViewClickListener(new OnCircularViewClickListener() {
            @Override
            public void onCircularViewClicked(View view, int position) {

            }
        });
    }
}