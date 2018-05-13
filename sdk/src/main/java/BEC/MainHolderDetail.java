package BEC;

public final class MainHolderDetail extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sDate = "";

    public String sDateDesc = "";

    public float fRate = 0;

    public float fPrice = 0;

    public String getSDate()
    {
        return sDate;
    }

    public void  setSDate(String sDate)
    {
        this.sDate = sDate;
    }

    public String getSDateDesc()
    {
        return sDateDesc;
    }

    public void  setSDateDesc(String sDateDesc)
    {
        this.sDateDesc = sDateDesc;
    }

    public float getFRate()
    {
        return fRate;
    }

    public void  setFRate(float fRate)
    {
        this.fRate = fRate;
    }

    public float getFPrice()
    {
        return fPrice;
    }

    public void  setFPrice(float fPrice)
    {
        this.fPrice = fPrice;
    }

    public MainHolderDetail()
    {
    }

    public MainHolderDetail(String sDate, String sDateDesc, float fRate, float fPrice)
    {
        this.sDate = sDate;
        this.sDateDesc = sDateDesc;
        this.fRate = fRate;
        this.fPrice = fPrice;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sDate) {
            ostream.writeString(0, sDate);
        }
        if (null != sDateDesc) {
            ostream.writeString(1, sDateDesc);
        }
        ostream.writeFloat(2, fRate);
        ostream.writeFloat(3, fPrice);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sDate = (String)istream.readString(0, false, this.sDate);
        this.sDateDesc = (String)istream.readString(1, false, this.sDateDesc);
        this.fRate = (float)istream.readFloat(2, false, this.fRate);
        this.fPrice = (float)istream.readFloat(3, false, this.fPrice);
    }

}

