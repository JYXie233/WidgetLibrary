package com.xjy.widget.example.home;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.xjy.widget.adapter.AbsHeaderFooterProvider;
import com.xjy.widget.adapter.AbsItemProvider;
import com.xjy.widget.adapter.MultipleAdapter;
import com.xjy.widget.adapter.MultipleViewHolder;
import com.xjy.widget.adapter.OnProviderItemClickListener;
import com.xjy.widget.bounce.CanRefreshLayout;
import com.xjy.widget.bounce.JYRefreshLayout;
import com.xjy.widget.example.Model;
import com.xjy.widget.example.R;
import com.xjy.widget.example.home.provider.BannerProvider;
import com.xjy.widget.example.home.provider.HomeItemProvider;
import com.xjy.widget.example.home.provider.NavProvider;


public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        final MultipleAdapter multipleAdapter = new MultipleAdapter(this);

        BannerProvider bannerProvider = new BannerProvider();
        multipleAdapter.registerProvider(bannerProvider);
        bannerProvider.add("banner");

        NavProvider navProvider = new NavProvider();
        navProvider.setSpanSize(4);
        multipleAdapter.registerProvider(navProvider);
        navProvider.add(new Model("nav1"));
        navProvider.add(new Model("nav2"));
        navProvider.add(new Model("nav3"));
        navProvider.add(new Model("nav4"));

        HomeItemProvider homeItemProvider = new HomeItemProvider();
        homeItemProvider.setSpanSize(3);
        homeItemProvider.add(new Model("item1"));
        homeItemProvider.add(new Model("item2"));
        homeItemProvider.add(new Model("item3"));
        homeItemProvider.add(new Model("item4"));
        homeItemProvider.add(new Model("item5"));
        homeItemProvider.add(new Model("item6"));
        homeItemProvider.add(new Model("item7"));
        homeItemProvider.add(new Model("item8"));

        AbsHeaderFooterProvider<String> header = new AbsHeaderFooterProvider<String>() {
            @Override
            public MultipleViewHolder onCreateViewHolder(ViewGroup parent) {
                return new MultipleViewHolder(parent, R.layout.item_home_header);
            }

            @Override
            public void onBindHeaderFooterHolder(MultipleViewHolder viewHolder, int position, String item) {
                viewHolder.setText(R.id.textView, item);
            }
        };

        header.setKeep(true);
        header.setOnProviderClickListener(new OnProviderItemClickListener() {
            @Override
            public void onProviderClick(Object provider, MultipleViewHolder viewHolder, View view, int position) {
                Toast.makeText(HomeActivity.this, "header section:" + position, Toast.LENGTH_SHORT).show();
                multipleAdapter.toggleExpand(position);
            }
        });

        homeItemProvider.registerHeaderProvider("Header", header);

        multipleAdapter.registerProvider(homeItemProvider);



        HomeItemProvider singleLineProvider = new HomeItemProvider();
        singleLineProvider.add(new Model("item1"));
        singleLineProvider.add(new Model("item2"));
        singleLineProvider.add(new Model("item3"));
        singleLineProvider.add(new Model("item4"));
        singleLineProvider.add(new Model("item5"));
        singleLineProvider.add(new Model("item6"));
        singleLineProvider.add(new Model("item7"));
        singleLineProvider.add(new Model("item8"));
        singleLineProvider.add(new Model("item1"));
        singleLineProvider.add(new Model("item2"));
        singleLineProvider.add(new Model("item3"));
        singleLineProvider.add(new Model("item4"));
        singleLineProvider.add(new Model("item5"));
        singleLineProvider.add(new Model("item6"));
        singleLineProvider.add(new Model("item7"));
        singleLineProvider.add(new Model("item8"));
        singleLineProvider.registerHeaderProvider("Header2", header);
        multipleAdapter.registerProvider(singleLineProvider);

        HomeItemProvider homeItemProvider1 = new HomeItemProvider();
        homeItemProvider1.setSpanSize(3);
        homeItemProvider1.add(new Model("item1"));
        homeItemProvider1.add(new Model("item2"));
        homeItemProvider1.add(new Model("item3"));
        homeItemProvider1.add(new Model("item4"));
        homeItemProvider1.add(new Model("item5"));
        homeItemProvider1.add(new Model("item6"));
        homeItemProvider1.add(new Model("item7"));
        homeItemProvider1.add(new Model("item8"));
        multipleAdapter.registerProvider(homeItemProvider1);

        recyclerView.setAdapter(multipleAdapter);

        multipleAdapter.notifyDataSetChanged();

        bannerProvider.setOnProviderClickListener(new OnProviderItemClickListener<AbsItemProvider<String, MultipleViewHolder>>() {
            @Override
            public void onProviderClick(AbsItemProvider<String, MultipleViewHolder> provider, MultipleViewHolder viewHolder, View view, int position) {

            }
        });


        homeItemProvider.setOnClickViewListener(R.id.textView, new OnProviderItemClickListener<AbsItemProvider<Model, MultipleViewHolder>>() {
            @Override
            public void onProviderClick(AbsItemProvider<Model, MultipleViewHolder> provider, MultipleViewHolder viewHolder, View view, int position) {

            }
        });

        navProvider.setOnProviderClickListener(new OnProviderItemClickListener<AbsItemProvider<Model, MultipleViewHolder>>() {
            @Override
            public void onProviderClick(AbsItemProvider<Model, MultipleViewHolder> provider, MultipleViewHolder viewHolder, View view, int position) {

            }
        });

        homeItemProvider.setOnProviderClickListener(new OnProviderItemClickListener<AbsItemProvider<Model, MultipleViewHolder>>() {
            @Override
            public void onProviderClick(AbsItemProvider<Model, MultipleViewHolder> provider, MultipleViewHolder viewHolder, View view, int position) {
                Toast.makeText(HomeActivity.this, "multipleItem" + position, Toast.LENGTH_SHORT).show();
            }
        });

        singleLineProvider.setOnProviderClickListener(new OnProviderItemClickListener<AbsItemProvider<Model, MultipleViewHolder>>() {
            @Override
            public void onProviderClick(AbsItemProvider<Model, MultipleViewHolder> provider, MultipleViewHolder viewHolder, View view, int position) {
                Toast.makeText(HomeActivity.this, "singleItem" + position, Toast.LENGTH_SHORT).show();
            }
        });

        final JYRefreshLayout refreshLayout = (JYRefreshLayout) findViewById(R.id.refreshLayout);
        refreshLayout.autoRefresh();
        refreshLayout.setOnRefreshListener(new CanRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.refreshComplete();
                    }
                }, 2000);
            }
        });

        refreshLayout.setOnLoadMoreListener(new CanRefreshLayout.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.loadMoreComplete();
                        refreshLayout.setNoMoreData(true);
                    }
                }, 2000);
            }
        });



    }

}
