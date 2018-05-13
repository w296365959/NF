package BEC;

public final class Profit extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sYear = "";

    public float fNetProfit = 0;

    public float fYearOnYear = 0;

    public String getSYear()
    {
        return sYear;
    }

    public void  setSYear(String sYear)
    {
        this.sYear = sYear;
    }

    public float getFNetProfit()
    {
        return fNetProfit;
    }

    public void  setFNetProfit(float fNetProfit)
    {
        this.fNetProfit = fNetProfit;
    }

    public float getFYearOnYear()
    {
        return fYearOnYear;
    }

    public void  setFYearOnYear(float fYearOnYear)
    {
        this.fYearOnYear = fYearOnYear;
    }

    public Profit()
    {
    }

    public Profit(String sYear, float fNetProfit, float fYearOnYear)
    {
        this.sYear = sYear;
        this.fNetProfit = fNetProfit;
        this.fYearOnYear = fYearOnYear;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sYear) {
            ostream.writeString(0, sYear);
        }
        ostream.writeFloat(1, fNetProfit);
        ostream.writeFloat(2, fYearOnYear);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sYear = (String)istream.readString(0, false, this.sYear);
        this.fNetProfit = (float)istream.readFloat(1, false, this.fNetProfit);
        this.fYearOnYear = (float)istream.readFloat(2, false, this.fYearOnYear);
    }

}

