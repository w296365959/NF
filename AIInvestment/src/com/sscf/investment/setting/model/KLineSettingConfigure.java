package com.sscf.investment.setting.model;

import java.util.List;

/**
 * Created by LEN on 2018/4/4.
 */

public class KLineSettingConfigure {

    public int rightStatus;//0,不复权, 1前复权

    public int klineStyle;//0,实心线， 1空心线

    public boolean isMAOpen;
    public String MAConfigure;
    public boolean isDKOpen;
    public boolean isGapOpen;
    public boolean isMagicNineOpen;
    public boolean isBullBearOpen;
    public int indicatorNum = 2;

    public String showIndicators;

    public List<IndicatorConfigure> allIndicators;

}
