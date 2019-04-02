package com.example.caoan.shop.Interface;

public interface ItemTouchListener {
    void onSwipe(int vitri, int huong);

    void onMove(int oldPostion, int newPosition);
}
