package BEC;

public final class PayInfo extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sVideoKey = "";

    public String sDate = "";

    public String sExpiryDate = "";

    public String getSVideoKey()
    {
        return sVideoKey;
    }

    public void  setSVideoKey(String sVideoKey)
    {
        this.sVideoKey = sVideoKey;
    }

    public String getSDate()
    {
        return sDate;
    }

    public void  setSDate(String sDate)
    {
        this.sDate = sDate;
    }

    public String getSExpiryDate()
    {
        return sExpiryDate;
    }

    public void  setSExpiryDate(String sExpiryDate)
    {
        this.sExpiryDate = sExpiryDate;
    }

    public PayInfo()
    {
    }

    public PayInfo(String sVideoKey, String sDate, String sExpiryDate)
    {
        this.sVideoKey = sVideoKey;
        this.sDate = sDate;
        this.sExpiryDate = sExpiryDate;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sVideoKey) {
            ostream.writeString(0, sVideoKey);
        }
        if (null != sDate) {
            ostream.writeString(1, sDate);
        }
        if (null != sExpiryDate) {
            ostream.writeString(2, sExpiryDate);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sVideoKey = (String)istream.readString(0, false, this.sVideoKey);
        this.sDate = (String)istream.readString(1, false, this.sDate);
        this.sExpiryDate = (String)istream.readString(2, false, this.sExpiryDate);
    }

}

