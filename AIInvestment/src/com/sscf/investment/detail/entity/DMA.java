package com.sscf.investment.detail.entity;

/**
 * Created by LEN on 2018/3/19.
 */

public class DMA {

    private float ama;

    private float dma;

    public DMA(float ama, float dma) {
        this.ama = ama;
        this.dma = dma;
    }

    public float getAma() {
        return ama;
    }

    public void setAma(float ama) {
        this.ama = ama;
    }

    public float getDma() {
        return dma;
    }

    public void setDdd(float ddd) {
        this.dma = ddd;
    }

    @Override
    public String toString() {
        return "DMA{" +
                "ama=" + ama +
                ", dma=" + dma +
                '}';
    }
}
