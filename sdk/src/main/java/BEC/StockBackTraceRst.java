package BEC;

public final class StockBackTraceRst extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sDtSecCode = "";

    public int e_strategy_type = 0;

    public int iHoldDays = 0;

    public float fIncRateAvg = 0;

    public float fWinPer = 0;

    public String getSDtSecCode()
    {
        return sDtSecCode;
    }

    public void  setSDtSecCode(String sDtSecCode)
    {
        this.sDtSecCode = sDtSecCode;
    }

    public int getE_strategy_type()
    {
        return e_strategy_type;
    }

    public void  setE_strategy_type(int e_strategy_type)
    {
        this.e_strategy_type = e_strategy_type;
    }

    public int getIHoldDays()
    {
        return iHoldDays;
    }

    public void  setIHoldDays(int iHoldDays)
    {
        this.iHoldDays = iHoldDays;
    }

    public float getFIncRateAvg()
    {
        return fIncRateAvg;
    }

    public void  setFIncRateAvg(float fIncRateAvg)
    {
        this.fIncRateAvg = fIncRateAvg;
    }

    public float getFWinPer()
    {
        return fWinPer;
    }

    public void  setFWinPer(float fWinPer)
    {
        this.fWinPer = fWinPer;
    }

    public StockBackTraceRst()
    {
    }

    public StockBackTraceRst(String sDtSecCode, int e_strategy_type, int iHoldDays, float fIncRateAvg, float fWinPer)
    {
        this.sDtSecCode = sDtSecCode;
        this.e_strategy_type = e_strategy_type;
        this.iHoldDays = iHoldDays;
        this.fIncRateAvg = fIncRateAvg;
        this.fWinPer = fWinPer;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sDtSecCode) {
            ostream.writeString(0, sDtSecCode);
        }
        ostream.writeInt32(1, e_strategy_type);
        ostream.writeInt32(2, iHoldDays);
        ostream.writeFloat(3, fIncRateAvg);
        ostream.writeFloat(4, fWinPer);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sDtSecCode = (String)istream.readString(0, false, this.sDtSecCode);
        this.e_strategy_type = (int)istream.readInt32(1, false, this.e_strategy_type);
        this.iHoldDays = (int)istream.readInt32(2, false, this.iHoldDays);
        this.fIncRateAvg = (float)istream.readFloat(3, false, this.fIncRateAvg);
        this.fWinPer = (float)istream.readFloat(4, false, this.fWinPer);
    }

}

