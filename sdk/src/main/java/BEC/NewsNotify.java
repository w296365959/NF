package BEC;

public final class NewsNotify extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sCHNShortName = "";

    public String sDtSecCode = "";

    public String sNewsID = "";

    public int eNewsType = 0;

    public String sTitle = "";

    public String sDtInfoUrl = "";

    public String sNotifyMsg = "";

    public int eNotifyNewType = E_NOTIFY_NEW_TYPE.E_NNT_COMMON;

    public java.util.ArrayList<TagInfo> vtTagInfo = null;

    public String sAbstract = "";

    public String getSCHNShortName()
    {
        return sCHNShortName;
    }

    public void  setSCHNShortName(String sCHNShortName)
    {
        this.sCHNShortName = sCHNShortName;
    }

    public String getSDtSecCode()
    {
        return sDtSecCode;
    }

    public void  setSDtSecCode(String sDtSecCode)
    {
        this.sDtSecCode = sDtSecCode;
    }

    public String getSNewsID()
    {
        return sNewsID;
    }

    public void  setSNewsID(String sNewsID)
    {
        this.sNewsID = sNewsID;
    }

    public int getENewsType()
    {
        return eNewsType;
    }

    public void  setENewsType(int eNewsType)
    {
        this.eNewsType = eNewsType;
    }

    public String getSTitle()
    {
        return sTitle;
    }

    public void  setSTitle(String sTitle)
    {
        this.sTitle = sTitle;
    }

    public String getSDtInfoUrl()
    {
        return sDtInfoUrl;
    }

    public void  setSDtInfoUrl(String sDtInfoUrl)
    {
        this.sDtInfoUrl = sDtInfoUrl;
    }

    public String getSNotifyMsg()
    {
        return sNotifyMsg;
    }

    public void  setSNotifyMsg(String sNotifyMsg)
    {
        this.sNotifyMsg = sNotifyMsg;
    }

    public int getENotifyNewType()
    {
        return eNotifyNewType;
    }

    public void  setENotifyNewType(int eNotifyNewType)
    {
        this.eNotifyNewType = eNotifyNewType;
    }

    public java.util.ArrayList<TagInfo> getVtTagInfo()
    {
        return vtTagInfo;
    }

    public void  setVtTagInfo(java.util.ArrayList<TagInfo> vtTagInfo)
    {
        this.vtTagInfo = vtTagInfo;
    }

    public String getSAbstract()
    {
        return sAbstract;
    }

    public void  setSAbstract(String sAbstract)
    {
        this.sAbstract = sAbstract;
    }

    public NewsNotify()
    {
    }

    public NewsNotify(String sCHNShortName, String sDtSecCode, String sNewsID, int eNewsType, String sTitle, String sDtInfoUrl, String sNotifyMsg, int eNotifyNewType, java.util.ArrayList<TagInfo> vtTagInfo, String sAbstract)
    {
        this.sCHNShortName = sCHNShortName;
        this.sDtSecCode = sDtSecCode;
        this.sNewsID = sNewsID;
        this.eNewsType = eNewsType;
        this.sTitle = sTitle;
        this.sDtInfoUrl = sDtInfoUrl;
        this.sNotifyMsg = sNotifyMsg;
        this.eNotifyNewType = eNotifyNewType;
        this.vtTagInfo = vtTagInfo;
        this.sAbstract = sAbstract;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sCHNShortName) {
            ostream.writeString(0, sCHNShortName);
        }
        if (null != sDtSecCode) {
            ostream.writeString(1, sDtSecCode);
        }
        if (null != sNewsID) {
            ostream.writeString(2, sNewsID);
        }
        ostream.writeInt32(3, eNewsType);
        if (null != sTitle) {
            ostream.writeString(4, sTitle);
        }
        if (null != sDtInfoUrl) {
            ostream.writeString(5, sDtInfoUrl);
        }
        if (null != sNotifyMsg) {
            ostream.writeString(6, sNotifyMsg);
        }
        ostream.writeInt32(7, eNotifyNewType);
        if (null != vtTagInfo) {
            ostream.writeList(8, vtTagInfo);
        }
        if (null != sAbstract) {
            ostream.writeString(9, sAbstract);
        }
    }

    static java.util.ArrayList<TagInfo> VAR_TYPE_4_VTTAGINFO = new java.util.ArrayList<TagInfo>();
    static {
        VAR_TYPE_4_VTTAGINFO.add(new TagInfo());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sCHNShortName = (String)istream.readString(0, false, this.sCHNShortName);
        this.sDtSecCode = (String)istream.readString(1, false, this.sDtSecCode);
        this.sNewsID = (String)istream.readString(2, false, this.sNewsID);
        this.eNewsType = (int)istream.readInt32(3, false, this.eNewsType);
        this.sTitle = (String)istream.readString(4, false, this.sTitle);
        this.sDtInfoUrl = (String)istream.readString(5, false, this.sDtInfoUrl);
        this.sNotifyMsg = (String)istream.readString(6, false, this.sNotifyMsg);
        this.eNotifyNewType = (int)istream.readInt32(7, false, this.eNotifyNewType);
        this.vtTagInfo = (java.util.ArrayList<TagInfo>)istream.readList(8, false, VAR_TYPE_4_VTTAGINFO);
        this.sAbstract = (String)istream.readString(9, false, this.sAbstract);
    }

}

