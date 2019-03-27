package com.example.caoan.shop;

import com.example.caoan.shop.Model.Bill;

public class BillEvent {

    private Bill bill;

    public BillEvent(Bill bill) {
        this.bill = bill;
    }

    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }
}
