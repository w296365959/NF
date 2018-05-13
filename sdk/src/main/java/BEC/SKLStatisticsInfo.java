package BEC;

public final class SKLStatisticsInfo extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sDtSecCode = "";

    public String sSecName = "";

    public float fUpDownPct = 0;

    public float fUpDownRate = 0;

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

    public float getFUpDownPct()
    {
        return fUpDownPct;
    }

    public void  setFUpDownPct(float fUpDownPct)
    {
        this.fUpDownPct = fUpDownPct;
    }

    public float getFUpDownRate()
    {
        return fUpDownRate;
    }

    public void  setFUpDownRate(float fUpDownRate)
    {
        this.fUpDownRate = fUpDownRate;
    }

    public SKLStatisticsInfo()
    {
    }

    public SKLStatisticsInfo(String sDtSecCode, String sSecName, float fUpDownPct, float fUpDownRate)
    {
        this.sDtSecCode = sDtSecCode;
        this.sSecName = sSecName;
        this.fUpDownPct = fUpDownPct;
        this.fUpDownRate = fUpDownRate;
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
        ostream.writeFloat(2, fUpDownPct);
        ostream.writeFloat(3, fUpDownRate);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sDtSecCode = (String)istream.readString(0, false, this.sDtSecCode);
        this.sSecName = (String)istream.readString(1, false, this.sSecName);
        this.fUpDownPct = (float)istream.readFloat(2, false, this.fUpDownPct);
        this.fUpDownRate = (float)istream.readFloat(3, false, this.fUpDownRate);
    }

}

