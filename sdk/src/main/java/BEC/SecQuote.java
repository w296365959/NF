package BEC;

public final class SecQuote extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sDtSecCode = "";

    public String sSecName = "";

    public float fClose = 0;

    public float fOpen = 0;

    public float fMax = 0;

    public float fMin = 0;

    public float fNow = 0;

    public float fBuy = 0;

    public float fSell = 0;

    public long lVolume = 0;

    public long lNowVolume = 0;

    public float fAmout = 0;

    public long lInside = 0;

    public long lOutside = 0;

    public float fTickdiff = 0;

    public float fVolinstock = 0;

    public float fFhsl = 0;

    public java.util.ArrayList<Float> vBuyp = null;

    public java.util.ArrayList<Long> vBuyv = null;

    public java.util.ArrayList<Float> vSellp = null;

    public java.util.ArrayList<Long> vSellv = null;

    public byte cInoutflag = 0;

    public long lRestvol = 0;

    public int iTpFlag = 0;

    public float fSyl = 0;

    public int iUpnum = 0;

    public int iDownnum = 0;

    public float fLtg = 0;

    public float fYclose = 0;

    public long lYvolinstock = 0;

    public float fAverageprice = 0;

    public float fTotalmarketvalue = 0;

    public float fCirculationmarketvalue = 0;

    public int iErveyhand = 0;

    public float fDayincrease = 0;

    public float fCointype = 0;

    public float fHot = 0;

    public int iTime = 0;

    public int eSecStatus = BEC.E_SEC_STATUS.E_SS_NORMAL;

    public float fFundNetValue = 0;

    public float fMaxLimit = 0;

    public float fMinLimit = 0;

    public long lVolinstock = 0;

    public float fVolumeRatio = 0;

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

    public float getFClose()
    {
        return fClose;
    }

    public void  setFClose(float fClose)
    {
        this.fClose = fClose;
    }

    public float getFOpen()
    {
        return fOpen;
    }

    public void  setFOpen(float fOpen)
    {
        this.fOpen = fOpen;
    }

    public float getFMax()
    {
        return fMax;
    }

    public void  setFMax(float fMax)
    {
        this.fMax = fMax;
    }

    public float getFMin()
    {
        return fMin;
    }

    public void  setFMin(float fMin)
    {
        this.fMin = fMin;
    }

    public float getFNow()
    {
        return fNow;
    }

    public void  setFNow(float fNow)
    {
        this.fNow = fNow;
    }

    public float getFBuy()
    {
        return fBuy;
    }

    public void  setFBuy(float fBuy)
    {
        this.fBuy = fBuy;
    }

    public float getFSell()
    {
        return fSell;
    }

    public void  setFSell(float fSell)
    {
        this.fSell = fSell;
    }

    public long getLVolume()
    {
        return lVolume;
    }

    public void  setLVolume(long lVolume)
    {
        this.lVolume = lVolume;
    }

    public long getLNowVolume()
    {
        return lNowVolume;
    }

    public void  setLNowVolume(long lNowVolume)
    {
        this.lNowVolume = lNowVolume;
    }

    public float getFAmout()
    {
        return fAmout;
    }

    public void  setFAmout(float fAmout)
    {
        this.fAmout = fAmout;
    }

    public long getLInside()
    {
        return lInside;
    }

    public void  setLInside(long lInside)
    {
        this.lInside = lInside;
    }

    public long getLOutside()
    {
        return lOutside;
    }

    public void  setLOutside(long lOutside)
    {
        this.lOutside = lOutside;
    }

    public float getFTickdiff()
    {
        return fTickdiff;
    }

    public void  setFTickdiff(float fTickdiff)
    {
        this.fTickdiff = fTickdiff;
    }

    public float getFVolinstock()
    {
        return fVolinstock;
    }

    public void  setFVolinstock(float fVolinstock)
    {
        this.fVolinstock = fVolinstock;
    }

    public float getFFhsl()
    {
        return fFhsl;
    }

    public void  setFFhsl(float fFhsl)
    {
        this.fFhsl = fFhsl;
    }

    public java.util.ArrayList<Float> getVBuyp()
    {
        return vBuyp;
    }

    public void  setVBuyp(java.util.ArrayList<Float> vBuyp)
    {
        this.vBuyp = vBuyp;
    }

    public java.util.ArrayList<Long> getVBuyv()
    {
        return vBuyv;
    }

    public void  setVBuyv(java.util.ArrayList<Long> vBuyv)
    {
        this.vBuyv = vBuyv;
    }

    public java.util.ArrayList<Float> getVSellp()
    {
        return vSellp;
    }

    public void  setVSellp(java.util.ArrayList<Float> vSellp)
    {
        this.vSellp = vSellp;
    }

    public java.util.ArrayList<Long> getVSellv()
    {
        return vSellv;
    }

    public void  setVSellv(java.util.ArrayList<Long> vSellv)
    {
        this.vSellv = vSellv;
    }

    public byte getCInoutflag()
    {
        return cInoutflag;
    }

    public void  setCInoutflag(byte cInoutflag)
    {
        this.cInoutflag = cInoutflag;
    }

    public long getLRestvol()
    {
        return lRestvol;
    }

    public void  setLRestvol(long lRestvol)
    {
        this.lRestvol = lRestvol;
    }

    public int getITpFlag()
    {
        return iTpFlag;
    }

    public void  setITpFlag(int iTpFlag)
    {
        this.iTpFlag = iTpFlag;
    }

    public float getFSyl()
    {
        return fSyl;
    }

    public void  setFSyl(float fSyl)
    {
        this.fSyl = fSyl;
    }

    public int getIUpnum()
    {
        return iUpnum;
    }

    public void  setIUpnum(int iUpnum)
    {
        this.iUpnum = iUpnum;
    }

    public int getIDownnum()
    {
        return iDownnum;
    }

    public void  setIDownnum(int iDownnum)
    {
        this.iDownnum = iDownnum;
    }

    public float getFLtg()
    {
        return fLtg;
    }

    public void  setFLtg(float fLtg)
    {
        this.fLtg = fLtg;
    }

    public float getFYclose()
    {
        return fYclose;
    }

    public void  setFYclose(float fYclose)
    {
        this.fYclose = fYclose;
    }

    public long getLYvolinstock()
    {
        return lYvolinstock;
    }

    public void  setLYvolinstock(long lYvolinstock)
    {
        this.lYvolinstock = lYvolinstock;
    }

    public float getFAverageprice()
    {
        return fAverageprice;
    }

    public void  setFAverageprice(float fAverageprice)
    {
        this.fAverageprice = fAverageprice;
    }

    public float getFTotalmarketvalue()
    {
        return fTotalmarketvalue;
    }

    public void  setFTotalmarketvalue(float fTotalmarketvalue)
    {
        this.fTotalmarketvalue = fTotalmarketvalue;
    }

    public float getFCirculationmarketvalue()
    {
        return fCirculationmarketvalue;
    }

    public void  setFCirculationmarketvalue(float fCirculationmarketvalue)
    {
        this.fCirculationmarketvalue = fCirculationmarketvalue;
    }

    public int getIErveyhand()
    {
        return iErveyhand;
    }

    public void  setIErveyhand(int iErveyhand)
    {
        this.iErveyhand = iErveyhand;
    }

    public float getFDayincrease()
    {
        return fDayincrease;
    }

    public void  setFDayincrease(float fDayincrease)
    {
        this.fDayincrease = fDayincrease;
    }

    public float getFCointype()
    {
        return fCointype;
    }

    public void  setFCointype(float fCointype)
    {
        this.fCointype = fCointype;
    }

    public float getFHot()
    {
        return fHot;
    }

    public void  setFHot(float fHot)
    {
        this.fHot = fHot;
    }

    public int getITime()
    {
        return iTime;
    }

    public void  setITime(int iTime)
    {
        this.iTime = iTime;
    }

    public int getESecStatus()
    {
        return eSecStatus;
    }

    public void  setESecStatus(int eSecStatus)
    {
        this.eSecStatus = eSecStatus;
    }

    public float getFFundNetValue()
    {
        return fFundNetValue;
    }

    public void  setFFundNetValue(float fFundNetValue)
    {
        this.fFundNetValue = fFundNetValue;
    }

    public float getFMaxLimit()
    {
        return fMaxLimit;
    }

    public void  setFMaxLimit(float fMaxLimit)
    {
        this.fMaxLimit = fMaxLimit;
    }

    public float getFMinLimit()
    {
        return fMinLimit;
    }

    public void  setFMinLimit(float fMinLimit)
    {
        this.fMinLimit = fMinLimit;
    }

    public long getLVolinstock()
    {
        return lVolinstock;
    }

    public void  setLVolinstock(long lVolinstock)
    {
        this.lVolinstock = lVolinstock;
    }

    public float getFVolumeRatio()
    {
        return fVolumeRatio;
    }

    public void  setFVolumeRatio(float fVolumeRatio)
    {
        this.fVolumeRatio = fVolumeRatio;
    }

    public SecQuote()
    {
    }

    public SecQuote(String sDtSecCode, String sSecName, float fClose, float fOpen, float fMax, float fMin, float fNow, float fBuy, float fSell, long lVolume, long lNowVolume, float fAmout, long lInside, long lOutside, float fTickdiff, float fVolinstock, float fFhsl, java.util.ArrayList<Float> vBuyp, java.util.ArrayList<Long> vBuyv, java.util.ArrayList<Float> vSellp, java.util.ArrayList<Long> vSellv, byte cInoutflag, long lRestvol, int iTpFlag, float fSyl, int iUpnum, int iDownnum, float fLtg, float fYclose, long lYvolinstock, float fAverageprice, float fTotalmarketvalue, float fCirculationmarketvalue, int iErveyhand, float fDayincrease, float fCointype, float fHot, int iTime, int eSecStatus, float fFundNetValue, float fMaxLimit, float fMinLimit, long lVolinstock, float fVolumeRatio)
    {
        this.sDtSecCode = sDtSecCode;
        this.sSecName = sSecName;
        this.fClose = fClose;
        this.fOpen = fOpen;
        this.fMax = fMax;
        this.fMin = fMin;
        this.fNow = fNow;
        this.fBuy = fBuy;
        this.fSell = fSell;
        this.lVolume = lVolume;
        this.lNowVolume = lNowVolume;
        this.fAmout = fAmout;
        this.lInside = lInside;
        this.lOutside = lOutside;
        this.fTickdiff = fTickdiff;
        this.fVolinstock = fVolinstock;
        this.fFhsl = fFhsl;
        this.vBuyp = vBuyp;
        this.vBuyv = vBuyv;
        this.vSellp = vSellp;
        this.vSellv = vSellv;
        this.cInoutflag = cInoutflag;
        this.lRestvol = lRestvol;
        this.iTpFlag = iTpFlag;
        this.fSyl = fSyl;
        this.iUpnum = iUpnum;
        this.iDownnum = iDownnum;
        this.fLtg = fLtg;
        this.fYclose = fYclose;
        this.lYvolinstock = lYvolinstock;
        this.fAverageprice = fAverageprice;
        this.fTotalmarketvalue = fTotalmarketvalue;
        this.fCirculationmarketvalue = fCirculationmarketvalue;
        this.iErveyhand = iErveyhand;
        this.fDayincrease = fDayincrease;
        this.fCointype = fCointype;
        this.fHot = fHot;
        this.iTime = iTime;
        this.eSecStatus = eSecStatus;
        this.fFundNetValue = fFundNetValue;
        this.fMaxLimit = fMaxLimit;
        this.fMinLimit = fMinLimit;
        this.lVolinstock = lVolinstock;
        this.fVolumeRatio = fVolumeRatio;
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
        ostream.writeFloat(2, fClose);
        ostream.writeFloat(3, fOpen);
        ostream.writeFloat(4, fMax);
        ostream.writeFloat(5, fMin);
        ostream.writeFloat(6, fNow);
        ostream.writeFloat(7, fBuy);
        ostream.writeFloat(8, fSell);
        ostream.writeInt64(9, lVolume);
        ostream.writeInt64(10, lNowVolume);
        ostream.writeFloat(11, fAmout);
        ostream.writeInt64(12, lInside);
        ostream.writeInt64(13, lOutside);
        ostream.writeFloat(14, fTickdiff);
        ostream.writeFloat(15, fVolinstock);
        ostream.writeFloat(16, fFhsl);
        if (null != vBuyp) {
            ostream.writeList(17, vBuyp);
        }
        if (null != vBuyv) {
            ostream.writeList(18, vBuyv);
        }
        if (null != vSellp) {
            ostream.writeList(19, vSellp);
        }
        if (null != vSellv) {
            ostream.writeList(20, vSellv);
        }
        ostream.writeInt8(21, cInoutflag);
        ostream.writeInt64(22, lRestvol);
        ostream.writeInt32(23, iTpFlag);
        ostream.writeFloat(24, fSyl);
        ostream.writeInt32(25, iUpnum);
        ostream.writeInt32(26, iDownnum);
        ostream.writeFloat(27, fLtg);
        ostream.writeFloat(28, fYclose);
        ostream.writeInt64(29, lYvolinstock);
        ostream.writeFloat(30, fAverageprice);
        ostream.writeFloat(31, fTotalmarketvalue);
        ostream.writeFloat(32, fCirculationmarketvalue);
        ostream.writeInt32(33, iErveyhand);
        ostream.writeFloat(34, fDayincrease);
        ostream.writeFloat(35, fCointype);
        ostream.writeFloat(36, fHot);
        ostream.writeInt32(37, iTime);
        ostream.writeInt32(38, eSecStatus);
        ostream.writeFloat(39, fFundNetValue);
        ostream.writeFloat(40, fMaxLimit);
        ostream.writeFloat(41, fMinLimit);
        ostream.writeInt64(42, lVolinstock);
        ostream.writeFloat(43, fVolumeRatio);
    }

    static java.util.ArrayList<Float> VAR_TYPE_4_VBUYP = new java.util.ArrayList<Float>();
    static {
        VAR_TYPE_4_VBUYP.add(0.0f);
    }

    static java.util.ArrayList<Long> VAR_TYPE_4_VBUYV = new java.util.ArrayList<Long>();
    static {
        VAR_TYPE_4_VBUYV.add(0L);
    }

    static java.util.ArrayList<Float> VAR_TYPE_4_VSELLP = new java.util.ArrayList<Float>();
    static {
        VAR_TYPE_4_VSELLP.add(0.0f);
    }

    static java.util.ArrayList<Long> VAR_TYPE_4_VSELLV = new java.util.ArrayList<Long>();
    static {
        VAR_TYPE_4_VSELLV.add(0L);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sDtSecCode = (String)istream.readString(0, false, this.sDtSecCode);
        this.sSecName = (String)istream.readString(1, false, this.sSecName);
        this.fClose = (float)istream.readFloat(2, false, this.fClose);
        this.fOpen = (float)istream.readFloat(3, false, this.fOpen);
        this.fMax = (float)istream.readFloat(4, false, this.fMax);
        this.fMin = (float)istream.readFloat(5, false, this.fMin);
        this.fNow = (float)istream.readFloat(6, false, this.fNow);
        this.fBuy = (float)istream.readFloat(7, false, this.fBuy);
        this.fSell = (float)istream.readFloat(8, false, this.fSell);
        this.lVolume = (long)istream.readInt64(9, false, this.lVolume);
        this.lNowVolume = (long)istream.readInt64(10, false, this.lNowVolume);
        this.fAmout = (float)istream.readFloat(11, false, this.fAmout);
        this.lInside = (long)istream.readInt64(12, false, this.lInside);
        this.lOutside = (long)istream.readInt64(13, false, this.lOutside);
        this.fTickdiff = (float)istream.readFloat(14, false, this.fTickdiff);
        this.fVolinstock = (float)istream.readFloat(15, false, this.fVolinstock);
        this.fFhsl = (float)istream.readFloat(16, false, this.fFhsl);
        this.vBuyp = (java.util.ArrayList<Float>)istream.readList(17, false, VAR_TYPE_4_VBUYP);
        this.vBuyv = (java.util.ArrayList<Long>)istream.readList(18, false, VAR_TYPE_4_VBUYV);
        this.vSellp = (java.util.ArrayList<Float>)istream.readList(19, false, VAR_TYPE_4_VSELLP);
        this.vSellv = (java.util.ArrayList<Long>)istream.readList(20, false, VAR_TYPE_4_VSELLV);
        this.cInoutflag = (byte)istream.readInt8(21, false, this.cInoutflag);
        this.lRestvol = (long)istream.readInt64(22, false, this.lRestvol);
        this.iTpFlag = (int)istream.readInt32(23, false, this.iTpFlag);
        this.fSyl = (float)istream.readFloat(24, false, this.fSyl);
        this.iUpnum = (int)istream.readInt32(25, false, this.iUpnum);
        this.iDownnum = (int)istream.readInt32(26, false, this.iDownnum);
        this.fLtg = (float)istream.readFloat(27, false, this.fLtg);
        this.fYclose = (float)istream.readFloat(28, false, this.fYclose);
        this.lYvolinstock = (long)istream.readInt64(29, false, this.lYvolinstock);
        this.fAverageprice = (float)istream.readFloat(30, false, this.fAverageprice);
        this.fTotalmarketvalue = (float)istream.readFloat(31, false, this.fTotalmarketvalue);
        this.fCirculationmarketvalue = (float)istream.readFloat(32, false, this.fCirculationmarketvalue);
        this.iErveyhand = (int)istream.readInt32(33, false, this.iErveyhand);
        this.fDayincrease = (float)istream.readFloat(34, false, this.fDayincrease);
        this.fCointype = (float)istream.readFloat(35, false, this.fCointype);
        this.fHot = (float)istream.readFloat(36, false, this.fHot);
        this.iTime = (int)istream.readInt32(37, false, this.iTime);
        this.eSecStatus = (int)istream.readInt32(38, false, this.eSecStatus);
        this.fFundNetValue = (float)istream.readFloat(39, false, this.fFundNetValue);
        this.fMaxLimit = (float)istream.readFloat(40, false, this.fMaxLimit);
        this.fMinLimit = (float)istream.readFloat(41, false, this.fMinLimit);
        this.lVolinstock = (long)istream.readInt64(42, false, this.lVolinstock);
        this.fVolumeRatio = (float)istream.readFloat(43, false, this.fVolumeRatio);
    }

}

