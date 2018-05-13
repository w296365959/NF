package BEC;

public final class GetSecWeekHistoryRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sSecName = "";

    public String sDtSecCode = "";

    public BEC.SecHisWeekQuota stWeekQuota = null;

    public int iYear = 0;

    public int iWeek = 0;

    public float fAvgUpPct = 0;

    public float fAvgUpChancePct = 0;

    public int iYearNum = 0;

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

    public BEC.SecHisWeekQuota getStWeekQuota()
    {
        return stWeekQuota;
    }

    public void  setStWeekQuota(BEC.SecHisWeekQuota stWeekQuota)
    {
        this.stWeekQuota = stWeekQuota;
    }

    public int getIYear()
    {
        return iYear;
    }

    public void  setIYear(int iYear)
    {
        this.iYear = iYear;
    }

    public int getIWeek()
    {
        return iWeek;
    }

    public void  setIWeek(int iWeek)
    {
        this.iWeek = iWeek;
    }

    public float getFAvgUpPct()
    {
        return fAvgUpPct;
    }

    public void  setFAvgUpPct(float fAvgUpPct)
    {
        this.fAvgUpPct = fAvgUpPct;
    }

    public float getFAvgUpChancePct()
    {
        return fAvgUpChancePct;
    }

    public void  setFAvgUpChancePct(float fAvgUpChancePct)
    {
        this.fAvgUpChancePct = fAvgUpChancePct;
    }

    public int getIYearNum()
    {
        return iYearNum;
    }

    public void  setIYearNum(int iYearNum)
    {
        this.iYearNum = iYearNum;
    }

    public GetSecWeekHistoryRsp()
    {
    }

    public GetSecWeekHistoryRsp(String sSecName, String sDtSecCode, BEC.SecHisWeekQuota stWeekQuota, int iYear, int iWeek, float fAvgUpPct, float fAvgUpChancePct, int iYearNum)
    {
        this.sSecName = sSecName;
        this.sDtSecCode = sDtSecCode;
        this.stWeekQuota = stWeekQuota;
        this.iYear = iYear;
        this.iWeek = iWeek;
        this.fAvgUpPct = fAvgUpPct;
        this.fAvgUpChancePct = fAvgUpChancePct;
        this.iYearNum = iYearNum;
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
        if (null != stWeekQuota) {
            ostream.writeMessage(2, stWeekQuota);
        }
        ostream.writeInt32(3, iYear);
        ostream.writeInt32(4, iWeek);
        ostream.writeFloat(5, fAvgUpPct);
        ostream.writeFloat(6, fAvgUpChancePct);
        ostream.writeInt32(7, iYearNum);
    }

    static BEC.SecHisWeekQuota VAR_TYPE_4_STWEEKQUOTA = new BEC.SecHisWeekQuota();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sSecName = (String)istream.readString(0, false, this.sSecName);
        this.sDtSecCode = (String)istream.readString(1, false, this.sDtSecCode);
        this.stWeekQuota = (BEC.SecHisWeekQuota)istream.readMessage(2, false, VAR_TYPE_4_STWEEKQUOTA);
        this.iYear = (int)istream.readInt32(3, false, this.iYear);
        this.iWeek = (int)istream.readInt32(4, false, this.iWeek);
        this.fAvgUpPct = (float)istream.readFloat(5, false, this.fAvgUpPct);
        this.fAvgUpChancePct = (float)istream.readFloat(6, false, this.fAvgUpChancePct);
        this.iYearNum = (int)istream.readInt32(7, false, this.iYearNum);
    }

}

