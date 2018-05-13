package com.sscf.investment.detail.entity;

public class BOLL {
	private float bollval;
	private float upper;
	private float lower;

	public BOLL(float bollval, float upper, float lower) {
		super();
		this.bollval = bollval;
		this.upper = upper;
		this.lower = lower;
	}

	public float getBollval() {
		return bollval;
	}

	public void setBollval(float bollval) {
		this.bollval = bollval;
	}

	public float getUpper() {
		return upper;
	}

	public void setUpper(float upper) {
		this.upper = upper;
	}

	public float getLower() {
		return lower;
	}

	public void setLower(float lower) {
		this.lower = lower;
	}
}
