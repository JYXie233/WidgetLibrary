package com.xjy.widget.adapter;

/**
 * User: Tom
 * Date: 2016-11-17
 * Time: 16:44
 * FIXME
 */
public interface JYItemTouchHelperAdapter {
    void onItemMove(int fromPosition, int toPosition);
    void onItemDismiss(int position);
    boolean canSwipe(int position);
    boolean canMove(int position);
}  