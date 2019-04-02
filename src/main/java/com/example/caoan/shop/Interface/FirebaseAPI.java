package com.example.caoan.shop.Interface;

import com.example.caoan.shop.Model.Store;

import retrofit2.Call;
import retrofit2.http.GET;

public interface FirebaseAPI {
    @GET("Store/-LZc0xgUdnAYkXD6AZv6.json")
    Call<Store> listStore();
}
