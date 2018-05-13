package BEC;

public final class STMarginTrade extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sDtSecCode = "";

    public float fVal = 0;

    public java.util.ArrayList<BEC.STMTBuy> vtMTBuy = null;

    public int iIndustryRank = 0;

    public int iIndDtNum = 0;

    public String sMarginTradeDesc = "";

    public String getSDtSecCode()
    {
        return sDtSecCode;
    }

    public void  setSDtSecCode(String sDtSecCode)
    {
        this.sDtSecCode = sDtSecCode;
    }

    public float getFVal()
    {
        return fVal;
    }

    public void  setFVal(float fVal)
    {
        this.fVal = fVal;
    }

    public java.util.ArrayList<BEC.STMTBuy> getVtMTBuy()
    {
        return vtMTBuy;
    }

    public void  setVtMTBuy(java.util.ArrayList<BEC.STMTBuy> vtMTBuy)
    {
        this.vtMTBuy = vtMTBuy;
    }

    public int getIIndustryRank()
    {
        return iIndustryRank;
    }

    public void  setIIndustryRank(int iIndustryRank)
    {
        this.iIndustryRank = iIndustryRank;
    }

    public int getIIndDtNum()
    {
        return iIndDtNum;
    }

    public void  setIIndDtNum(int iIndDtNum)
    {
        this.iIndDtNum = iIndDtNum;
    }

    public String getSMarginTradeDesc()
    {
        return sMarginTradeDesc;
    }

    public void  setSMarginTradeDesc(String sMarginTradeDesc)
    {
        this.sMarginTradeDesc = sMarginTradeDesc;
    }

    public STMarginTrade()
    {
    }

    public STMarginTrade(String sDtSecCode, float fVal, java.util.ArrayList<BEC.STMTBuy> vtMTBuy, int iIndustryRank, int iIndDtNum, String sMarginTradeDesc)
    {
        this.sDtSecCode = sDtSecCode;
        this.fVal = fVal;
        this.vtMTBuy = vtMTBuy;
        this.iIndustryRank = iIndustryRank;
        this.iIndDtNum = iIndDtNum;
        this.sMarginTradeDesc = sMarginTradeDesc;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sDtSecCode) {
            ostream.writeString(0, sDtSecCode);
        }
        ostream.writeFloat(1, fVal);
        if (null != vtMTBuy) {
            ostream.writeList(2, vtMTBuy);
        }
        ostream.writeInt32(3, iIndustryRank);
        ostream.writeInt32(4, iIndDtNum);
        if (null != sMarginTradeDesc) {
            ostream.writeString(5, sMarginTradeDesc);
        }
    }

    static java.util.ArrayList<BEC.STMTBuy> VAR_TYPE_4_VTMTBUY = new java.util.ArrayList<BEC.STMTBuy>();
    static {
        VAR_TYPE_4_VTMTBUY.add(new BEC.STMTBuy());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sDtSecCode = (String)istream.readString(0, false, this.sDtSecCode);
        this.fVal = (float)istream.readFloat(1, false, this.fVal);
        this.vtMTBuy = (java.util.ArrayList<BEC.STMTBuy>)istream.readList(2, false, VAR_TYPE_4_VTMTBUY);
        this.iIndustryRank = (int)istream.readInt32(3, false, this.iIndustryRank);
        this.iIndDtNum = (int)istream.readInt32(4, false, this.iIndDtNum);
        this.sMarginTradeDesc = (String)istream.readString(5, false, this.sMarginTradeDesc);
    }

}

