package com.example.caoan.shop.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StoreList {

    private List<Store> storeList;

    public List<Store> getStoreList() {
        return storeList;
    }

    public void setStoreList(List<Store> storeList) {
        this.storeList = storeList;
    }
}
