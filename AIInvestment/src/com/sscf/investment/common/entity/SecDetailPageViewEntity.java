package com.sscf.investment.common.entity;

import com.sscf.investment.afinal.annotation.sqlite.Id;
import com.sscf.investment.afinal.annotation.sqlite.Property;
import com.sscf.investment.afinal.annotation.sqlite.Table;

import java.io.Serializable;

/**
 * Created by liqf on 2016/11/1.
 */
@Table(name = "statistics_sec_pv")
public final class SecDetailPageViewEntity implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    private String dtSecCode;
    @Property
    private int pv;

    public String getDtSecCode() {
        return dtSecCode;
    }

    public void setDtSecCode(String dtSecCode) {
        this.dtSecCode = dtSecCode;
    }

    public int getPv() {
        return pv;
    }

    public void setPv(int pv) {
        this.pv = pv;
    }
}
