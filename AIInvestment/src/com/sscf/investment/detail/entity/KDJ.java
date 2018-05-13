package com.sscf.investment.detail.entity;

public class KDJ {
	private float kvalue;
	private float dvalue;
	private float jvalue;

	public KDJ(float kvalue, float dvalue, float jvalue) {
		super();
		this.kvalue = kvalue;
		this.dvalue = dvalue;
		this.jvalue = jvalue;
	}
	public float getKvalue() {
		return kvalue;
	}
	public void setKvalue(float kvalue) {
		this.kvalue = kvalue;
	}
	public float getDvalue() {
		return dvalue;
	}
	public void setDvalue(float dvalue) {
		this.dvalue = dvalue;
	}
	public float getJvalue() {
		return jvalue;
	}
	public void setJvalue(float jvalue) {
		this.jvalue = jvalue;
	}
}
