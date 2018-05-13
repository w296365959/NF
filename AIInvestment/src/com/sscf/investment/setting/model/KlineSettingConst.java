package com.sscf.investment.setting.model;

/**
 * Created by LEN on 2018/4/4.
 */

public interface KlineSettingConst {

    public static final int K_LINE_STYLE_RISE_LINE_FILL = 0;
    public static final int K_LINE_STYLE_RISE_LINE_STROKE = 1;

    public static final int K_LINE_UN_REPAIR = 0;
    public static final int K_LINE_REPAIR = 1;

    public static final int DEFAULT_K_LINE_REPAIR_TYPE = K_LINE_REPAIR;

    public static final int MIN_SHOW_CANDICATORS_NUM = 2;

    public static final String DEFAULT_KLINE_CONFIGURE = "{\n" +
            "    \"rightStatus\":1,\n" +
            "    \"klineStyle\":1,\n" +
            "    \"isMAOpen\":true,\n" +
            "    \"MAConfigure\":\"5,10,20\",\n" +
            "    \"isDKOpen\":true,\n" +
            "    \"isGapOpen\":true,\n" +
            "    \"isMagicNineOpen\":false,\n" +
            "    \"isBullBearOpen\":false,\n" +
            "    \"indicatorNum\":2,\n" +
            "    \"showIndicators\":\"成交量,横盘突破,主力资金,MACD,KDJ,RSI,BOLL,DMI,CCI,ENE,DMA,EXPMA,VR,BBI,OBV,BIAS,WR\",\n" +
            "    \"allIndicators\":[\n" +
            "        {\n" +
            "            \"name\":\"成交量\",\n" +
            "            \"values\":\"5,10\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"name\":\"主力资金\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"name\":\"MACD\",\n" +
            "            \"values\":\"12,26,9\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"name\":\"KDJ\",\n" +
            "            \"values\":\"9,3,3\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"name\":\"RSI\",\n" +
            "            \"values\":\"6,12,24\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"name\":\"BOLL\",\n" +
            "            \"values\":\"20,2\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"name\":\"DMI\",\n" +
            "            \"values\":\"14,6\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"name\":\"CCI\",\n" +
            "            \"values\":\"14,10\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"name\":\"ENE\",\n" +
            "            \"values\":\"10,11,9\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"name\":\"DMA\",\n" +
            "            \"values\":\"10,50,10\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"name\":\"EXPMA\",\n" +
            "            \"values\":\"5,10\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"name\":\"VR\",\n" +
            "            \"values\":\"26\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"name\":\"BBI\",\n" +
            "            \"values\":\"3,6,12,24\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"name\":\"OBV\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"name\":\"BIAS\",\n" +
            "            \"values\":\"6,12,24\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"name\":\"WR\",\n" +
            "            \"values\":\"10\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"name\":\"横盘突破\"\n" +
            "        }\n" +
            "    ]\n" +
            "}";
}
