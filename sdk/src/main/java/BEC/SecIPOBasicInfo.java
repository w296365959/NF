package BEC;

public final class SecIPOBasicInfo extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int eStatus = 0;

    public String sIPOCode = "";

    public String sDtSecCode = "";

    public String sSecName = "";

    public float fIssPrice = 0;

    public float fAplCeil = 0;

    public float fPerCeil = 0;

    public float fAplRatio = 0;

    public String sListDate = "";

    public float fChangePct = 0;

    public String sIPODate = "";

    public String sLotNumberDate = "";

    public String sPayInDate = "";

    public String sLotResPubDay = "";

    public java.util.ArrayList<LotNumResult> vLotNumResult = null;

    public int eMarketType = 0;

    public float fIncomePerSign = 0;

    public int iTopIncreaseDay = 0;

    public int iIncreseStatus = 0;

    public int getEStatus()
    {
        return eStatus;
    }

    public void  setEStatus(int eStatus)
    {
        this.eStatus = eStatus;
    }

    public String getSIPOCode()
    {
        return sIPOCode;
    }

    public void  setSIPOCode(String sIPOCode)
    {
        this.sIPOCode = sIPOCode;
    }

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

    public float getFIssPrice()
    {
        return fIssPrice;
    }

    public void  setFIssPrice(float fIssPrice)
    {
        this.fIssPrice = fIssPrice;
    }

    public float getFAplCeil()
    {
        return fAplCeil;
    }

    public void  setFAplCeil(float fAplCeil)
    {
        this.fAplCeil = fAplCeil;
    }

    public float getFPerCeil()
    {
        return fPerCeil;
    }

    public void  setFPerCeil(float fPerCeil)
    {
        this.fPerCeil = fPerCeil;
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

    public float getFChangePct()
    {
        return fChangePct;
    }

    public void  setFChangePct(float fChangePct)
    {
        this.fChangePct = fChangePct;
    }

    public String getSIPODate()
    {
        return sIPODate;
    }

    public void  setSIPODate(String sIPODate)
    {
        this.sIPODate = sIPODate;
    }

    public String getSLotNumberDate()
    {
        return sLotNumberDate;
    }

    public void  setSLotNumberDate(String sLotNumberDate)
    {
        this.sLotNumberDate = sLotNumberDate;
    }

    public String getSPayInDate()
    {
        return sPayInDate;
    }

    public void  setSPayInDate(String sPayInDate)
    {
        this.sPayInDate = sPayInDate;
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

    public int getEMarketType()
    {
        return eMarketType;
    }

    public void  setEMarketType(int eMarketType)
    {
        this.eMarketType = eMarketType;
    }

    public float getFIncomePerSign()
    {
        return fIncomePerSign;
    }

    public void  setFIncomePerSign(float fIncomePerSign)
    {
        this.fIncomePerSign = fIncomePerSign;
    }

    public int getITopIncreaseDay()
    {
        return iTopIncreaseDay;
    }

    public void  setITopIncreaseDay(int iTopIncreaseDay)
    {
        this.iTopIncreaseDay = iTopIncreaseDay;
    }

    public int getIIncreseStatus()
    {
        return iIncreseStatus;
    }

    public void  setIIncreseStatus(int iIncreseStatus)
    {
        this.iIncreseStatus = iIncreseStatus;
    }

    public SecIPOBasicInfo()
    {
    }

    public SecIPOBasicInfo(int eStatus, String sIPOCode, String sDtSecCode, String sSecName, float fIssPrice, float fAplCeil, float fPerCeil, float fAplRatio, String sListDate, float fChangePct, String sIPODate, String sLotNumberDate, String sPayInDate, String sLotResPubDay, java.util.ArrayList<LotNumResult> vLotNumResult, int eMarketType, float fIncomePerSign, int iTopIncreaseDay, int iIncreseStatus)
    {
        this.eStatus = eStatus;
        this.sIPOCode = sIPOCode;
        this.sDtSecCode = sDtSecCode;
        this.sSecName = sSecName;
        this.fIssPrice = fIssPrice;
        this.fAplCeil = fAplCeil;
        this.fPerCeil = fPerCeil;
        this.fAplRatio = fAplRatio;
        this.sListDate = sListDate;
        this.fChangePct = fChangePct;
        this.sIPODate = sIPODate;
        this.sLotNumberDate = sLotNumberDate;
        this.sPayInDate = sPayInDate;
        this.sLotResPubDay = sLotResPubDay;
        this.vLotNumResult = vLotNumResult;
        this.eMarketType = eMarketType;
        this.fIncomePerSign = fIncomePerSign;
        this.iTopIncreaseDay = iTopIncreaseDay;
        this.iIncreseStatus = iIncreseStatus;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, eStatus);
        if (null != sIPOCode) {
            ostream.writeString(1, sIPOCode);
        }
        if (null != sDtSecCode) {
            ostream.writeString(2, sDtSecCode);
        }
        if (null != sSecName) {
            ostream.writeString(3, sSecName);
        }
        ostream.writeFloat(4, fIssPrice);
        ostream.writeFloat(5, fAplCeil);
        ostream.writeFloat(6, fPerCeil);
        ostream.writeFloat(7, fAplRatio);
        if (null != sListDate) {
            ostream.writeString(8, sListDate);
        }
        ostream.writeFloat(9, fChangePct);
        if (null != sIPODate) {
            ostream.writeString(10, sIPODate);
        }
        if (null != sLotNumberDate) {
            ostream.writeString(11, sLotNumberDate);
        }
        if (null != sPayInDate) {
            ostream.writeString(12, sPayInDate);
        }
        if (null != sLotResPubDay) {
            ostream.writeString(13, sLotResPubDay);
        }
        if (null != vLotNumResult) {
            ostream.writeList(14, vLotNumResult);
        }
        ostream.writeInt32(15, eMarketType);
        ostream.writeFloat(16, fIncomePerSign);
        ostream.writeInt32(17, iTopIncreaseDay);
        ostream.writeInt32(18, iIncreseStatus);
    }

    static java.util.ArrayList<LotNumResult> VAR_TYPE_4_VLOTNUMRESULT = new java.util.ArrayList<LotNumResult>();
    static {
        VAR_TYPE_4_VLOTNUMRESULT.add(new LotNumResult());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.eStatus = (int)istream.readInt32(0, false, this.eStatus);
        this.sIPOCode = (String)istream.readString(1, false, this.sIPOCode);
        this.sDtSecCode = (String)istream.readString(2, false, this.sDtSecCode);
        this.sSecName = (String)istream.readString(3, false, this.sSecName);
        this.fIssPrice = (float)istream.readFloat(4, false, this.fIssPrice);
        this.fAplCeil = (float)istream.readFloat(5, false, this.fAplCeil);
        this.fPerCeil = (float)istream.readFloat(6, false, this.fPerCeil);
        this.fAplRatio = (float)istream.readFloat(7, false, this.fAplRatio);
        this.sListDate = (String)istream.readString(8, false, this.sListDate);
        this.fChangePct = (float)istream.readFloat(9, false, this.fChangePct);
        this.sIPODate = (String)istream.readString(10, false, this.sIPODate);
        this.sLotNumberDate = (String)istream.readString(11, false, this.sLotNumberDate);
        this.sPayInDate = (String)istream.readString(12, false, this.sPayInDate);
        this.sLotResPubDay = (String)istream.readString(13, false, this.sLotResPubDay);
        this.vLotNumResult = (java.util.ArrayList<LotNumResult>)istream.readList(14, false, VAR_TYPE_4_VLOTNUMRESULT);
        this.eMarketType = (int)istream.readInt32(15, false, this.eMarketType);
        this.fIncomePerSign = (float)istream.readFloat(16, false, this.fIncomePerSign);
        this.iTopIncreaseDay = (int)istream.readInt32(17, false, this.iTopIncreaseDay);
        this.iIncreseStatus = (int)istream.readInt32(18, false, this.iIncreseStatus);
    }

}

