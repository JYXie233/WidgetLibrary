package com.xjy.widget.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * User: Tom
 * Date: 2016-11-17
 * Time: 16:43
 * FIXME
 */
public class JYItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private final JYItemTouchHelperAdapter mAdapter;

    public JYItemTouchHelperCallback(JYItemTouchHelperAdapter adapter) {
        mAdapter = adapter;
    }


    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }


    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {

        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;

        if (recyclerView.getLayoutManager() instanceof GridLayoutManager){
            dragFlags = dragFlags | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        }

        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;

        int position = viewHolder.getAdapterPosition();

        if (!mAdapter.canSwipe(position)){
            swipeFlags = 0;
        }
        if (!mAdapter.canMove(position)){
            dragFlags = 0;
        }
        return makeMovementFlags(dragFlags, swipeFlags);
    }


    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target) {
        mAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
    }




}