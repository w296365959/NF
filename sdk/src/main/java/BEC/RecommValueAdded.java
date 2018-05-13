package BEC;

public final class RecommValueAdded extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public ValueAddedDesc stValueAddedDesc = null;

    public String sUpdateTime = "";

    public java.util.ArrayList<SecBaseInfo> vSecBaseInfo = null;

    public int iNewFlag = 0;

    public int iBuyFlag = 0;

    public String sUrl = "";

    public String sSource = "";

    public String sDetailUrl = "";

    public ValueAddedDesc getStValueAddedDesc()
    {
        return stValueAddedDesc;
    }

    public void  setStValueAddedDesc(ValueAddedDesc stValueAddedDesc)
    {
        this.stValueAddedDesc = stValueAddedDesc;
    }

    public String getSUpdateTime()
    {
        return sUpdateTime;
    }

    public void  setSUpdateTime(String sUpdateTime)
    {
        this.sUpdateTime = sUpdateTime;
    }

    public java.util.ArrayList<SecBaseInfo> getVSecBaseInfo()
    {
        return vSecBaseInfo;
    }

    public void  setVSecBaseInfo(java.util.ArrayList<SecBaseInfo> vSecBaseInfo)
    {
        this.vSecBaseInfo = vSecBaseInfo;
    }

    public int getINewFlag()
    {
        return iNewFlag;
    }

    public void  setINewFlag(int iNewFlag)
    {
        this.iNewFlag = iNewFlag;
    }

    public int getIBuyFlag()
    {
        return iBuyFlag;
    }

    public void  setIBuyFlag(int iBuyFlag)
    {
        this.iBuyFlag = iBuyFlag;
    }

    public String getSUrl()
    {
        return sUrl;
    }

    public void  setSUrl(String sUrl)
    {
        this.sUrl = sUrl;
    }

    public String getSSource()
    {
        return sSource;
    }

    public void  setSSource(String sSource)
    {
        this.sSource = sSource;
    }

    public String getSDetailUrl()
    {
        return sDetailUrl;
    }

    public void  setSDetailUrl(String sDetailUrl)
    {
        this.sDetailUrl = sDetailUrl;
    }

    public RecommValueAdded()
    {
    }

    public RecommValueAdded(ValueAddedDesc stValueAddedDesc, String sUpdateTime, java.util.ArrayList<SecBaseInfo> vSecBaseInfo, int iNewFlag, int iBuyFlag, String sUrl, String sSource, String sDetailUrl)
    {
        this.stValueAddedDesc = stValueAddedDesc;
        this.sUpdateTime = sUpdateTime;
        this.vSecBaseInfo = vSecBaseInfo;
        this.iNewFlag = iNewFlag;
        this.iBuyFlag = iBuyFlag;
        this.sUrl = sUrl;
        this.sSource = sSource;
        this.sDetailUrl = sDetailUrl;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stValueAddedDesc) {
            ostream.writeMessage(0, stValueAddedDesc);
        }
        if (null != sUpdateTime) {
            ostream.writeString(1, sUpdateTime);
        }
        if (null != vSecBaseInfo) {
            ostream.writeList(2, vSecBaseInfo);
        }
        ostream.writeInt32(3, iNewFlag);
        ostream.writeInt32(4, iBuyFlag);
        if (null != sUrl) {
            ostream.writeString(5, sUrl);
        }
        if (null != sSource) {
            ostream.writeString(6, sSource);
        }
        if (null != sDetailUrl) {
            ostream.writeString(7, sDetailUrl);
        }
    }

    static ValueAddedDesc VAR_TYPE_4_STVALUEADDEDDESC = new ValueAddedDesc();

    static java.util.ArrayList<SecBaseInfo> VAR_TYPE_4_VSECBASEINFO = new java.util.ArrayList<SecBaseInfo>();
    static {
        VAR_TYPE_4_VSECBASEINFO.add(new SecBaseInfo());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stValueAddedDesc = (ValueAddedDesc)istream.readMessage(0, false, VAR_TYPE_4_STVALUEADDEDDESC);
        this.sUpdateTime = (String)istream.readString(1, false, this.sUpdateTime);
        this.vSecBaseInfo = (java.util.ArrayList<SecBaseInfo>)istream.readList(2, false, VAR_TYPE_4_VSECBASEINFO);
        this.iNewFlag = (int)istream.readInt32(3, false, this.iNewFlag);
        this.iBuyFlag = (int)istream.readInt32(4, false, this.iBuyFlag);
        this.sUrl = (String)istream.readString(5, false, this.sUrl);
        this.sSource = (String)istream.readString(6, false, this.sSource);
        this.sDetailUrl = (String)istream.readString(7, false, this.sDetailUrl);
    }

}

