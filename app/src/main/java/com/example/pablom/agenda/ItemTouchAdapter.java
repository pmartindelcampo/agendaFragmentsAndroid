package com.example.pablom.agenda;

public interface ItemTouchAdapter {

    void onMoveItem(int fromPosition, int toPosition);

    void onDeleteItem(int position);
}
