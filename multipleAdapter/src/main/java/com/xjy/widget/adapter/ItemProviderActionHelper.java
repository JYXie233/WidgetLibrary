package com.xjy.widget.adapter;

/**
 * User: Tom
 * Date: 2016-11-17
 * Time: 16:37
 * FIXME
 */
public interface ItemProviderActionHelper {
    boolean isItemCanSwipe(int position);
    boolean isItemCanMove(int position);
    boolean onItemSwipe(int position);

    boolean onItemMove(int oldPosition, int newPosition);
}  