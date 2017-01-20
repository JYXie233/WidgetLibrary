package com.xjy.widget.example;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.xjy.widget.adapter.AbsItemProvider;
import com.xjy.widget.adapter.MultipleAdapter;
import com.xjy.widget.adapter.MultipleViewHolder;
import com.xjy.widget.adapter.OnProviderItemClickListener;
import com.xjy.widget.adapter.OnProviderLongClickListener;
import com.xjy.widget.bounce.BounceLayout;
import com.xjy.widget.bounce.TextPathView;
import com.xjy.widget.circularView.CircularView;
import com.xjy.widget.circularView.ICircularImageLoader;
import com.xjy.widget.circularView.OnCircularViewClickListener;
import com.xjy.widget.example.contact.ContactActivity;
import com.xjy.widget.example.home.HomeActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements OnProviderItemClickListener {

    private MultipleAdapter<MainViewHolder> mMultipleAdapter;

    MainProvider mMainProvider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        mMultipleAdapter = new MultipleAdapter<MainViewHolder>(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mMainProvider = new MainProvider();
        mMainProvider.add(new Model("常见首页效果+JYRefreshLayout", HomeActivity.class));
        mMainProvider.add(new Model("联系人效果+BounceLayout", ContactActivity.class));


        mMultipleAdapter.registerProvider(mMainProvider);
        recyclerView.setAdapter(mMultipleAdapter);
        mMainProvider.setOnProviderClickListener(this);

        mMainProvider.setOnClickViewListener(R.id.action, new OnProviderItemClickListener<AbsItemProvider<Model, MainViewHolder>>() {
            @Override
            public void onProviderClick(AbsItemProvider<Model, MainViewHolder> provider, MultipleViewHolder viewHolder, View view, int position) {
                Toast.makeText(MainActivity.this, "Action Click And Close", Toast.LENGTH_SHORT).show();
                viewHolder.closeActionLayout();
            }
        });


        CircularView.installImageLoader(new ICircularImageLoader() {
            @Override
            public void onImageLoad(ImageView imageView, String url, String title, int position) {
                loadImage(imageView, url);
            }
        });

        TextPathView textPathView = (TextPathView) findViewById(R.id.textView);
        textPathView.setText("EYEVISION");

    }

    @Override
    public void onProviderClick(Object provider, MultipleViewHolder viewHolder, View view, int position) {
        Class clazz = mMainProvider.get(position).clazz;
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }


    private void loadImage(final ImageView imageView, final String urlStr){
        new Thread(new Runnable() {
            @Override
            public void run() {
                InputStream is = null;
                try {
                    URL url = new URL(urlStr);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    //设置超时的时间，5000毫秒即5秒
                    httpURLConnection.setConnectTimeout(5000);
                    //设置获取图片的方式为GET
                    httpURLConnection.setRequestMethod("GET");
                    //响应码为200，则访问成功
                    if (httpURLConnection.getResponseCode() == 200) {
                        //获取连接的输入流，这个输入流就是图片的输入流
                        is = httpURLConnection.getInputStream();
                        final Bitmap bitmap = BitmapFactory.decodeStream(is);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                imageView.setImageBitmap(bitmap);
                            }
                        });
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    try {
                        if (is != null) {
                            is.close();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }


}
