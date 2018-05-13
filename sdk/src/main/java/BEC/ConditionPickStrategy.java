package BEC;

public final class ConditionPickStrategy extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sStrategyName = "";

    public String sUniKey = "";

    public java.util.ArrayList<String> vCondition = null;

    public java.util.ArrayList<TagInfo> vtTagInfo = null;

    public int iSubTime = 0;

    public String sUrl = "";

    public String getSStrategyName()
    {
        return sStrategyName;
    }

    public void  setSStrategyName(String sStrategyName)
    {
        this.sStrategyName = sStrategyName;
    }

    public String getSUniKey()
    {
        return sUniKey;
    }

    public void  setSUniKey(String sUniKey)
    {
        this.sUniKey = sUniKey;
    }

    public java.util.ArrayList<String> getVCondition()
    {
        return vCondition;
    }

    public void  setVCondition(java.util.ArrayList<String> vCondition)
    {
        this.vCondition = vCondition;
    }

    public java.util.ArrayList<TagInfo> getVtTagInfo()
    {
        return vtTagInfo;
    }

    public void  setVtTagInfo(java.util.ArrayList<TagInfo> vtTagInfo)
    {
        this.vtTagInfo = vtTagInfo;
    }

    public int getISubTime()
    {
        return iSubTime;
    }

    public void  setISubTime(int iSubTime)
    {
        this.iSubTime = iSubTime;
    }

    public String getSUrl()
    {
        return sUrl;
    }

    public void  setSUrl(String sUrl)
    {
        this.sUrl = sUrl;
    }

    public ConditionPickStrategy()
    {
    }

    public ConditionPickStrategy(String sStrategyName, String sUniKey, java.util.ArrayList<String> vCondition, java.util.ArrayList<TagInfo> vtTagInfo, int iSubTime, String sUrl)
    {
        this.sStrategyName = sStrategyName;
        this.sUniKey = sUniKey;
        this.vCondition = vCondition;
        this.vtTagInfo = vtTagInfo;
        this.iSubTime = iSubTime;
        this.sUrl = sUrl;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sStrategyName) {
            ostream.writeString(0, sStrategyName);
        }
        if (null != sUniKey) {
            ostream.writeString(1, sUniKey);
        }
        if (null != vCondition) {
            ostream.writeList(2, vCondition);
        }
        if (null != vtTagInfo) {
            ostream.writeList(3, vtTagInfo);
        }
        ostream.writeInt32(4, iSubTime);
        if (null != sUrl) {
            ostream.writeString(5, sUrl);
        }
    }

    static java.util.ArrayList<String> VAR_TYPE_4_VCONDITION = new java.util.ArrayList<String>();
    static {
        VAR_TYPE_4_VCONDITION.add("");
    }

    static java.util.ArrayList<TagInfo> VAR_TYPE_4_VTTAGINFO = new java.util.ArrayList<TagInfo>();
    static {
        VAR_TYPE_4_VTTAGINFO.add(new TagInfo());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sStrategyName = (String)istream.readString(0, false, this.sStrategyName);
        this.sUniKey = (String)istream.readString(1, false, this.sUniKey);
        this.vCondition = (java.util.ArrayList<String>)istream.readList(2, false, VAR_TYPE_4_VCONDITION);
        this.vtTagInfo = (java.util.ArrayList<TagInfo>)istream.readList(3, false, VAR_TYPE_4_VTTAGINFO);
        this.iSubTime = (int)istream.readInt32(4, false, this.iSubTime);
        this.sUrl = (String)istream.readString(5, false, this.sUrl);
    }

}

