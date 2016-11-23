package com.xjy.widget.bounce;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * User: Tom
 * Date: 2016-11-23
 * Time: 16:47
 * FIXME
 */
public class BounceLayout extends EventDispenseLayout{

    private static int headerBackgroundColor = 0xffd4d4d4;

    private static int footerBackgroundColor = 0xffd4d4d4;

    private static String headerText = "Use BounceLayout Header";

    private static String footerText = "Use BounceLayout Footer";

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
        TextView textView = (TextView) header.findViewById(R.id.textView);
        textView.setText(headerText);
        setHeaderView(header);

        HeaderOrFooter footer = new HeaderOrFooter(getContext(), R.layout.jy_header_footer);
        footer.setBackgroundColor(footerBackgroundColor);
        textView = (TextView) footer.findViewById(R.id.textView);
        textView.setText(footerText);
        setFooterView(footer);
    }

    public static void setHeaderBackgroundColor(int headerBackgroundColor) {
        BounceLayout.headerBackgroundColor = headerBackgroundColor;
    }

    public static void setFooterBackgroundColor(int footerBackgroundColor) {
        BounceLayout.footerBackgroundColor = footerBackgroundColor;
    }

    public static void setHeaderText(String headerText) {
        BounceLayout.headerText = headerText;
    }

    public static void setFooterText(String footerText) {
        BounceLayout.footerText = footerText;
    }

    private class HeaderOrFooter extends RelativeLayout implements IEventHandlePart {

        private boolean mIsHeader = false;

        public HeaderOrFooter(Context context, int layoutId) {
            super(context);
            setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            LayoutInflater.from(getContext()).inflate(layoutId, this, true);
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
    }
}