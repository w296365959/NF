package BEC;

public final class SecDailyInfo extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sDtSecCode = "";

    public String sSecName = "";

    public java.util.ArrayList<SecStatusInfo> vStatus = null;

    public float fClose = 0;

    public float fUpPct = 0;

    public float fWinSHIndexUpPct = 0;

    public float fWinIndusPlatePct = 0;

    public String sIndusPlateName = "";

    public String sIndusPlateCode = "";

    public float fMainFundIn = 0;

    public int iPosInHSMarket = 0;

    public int iHSMarketSecNum = 0;

    public int iPosInIndusPlate = 0;

    public int iIndusPlateSecNum = 0;

    public boolean bInLHList = true;

    public float fSupportPrice = 0;

    public float fPressPrice = 0;

    public float fAvgCost = 0;

    public InvestAdviseInfo stAdvise = null;

    public String sTradingDay = "";

    public int eSecStatus = 0;

    public BEC.SecHisStat stHis = null;

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

    public java.util.ArrayList<SecStatusInfo> getVStatus()
    {
        return vStatus;
    }

    public void  setVStatus(java.util.ArrayList<SecStatusInfo> vStatus)
    {
        this.vStatus = vStatus;
    }

    public float getFClose()
    {
        return fClose;
    }

    public void  setFClose(float fClose)
    {
        this.fClose = fClose;
    }

    public float getFUpPct()
    {
        return fUpPct;
    }

    public void  setFUpPct(float fUpPct)
    {
        this.fUpPct = fUpPct;
    }

    public float getFWinSHIndexUpPct()
    {
        return fWinSHIndexUpPct;
    }

    public void  setFWinSHIndexUpPct(float fWinSHIndexUpPct)
    {
        this.fWinSHIndexUpPct = fWinSHIndexUpPct;
    }

    public float getFWinIndusPlatePct()
    {
        return fWinIndusPlatePct;
    }

    public void  setFWinIndusPlatePct(float fWinIndusPlatePct)
    {
        this.fWinIndusPlatePct = fWinIndusPlatePct;
    }

    public String getSIndusPlateName()
    {
        return sIndusPlateName;
    }

    public void  setSIndusPlateName(String sIndusPlateName)
    {
        this.sIndusPlateName = sIndusPlateName;
    }

    public String getSIndusPlateCode()
    {
        return sIndusPlateCode;
    }

    public void  setSIndusPlateCode(String sIndusPlateCode)
    {
        this.sIndusPlateCode = sIndusPlateCode;
    }

    public float getFMainFundIn()
    {
        return fMainFundIn;
    }

    public void  setFMainFundIn(float fMainFundIn)
    {
        this.fMainFundIn = fMainFundIn;
    }

    public int getIPosInHSMarket()
    {
        return iPosInHSMarket;
    }

    public void  setIPosInHSMarket(int iPosInHSMarket)
    {
        this.iPosInHSMarket = iPosInHSMarket;
    }

    public int getIHSMarketSecNum()
    {
        return iHSMarketSecNum;
    }

    public void  setIHSMarketSecNum(int iHSMarketSecNum)
    {
        this.iHSMarketSecNum = iHSMarketSecNum;
    }

    public int getIPosInIndusPlate()
    {
        return iPosInIndusPlate;
    }

    public void  setIPosInIndusPlate(int iPosInIndusPlate)
    {
        this.iPosInIndusPlate = iPosInIndusPlate;
    }

    public int getIIndusPlateSecNum()
    {
        return iIndusPlateSecNum;
    }

    public void  setIIndusPlateSecNum(int iIndusPlateSecNum)
    {
        this.iIndusPlateSecNum = iIndusPlateSecNum;
    }

    public boolean getBInLHList()
    {
        return bInLHList;
    }

    public void  setBInLHList(boolean bInLHList)
    {
        this.bInLHList = bInLHList;
    }

    public float getFSupportPrice()
    {
        return fSupportPrice;
    }

    public void  setFSupportPrice(float fSupportPrice)
    {
        this.fSupportPrice = fSupportPrice;
    }

    public float getFPressPrice()
    {
        return fPressPrice;
    }

    public void  setFPressPrice(float fPressPrice)
    {
        this.fPressPrice = fPressPrice;
    }

    public float getFAvgCost()
    {
        return fAvgCost;
    }

    public void  setFAvgCost(float fAvgCost)
    {
        this.fAvgCost = fAvgCost;
    }

    public InvestAdviseInfo getStAdvise()
    {
        return stAdvise;
    }

    public void  setStAdvise(InvestAdviseInfo stAdvise)
    {
        this.stAdvise = stAdvise;
    }

    public String getSTradingDay()
    {
        return sTradingDay;
    }

    public void  setSTradingDay(String sTradingDay)
    {
        this.sTradingDay = sTradingDay;
    }

    public int getESecStatus()
    {
        return eSecStatus;
    }

    public void  setESecStatus(int eSecStatus)
    {
        this.eSecStatus = eSecStatus;
    }

    public BEC.SecHisStat getStHis()
    {
        return stHis;
    }

    public void  setStHis(BEC.SecHisStat stHis)
    {
        this.stHis = stHis;
    }

    public SecDailyInfo()
    {
    }

    public SecDailyInfo(String sDtSecCode, String sSecName, java.util.ArrayList<SecStatusInfo> vStatus, float fClose, float fUpPct, float fWinSHIndexUpPct, float fWinIndusPlatePct, String sIndusPlateName, String sIndusPlateCode, float fMainFundIn, int iPosInHSMarket, int iHSMarketSecNum, int iPosInIndusPlate, int iIndusPlateSecNum, boolean bInLHList, float fSupportPrice, float fPressPrice, float fAvgCost, InvestAdviseInfo stAdvise, String sTradingDay, int eSecStatus, BEC.SecHisStat stHis)
    {
        this.sDtSecCode = sDtSecCode;
        this.sSecName = sSecName;
        this.vStatus = vStatus;
        this.fClose = fClose;
        this.fUpPct = fUpPct;
        this.fWinSHIndexUpPct = fWinSHIndexUpPct;
        this.fWinIndusPlatePct = fWinIndusPlatePct;
        this.sIndusPlateName = sIndusPlateName;
        this.sIndusPlateCode = sIndusPlateCode;
        this.fMainFundIn = fMainFundIn;
        this.iPosInHSMarket = iPosInHSMarket;
        this.iHSMarketSecNum = iHSMarketSecNum;
        this.iPosInIndusPlate = iPosInIndusPlate;
        this.iIndusPlateSecNum = iIndusPlateSecNum;
        this.bInLHList = bInLHList;
        this.fSupportPrice = fSupportPrice;
        this.fPressPrice = fPressPrice;
        this.fAvgCost = fAvgCost;
        this.stAdvise = stAdvise;
        this.sTradingDay = sTradingDay;
        this.eSecStatus = eSecStatus;
        this.stHis = stHis;
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
        if (null != vStatus) {
            ostream.writeList(2, vStatus);
        }
        ostream.writeFloat(3, fClose);
        ostream.writeFloat(4, fUpPct);
        ostream.writeFloat(5, fWinSHIndexUpPct);
        ostream.writeFloat(6, fWinIndusPlatePct);
        if (null != sIndusPlateName) {
            ostream.writeString(7, sIndusPlateName);
        }
        if (null != sIndusPlateCode) {
            ostream.writeString(8, sIndusPlateCode);
        }
        ostream.writeFloat(9, fMainFundIn);
        ostream.writeInt32(10, iPosInHSMarket);
        ostream.writeInt32(11, iHSMarketSecNum);
        ostream.writeInt32(12, iPosInIndusPlate);
        ostream.writeInt32(13, iIndusPlateSecNum);
        ostream.writeBoolean(14, bInLHList);
        ostream.writeFloat(15, fSupportPrice);
        ostream.writeFloat(16, fPressPrice);
        ostream.writeFloat(17, fAvgCost);
        if (null != stAdvise) {
            ostream.writeMessage(18, stAdvise);
        }
        if (null != sTradingDay) {
            ostream.writeString(19, sTradingDay);
        }
        ostream.writeInt32(20, eSecStatus);
        if (null != stHis) {
            ostream.writeMessage(21, stHis);
        }
    }

    static java.util.ArrayList<SecStatusInfo> VAR_TYPE_4_VSTATUS = new java.util.ArrayList<SecStatusInfo>();
    static {
        VAR_TYPE_4_VSTATUS.add(new SecStatusInfo());
    }

    static InvestAdviseInfo VAR_TYPE_4_STADVISE = new InvestAdviseInfo();

    static BEC.SecHisStat VAR_TYPE_4_STHIS = new BEC.SecHisStat();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sDtSecCode = (String)istream.readString(0, false, this.sDtSecCode);
        this.sSecName = (String)istream.readString(1, false, this.sSecName);
        this.vStatus = (java.util.ArrayList<SecStatusInfo>)istream.readList(2, false, VAR_TYPE_4_VSTATUS);
        this.fClose = (float)istream.readFloat(3, false, this.fClose);
        this.fUpPct = (float)istream.readFloat(4, false, this.fUpPct);
        this.fWinSHIndexUpPct = (float)istream.readFloat(5, false, this.fWinSHIndexUpPct);
        this.fWinIndusPlatePct = (float)istream.readFloat(6, false, this.fWinIndusPlatePct);
        this.sIndusPlateName = (String)istream.readString(7, false, this.sIndusPlateName);
        this.sIndusPlateCode = (String)istream.readString(8, false, this.sIndusPlateCode);
        this.fMainFundIn = (float)istream.readFloat(9, false, this.fMainFundIn);
        this.iPosInHSMarket = (int)istream.readInt32(10, false, this.iPosInHSMarket);
        this.iHSMarketSecNum = (int)istream.readInt32(11, false, this.iHSMarketSecNum);
        this.iPosInIndusPlate = (int)istream.readInt32(12, false, this.iPosInIndusPlate);
        this.iIndusPlateSecNum = (int)istream.readInt32(13, false, this.iIndusPlateSecNum);
        this.bInLHList = (boolean)istream.readBoolean(14, false, this.bInLHList);
        this.fSupportPrice = (float)istream.readFloat(15, false, this.fSupportPrice);
        this.fPressPrice = (float)istream.readFloat(16, false, this.fPressPrice);
        this.fAvgCost = (float)istream.readFloat(17, false, this.fAvgCost);
        this.stAdvise = (InvestAdviseInfo)istream.readMessage(18, false, VAR_TYPE_4_STADVISE);
        this.sTradingDay = (String)istream.readString(19, false, this.sTradingDay);
        this.eSecStatus = (int)istream.readInt32(20, false, this.eSecStatus);
        this.stHis = (BEC.SecHisStat)istream.readMessage(21, false, VAR_TYPE_4_STHIS);
    }

}

