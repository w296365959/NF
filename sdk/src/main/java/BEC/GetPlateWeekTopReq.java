package BEC;

public final class GetPlateWeekTopReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sPlateCode = "";

    public int iYear = 0;

    public int iWeekDay = 0;

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

    public int getIWeekDay()
    {
        return iWeekDay;
    }

    public void  setIWeekDay(int iWeekDay)
    {
        this.iWeekDay = iWeekDay;
    }

    public GetPlateWeekTopReq()
    {
    }

    public GetPlateWeekTopReq(String sPlateCode, int iYear, int iWeekDay)
    {
        this.sPlateCode = sPlateCode;
        this.iYear = iYear;
        this.iWeekDay = iWeekDay;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sPlateCode) {
            ostream.writeString(0, sPlateCode);
        }
        ostream.writeInt32(1, iYear);
        ostream.writeInt32(2, iWeekDay);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sPlateCode = (String)istream.readString(0, false, this.sPlateCode);
        this.iYear = (int)istream.readInt32(1, false, this.iYear);
        this.iWeekDay = (int)istream.readInt32(2, false, this.iWeekDay);
    }

}

