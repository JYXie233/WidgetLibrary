package com.xjy.widget.circularView;

import android.widget.ImageView;

/**
 * User: Tom
 * Date: 2016-11-26
 * Time: 14:34
 * FIXME
 */
public abstract class ICircularImageLoader {
    public abstract void onImageLoad(ImageView imageView, String url, String title, int position);
}  