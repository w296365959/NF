package BEC;

public final class MaxDateValue extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sMaxDate = "";

    public float fMaxValue = 0;

    public String getSMaxDate()
    {
        return sMaxDate;
    }

    public void  setSMaxDate(String sMaxDate)
    {
        this.sMaxDate = sMaxDate;
    }

    public float getFMaxValue()
    {
        return fMaxValue;
    }

    public void  setFMaxValue(float fMaxValue)
    {
        this.fMaxValue = fMaxValue;
    }

    public MaxDateValue()
    {
    }

    public MaxDateValue(String sMaxDate, float fMaxValue)
    {
        this.sMaxDate = sMaxDate;
        this.fMaxValue = fMaxValue;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sMaxDate) {
            ostream.writeString(0, sMaxDate);
        }
        ostream.writeFloat(1, fMaxValue);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sMaxDate = (String)istream.readString(0, false, this.sMaxDate);
        this.fMaxValue = (float)istream.readFloat(1, false, this.fMaxValue);
    }

}

