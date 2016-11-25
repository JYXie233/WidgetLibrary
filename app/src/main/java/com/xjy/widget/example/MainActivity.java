package com.xjy.widget.example;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.xjy.widget.adapter.AbsItemProvider;
import com.xjy.widget.adapter.MultipleAdapter;
import com.xjy.widget.adapter.MultipleViewHolder;
import com.xjy.widget.adapter.OnProviderItemClickListener;
import com.xjy.widget.adapter.OnProviderLongClickListener;
import com.xjy.widget.bounce.BounceLayout;
import com.xjy.widget.example.contact.ContactActivity;
import com.xjy.widget.example.home.HomeActivity;


public class MainActivity extends AppCompatActivity implements OnProviderItemClickListener<AbsItemProvider<Model, MultipleViewHolder>> {

    private MultipleAdapter mMultipleAdapter;

    MainProvider mMainProvider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        mMultipleAdapter = new MultipleAdapter(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mMainProvider = new MainProvider();
        mMainProvider.add(new Model("常见首页效果+JYRefreshLayout", HomeActivity.class));
        mMainProvider.add(new Model("联系人效果+BounceLayout", ContactActivity.class));


        mMultipleAdapter.registerProvider(mMainProvider);
        recyclerView.setAdapter(mMultipleAdapter);
        mMainProvider.setOnProviderClickListener(this);

        mMainProvider.setOnClickViewListener(R.id.action, new OnProviderItemClickListener<AbsItemProvider<Model, MultipleViewHolder>>() {
            @Override
            public void onProviderClick(AbsItemProvider<Model, MultipleViewHolder> provider, MultipleViewHolder holder, View view, int position) {
                Toast.makeText(MainActivity.this, "Action Click And Close", Toast.LENGTH_SHORT).show();
                holder.closeActionLayout();
            }
        });

        mMainProvider.setOnClickViewListener(R.id.action2, new OnProviderItemClickListener<AbsItemProvider<Model, MultipleViewHolder>>() {
            @Override
            public void onProviderClick(AbsItemProvider<Model, MultipleViewHolder> provider, MultipleViewHolder holder, View view, int position) {
                Toast.makeText(MainActivity.this, "Action Just Click", Toast.LENGTH_SHORT).show();
            }
        });

        mMainProvider.setOnProviderLongClickListener(new OnProviderLongClickListener<AbsItemProvider<Model, MultipleViewHolder>>() {
            @Override
            public boolean onProviderLongClick(AbsItemProvider<Model, MultipleViewHolder> provider, View view, int position) {
                Toast.makeText(MainActivity.this, "Long Click" + position, Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    @Override
    public void onProviderClick(AbsItemProvider<Model, MultipleViewHolder> provider, MultipleViewHolder holder, View view, int position) {
        Class clazz = mMainProvider.get(position).clazz;
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

}
