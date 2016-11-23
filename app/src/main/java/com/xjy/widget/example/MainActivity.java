package com.xjy.widget.example;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xjy.widget.adapter.AbsItemProvider;
import com.xjy.widget.adapter.MultipleAdapter;
import com.xjy.widget.adapter.MultipleViewHolder;
import com.xjy.widget.adapter.OnProviderItemClickListener;
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
        mMainProvider.add(new Model("常见首页效果", HomeActivity.class));
        mMainProvider.add(new Model("联系人效果", ContactActivity.class));


        mMultipleAdapter.registerProvider(mMainProvider);

        recyclerView.setAdapter(mMultipleAdapter);


        mMainProvider.setOnProviderClickListener(this);

    }

    @Override
    public void onProviderClick(AbsItemProvider<Model, MultipleViewHolder> provider, View view, int position) {
        Class clazz = mMainProvider.get(position).clazz;
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    public void reset(View view){
        for (int i = mMainProvider.size() - 1; i > 2; i --){
            mMainProvider.remove(i);
        }
        mMultipleAdapter.notifyDataSetChanged();
    }

    public void add(View view){
        for (int i = 0 ; i < 12; i ++){
            mMainProvider.add(new Model("联系人效果" + i, ContactActivity.class));
        }
        mMultipleAdapter.notifyDataSetChanged();
    }
}