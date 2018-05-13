package BEC;

public final class GetSecPeriodHistoryRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sSecName = "";

    public String sDtSecCode = "";

    public float fRisePct = 0;

    public float fRiseChance = 0;

    public BEC.SecHisMonthQuota stMonthQuota = null;

    public String sTradingDay = "";

    public String getSSecName()
    {
        return sSecName;
    }

    public void  setSSecName(String sSecName)
    {
        this.sSecName = sSecName;
    }

    public String getSDtSecCode()
    {
        return sDtSecCode;
    }

    public void  setSDtSecCode(String sDtSecCode)
    {
        this.sDtSecCode = sDtSecCode;
    }

    public float getFRisePct()
    {
        return fRisePct;
    }

    public void  setFRisePct(float fRisePct)
    {
        this.fRisePct = fRisePct;
    }

    public float getFRiseChance()
    {
        return fRiseChance;
    }

    public void  setFRiseChance(float fRiseChance)
    {
        this.fRiseChance = fRiseChance;
    }

    public BEC.SecHisMonthQuota getStMonthQuota()
    {
        return stMonthQuota;
    }

    public void  setStMonthQuota(BEC.SecHisMonthQuota stMonthQuota)
    {
        this.stMonthQuota = stMonthQuota;
    }

    public String getSTradingDay()
    {
        return sTradingDay;
    }

    public void  setSTradingDay(String sTradingDay)
    {
        this.sTradingDay = sTradingDay;
    }

    public GetSecPeriodHistoryRsp()
    {
    }

    public GetSecPeriodHistoryRsp(String sSecName, String sDtSecCode, float fRisePct, float fRiseChance, BEC.SecHisMonthQuota stMonthQuota, String sTradingDay)
    {
        this.sSecName = sSecName;
        this.sDtSecCode = sDtSecCode;
        this.fRisePct = fRisePct;
        this.fRiseChance = fRiseChance;
        this.stMonthQuota = stMonthQuota;
        this.sTradingDay = sTradingDay;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sSecName) {
            ostream.writeString(0, sSecName);
        }
        if (null != sDtSecCode) {
            ostream.writeString(1, sDtSecCode);
        }
        ostream.writeFloat(2, fRisePct);
        ostream.writeFloat(3, fRiseChance);
        if (null != stMonthQuota) {
            ostream.writeMessage(4, stMonthQuota);
        }
        if (null != sTradingDay) {
            ostream.writeString(5, sTradingDay);
        }
    }

    static BEC.SecHisMonthQuota VAR_TYPE_4_STMONTHQUOTA = new BEC.SecHisMonthQuota();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sSecName = (String)istream.readString(0, false, this.sSecName);
        this.sDtSecCode = (String)istream.readString(1, false, this.sDtSecCode);
        this.fRisePct = (float)istream.readFloat(2, false, this.fRisePct);
        this.fRiseChance = (float)istream.readFloat(3, false, this.fRiseChance);
        this.stMonthQuota = (BEC.SecHisMonthQuota)istream.readMessage(4, false, VAR_TYPE_4_STMONTHQUOTA);
        this.sTradingDay = (String)istream.readString(5, false, this.sTradingDay);
    }

}

