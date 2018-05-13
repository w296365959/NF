package BEC;

public final class GetPlatePeriodTopReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sPlateCode = "";

    public int iYear = 0;

    public String sTradingDay = "";

    public String getSPlateCode()
    {
        return sPlateCode;
    }

    public void  setSPlateCode(String sPlateCode)
    {
        this.sPlateCode = sPlateCode;
    }

    public int getIYear()
    {
        return iYear;
    }

    public void  setIYear(int iYear)
    {
        this.iYear = iYear;
    }

    public String getSTradingDay()
    {
        return sTradingDay;
    }

    public void  setSTradingDay(String sTradingDay)
    {
        this.sTradingDay = sTradingDay;
    }

    public GetPlatePeriodTopReq()
    {
    }

    public GetPlatePeriodTopReq(String sPlateCode, int iYear, String sTradingDay)
    {
        this.sPlateCode = sPlateCode;
        this.iYear = iYear;
        this.sTradingDay = sTradingDay;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sPlateCode) {
            ostream.writeString(0, sPlateCode);
        }
        ostream.writeInt32(1, iYear);
        if (null != sTradingDay) {
            ostream.writeString(2, sTradingDay);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sPlateCode = (String)istream.readString(0, false, this.sPlateCode);
        this.iYear = (int)istream.readInt32(1, false, this.iYear);
        this.sTradingDay = (String)istream.readString(2, false, this.sTradingDay);
    }

}

