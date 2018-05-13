package BEC;

public final class PlateHisCycleInfo extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sDtPlateCode = "";

    public String sPlateName = "";

    public float fClosePrive = 0;

    public String sTradingDay = "";

    public String getSDtPlateCode()
    {
        return sDtPlateCode;
    }

    public void  setSDtPlateCode(String sDtPlateCode)
    {
        this.sDtPlateCode = sDtPlateCode;
    }

    public String getSPlateName()
    {
        return sPlateName;
    }

    public void  setSPlateName(String sPlateName)
    {
        this.sPlateName = sPlateName;
    }

    public float getFClosePrive()
    {
        return fClosePrive;
    }

    public void  setFClosePrive(float fClosePrive)
    {
        this.fClosePrive = fClosePrive;
    }

    public String getSTradingDay()
    {
        return sTradingDay;
    }

    public void  setSTradingDay(String sTradingDay)
    {
        this.sTradingDay = sTradingDay;
    }

    public PlateHisCycleInfo()
    {
    }

    public PlateHisCycleInfo(String sDtPlateCode, String sPlateName, float fClosePrive, String sTradingDay)
    {
        this.sDtPlateCode = sDtPlateCode;
        this.sPlateName = sPlateName;
        this.fClosePrive = fClosePrive;
        this.sTradingDay = sTradingDay;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sDtPlateCode) {
            ostream.writeString(0, sDtPlateCode);
        }
        if (null != sPlateName) {
            ostream.writeString(1, sPlateName);
        }
        ostream.writeFloat(2, fClosePrive);
        if (null != sTradingDay) {
            ostream.writeString(3, sTradingDay);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sDtPlateCode = (String)istream.readString(0, false, this.sDtPlateCode);
        this.sPlateName = (String)istream.readString(1, false, this.sPlateName);
        this.fClosePrive = (float)istream.readFloat(2, false, this.fClosePrive);
        this.sTradingDay = (String)istream.readString(3, false, this.sTradingDay);
    }

}

