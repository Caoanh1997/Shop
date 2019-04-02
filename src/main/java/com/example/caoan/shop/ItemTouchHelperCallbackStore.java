package com.example.caoan.shop;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.example.caoan.shop.Interface.ItemTouchListenerStore;

public class ItemTouchHelperCallbackStore extends ItemTouchHelper.Callback {

    private ItemTouchListenerStore itemTouchListenerStore;

    public ItemTouchHelperCallbackStore(ItemTouchListenerStore itemTouchListenerStore) {
        this.itemTouchListenerStore = itemTouchListenerStore;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int swipeFlag = ItemTouchHelper.RIGHT;
        int moveFlag = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        return makeMovementFlags(moveFlag, swipeFlag);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        itemTouchListenerStore.onMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        itemTouchListenerStore.onSwipe(viewHolder.getAdapterPosition(), direction);
    }
}
