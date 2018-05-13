package BEC;

public final class STInsRes extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sDtSecCode = "";

    public float fVal = 0;

    public int iIndustryRank = 0;

    public int iIndDtNum = 0;

    public float fIndInsResAvg = 0;

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

    public float getFIndInsResAvg()
    {
        return fIndInsResAvg;
    }

    public void  setFIndInsResAvg(float fIndInsResAvg)
    {
        this.fIndInsResAvg = fIndInsResAvg;
    }

    public STInsRes()
    {
    }

    public STInsRes(String sDtSecCode, float fVal, int iIndustryRank, int iIndDtNum, float fIndInsResAvg)
    {
        this.sDtSecCode = sDtSecCode;
        this.fVal = fVal;
        this.iIndustryRank = iIndustryRank;
        this.iIndDtNum = iIndDtNum;
        this.fIndInsResAvg = fIndInsResAvg;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sDtSecCode) {
            ostream.writeString(0, sDtSecCode);
        }
        ostream.writeFloat(1, fVal);
        ostream.writeInt32(2, iIndustryRank);
        ostream.writeInt32(3, iIndDtNum);
        ostream.writeFloat(4, fIndInsResAvg);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sDtSecCode = (String)istream.readString(0, false, this.sDtSecCode);
        this.fVal = (float)istream.readFloat(1, false, this.fVal);
        this.iIndustryRank = (int)istream.readInt32(2, false, this.iIndustryRank);
        this.iIndDtNum = (int)istream.readInt32(3, false, this.iIndDtNum);
        this.fIndInsResAvg = (float)istream.readFloat(4, false, this.fIndInsResAvg);
    }

}

