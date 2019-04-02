package com.example.caoan.shop.Interface;

public interface ItemTouchListenerStore {
    void onSwipe(int position, int direction);

    void onMove(int oldPostion, int newPosition);
}
