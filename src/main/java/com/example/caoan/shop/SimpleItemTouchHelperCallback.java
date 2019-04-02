package com.example.caoan.shop;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.example.caoan.shop.Interface.ItemTouchListener;

public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private ItemTouchListener itemTouchListener;

    public SimpleItemTouchHelperCallback(ItemTouchListener itemTouchListener) {
        this.itemTouchListener = itemTouchListener;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return super.isLongPressDragEnabled();
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return super.isItemViewSwipeEnabled();
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int swipeFlag = ItemTouchHelper.RIGHT;
        int moveFlag = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        return makeMovementFlags(moveFlag, swipeFlag);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        itemTouchListener.onMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        itemTouchListener.onSwipe(viewHolder.getAdapterPosition(), direction);
    }
}
