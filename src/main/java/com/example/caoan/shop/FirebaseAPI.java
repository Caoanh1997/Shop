package com.example.caoan.shop;

import com.example.caoan.shop.Model.Store;
import com.example.caoan.shop.Model.StoreList;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface FirebaseAPI {
    @GET("Store/-LZc0xgUdnAYkXD6AZv6.json")
    Call<Store> listStore();
}
