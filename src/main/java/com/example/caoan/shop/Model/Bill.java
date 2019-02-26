package com.example.caoan.shop.Model;

public class Bill {
    private String key_cart;
    private String name_user;
    private String address;
    private String phone;
    private String product;
    private String total_price;
    private String state;

    public Bill(String key_cart, String name_user, String address, String phone, String product, String total_price, String state) {
        this.key_cart = key_cart;
        this.name_user = name_user;
        this.address = address;
        this.phone = phone;
        this.product = product;
        this.total_price = total_price;
        this.state = state;
    }

    public Bill(String name_user, String address, String phone, String product, String total_price, String state) {
        this.name_user = name_user;
        this.address = address;
        this.phone = phone;
        this.product = product;
        this.total_price = total_price;
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getKey_cart() {
        return key_cart;
    }

    public void setKey_cart(String key_cart) {
        this.key_cart = key_cart;
    }

    public String getName_user() {
        return name_user;
    }

    public void setName_user(String name_user) {
        this.name_user = name_user;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    @Override
    public String toString() {
        return "Bill{" +
                "key_cart='" + key_cart + '\'' +
                ", name_user='" + name_user + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", product='" + product + '\'' +
                ", total_price='" + total_price + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}