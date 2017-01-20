package com.xjy.widget.bounce;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * User: Tom
 * Date: 2016-11-23
 * Time: 16:47
 * FIXME
 */
public class BounceLayout extends CanRefreshLayout {

    private static int headerBackgroundColor = 0x00000000;

    private static int footerBackgroundColor = 0x00000000;

    private static int textColor = 0xFF000000;

    private static String headerText = "BOUNCELAYOUT HEADER";

    private static String footerText = "BOUNCELAYOUT FOOTER";

    public BounceLayout(Context context) {
        this(context, null);
    }

    public BounceLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BounceLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onInflateHeaderFooter() {
        HeaderOrFooter header = new HeaderOrFooter(getContext(), R.layout.jy_header_footer);
        header.setBackgroundColor(headerBackgroundColor);
        TextPathView textView = (TextPathView) header.findViewById(R.id.xjy_textView);
        textView.setText(headerText);
        setHeaderView(header);

        HeaderOrFooter footer = new HeaderOrFooter(getContext(), R.layout.jy_header_footer);
        footer.setBackgroundColor(footerBackgroundColor);
        textView = (TextPathView) footer.findViewById(R.id.xjy_textView);
        textView.setText(footerText);
        setFooterView(footer);

    }

    public void disableBounceTop(){
        setRefreshEnabled(false);
    }

    public void disableBounceBottom(){
        setLoadMoreEnabled(false);
    }

    public static void setHeaderBackgroundColor(int headerBackgroundColor) {
        BounceLayout.headerBackgroundColor = headerBackgroundColor;
    }

    public static void setFooterBackgroundColor(int footerBackgroundColor) {
        BounceLayout.footerBackgroundColor = footerBackgroundColor;
    }

    public static void setTextColor(int textColor){
        BounceLayout.textColor = textColor;
    }

    public static void setHeaderText(String headerText) {
        BounceLayout.headerText = headerText;
    }

    public static void setFooterText(String footerText) {
        BounceLayout.footerText = footerText;
    }



    private class HeaderOrFooter extends RelativeLayout implements CanRefresh {

        private boolean mIsHeader = false;

        public HeaderOrFooter(Context context, int layoutId) {
            super(context);
            setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            LayoutInflater.from(getContext()).inflate(layoutId, this, true);

        }

        @Override
        protected void onFinishInflate() {
            super.onFinishInflate();
        }

        @Override
        public void onReset() {

        }

        @Override
        public void onPrepare() {

        }

        @Override
        public void onRelease() {
            if (mIsHeader) {
                closeHeader();
            } else {
                closeFooter();
            }
        }

        @Override
        public void onComplete() {

        }

        @Override
        public void onPositionChange(float currentPercent) {

        }

        @Override
        public void setIsHeaderOrFooter(boolean isHeader) {
            mIsHeader = isHeader;
        }

        @Override
        public void onNoMoreData() {

        }
    }
}