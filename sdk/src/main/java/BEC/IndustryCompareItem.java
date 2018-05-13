package BEC;

public final class IndustryCompareItem extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iOrder = 0;

    public String sName = "";

    public String sValue = "";

    public int iUpdateTime = 0;

    public String sDtSecCode = "";

    public double dValue = 0;

    public int getIOrder()
    {
        return iOrder;
    }

    public void  setIOrder(int iOrder)
    {
        this.iOrder = iOrder;
    }

    public String getSName()
    {
        return sName;
    }

    public void  setSName(String sName)
    {
        this.sName = sName;
    }

    public String getSValue()
    {
        return sValue;
    }

    public void  setSValue(String sValue)
    {
        this.sValue = sValue;
    }

    public int getIUpdateTime()
    {
        return iUpdateTime;
    }

    public void  setIUpdateTime(int iUpdateTime)
    {
        this.iUpdateTime = iUpdateTime;
    }

    public String getSDtSecCode()
    {
        return sDtSecCode;
    }

    public void  setSDtSecCode(String sDtSecCode)
    {
        this.sDtSecCode = sDtSecCode;
    }

    public double getDValue()
    {
        return dValue;
    }

    public void  setDValue(double dValue)
    {
        this.dValue = dValue;
    }

    public IndustryCompareItem()
    {
    }

    public IndustryCompareItem(int iOrder, String sName, String sValue, int iUpdateTime, String sDtSecCode, double dValue)
    {
        this.iOrder = iOrder;
        this.sName = sName;
        this.sValue = sValue;
        this.iUpdateTime = iUpdateTime;
        this.sDtSecCode = sDtSecCode;
        this.dValue = dValue;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iOrder);
        if (null != sName) {
            ostream.writeString(1, sName);
        }
        if (null != sValue) {
            ostream.writeString(2, sValue);
        }
        ostream.writeInt32(3, iUpdateTime);
        if (null != sDtSecCode) {
            ostream.writeString(4, sDtSecCode);
        }
        ostream.writeDouble(5, dValue);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iOrder = (int)istream.readInt32(0, false, this.iOrder);
        this.sName = (String)istream.readString(1, false, this.sName);
        this.sValue = (String)istream.readString(2, false, this.sValue);
        this.iUpdateTime = (int)istream.readInt32(3, false, this.iUpdateTime);
        this.sDtSecCode = (String)istream.readString(4, false, this.sDtSecCode);
        this.dValue = (double)istream.readDouble(5, false, this.dValue);
    }

}

