package com.sscf.investment.detail.entity;

public class MACD {
	private float macd;
	private float dif;
	private float dea;

	public MACD(float macd, float dif, float dea) {
		super();
		this.macd = macd;
		this.dif = dif;
		this.dea = dea;
	}

	@Override
	public String toString() {
		return "dif="+dif+"  dea="+dea+"  macd="+macd;
	}

	public float getMacd() {
		return macd;
	}

	public void setMacd(float macd) {
		this.macd = macd;
	}

	public float getDif() {
		return dif;
	}

	public void setDif(float dif) {
		this.dif = dif;
	}

	public float getDea() {
		return dea;
	}

	public void setDea(float dea) {
		this.dea = dea;
	}
}
