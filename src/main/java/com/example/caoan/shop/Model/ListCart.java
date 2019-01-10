package com.example.caoan.shop.Model;

import java.util.ArrayList;
import java.util.List;

public class ListCart {
    public List<Cart> cartList;
    private static final ListCart ourInstance = new ListCart();

    public static ListCart getInstance() {
            return ourInstance;
    }

    private ListCart() {
        cartList = new ArrayList<Cart>();
    }

    public List<Cart> getCartList() {
        return cartList;
    }
    public void addCart(Cart cart){
        cartList.add(cart);
    }
    public void removeAllCart(){
        cartList = new ArrayList<Cart>();
    }
}
