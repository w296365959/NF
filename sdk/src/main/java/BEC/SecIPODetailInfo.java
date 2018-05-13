package BEC;

public final class SecIPODetailInfo extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sDtSecCode = "";

    public String sSecName = "";

    public String sIPOCode = "";

    public String sIPODate = "";

    public String sLotDeclDate = "";

    public float fAplRatio = 0;

    public String sListDate = "";

    public float fIssPrice = 0;

    public float fPerCeil = 0;

    public long fShareNum = 0;

    public float fOnlineShareNum = 0;

    public float fAplCeil = 0;

    public float fAplFundCeil = 0;

    public String sCompanyDesc = "";

    public String sMainBusi = "";

    public String sLotResPubDay = "";

    public java.util.ArrayList<LotNumResult> vLotNumResult = null;

    public String getSDtSecCode()
    {
        return sDtSecCode;
    }

    public void  setSDtSecCode(String sDtSecCode)
    {
        this.sDtSecCode = sDtSecCode;
    }

    public String getSSecName()
    {
        return sSecName;
    }

    public void  setSSecName(String sSecName)
    {
        this.sSecName = sSecName;
    }

    public String getSIPOCode()
    {
        return sIPOCode;
    }

    public void  setSIPOCode(String sIPOCode)
    {
        this.sIPOCode = sIPOCode;
    }

    public String getSIPODate()
    {
        return sIPODate;
    }

    public void  setSIPODate(String sIPODate)
    {
        this.sIPODate = sIPODate;
    }

    public String getSLotDeclDate()
    {
        return sLotDeclDate;
    }

    public void  setSLotDeclDate(String sLotDeclDate)
    {
        this.sLotDeclDate = sLotDeclDate;
    }

    public float getFAplRatio()
    {
        return fAplRatio;
    }

    public void  setFAplRatio(float fAplRatio)
    {
        this.fAplRatio = fAplRatio;
    }

    public String getSListDate()
    {
        return sListDate;
    }

    public void  setSListDate(String sListDate)
    {
        this.sListDate = sListDate;
    }

    public float getFIssPrice()
    {
        return fIssPrice;
    }

    public void  setFIssPrice(float fIssPrice)
    {
        this.fIssPrice = fIssPrice;
    }

    public float getFPerCeil()
    {
        return fPerCeil;
    }

    public void  setFPerCeil(float fPerCeil)
    {
        this.fPerCeil = fPerCeil;
    }

    public long getFShareNum()
    {
        return fShareNum;
    }

    public void  setFShareNum(long fShareNum)
    {
        this.fShareNum = fShareNum;
    }

    public float getFOnlineShareNum()
    {
        return fOnlineShareNum;
    }

    public void  setFOnlineShareNum(float fOnlineShareNum)
    {
        this.fOnlineShareNum = fOnlineShareNum;
    }

    public float getFAplCeil()
    {
        return fAplCeil;
    }

    public void  setFAplCeil(float fAplCeil)
    {
        this.fAplCeil = fAplCeil;
    }

    public float getFAplFundCeil()
    {
        return fAplFundCeil;
    }

    public void  setFAplFundCeil(float fAplFundCeil)
    {
        this.fAplFundCeil = fAplFundCeil;
    }

    public String getSCompanyDesc()
    {
        return sCompanyDesc;
    }

    public void  setSCompanyDesc(String sCompanyDesc)
    {
        this.sCompanyDesc = sCompanyDesc;
    }

    public String getSMainBusi()
    {
        return sMainBusi;
    }

    public void  setSMainBusi(String sMainBusi)
    {
        this.sMainBusi = sMainBusi;
    }

    public String getSLotResPubDay()
    {
        return sLotResPubDay;
    }

    public void  setSLotResPubDay(String sLotResPubDay)
    {
        this.sLotResPubDay = sLotResPubDay;
    }

    public java.util.ArrayList<LotNumResult> getVLotNumResult()
    {
        return vLotNumResult;
    }

    public void  setVLotNumResult(java.util.ArrayList<LotNumResult> vLotNumResult)
    {
        this.vLotNumResult = vLotNumResult;
    }

    public SecIPODetailInfo()
    {
    }

    public SecIPODetailInfo(String sDtSecCode, String sSecName, String sIPOCode, String sIPODate, String sLotDeclDate, float fAplRatio, String sListDate, float fIssPrice, float fPerCeil, long fShareNum, float fOnlineShareNum, float fAplCeil, float fAplFundCeil, String sCompanyDesc, String sMainBusi, String sLotResPubDay, java.util.ArrayList<LotNumResult> vLotNumResult)
    {
        this.sDtSecCode = sDtSecCode;
        this.sSecName = sSecName;
        this.sIPOCode = sIPOCode;
        this.sIPODate = sIPODate;
        this.sLotDeclDate = sLotDeclDate;
        this.fAplRatio = fAplRatio;
        this.sListDate = sListDate;
        this.fIssPrice = fIssPrice;
        this.fPerCeil = fPerCeil;
        this.fShareNum = fShareNum;
        this.fOnlineShareNum = fOnlineShareNum;
        this.fAplCeil = fAplCeil;
        this.fAplFundCeil = fAplFundCeil;
        this.sCompanyDesc = sCompanyDesc;
        this.sMainBusi = sMainBusi;
        this.sLotResPubDay = sLotResPubDay;
        this.vLotNumResult = vLotNumResult;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sDtSecCode) {
            ostream.writeString(0, sDtSecCode);
        }
        if (null != sSecName) {
            ostream.writeString(1, sSecName);
        }
        if (null != sIPOCode) {
            ostream.writeString(2, sIPOCode);
        }
        if (null != sIPODate) {
            ostream.writeString(3, sIPODate);
        }
        if (null != sLotDeclDate) {
            ostream.writeString(4, sLotDeclDate);
        }
        ostream.writeFloat(5, fAplRatio);
        if (null != sListDate) {
            ostream.writeString(6, sListDate);
        }
        ostream.writeFloat(7, fIssPrice);
        ostream.writeFloat(8, fPerCeil);
        ostream.writeInt64(9, fShareNum);
        ostream.writeFloat(10, fOnlineShareNum);
        ostream.writeFloat(11, fAplCeil);
        ostream.writeFloat(12, fAplFundCeil);
        if (null != sCompanyDesc) {
            ostream.writeString(13, sCompanyDesc);
        }
        if (null != sMainBusi) {
            ostream.writeString(14, sMainBusi);
        }
        if (null != sLotResPubDay) {
            ostream.writeString(15, sLotResPubDay);
        }
        if (null != vLotNumResult) {
            ostream.writeList(16, vLotNumResult);
        }
    }

    static java.util.ArrayList<LotNumResult> VAR_TYPE_4_VLOTNUMRESULT = new java.util.ArrayList<LotNumResult>();
    static {
        VAR_TYPE_4_VLOTNUMRESULT.add(new LotNumResult());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sDtSecCode = (String)istream.readString(0, false, this.sDtSecCode);
        this.sSecName = (String)istream.readString(1, false, this.sSecName);
        this.sIPOCode = (String)istream.readString(2, false, this.sIPOCode);
        this.sIPODate = (String)istream.readString(3, false, this.sIPODate);
        this.sLotDeclDate = (String)istream.readString(4, false, this.sLotDeclDate);
        this.fAplRatio = (float)istream.readFloat(5, false, this.fAplRatio);
        this.sListDate = (String)istream.readString(6, false, this.sListDate);
        this.fIssPrice = (float)istream.readFloat(7, false, this.fIssPrice);
        this.fPerCeil = (float)istream.readFloat(8, false, this.fPerCeil);
        this.fShareNum = (long)istream.readInt64(9, false, this.fShareNum);
        this.fOnlineShareNum = (float)istream.readFloat(10, false, this.fOnlineShareNum);
        this.fAplCeil = (float)istream.readFloat(11, false, this.fAplCeil);
        this.fAplFundCeil = (float)istream.readFloat(12, false, this.fAplFundCeil);
        this.sCompanyDesc = (String)istream.readString(13, false, this.sCompanyDesc);
        this.sMainBusi = (String)istream.readString(14, false, this.sMainBusi);
        this.sLotResPubDay = (String)istream.readString(15, false, this.sLotResPubDay);
        this.vLotNumResult = (java.util.ArrayList<LotNumResult>)istream.readList(16, false, VAR_TYPE_4_VLOTNUMRESULT);
    }

}

