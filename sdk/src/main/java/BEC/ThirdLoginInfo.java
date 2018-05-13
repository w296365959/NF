package BEC;

public final class ThirdLoginInfo extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sOpenId = "";

    public String sAccessToken = "";

    public int eType = 0;

    public java.util.Map<String, String> mpParam = null;

    public String getSOpenId()
    {
        return sOpenId;
    }

    public void  setSOpenId(String sOpenId)
    {
        this.sOpenId = sOpenId;
    }

    public String getSAccessToken()
    {
        return sAccessToken;
    }

    public void  setSAccessToken(String sAccessToken)
    {
        this.sAccessToken = sAccessToken;
    }

    public int getEType()
    {
        return eType;
    }

    public void  setEType(int eType)
    {
        this.eType = eType;
    }

    public java.util.Map<String, String> getMpParam()
    {
        return mpParam;
    }

    public void  setMpParam(java.util.Map<String, String> mpParam)
    {
        this.mpParam = mpParam;
    }

    public ThirdLoginInfo()
    {
    }

    public ThirdLoginInfo(String sOpenId, String sAccessToken, int eType, java.util.Map<String, String> mpParam)
    {
        this.sOpenId = sOpenId;
        this.sAccessToken = sAccessToken;
        this.eType = eType;
        this.mpParam = mpParam;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sOpenId) {
            ostream.writeString(0, sOpenId);
        }
        if (null != sAccessToken) {
            ostream.writeString(1, sAccessToken);
        }
        ostream.writeInt32(2, eType);
        if (null != mpParam) {
            ostream.writeMap(3, mpParam);
        }
    }

    static java.util.Map<String, String> VAR_TYPE_4_MPPARAM = new java.util.HashMap<String, String>();
    static {
        VAR_TYPE_4_MPPARAM.put("", "");
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sOpenId = (String)istream.readString(0, false, this.sOpenId);
        this.sAccessToken = (String)istream.readString(1, false, this.sAccessToken);
        this.eType = (int)istream.readInt32(2, false, this.eType);
        this.mpParam = (java.util.Map<String, String>)istream.readMap(3, false, VAR_TYPE_4_MPPARAM);
    }

}

