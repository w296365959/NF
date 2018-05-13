package BEC;

public final class GetSecBsInfoRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<SecBsInfo> vSecBsInfo = null;

    public float fValue = 0;

    public String sTypeText = "";

    public String sDescText = "";

    public String sShortDescText = "";

    public long lTimeStamp = 0;

    public java.util.ArrayList<SecBsInfo> getVSecBsInfo()
    {
        return vSecBsInfo;
    }

    public void  setVSecBsInfo(java.util.ArrayList<SecBsInfo> vSecBsInfo)
    {
        this.vSecBsInfo = vSecBsInfo;
    }

    public float getFValue()
    {
        return fValue;
    }

    public void  setFValue(float fValue)
    {
        this.fValue = fValue;
    }

    public String getSTypeText()
    {
        return sTypeText;
    }

    public void  setSTypeText(String sTypeText)
    {
        this.sTypeText = sTypeText;
    }

    public String getSDescText()
    {
        return sDescText;
    }

    public void  setSDescText(String sDescText)
    {
        this.sDescText = sDescText;
    }

    public String getSShortDescText()
    {
        return sShortDescText;
    }

    public void  setSShortDescText(String sShortDescText)
    {
        this.sShortDescText = sShortDescText;
    }

    public long getLTimeStamp()
    {
        return lTimeStamp;
    }

    public void  setLTimeStamp(long lTimeStamp)
    {
        this.lTimeStamp = lTimeStamp;
    }

    public GetSecBsInfoRsp()
    {
    }

    public GetSecBsInfoRsp(java.util.ArrayList<SecBsInfo> vSecBsInfo, float fValue, String sTypeText, String sDescText, String sShortDescText, long lTimeStamp)
    {
        this.vSecBsInfo = vSecBsInfo;
        this.fValue = fValue;
        this.sTypeText = sTypeText;
        this.sDescText = sDescText;
        this.sShortDescText = sShortDescText;
        this.lTimeStamp = lTimeStamp;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vSecBsInfo) {
            ostream.writeList(0, vSecBsInfo);
        }
        ostream.writeFloat(1, fValue);
        if (null != sTypeText) {
            ostream.writeString(2, sTypeText);
        }
        if (null != sDescText) {
            ostream.writeString(3, sDescText);
        }
        if (null != sShortDescText) {
            ostream.writeString(4, sShortDescText);
        }
        ostream.writeInt64(5, lTimeStamp);
    }

    static java.util.ArrayList<SecBsInfo> VAR_TYPE_4_VSECBSINFO = new java.util.ArrayList<SecBsInfo>();
    static {
        VAR_TYPE_4_VSECBSINFO.add(new SecBsInfo());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vSecBsInfo = (java.util.ArrayList<SecBsInfo>)istream.readList(0, false, VAR_TYPE_4_VSECBSINFO);
        this.fValue = (float)istream.readFloat(1, false, this.fValue);
        this.sTypeText = (String)istream.readString(2, false, this.sTypeText);
        this.sDescText = (String)istream.readString(3, false, this.sDescText);
        this.sShortDescText = (String)istream.readString(4, false, this.sShortDescText);
        this.lTimeStamp = (long)istream.readInt64(5, false, this.lTimeStamp);
    }

}

