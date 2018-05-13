package BEC;

public final class PayUserAgreementDesc extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iTextType = 0;

    public String sText = "";

    public String sTextUrl = "";

    public boolean bLineFeed = false;

    public int getITextType()
    {
        return iTextType;
    }

    public void  setITextType(int iTextType)
    {
        this.iTextType = iTextType;
    }

    public String getSText()
    {
        return sText;
    }

    public void  setSText(String sText)
    {
        this.sText = sText;
    }

    public String getSTextUrl()
    {
        return sTextUrl;
    }

    public void  setSTextUrl(String sTextUrl)
    {
        this.sTextUrl = sTextUrl;
    }

    public boolean getBLineFeed()
    {
        return bLineFeed;
    }

    public void  setBLineFeed(boolean bLineFeed)
    {
        this.bLineFeed = bLineFeed;
    }

    public PayUserAgreementDesc()
    {
    }

    public PayUserAgreementDesc(int iTextType, String sText, String sTextUrl, boolean bLineFeed)
    {
        this.iTextType = iTextType;
        this.sText = sText;
        this.sTextUrl = sTextUrl;
        this.bLineFeed = bLineFeed;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iTextType);
        if (null != sText) {
            ostream.writeString(1, sText);
        }
        if (null != sTextUrl) {
            ostream.writeString(2, sTextUrl);
        }
        ostream.writeBoolean(3, bLineFeed);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iTextType = (int)istream.readInt32(0, false, this.iTextType);
        this.sText = (String)istream.readString(1, false, this.sText);
        this.sTextUrl = (String)istream.readString(2, false, this.sTextUrl);
        this.bLineFeed = (boolean)istream.readBoolean(3, false, this.bLineFeed);
    }

}

