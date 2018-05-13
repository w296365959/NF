package BEC;

public final class TransStatDesc extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sDtSecCode = "";

    public int eTranStatType = 0;

    public int iNo = 0;

    public long lTotalAmt = 0;

    public float fPriceAvg = 0;

    public long lTotalVol = 0;

    public int iLastTime = 0;

    public String getSDtSecCode()
    {
        return sDtSecCode;
    }

    public void  setSDtSecCode(String sDtSecCode)
    {
        this.sDtSecCode = sDtSecCode;
    }

    public int getETranStatType()
    {
        return eTranStatType;
    }

    public void  setETranStatType(int eTranStatType)
    {
        this.eTranStatType = eTranStatType;
    }

    public int getINo()
    {
        return iNo;
    }

    public void  setINo(int iNo)
    {
        this.iNo = iNo;
    }

    public long getLTotalAmt()
    {
        return lTotalAmt;
    }

    public void  setLTotalAmt(long lTotalAmt)
    {
        this.lTotalAmt = lTotalAmt;
    }

    public float getFPriceAvg()
    {
        return fPriceAvg;
    }

    public void  setFPriceAvg(float fPriceAvg)
    {
        this.fPriceAvg = fPriceAvg;
    }

    public long getLTotalVol()
    {
        return lTotalVol;
    }

    public void  setLTotalVol(long lTotalVol)
    {
        this.lTotalVol = lTotalVol;
    }

    public int getILastTime()
    {
        return iLastTime;
    }

    public void  setILastTime(int iLastTime)
    {
        this.iLastTime = iLastTime;
    }

    public TransStatDesc()
    {
    }

    public TransStatDesc(String sDtSecCode, int eTranStatType, int iNo, long lTotalAmt, float fPriceAvg, long lTotalVol, int iLastTime)
    {
        this.sDtSecCode = sDtSecCode;
        this.eTranStatType = eTranStatType;
        this.iNo = iNo;
        this.lTotalAmt = lTotalAmt;
        this.fPriceAvg = fPriceAvg;
        this.lTotalVol = lTotalVol;
        this.iLastTime = iLastTime;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sDtSecCode) {
            ostream.writeString(0, sDtSecCode);
        }
        ostream.writeInt32(1, eTranStatType);
        ostream.writeInt32(2, iNo);
        ostream.writeInt64(3, lTotalAmt);
        ostream.writeFloat(4, fPriceAvg);
        ostream.writeInt64(5, lTotalVol);
        ostream.writeInt32(6, iLastTime);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sDtSecCode = (String)istream.readString(0, false, this.sDtSecCode);
        this.eTranStatType = (int)istream.readInt32(1, false, this.eTranStatType);
        this.iNo = (int)istream.readInt32(2, false, this.iNo);
        this.lTotalAmt = (long)istream.readInt64(3, false, this.lTotalAmt);
        this.fPriceAvg = (float)istream.readFloat(4, false, this.fPriceAvg);
        this.lTotalVol = (long)istream.readInt64(5, false, this.lTotalVol);
        this.iLastTime = (int)istream.readInt32(6, false, this.iLastTime);
    }

}

