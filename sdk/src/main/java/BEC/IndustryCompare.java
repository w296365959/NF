package BEC;

public final class IndustryCompare extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sCompareType = "";

    public String sUpdateTime = "";

    public java.util.ArrayList<BEC.IndustryCompareItem> vtCompItem = null;

    public String sAAvgValue = "";

    public double dAAvgValue = 0;

    public String sBAvgValue = "";

    public double dBAvgValue = 0;

    public String getSCompareType()
    {
        return sCompareType;
    }

    public void  setSCompareType(String sCompareType)
    {
        this.sCompareType = sCompareType;
    }

    public String getSUpdateTime()
    {
        return sUpdateTime;
    }

    public void  setSUpdateTime(String sUpdateTime)
    {
        this.sUpdateTime = sUpdateTime;
    }

    public java.util.ArrayList<BEC.IndustryCompareItem> getVtCompItem()
    {
        return vtCompItem;
    }

    public void  setVtCompItem(java.util.ArrayList<BEC.IndustryCompareItem> vtCompItem)
    {
        this.vtCompItem = vtCompItem;
    }

    public String getSAAvgValue()
    {
        return sAAvgValue;
    }

    public void  setSAAvgValue(String sAAvgValue)
    {
        this.sAAvgValue = sAAvgValue;
    }

    public double getDAAvgValue()
    {
        return dAAvgValue;
    }

    public void  setDAAvgValue(double dAAvgValue)
    {
        this.dAAvgValue = dAAvgValue;
    }

    public String getSBAvgValue()
    {
        return sBAvgValue;
    }

    public void  setSBAvgValue(String sBAvgValue)
    {
        this.sBAvgValue = sBAvgValue;
    }

    public double getDBAvgValue()
    {
        return dBAvgValue;
    }

    public void  setDBAvgValue(double dBAvgValue)
    {
        this.dBAvgValue = dBAvgValue;
    }

    public IndustryCompare()
    {
    }

    public IndustryCompare(String sCompareType, String sUpdateTime, java.util.ArrayList<BEC.IndustryCompareItem> vtCompItem, String sAAvgValue, double dAAvgValue, String sBAvgValue, double dBAvgValue)
    {
        this.sCompareType = sCompareType;
        this.sUpdateTime = sUpdateTime;
        this.vtCompItem = vtCompItem;
        this.sAAvgValue = sAAvgValue;
        this.dAAvgValue = dAAvgValue;
        this.sBAvgValue = sBAvgValue;
        this.dBAvgValue = dBAvgValue;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sCompareType) {
            ostream.writeString(0, sCompareType);
        }
        if (null != sUpdateTime) {
            ostream.writeString(1, sUpdateTime);
        }
        if (null != vtCompItem) {
            ostream.writeList(2, vtCompItem);
        }
        if (null != sAAvgValue) {
            ostream.writeString(3, sAAvgValue);
        }
        ostream.writeDouble(4, dAAvgValue);
        if (null != sBAvgValue) {
            ostream.writeString(5, sBAvgValue);
        }
        ostream.writeDouble(6, dBAvgValue);
    }

    static java.util.ArrayList<BEC.IndustryCompareItem> VAR_TYPE_4_VTCOMPITEM = new java.util.ArrayList<BEC.IndustryCompareItem>();
    static {
        VAR_TYPE_4_VTCOMPITEM.add(new BEC.IndustryCompareItem());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sCompareType = (String)istream.readString(0, false, this.sCompareType);
        this.sUpdateTime = (String)istream.readString(1, false, this.sUpdateTime);
        this.vtCompItem = (java.util.ArrayList<BEC.IndustryCompareItem>)istream.readList(2, false, VAR_TYPE_4_VTCOMPITEM);
        this.sAAvgValue = (String)istream.readString(3, false, this.sAAvgValue);
        this.dAAvgValue = (double)istream.readDouble(4, false, this.dAAvgValue);
        this.sBAvgValue = (String)istream.readString(5, false, this.sBAvgValue);
        this.dBAvgValue = (double)istream.readDouble(6, false, this.dBAvgValue);
    }

}

