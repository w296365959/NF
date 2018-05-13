package BEC;

public final class VideoPrice extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sVideoKey = "";

    public float fPrice = 0;

    public int iDays = 0;

    public String getSVideoKey()
    {
        return sVideoKey;
    }

    public void  setSVideoKey(String sVideoKey)
    {
        this.sVideoKey = sVideoKey;
    }

    public float getFPrice()
    {
        return fPrice;
    }

    public void  setFPrice(float fPrice)
    {
        this.fPrice = fPrice;
    }

    public int getIDays()
    {
        return iDays;
    }

    public void  setIDays(int iDays)
    {
        this.iDays = iDays;
    }

    public VideoPrice()
    {
    }

    public VideoPrice(String sVideoKey, float fPrice, int iDays)
    {
        this.sVideoKey = sVideoKey;
        this.fPrice = fPrice;
        this.iDays = iDays;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sVideoKey) {
            ostream.writeString(0, sVideoKey);
        }
        ostream.writeFloat(1, fPrice);
        ostream.writeInt32(2, iDays);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sVideoKey = (String)istream.readString(0, false, this.sVideoKey);
        this.fPrice = (float)istream.readFloat(1, false, this.fPrice);
        this.iDays = (int)istream.readInt32(2, false, this.iDays);
    }

}

