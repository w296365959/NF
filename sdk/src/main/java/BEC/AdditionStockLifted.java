package BEC;

public final class AdditionStockLifted extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sId = "";

    public String sDtSecCode = "";

    public String sSecCode = "";

    public String sSecName = "";

    public float fFund = 0;

    public float fFundRate = 0;

    public float fPriceRate = 0;

    public String sDate = "";

    public float fClosePrice = 0;

    public float fAdditionPrice = 0;

    public float fLiftedCount = 0;

    public int iLockexMonth = 0;

    public String sIndustry = "";

    public float fScore = 0;

    public float fPERatio = 0;

    public String sType = "";

    public java.util.ArrayList<BEC.FiltedDetail> vLiftedDetail = null;

    public String getSId()
    {
        return sId;
    }

    public void  setSId(String sId)
    {
        this.sId = sId;
    }

    public String getSDtSecCode()
    {
        return sDtSecCode;
    }

    public void  setSDtSecCode(String sDtSecCode)
    {
        this.sDtSecCode = sDtSecCode;
    }

    public String getSSecCode()
    {
        return sSecCode;
    }

    public void  setSSecCode(String sSecCode)
    {
        this.sSecCode = sSecCode;
    }

    public String getSSecName()
    {
        return sSecName;
    }

    public void  setSSecName(String sSecName)
    {
        this.sSecName = sSecName;
    }

    public float getFFund()
    {
        return fFund;
    }

    public void  setFFund(float fFund)
    {
        this.fFund = fFund;
    }

    public float getFFundRate()
    {
        return fFundRate;
    }

    public void  setFFundRate(float fFundRate)
    {
        this.fFundRate = fFundRate;
    }

    public float getFPriceRate()
    {
        return fPriceRate;
    }

    public void  setFPriceRate(float fPriceRate)
    {
        this.fPriceRate = fPriceRate;
    }

    public String getSDate()
    {
        return sDate;
    }

    public void  setSDate(String sDate)
    {
        this.sDate = sDate;
    }

    public float getFClosePrice()
    {
        return fClosePrice;
    }

    public void  setFClosePrice(float fClosePrice)
    {
        this.fClosePrice = fClosePrice;
    }

    public float getFAdditionPrice()
    {
        return fAdditionPrice;
    }

    public void  setFAdditionPrice(float fAdditionPrice)
    {
        this.fAdditionPrice = fAdditionPrice;
    }

    public float getFLiftedCount()
    {
        return fLiftedCount;
    }

    public void  setFLiftedCount(float fLiftedCount)
    {
        this.fLiftedCount = fLiftedCount;
    }

    public int getILockexMonth()
    {
        return iLockexMonth;
    }

    public void  setILockexMonth(int iLockexMonth)
    {
        this.iLockexMonth = iLockexMonth;
    }

    public String getSIndustry()
    {
        return sIndustry;
    }

    public void  setSIndustry(String sIndustry)
    {
        this.sIndustry = sIndustry;
    }

    public float getFScore()
    {
        return fScore;
    }

    public void  setFScore(float fScore)
    {
        this.fScore = fScore;
    }

    public float getFPERatio()
    {
        return fPERatio;
    }

    public void  setFPERatio(float fPERatio)
    {
        this.fPERatio = fPERatio;
    }

    public String getSType()
    {
        return sType;
    }

    public void  setSType(String sType)
    {
        this.sType = sType;
    }

    public java.util.ArrayList<BEC.FiltedDetail> getVLiftedDetail()
    {
        return vLiftedDetail;
    }

    public void  setVLiftedDetail(java.util.ArrayList<BEC.FiltedDetail> vLiftedDetail)
    {
        this.vLiftedDetail = vLiftedDetail;
    }

    public AdditionStockLifted()
    {
    }

    public AdditionStockLifted(String sId, String sDtSecCode, String sSecCode, String sSecName, float fFund, float fFundRate, float fPriceRate, String sDate, float fClosePrice, float fAdditionPrice, float fLiftedCount, int iLockexMonth, String sIndustry, float fScore, float fPERatio, String sType, java.util.ArrayList<BEC.FiltedDetail> vLiftedDetail)
    {
        this.sId = sId;
        this.sDtSecCode = sDtSecCode;
        this.sSecCode = sSecCode;
        this.sSecName = sSecName;
        this.fFund = fFund;
        this.fFundRate = fFundRate;
        this.fPriceRate = fPriceRate;
        this.sDate = sDate;
        this.fClosePrice = fClosePrice;
        this.fAdditionPrice = fAdditionPrice;
        this.fLiftedCount = fLiftedCount;
        this.iLockexMonth = iLockexMonth;
        this.sIndustry = sIndustry;
        this.fScore = fScore;
        this.fPERatio = fPERatio;
        this.sType = sType;
        this.vLiftedDetail = vLiftedDetail;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sId) {
            ostream.writeString(0, sId);
        }
        if (null != sDtSecCode) {
            ostream.writeString(1, sDtSecCode);
        }
        if (null != sSecCode) {
            ostream.writeString(2, sSecCode);
        }
        if (null != sSecName) {
            ostream.writeString(3, sSecName);
        }
        ostream.writeFloat(4, fFund);
        ostream.writeFloat(5, fFundRate);
        ostream.writeFloat(6, fPriceRate);
        if (null != sDate) {
            ostream.writeString(7, sDate);
        }
        ostream.writeFloat(8, fClosePrice);
        ostream.writeFloat(9, fAdditionPrice);
        ostream.writeFloat(10, fLiftedCount);
        ostream.writeInt32(11, iLockexMonth);
        if (null != sIndustry) {
            ostream.writeString(12, sIndustry);
        }
        ostream.writeFloat(13, fScore);
        ostream.writeFloat(14, fPERatio);
        if (null != sType) {
            ostream.writeString(15, sType);
        }
        if (null != vLiftedDetail) {
            ostream.writeList(16, vLiftedDetail);
        }
    }

    static java.util.ArrayList<BEC.FiltedDetail> VAR_TYPE_4_VLIFTEDDETAIL = new java.util.ArrayList<BEC.FiltedDetail>();
    static {
        VAR_TYPE_4_VLIFTEDDETAIL.add(new BEC.FiltedDetail());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sId = (String)istream.readString(0, false, this.sId);
        this.sDtSecCode = (String)istream.readString(1, false, this.sDtSecCode);
        this.sSecCode = (String)istream.readString(2, false, this.sSecCode);
        this.sSecName = (String)istream.readString(3, false, this.sSecName);
        this.fFund = (float)istream.readFloat(4, false, this.fFund);
        this.fFundRate = (float)istream.readFloat(5, false, this.fFundRate);
        this.fPriceRate = (float)istream.readFloat(6, false, this.fPriceRate);
        this.sDate = (String)istream.readString(7, false, this.sDate);
        this.fClosePrice = (float)istream.readFloat(8, false, this.fClosePrice);
        this.fAdditionPrice = (float)istream.readFloat(9, false, this.fAdditionPrice);
        this.fLiftedCount = (float)istream.readFloat(10, false, this.fLiftedCount);
        this.iLockexMonth = (int)istream.readInt32(11, false, this.iLockexMonth);
        this.sIndustry = (String)istream.readString(12, false, this.sIndustry);
        this.fScore = (float)istream.readFloat(13, false, this.fScore);
        this.fPERatio = (float)istream.readFloat(14, false, this.fPERatio);
        this.sType = (String)istream.readString(15, false, this.sType);
        this.vLiftedDetail = (java.util.ArrayList<BEC.FiltedDetail>)istream.readList(16, false, VAR_TYPE_4_VLIFTEDDETAIL);
    }

}

