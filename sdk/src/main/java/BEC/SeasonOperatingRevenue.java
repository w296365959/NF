package BEC;

public final class SeasonOperatingRevenue extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int eSeasonType = 0;

    public float fIncome = 0;

    public float fNetProfit = 0;

    public int getESeasonType()
    {
        return eSeasonType;
    }

    public void  setESeasonType(int eSeasonType)
    {
        this.eSeasonType = eSeasonType;
    }

    public float getFIncome()
    {
        return fIncome;
    }

    public void  setFIncome(float fIncome)
    {
        this.fIncome = fIncome;
    }

    public float getFNetProfit()
    {
        return fNetProfit;
    }

    public void  setFNetProfit(float fNetProfit)
    {
        this.fNetProfit = fNetProfit;
    }

    public SeasonOperatingRevenue()
    {
    }

    public SeasonOperatingRevenue(int eSeasonType, float fIncome, float fNetProfit)
    {
        this.eSeasonType = eSeasonType;
        this.fIncome = fIncome;
        this.fNetProfit = fNetProfit;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, eSeasonType);
        ostream.writeFloat(1, fIncome);
        ostream.writeFloat(2, fNetProfit);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.eSeasonType = (int)istream.readInt32(0, false, this.eSeasonType);
        this.fIncome = (float)istream.readFloat(1, false, this.fIncome);
        this.fNetProfit = (float)istream.readFloat(2, false, this.fNetProfit);
    }

}

