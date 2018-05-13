package BEC;

public final class GetSecHistoryRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sSecName = "";

    public String sDtSecCode = "";

    public int iYear = 0;

    public int iMonth = 0;

    public BEC.SecHisMonthQuota stMonthQuota = null;

    public BEC.SecHisAvgRiseQuota stAvgRiseQuota = null;

    public BEC.SecHisAvgRiseChanceQuota stAvgRiseChanceQuota = null;

    public BEC.SecLHHisQuota stLHQuota = null;

    public java.util.ArrayList<BEC.SecLHHisQuota> vSubjectQuota = null;

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

    public int getIYear()
    {
        return iYear;
    }

    public void  setIYear(int iYear)
    {
        this.iYear = iYear;
    }

    public int getIMonth()
    {
        return iMonth;
    }

    public void  setIMonth(int iMonth)
    {
        this.iMonth = iMonth;
    }

    public BEC.SecHisMonthQuota getStMonthQuota()
    {
        return stMonthQuota;
    }

    public void  setStMonthQuota(BEC.SecHisMonthQuota stMonthQuota)
    {
        this.stMonthQuota = stMonthQuota;
    }

    public BEC.SecHisAvgRiseQuota getStAvgRiseQuota()
    {
        return stAvgRiseQuota;
    }

    public void  setStAvgRiseQuota(BEC.SecHisAvgRiseQuota stAvgRiseQuota)
    {
        this.stAvgRiseQuota = stAvgRiseQuota;
    }

    public BEC.SecHisAvgRiseChanceQuota getStAvgRiseChanceQuota()
    {
        return stAvgRiseChanceQuota;
    }

    public void  setStAvgRiseChanceQuota(BEC.SecHisAvgRiseChanceQuota stAvgRiseChanceQuota)
    {
        this.stAvgRiseChanceQuota = stAvgRiseChanceQuota;
    }

    public BEC.SecLHHisQuota getStLHQuota()
    {
        return stLHQuota;
    }

    public void  setStLHQuota(BEC.SecLHHisQuota stLHQuota)
    {
        this.stLHQuota = stLHQuota;
    }

    public java.util.ArrayList<BEC.SecLHHisQuota> getVSubjectQuota()
    {
        return vSubjectQuota;
    }

    public void  setVSubjectQuota(java.util.ArrayList<BEC.SecLHHisQuota> vSubjectQuota)
    {
        this.vSubjectQuota = vSubjectQuota;
    }

    public GetSecHistoryRsp()
    {
    }

    public GetSecHistoryRsp(String sSecName, String sDtSecCode, int iYear, int iMonth, BEC.SecHisMonthQuota stMonthQuota, BEC.SecHisAvgRiseQuota stAvgRiseQuota, BEC.SecHisAvgRiseChanceQuota stAvgRiseChanceQuota, BEC.SecLHHisQuota stLHQuota, java.util.ArrayList<BEC.SecLHHisQuota> vSubjectQuota)
    {
        this.sSecName = sSecName;
        this.sDtSecCode = sDtSecCode;
        this.iYear = iYear;
        this.iMonth = iMonth;
        this.stMonthQuota = stMonthQuota;
        this.stAvgRiseQuota = stAvgRiseQuota;
        this.stAvgRiseChanceQuota = stAvgRiseChanceQuota;
        this.stLHQuota = stLHQuota;
        this.vSubjectQuota = vSubjectQuota;
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
        ostream.writeInt32(2, iYear);
        ostream.writeInt32(3, iMonth);
        if (null != stMonthQuota) {
            ostream.writeMessage(4, stMonthQuota);
        }
        if (null != stAvgRiseQuota) {
            ostream.writeMessage(5, stAvgRiseQuota);
        }
        if (null != stAvgRiseChanceQuota) {
            ostream.writeMessage(6, stAvgRiseChanceQuota);
        }
        if (null != stLHQuota) {
            ostream.writeMessage(7, stLHQuota);
        }
        if (null != vSubjectQuota) {
            ostream.writeList(8, vSubjectQuota);
        }
    }

    static BEC.SecHisMonthQuota VAR_TYPE_4_STMONTHQUOTA = new BEC.SecHisMonthQuota();

    static BEC.SecHisAvgRiseQuota VAR_TYPE_4_STAVGRISEQUOTA = new BEC.SecHisAvgRiseQuota();

    static BEC.SecHisAvgRiseChanceQuota VAR_TYPE_4_STAVGRISECHANCEQUOTA = new BEC.SecHisAvgRiseChanceQuota();

    static BEC.SecLHHisQuota VAR_TYPE_4_STLHQUOTA = new BEC.SecLHHisQuota();

    static java.util.ArrayList<BEC.SecLHHisQuota> VAR_TYPE_4_VSUBJECTQUOTA = new java.util.ArrayList<BEC.SecLHHisQuota>();
    static {
        VAR_TYPE_4_VSUBJECTQUOTA.add(new BEC.SecLHHisQuota());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sSecName = (String)istream.readString(0, false, this.sSecName);
        this.sDtSecCode = (String)istream.readString(1, false, this.sDtSecCode);
        this.iYear = (int)istream.readInt32(2, false, this.iYear);
        this.iMonth = (int)istream.readInt32(3, false, this.iMonth);
        this.stMonthQuota = (BEC.SecHisMonthQuota)istream.readMessage(4, false, VAR_TYPE_4_STMONTHQUOTA);
        this.stAvgRiseQuota = (BEC.SecHisAvgRiseQuota)istream.readMessage(5, false, VAR_TYPE_4_STAVGRISEQUOTA);
        this.stAvgRiseChanceQuota = (BEC.SecHisAvgRiseChanceQuota)istream.readMessage(6, false, VAR_TYPE_4_STAVGRISECHANCEQUOTA);
        this.stLHQuota = (BEC.SecLHHisQuota)istream.readMessage(7, false, VAR_TYPE_4_STLHQUOTA);
        this.vSubjectQuota = (java.util.ArrayList<BEC.SecLHHisQuota>)istream.readList(8, false, VAR_TYPE_4_VSUBJECTQUOTA);
    }

}

