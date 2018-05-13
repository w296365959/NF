package BEC;

public final class WxPayPrepayInfo extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sAppId = "";

    public String sPartnerId = "";

    public String sPrepayId = "";

    public String sPackage = "";

    public String sNonceStr = "";

    public long lTimeStamp = 0;

    public String sSign = "";

    public String getSAppId()
    {
        return sAppId;
    }

    public void  setSAppId(String sAppId)
    {
        this.sAppId = sAppId;
    }

    public String getSPartnerId()
    {
        return sPartnerId;
    }

    public void  setSPartnerId(String sPartnerId)
    {
        this.sPartnerId = sPartnerId;
    }

    public String getSPrepayId()
    {
        return sPrepayId;
    }

    public void  setSPrepayId(String sPrepayId)
    {
        this.sPrepayId = sPrepayId;
    }

    public String getSPackage()
    {
        return sPackage;
    }

    public void  setSPackage(String sPackage)
    {
        this.sPackage = sPackage;
    }

    public String getSNonceStr()
    {
        return sNonceStr;
    }

    public void  setSNonceStr(String sNonceStr)
    {
        this.sNonceStr = sNonceStr;
    }

    public long getLTimeStamp()
    {
        return lTimeStamp;
    }

    public void  setLTimeStamp(long lTimeStamp)
    {
        this.lTimeStamp = lTimeStamp;
    }

    public String getSSign()
    {
        return sSign;
    }

    public void  setSSign(String sSign)
    {
        this.sSign = sSign;
    }

    public WxPayPrepayInfo()
    {
    }

    public WxPayPrepayInfo(String sAppId, String sPartnerId, String sPrepayId, String sPackage, String sNonceStr, long lTimeStamp, String sSign)
    {
        this.sAppId = sAppId;
        this.sPartnerId = sPartnerId;
        this.sPrepayId = sPrepayId;
        this.sPackage = sPackage;
        this.sNonceStr = sNonceStr;
        this.lTimeStamp = lTimeStamp;
        this.sSign = sSign;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sAppId) {
            ostream.writeString(0, sAppId);
        }
        if (null != sPartnerId) {
            ostream.writeString(1, sPartnerId);
        }
        if (null != sPrepayId) {
            ostream.writeString(2, sPrepayId);
        }
        if (null != sPackage) {
            ostream.writeString(3, sPackage);
        }
        if (null != sNonceStr) {
            ostream.writeString(4, sNonceStr);
        }
        ostream.writeInt64(5, lTimeStamp);
        if (null != sSign) {
            ostream.writeString(6, sSign);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sAppId = (String)istream.readString(0, false, this.sAppId);
        this.sPartnerId = (String)istream.readString(1, false, this.sPartnerId);
        this.sPrepayId = (String)istream.readString(2, false, this.sPrepayId);
        this.sPackage = (String)istream.readString(3, false, this.sPackage);
        this.sNonceStr = (String)istream.readString(4, false, this.sNonceStr);
        this.lTimeStamp = (long)istream.readInt64(5, false, this.lTimeStamp);
        this.sSign = (String)istream.readString(6, false, this.sSign);
    }

}

