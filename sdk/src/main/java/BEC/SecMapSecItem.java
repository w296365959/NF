package BEC;

public final class SecMapSecItem extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sDtSecCode = "";

    public String sSecName = "";

    public SecCoordsInfo stCoords = null;

    public float fValue = 0;

    public boolean bSuspended = false;

    public float fNow = 0;

    public float fRate = 0;

    public float fAmout = 0;

    public float fTotalmarketvalue = 0;

    public float fMainFundIn = 0;

    public float fIncome = 0;

    public float fProfit = 0;

    public int eOPUnit = 0;

    public int eProfitUnit = 0;

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

    public SecCoordsInfo getStCoords()
    {
        return stCoords;
    }

    public void  setStCoords(SecCoordsInfo stCoords)
    {
        this.stCoords = stCoords;
    }

    public float getFValue()
    {
        return fValue;
    }

    public void  setFValue(float fValue)
    {
        this.fValue = fValue;
    }

    public boolean getBSuspended()
    {
        return bSuspended;
    }

    public void  setBSuspended(boolean bSuspended)
    {
        this.bSuspended = bSuspended;
    }

    public float getFNow()
    {
        return fNow;
    }

    public void  setFNow(float fNow)
    {
        this.fNow = fNow;
    }

    public float getFRate()
    {
        return fRate;
    }

    public void  setFRate(float fRate)
    {
        this.fRate = fRate;
    }

    public float getFAmout()
    {
        return fAmout;
    }

    public void  setFAmout(float fAmout)
    {
        this.fAmout = fAmout;
    }

    public float getFTotalmarketvalue()
    {
        return fTotalmarketvalue;
    }

    public void  setFTotalmarketvalue(float fTotalmarketvalue)
    {
        this.fTotalmarketvalue = fTotalmarketvalue;
    }

    public float getFMainFundIn()
    {
        return fMainFundIn;
    }

    public void  setFMainFundIn(float fMainFundIn)
    {
        this.fMainFundIn = fMainFundIn;
    }

    public float getFIncome()
    {
        return fIncome;
    }

    public void  setFIncome(float fIncome)
    {
        this.fIncome = fIncome;
    }

    public float getFProfit()
    {
        return fProfit;
    }

    public void  setFProfit(float fProfit)
    {
        this.fProfit = fProfit;
    }

    public int getEOPUnit()
    {
        return eOPUnit;
    }

    public void  setEOPUnit(int eOPUnit)
    {
        this.eOPUnit = eOPUnit;
    }

    public int getEProfitUnit()
    {
        return eProfitUnit;
    }

    public void  setEProfitUnit(int eProfitUnit)
    {
        this.eProfitUnit = eProfitUnit;
    }

    public SecMapSecItem()
    {
    }

    public SecMapSecItem(String sDtSecCode, String sSecName, SecCoordsInfo stCoords, float fValue, boolean bSuspended, float fNow, float fRate, float fAmout, float fTotalmarketvalue, float fMainFundIn, float fIncome, float fProfit, int eOPUnit, int eProfitUnit)
    {
        this.sDtSecCode = sDtSecCode;
        this.sSecName = sSecName;
        this.stCoords = stCoords;
        this.fValue = fValue;
        this.bSuspended = bSuspended;
        this.fNow = fNow;
        this.fRate = fRate;
        this.fAmout = fAmout;
        this.fTotalmarketvalue = fTotalmarketvalue;
        this.fMainFundIn = fMainFundIn;
        this.fIncome = fIncome;
        this.fProfit = fProfit;
        this.eOPUnit = eOPUnit;
        this.eProfitUnit = eProfitUnit;
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
        if (null != stCoords) {
            ostream.writeMessage(2, stCoords);
        }
        ostream.writeFloat(3, fValue);
        ostream.writeBoolean(4, bSuspended);
        ostream.writeFloat(5, fNow);
        ostream.writeFloat(6, fRate);
        ostream.writeFloat(7, fAmout);
        ostream.writeFloat(8, fTotalmarketvalue);
        ostream.writeFloat(9, fMainFundIn);
        ostream.writeFloat(10, fIncome);
        ostream.writeFloat(11, fProfit);
        ostream.writeInt32(12, eOPUnit);
        ostream.writeInt32(13, eProfitUnit);
    }

    static SecCoordsInfo VAR_TYPE_4_STCOORDS = new SecCoordsInfo();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sDtSecCode = (String)istream.readString(0, false, this.sDtSecCode);
        this.sSecName = (String)istream.readString(1, false, this.sSecName);
        this.stCoords = (SecCoordsInfo)istream.readMessage(2, false, VAR_TYPE_4_STCOORDS);
        this.fValue = (float)istream.readFloat(3, false, this.fValue);
        this.bSuspended = (boolean)istream.readBoolean(4, false, this.bSuspended);
        this.fNow = (float)istream.readFloat(5, false, this.fNow);
        this.fRate = (float)istream.readFloat(6, false, this.fRate);
        this.fAmout = (float)istream.readFloat(7, false, this.fAmout);
        this.fTotalmarketvalue = (float)istream.readFloat(8, false, this.fTotalmarketvalue);
        this.fMainFundIn = (float)istream.readFloat(9, false, this.fMainFundIn);
        this.fIncome = (float)istream.readFloat(10, false, this.fIncome);
        this.fProfit = (float)istream.readFloat(11, false, this.fProfit);
        this.eOPUnit = (int)istream.readInt32(12, false, this.eOPUnit);
        this.eProfitUnit = (int)istream.readInt32(13, false, this.eProfitUnit);
    }

}

