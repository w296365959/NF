package BEC;

public final class LHBReasonDetail extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sSaleDepName = "";

    public float fBuyAmount = 0;

    public float fSellAmount = 0;

    public int eActType = 0;

    public String sDtSecCode = "";

    public String sSecName = "";

    public float fIncome = 0;

    public String getSSaleDepName()
    {
        return sSaleDepName;
    }

    public void  setSSaleDepName(String sSaleDepName)
    {
        this.sSaleDepName = sSaleDepName;
    }

    public float getFBuyAmount()
    {
        return fBuyAmount;
    }

    public void  setFBuyAmount(float fBuyAmount)
    {
        this.fBuyAmount = fBuyAmount;
    }

    public float getFSellAmount()
    {
        return fSellAmount;
    }

    public void  setFSellAmount(float fSellAmount)
    {
        this.fSellAmount = fSellAmount;
    }

    public int getEActType()
    {
        return eActType;
    }

    public void  setEActType(int eActType)
    {
        this.eActType = eActType;
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

    public float getFIncome()
    {
        return fIncome;
    }

    public void  setFIncome(float fIncome)
    {
        this.fIncome = fIncome;
    }

    public LHBReasonDetail()
    {
    }

    public LHBReasonDetail(String sSaleDepName, float fBuyAmount, float fSellAmount, int eActType, String sDtSecCode, String sSecName, float fIncome)
    {
        this.sSaleDepName = sSaleDepName;
        this.fBuyAmount = fBuyAmount;
        this.fSellAmount = fSellAmount;
        this.eActType = eActType;
        this.sDtSecCode = sDtSecCode;
        this.sSecName = sSecName;
        this.fIncome = fIncome;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sSaleDepName) {
            ostream.writeString(0, sSaleDepName);
        }
        ostream.writeFloat(1, fBuyAmount);
        ostream.writeFloat(2, fSellAmount);
        ostream.writeInt32(3, eActType);
        if (null != sDtSecCode) {
            ostream.writeString(4, sDtSecCode);
        }
        if (null != sSecName) {
            ostream.writeString(5, sSecName);
        }
        ostream.writeFloat(6, fIncome);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sSaleDepName = (String)istream.readString(0, false, this.sSaleDepName);
        this.fBuyAmount = (float)istream.readFloat(1, false, this.fBuyAmount);
        this.fSellAmount = (float)istream.readFloat(2, false, this.fSellAmount);
        this.eActType = (int)istream.readInt32(3, false, this.eActType);
        this.sDtSecCode = (String)istream.readString(4, false, this.sDtSecCode);
        this.sSecName = (String)istream.readString(5, false, this.sSecName);
        this.fIncome = (float)istream.readFloat(6, false, this.fIncome);
    }

}

