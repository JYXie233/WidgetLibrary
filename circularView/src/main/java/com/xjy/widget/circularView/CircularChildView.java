package com.xjy.widget.circularView;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * User: Tom
 * Date: 2016-11-26
 * Time: 14:15
 * FIXME
 */
public class CircularChildView extends LinearLayout{

    public ImageView imageView;

    public TextView textView;

    public CircularChildView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.layout_circular_child, this, true);
        imageView = (ImageView) findViewById(R.id.imageView);
        textView = (TextView) findViewById(R.id.textView);
    }
}