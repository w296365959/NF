package BEC;

public final class SecPolicyNXInfo extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public SecCodeDetail stSecCodeDetail = null;

    public float fPrice = 0;

    public float fClosePrice = 0;

    public float fTodayChange = 0;

    public float fInPrice = 0;

    public float fOutPrice = 0;

    public int iDuration = 0;

    public SecCodeDetail getStSecCodeDetail()
    {
        return stSecCodeDetail;
    }

    public void  setStSecCodeDetail(SecCodeDetail stSecCodeDetail)
    {
        this.stSecCodeDetail = stSecCodeDetail;
    }

    public float getFPrice()
    {
        return fPrice;
    }

    public void  setFPrice(float fPrice)
    {
        this.fPrice = fPrice;
    }

    public float getFClosePrice()
    {
        return fClosePrice;
    }

    public void  setFClosePrice(float fClosePrice)
    {
        this.fClosePrice = fClosePrice;
    }

    public float getFTodayChange()
    {
        return fTodayChange;
    }

    public void  setFTodayChange(float fTodayChange)
    {
        this.fTodayChange = fTodayChange;
    }

    public float getFInPrice()
    {
        return fInPrice;
    }

    public void  setFInPrice(float fInPrice)
    {
        this.fInPrice = fInPrice;
    }

    public float getFOutPrice()
    {
        return fOutPrice;
    }

    public void  setFOutPrice(float fOutPrice)
    {
        this.fOutPrice = fOutPrice;
    }

    public int getIDuration()
    {
        return iDuration;
    }

    public void  setIDuration(int iDuration)
    {
        this.iDuration = iDuration;
    }

    public SecPolicyNXInfo()
    {
    }

    public SecPolicyNXInfo(SecCodeDetail stSecCodeDetail, float fPrice, float fClosePrice, float fTodayChange, float fInPrice, float fOutPrice, int iDuration)
    {
        this.stSecCodeDetail = stSecCodeDetail;
        this.fPrice = fPrice;
        this.fClosePrice = fClosePrice;
        this.fTodayChange = fTodayChange;
        this.fInPrice = fInPrice;
        this.fOutPrice = fOutPrice;
        this.iDuration = iDuration;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stSecCodeDetail) {
            ostream.writeMessage(0, stSecCodeDetail);
        }
        ostream.writeFloat(1, fPrice);
        ostream.writeFloat(2, fClosePrice);
        ostream.writeFloat(3, fTodayChange);
        ostream.writeFloat(4, fInPrice);
        ostream.writeFloat(5, fOutPrice);
        ostream.writeInt32(6, iDuration);
    }

    static SecCodeDetail VAR_TYPE_4_STSECCODEDETAIL = new SecCodeDetail();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stSecCodeDetail = (SecCodeDetail)istream.readMessage(0, false, VAR_TYPE_4_STSECCODEDETAIL);
        this.fPrice = (float)istream.readFloat(1, false, this.fPrice);
        this.fClosePrice = (float)istream.readFloat(2, false, this.fClosePrice);
        this.fTodayChange = (float)istream.readFloat(3, false, this.fTodayChange);
        this.fInPrice = (float)istream.readFloat(4, false, this.fInPrice);
        this.fOutPrice = (float)istream.readFloat(5, false, this.fOutPrice);
        this.iDuration = (int)istream.readInt32(6, false, this.iDuration);
    }

}

