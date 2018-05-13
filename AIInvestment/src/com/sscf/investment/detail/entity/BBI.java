package com.sscf.investment.detail.entity;

/**
 * Created by LEN on 2018/3/16.
 */

public class BBI {

    private float price;

    private float bbi;

    public BBI(float price, float bbi) {
        this.price = price;
        this.bbi = bbi;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getBbi() {
        return bbi;
    }

    public void setBbi(float bbi) {
        this.bbi = bbi;
    }
}
