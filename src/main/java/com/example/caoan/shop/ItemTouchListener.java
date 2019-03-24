package com.example.caoan.shop;

public interface ItemTouchListener {
    void onSwipe(int vitri, int huong);

    void onMove(int oldPostion, int newPosition);
}
