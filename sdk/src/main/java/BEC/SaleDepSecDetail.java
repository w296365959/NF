package BEC;

public final class SaleDepSecDetail extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sSaleDepName = "";

    public SecCoordsInfo stCoords = null;

    public float fBuyAmount = 0;

    public float fSellAmount = 0;

    public String sDay = "";

    public String getSSaleDepName()
    {
        return sSaleDepName;
    }

    public void  setSSaleDepName(String sSaleDepName)
    {
        this.sSaleDepName = sSaleDepName;
    }

    public SecCoordsInfo getStCoords()
    {
        return stCoords;
    }

    public void  setStCoords(SecCoordsInfo stCoords)
    {
        this.stCoords = stCoords;
    }

    public float getFBuyAmount()
    {
        return fBuyAmount;
    }

    public void  setFBuyAmount(float fBuyAmount)
    {
        this.fBuyAmount = fBuyAmount;
    }

    public float getFSellAmount()
    {
        return fSellAmount;
    }

    public void  setFSellAmount(float fSellAmount)
    {
        this.fSellAmount = fSellAmount;
    }

    public String getSDay()
    {
        return sDay;
    }

    public void  setSDay(String sDay)
    {
        this.sDay = sDay;
    }

    public SaleDepSecDetail()
    {
    }

    public SaleDepSecDetail(String sSaleDepName, SecCoordsInfo stCoords, float fBuyAmount, float fSellAmount, String sDay)
    {
        this.sSaleDepName = sSaleDepName;
        this.stCoords = stCoords;
        this.fBuyAmount = fBuyAmount;
        this.fSellAmount = fSellAmount;
        this.sDay = sDay;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sSaleDepName) {
            ostream.writeString(0, sSaleDepName);
        }
        if (null != stCoords) {
            ostream.writeMessage(1, stCoords);
        }
        ostream.writeFloat(2, fBuyAmount);
        ostream.writeFloat(3, fSellAmount);
        if (null != sDay) {
            ostream.writeString(4, sDay);
        }
    }

    static SecCoordsInfo VAR_TYPE_4_STCOORDS = new SecCoordsInfo();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sSaleDepName = (String)istream.readString(0, false, this.sSaleDepName);
        this.stCoords = (SecCoordsInfo)istream.readMessage(1, false, VAR_TYPE_4_STCOORDS);
        this.fBuyAmount = (float)istream.readFloat(2, false, this.fBuyAmount);
        this.fSellAmount = (float)istream.readFloat(3, false, this.fSellAmount);
        this.sDay = (String)istream.readString(4, false, this.sDay);
    }

}

