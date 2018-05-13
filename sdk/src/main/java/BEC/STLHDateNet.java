package BEC;

public final class STLHDateNet extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sDate = "";

    public float fNetBuy = 0;

    public String getSDate()
    {
        return sDate;
    }

    public void  setSDate(String sDate)
    {
        this.sDate = sDate;
    }

    public float getFNetBuy()
    {
        return fNetBuy;
    }

    public void  setFNetBuy(float fNetBuy)
    {
        this.fNetBuy = fNetBuy;
    }

    public STLHDateNet()
    {
    }

    public STLHDateNet(String sDate, float fNetBuy)
    {
        this.sDate = sDate;
        this.fNetBuy = fNetBuy;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sDate) {
            ostream.writeString(0, sDate);
        }
        ostream.writeFloat(1, fNetBuy);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sDate = (String)istream.readString(0, false, this.sDate);
        this.fNetBuy = (float)istream.readFloat(1, false, this.fNetBuy);
    }

}

