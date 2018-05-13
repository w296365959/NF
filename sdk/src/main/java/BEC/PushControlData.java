package BEC;

public final class PushControlData extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iRealPushType = 0;

    public String sAndroidIconUrl = "";

    public String sIosIconUrl = "";

    public int getIRealPushType()
    {
        return iRealPushType;
    }

    public void  setIRealPushType(int iRealPushType)
    {
        this.iRealPushType = iRealPushType;
    }

    public String getSAndroidIconUrl()
    {
        return sAndroidIconUrl;
    }

    public void  setSAndroidIconUrl(String sAndroidIconUrl)
    {
        this.sAndroidIconUrl = sAndroidIconUrl;
    }

    public String getSIosIconUrl()
    {
        return sIosIconUrl;
    }

    public void  setSIosIconUrl(String sIosIconUrl)
    {
        this.sIosIconUrl = sIosIconUrl;
    }

    public PushControlData()
    {
    }

    public PushControlData(int iRealPushType, String sAndroidIconUrl, String sIosIconUrl)
    {
        this.iRealPushType = iRealPushType;
        this.sAndroidIconUrl = sAndroidIconUrl;
        this.sIosIconUrl = sIosIconUrl;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iRealPushType);
        if (null != sAndroidIconUrl) {
            ostream.writeString(1, sAndroidIconUrl);
        }
        if (null != sIosIconUrl) {
            ostream.writeString(2, sIosIconUrl);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iRealPushType = (int)istream.readInt32(0, false, this.iRealPushType);
        this.sAndroidIconUrl = (String)istream.readString(1, false, this.sAndroidIconUrl);
        this.sIosIconUrl = (String)istream.readString(2, false, this.sIosIconUrl);
    }

}

