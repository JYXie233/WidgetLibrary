package com.xjy.widget.bounce;

import android.content.Context;
import android.util.AttributeSet;

/**
 * User: Tom
 * Date: 2016-11-23
 * Time: 17:41
 * FIXME
 */
public class JYRefreshLayout extends EventDispenseLayout {

    public JYRefreshLayout(Context context) {
        this(context, null);
    }

    public JYRefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JYRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @Override
    public void onInflateHeaderFooter() {

    }
}  