package BEC;

public final class CapitalStructureDetail extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sDate = "";

    public int iShareholderNumber = 0;

    public float fPerCapitaHoldings = 0;

    public String getSDate()
    {
        return sDate;
    }

    public void  setSDate(String sDate)
    {
        this.sDate = sDate;
    }

    public int getIShareholderNumber()
    {
        return iShareholderNumber;
    }

    public void  setIShareholderNumber(int iShareholderNumber)
    {
        this.iShareholderNumber = iShareholderNumber;
    }

    public float getFPerCapitaHoldings()
    {
        return fPerCapitaHoldings;
    }

    public void  setFPerCapitaHoldings(float fPerCapitaHoldings)
    {
        this.fPerCapitaHoldings = fPerCapitaHoldings;
    }

    public CapitalStructureDetail()
    {
    }

    public CapitalStructureDetail(String sDate, int iShareholderNumber, float fPerCapitaHoldings)
    {
        this.sDate = sDate;
        this.iShareholderNumber = iShareholderNumber;
        this.fPerCapitaHoldings = fPerCapitaHoldings;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sDate) {
            ostream.writeString(0, sDate);
        }
        ostream.writeInt32(1, iShareholderNumber);
        ostream.writeFloat(2, fPerCapitaHoldings);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sDate = (String)istream.readString(0, false, this.sDate);
        this.iShareholderNumber = (int)istream.readInt32(1, false, this.iShareholderNumber);
        this.fPerCapitaHoldings = (float)istream.readFloat(2, false, this.fPerCapitaHoldings);
    }

}

