package BEC;

public final class ChipDistRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sDtSecCode = "";

    public float fAvgCost = 0;

    public float fEarningPer = 0;

    public float fClosePrice = 0;

    public String sUpdateDate = "";

    public java.util.ArrayList<BEC.StockChipDist> vStockChipDist = null;

    public float fSupportPrice = 0;

    public float fPressPrice = 0;

    public String sChipGroupInfo = "";

    public java.util.ArrayList<BEC.StockCostClose> vtStockCostClose = null;

    public float fConcentration = 0;

    public String sEarnConcenDesc = "";

    public java.util.ArrayList<BEC.StockChipDist> vtMainChipDist = null;

    public float fMainAvgCost = 0;

    public float fMainEarnPer = 0;

    public float fMainIncRate = 0;

    public int iCommChipUpdTime = 0;

    public String getSDtSecCode()
    {
        return sDtSecCode;
    }

    public void  setSDtSecCode(String sDtSecCode)
    {
        this.sDtSecCode = sDtSecCode;
    }

    public float getFAvgCost()
    {
        return fAvgCost;
    }

    public void  setFAvgCost(float fAvgCost)
    {
        this.fAvgCost = fAvgCost;
    }

    public float getFEarningPer()
    {
        return fEarningPer;
    }

    public void  setFEarningPer(float fEarningPer)
    {
        this.fEarningPer = fEarningPer;
    }

    public float getFClosePrice()
    {
        return fClosePrice;
    }

    public void  setFClosePrice(float fClosePrice)
    {
        this.fClosePrice = fClosePrice;
    }

    public String getSUpdateDate()
    {
        return sUpdateDate;
    }

    public void  setSUpdateDate(String sUpdateDate)
    {
        this.sUpdateDate = sUpdateDate;
    }

    public java.util.ArrayList<BEC.StockChipDist> getVStockChipDist()
    {
        return vStockChipDist;
    }

    public void  setVStockChipDist(java.util.ArrayList<BEC.StockChipDist> vStockChipDist)
    {
        this.vStockChipDist = vStockChipDist;
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

    public String getSChipGroupInfo()
    {
        return sChipGroupInfo;
    }

    public void  setSChipGroupInfo(String sChipGroupInfo)
    {
        this.sChipGroupInfo = sChipGroupInfo;
    }

    public java.util.ArrayList<BEC.StockCostClose> getVtStockCostClose()
    {
        return vtStockCostClose;
    }

    public void  setVtStockCostClose(java.util.ArrayList<BEC.StockCostClose> vtStockCostClose)
    {
        this.vtStockCostClose = vtStockCostClose;
    }

    public float getFConcentration()
    {
        return fConcentration;
    }

    public void  setFConcentration(float fConcentration)
    {
        this.fConcentration = fConcentration;
    }

    public String getSEarnConcenDesc()
    {
        return sEarnConcenDesc;
    }

    public void  setSEarnConcenDesc(String sEarnConcenDesc)
    {
        this.sEarnConcenDesc = sEarnConcenDesc;
    }

    public java.util.ArrayList<BEC.StockChipDist> getVtMainChipDist()
    {
        return vtMainChipDist;
    }

    public void  setVtMainChipDist(java.util.ArrayList<BEC.StockChipDist> vtMainChipDist)
    {
        this.vtMainChipDist = vtMainChipDist;
    }

    public float getFMainAvgCost()
    {
        return fMainAvgCost;
    }

    public void  setFMainAvgCost(float fMainAvgCost)
    {
        this.fMainAvgCost = fMainAvgCost;
    }

    public float getFMainEarnPer()
    {
        return fMainEarnPer;
    }

    public void  setFMainEarnPer(float fMainEarnPer)
    {
        this.fMainEarnPer = fMainEarnPer;
    }

    public float getFMainIncRate()
    {
        return fMainIncRate;
    }

    public void  setFMainIncRate(float fMainIncRate)
    {
        this.fMainIncRate = fMainIncRate;
    }

    public int getICommChipUpdTime()
    {
        return iCommChipUpdTime;
    }

    public void  setICommChipUpdTime(int iCommChipUpdTime)
    {
        this.iCommChipUpdTime = iCommChipUpdTime;
    }

    public ChipDistRsp()
    {
    }

    public ChipDistRsp(String sDtSecCode, float fAvgCost, float fEarningPer, float fClosePrice, String sUpdateDate, java.util.ArrayList<BEC.StockChipDist> vStockChipDist, float fSupportPrice, float fPressPrice, String sChipGroupInfo, java.util.ArrayList<BEC.StockCostClose> vtStockCostClose, float fConcentration, String sEarnConcenDesc, java.util.ArrayList<BEC.StockChipDist> vtMainChipDist, float fMainAvgCost, float fMainEarnPer, float fMainIncRate, int iCommChipUpdTime)
    {
        this.sDtSecCode = sDtSecCode;
        this.fAvgCost = fAvgCost;
        this.fEarningPer = fEarningPer;
        this.fClosePrice = fClosePrice;
        this.sUpdateDate = sUpdateDate;
        this.vStockChipDist = vStockChipDist;
        this.fSupportPrice = fSupportPrice;
        this.fPressPrice = fPressPrice;
        this.sChipGroupInfo = sChipGroupInfo;
        this.vtStockCostClose = vtStockCostClose;
        this.fConcentration = fConcentration;
        this.sEarnConcenDesc = sEarnConcenDesc;
        this.vtMainChipDist = vtMainChipDist;
        this.fMainAvgCost = fMainAvgCost;
        this.fMainEarnPer = fMainEarnPer;
        this.fMainIncRate = fMainIncRate;
        this.iCommChipUpdTime = iCommChipUpdTime;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sDtSecCode) {
            ostream.writeString(0, sDtSecCode);
        }
        ostream.writeFloat(1, fAvgCost);
        ostream.writeFloat(2, fEarningPer);
        ostream.writeFloat(3, fClosePrice);
        if (null != sUpdateDate) {
            ostream.writeString(4, sUpdateDate);
        }
        if (null != vStockChipDist) {
            ostream.writeList(5, vStockChipDist);
        }
        ostream.writeFloat(6, fSupportPrice);
        ostream.writeFloat(7, fPressPrice);
        if (null != sChipGroupInfo) {
            ostream.writeString(8, sChipGroupInfo);
        }
        if (null != vtStockCostClose) {
            ostream.writeList(9, vtStockCostClose);
        }
        ostream.writeFloat(10, fConcentration);
        if (null != sEarnConcenDesc) {
            ostream.writeString(11, sEarnConcenDesc);
        }
        if (null != vtMainChipDist) {
            ostream.writeList(12, vtMainChipDist);
        }
        ostream.writeFloat(13, fMainAvgCost);
        ostream.writeFloat(14, fMainEarnPer);
        ostream.writeFloat(15, fMainIncRate);
        ostream.writeInt32(16, iCommChipUpdTime);
    }

    static java.util.ArrayList<BEC.StockChipDist> VAR_TYPE_4_VSTOCKCHIPDIST = new java.util.ArrayList<BEC.StockChipDist>();
    static {
        VAR_TYPE_4_VSTOCKCHIPDIST.add(new BEC.StockChipDist());
    }

    static java.util.ArrayList<BEC.StockCostClose> VAR_TYPE_4_VTSTOCKCOSTCLOSE = new java.util.ArrayList<BEC.StockCostClose>();
    static {
        VAR_TYPE_4_VTSTOCKCOSTCLOSE.add(new BEC.StockCostClose());
    }

    static java.util.ArrayList<BEC.StockChipDist> VAR_TYPE_4_VTMAINCHIPDIST = new java.util.ArrayList<BEC.StockChipDist>();
    static {
        VAR_TYPE_4_VTMAINCHIPDIST.add(new BEC.StockChipDist());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sDtSecCode = (String)istream.readString(0, false, this.sDtSecCode);
        this.fAvgCost = (float)istream.readFloat(1, false, this.fAvgCost);
        this.fEarningPer = (float)istream.readFloat(2, false, this.fEarningPer);
        this.fClosePrice = (float)istream.readFloat(3, false, this.fClosePrice);
        this.sUpdateDate = (String)istream.readString(4, false, this.sUpdateDate);
        this.vStockChipDist = (java.util.ArrayList<BEC.StockChipDist>)istream.readList(5, false, VAR_TYPE_4_VSTOCKCHIPDIST);
        this.fSupportPrice = (float)istream.readFloat(6, false, this.fSupportPrice);
        this.fPressPrice = (float)istream.readFloat(7, false, this.fPressPrice);
        this.sChipGroupInfo = (String)istream.readString(8, false, this.sChipGroupInfo);
        this.vtStockCostClose = (java.util.ArrayList<BEC.StockCostClose>)istream.readList(9, false, VAR_TYPE_4_VTSTOCKCOSTCLOSE);
        this.fConcentration = (float)istream.readFloat(10, false, this.fConcentration);
        this.sEarnConcenDesc = (String)istream.readString(11, false, this.sEarnConcenDesc);
        this.vtMainChipDist = (java.util.ArrayList<BEC.StockChipDist>)istream.readList(12, false, VAR_TYPE_4_VTMAINCHIPDIST);
        this.fMainAvgCost = (float)istream.readFloat(13, false, this.fMainAvgCost);
        this.fMainEarnPer = (float)istream.readFloat(14, false, this.fMainEarnPer);
        this.fMainIncRate = (float)istream.readFloat(15, false, this.fMainIncRate);
        this.iCommChipUpdTime = (int)istream.readInt32(16, false, this.iCommChipUpdTime);
    }

}

