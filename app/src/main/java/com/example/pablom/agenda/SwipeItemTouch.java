package com.example.pablom.agenda;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

public class SwipeItemTouch extends ItemTouchHelper.Callback {

    private final ItemTouchAdapter mAdapter;

    public SwipeItemTouch(ItemTouchAdapter adapter) {
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
        // direccion en la que un elemento puede ser arrastrado
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        // direccion en la que un elemento puede hacerse Swipe
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        mAdapter.onMoveItem(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        mAdapter.onDeleteItem(viewHolder.getAdapterPosition());
    }

    @Override
    public float getSwipeVelocityThreshold(float defaultValue) {
        return defaultValue / 4f;
    }

    @Override
    public float getSwipeEscapeVelocity(float defaultValue) {
        return defaultValue * 300;
    }
}
