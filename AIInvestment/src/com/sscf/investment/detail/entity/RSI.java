package com.sscf.investment.detail.entity;

public class RSI {
	private float rsi1;
	private float rsi2;
	private float rsi3;

	public RSI(float rsi1, float rsi2, float rsi3) {
		super();
		this.rsi1 = rsi1;
		this.rsi2 = rsi2;
		this.rsi3 = rsi3;
	}

	public float getRsi1() {
		return rsi1;
	}

	public void setRsi1(float rsi1) {
		this.rsi1 = rsi1;
	}

	public float getRsi2() {
		return rsi2;
	}

	public void setRsi2(float rsi2) {
		this.rsi2 = rsi2;
	}

	public float getRsi3() {
		return rsi3;
	}

	public void setRsi3(float rsi3) {
		this.rsi3 = rsi3;
	}
}
