package BEC;

public final class STDateValue extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sYearMonth = "";

    public float fValue = 0;

    public String getSYearMonth()
    {
        return sYearMonth;
    }

    public void  setSYearMonth(String sYearMonth)
    {
        this.sYearMonth = sYearMonth;
    }

    public float getFValue()
    {
        return fValue;
    }

    public void  setFValue(float fValue)
    {
        this.fValue = fValue;
    }

    public STDateValue()
    {
    }

    public STDateValue(String sYearMonth, float fValue)
    {
        this.sYearMonth = sYearMonth;
        this.fValue = fValue;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sYearMonth) {
            ostream.writeString(0, sYearMonth);
        }
        ostream.writeFloat(1, fValue);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sYearMonth = (String)istream.readString(0, false, this.sYearMonth);
        this.fValue = (float)istream.readFloat(1, false, this.fValue);
    }

}

