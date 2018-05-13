package BEC;

public final class STLongHu extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sDtSecCode = "";

    public float fVal = 0;

    public float fSumNetBuy = 0;

    public java.util.ArrayList<BEC.STLHDateNet> vtDateNet = null;

    public int iIndustryRank = 0;

    public int iIndDtNum = 0;

    public String sLongHuDesc = "";

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

    public float getFSumNetBuy()
    {
        return fSumNetBuy;
    }

    public void  setFSumNetBuy(float fSumNetBuy)
    {
        this.fSumNetBuy = fSumNetBuy;
    }

    public java.util.ArrayList<BEC.STLHDateNet> getVtDateNet()
    {
        return vtDateNet;
    }

    public void  setVtDateNet(java.util.ArrayList<BEC.STLHDateNet> vtDateNet)
    {
        this.vtDateNet = vtDateNet;
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

    public String getSLongHuDesc()
    {
        return sLongHuDesc;
    }

    public void  setSLongHuDesc(String sLongHuDesc)
    {
        this.sLongHuDesc = sLongHuDesc;
    }

    public STLongHu()
    {
    }

    public STLongHu(String sDtSecCode, float fVal, float fSumNetBuy, java.util.ArrayList<BEC.STLHDateNet> vtDateNet, int iIndustryRank, int iIndDtNum, String sLongHuDesc)
    {
        this.sDtSecCode = sDtSecCode;
        this.fVal = fVal;
        this.fSumNetBuy = fSumNetBuy;
        this.vtDateNet = vtDateNet;
        this.iIndustryRank = iIndustryRank;
        this.iIndDtNum = iIndDtNum;
        this.sLongHuDesc = sLongHuDesc;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sDtSecCode) {
            ostream.writeString(0, sDtSecCode);
        }
        ostream.writeFloat(1, fVal);
        ostream.writeFloat(2, fSumNetBuy);
        if (null != vtDateNet) {
            ostream.writeList(3, vtDateNet);
        }
        ostream.writeInt32(4, iIndustryRank);
        ostream.writeInt32(5, iIndDtNum);
        if (null != sLongHuDesc) {
            ostream.writeString(6, sLongHuDesc);
        }
    }

    static java.util.ArrayList<BEC.STLHDateNet> VAR_TYPE_4_VTDATENET = new java.util.ArrayList<BEC.STLHDateNet>();
    static {
        VAR_TYPE_4_VTDATENET.add(new BEC.STLHDateNet());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sDtSecCode = (String)istream.readString(0, false, this.sDtSecCode);
        this.fVal = (float)istream.readFloat(1, false, this.fVal);
        this.fSumNetBuy = (float)istream.readFloat(2, false, this.fSumNetBuy);
        this.vtDateNet = (java.util.ArrayList<BEC.STLHDateNet>)istream.readList(3, false, VAR_TYPE_4_VTDATENET);
        this.iIndustryRank = (int)istream.readInt32(4, false, this.iIndustryRank);
        this.iIndDtNum = (int)istream.readInt32(5, false, this.iIndDtNum);
        this.sLongHuDesc = (String)istream.readString(6, false, this.sLongHuDesc);
    }

}

