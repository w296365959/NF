package BEC;

public final class NewsDesc extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sNewsID = "";

    public int eNewsType = 0;

    public String sTitle = "";

    public String sDescription = "";

    public String sFrom = "";

    public int iTime = 0;

    public String sDtSecCode = "";

    public java.util.ArrayList<BEC.TagInfo> vtTagInfo = null;

    public String sContent = "";

    public String sDtInfoUrl = "";

    public int iStyleType = 0;

    public java.util.ArrayList<BEC.SecInfo> vtRelaStock = null;

    public int iStatus = 0;

    public String sThirdUrl = "";

    public int iCreateTime = 0;

    public String sSubTypeName = "";

    public int iPublish = 0;

    public String sImgUrl = "";

    public int eNewsFlag = 0;

    public int iShow = 0;

    public java.util.ArrayList<BEC.VideoConfig> vVideoConfig = null;

    public int iTopicId = 0;

    public long iAutoID = 0;

    public java.util.ArrayList<BEC.Label> vLabel = null;

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

    public String getSDescription()
    {
        return sDescription;
    }

    public void  setSDescription(String sDescription)
    {
        this.sDescription = sDescription;
    }

    public String getSFrom()
    {
        return sFrom;
    }

    public void  setSFrom(String sFrom)
    {
        this.sFrom = sFrom;
    }

    public int getITime()
    {
        return iTime;
    }

    public void  setITime(int iTime)
    {
        this.iTime = iTime;
    }

    public String getSDtSecCode()
    {
        return sDtSecCode;
    }

    public void  setSDtSecCode(String sDtSecCode)
    {
        this.sDtSecCode = sDtSecCode;
    }

    public java.util.ArrayList<BEC.TagInfo> getVtTagInfo()
    {
        return vtTagInfo;
    }

    public void  setVtTagInfo(java.util.ArrayList<BEC.TagInfo> vtTagInfo)
    {
        this.vtTagInfo = vtTagInfo;
    }

    public String getSContent()
    {
        return sContent;
    }

    public void  setSContent(String sContent)
    {
        this.sContent = sContent;
    }

    public String getSDtInfoUrl()
    {
        return sDtInfoUrl;
    }

    public void  setSDtInfoUrl(String sDtInfoUrl)
    {
        this.sDtInfoUrl = sDtInfoUrl;
    }

    public int getIStyleType()
    {
        return iStyleType;
    }

    public void  setIStyleType(int iStyleType)
    {
        this.iStyleType = iStyleType;
    }

    public java.util.ArrayList<BEC.SecInfo> getVtRelaStock()
    {
        return vtRelaStock;
    }

    public void  setVtRelaStock(java.util.ArrayList<BEC.SecInfo> vtRelaStock)
    {
        this.vtRelaStock = vtRelaStock;
    }

    public int getIStatus()
    {
        return iStatus;
    }

    public void  setIStatus(int iStatus)
    {
        this.iStatus = iStatus;
    }

    public String getSThirdUrl()
    {
        return sThirdUrl;
    }

    public void  setSThirdUrl(String sThirdUrl)
    {
        this.sThirdUrl = sThirdUrl;
    }

    public int getICreateTime()
    {
        return iCreateTime;
    }

    public void  setICreateTime(int iCreateTime)
    {
        this.iCreateTime = iCreateTime;
    }

    public String getSSubTypeName()
    {
        return sSubTypeName;
    }

    public void  setSSubTypeName(String sSubTypeName)
    {
        this.sSubTypeName = sSubTypeName;
    }

    public int getIPublish()
    {
        return iPublish;
    }

    public void  setIPublish(int iPublish)
    {
        this.iPublish = iPublish;
    }

    public String getSImgUrl()
    {
        return sImgUrl;
    }

    public void  setSImgUrl(String sImgUrl)
    {
        this.sImgUrl = sImgUrl;
    }

    public int getENewsFlag()
    {
        return eNewsFlag;
    }

    public void  setENewsFlag(int eNewsFlag)
    {
        this.eNewsFlag = eNewsFlag;
    }

    public int getIShow()
    {
        return iShow;
    }

    public void  setIShow(int iShow)
    {
        this.iShow = iShow;
    }

    public java.util.ArrayList<BEC.VideoConfig> getVVideoConfig()
    {
        return vVideoConfig;
    }

    public void  setVVideoConfig(java.util.ArrayList<BEC.VideoConfig> vVideoConfig)
    {
        this.vVideoConfig = vVideoConfig;
    }

    public int getITopicId()
    {
        return iTopicId;
    }

    public void  setITopicId(int iTopicId)
    {
        this.iTopicId = iTopicId;
    }

    public long getIAutoID()
    {
        return iAutoID;
    }

    public void  setIAutoID(long iAutoID)
    {
        this.iAutoID = iAutoID;
    }

    public java.util.ArrayList<BEC.Label> getVLabel()
    {
        return vLabel;
    }

    public void  setVLabel(java.util.ArrayList<BEC.Label> vLabel)
    {
        this.vLabel = vLabel;
    }

    public NewsDesc()
    {
    }

    public NewsDesc(String sNewsID, int eNewsType, String sTitle, String sDescription, String sFrom, int iTime, String sDtSecCode, java.util.ArrayList<BEC.TagInfo> vtTagInfo, String sContent, String sDtInfoUrl, int iStyleType, java.util.ArrayList<BEC.SecInfo> vtRelaStock, int iStatus, String sThirdUrl, int iCreateTime, String sSubTypeName, int iPublish, String sImgUrl, int eNewsFlag, int iShow, java.util.ArrayList<BEC.VideoConfig> vVideoConfig, int iTopicId, long iAutoID, java.util.ArrayList<BEC.Label> vLabel)
    {
        this.sNewsID = sNewsID;
        this.eNewsType = eNewsType;
        this.sTitle = sTitle;
        this.sDescription = sDescription;
        this.sFrom = sFrom;
        this.iTime = iTime;
        this.sDtSecCode = sDtSecCode;
        this.vtTagInfo = vtTagInfo;
        this.sContent = sContent;
        this.sDtInfoUrl = sDtInfoUrl;
        this.iStyleType = iStyleType;
        this.vtRelaStock = vtRelaStock;
        this.iStatus = iStatus;
        this.sThirdUrl = sThirdUrl;
        this.iCreateTime = iCreateTime;
        this.sSubTypeName = sSubTypeName;
        this.iPublish = iPublish;
        this.sImgUrl = sImgUrl;
        this.eNewsFlag = eNewsFlag;
        this.iShow = iShow;
        this.vVideoConfig = vVideoConfig;
        this.iTopicId = iTopicId;
        this.iAutoID = iAutoID;
        this.vLabel = vLabel;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sNewsID) {
            ostream.writeString(0, sNewsID);
        }
        ostream.writeInt32(1, eNewsType);
        if (null != sTitle) {
            ostream.writeString(2, sTitle);
        }
        if (null != sDescription) {
            ostream.writeString(3, sDescription);
        }
        if (null != sFrom) {
            ostream.writeString(4, sFrom);
        }
        ostream.writeInt32(5, iTime);
        if (null != sDtSecCode) {
            ostream.writeString(6, sDtSecCode);
        }
        if (null != vtTagInfo) {
            ostream.writeList(7, vtTagInfo);
        }
        if (null != sContent) {
            ostream.writeString(8, sContent);
        }
        if (null != sDtInfoUrl) {
            ostream.writeString(9, sDtInfoUrl);
        }
        ostream.writeInt32(10, iStyleType);
        if (null != vtRelaStock) {
            ostream.writeList(11, vtRelaStock);
        }
        ostream.writeInt32(12, iStatus);
        if (null != sThirdUrl) {
            ostream.writeString(13, sThirdUrl);
        }
        ostream.writeInt32(14, iCreateTime);
        if (null != sSubTypeName) {
            ostream.writeString(16, sSubTypeName);
        }
        ostream.writeInt32(17, iPublish);
        if (null != sImgUrl) {
            ostream.writeString(18, sImgUrl);
        }
        ostream.writeInt32(19, eNewsFlag);
        ostream.writeInt32(20, iShow);
        if (null != vVideoConfig) {
            ostream.writeList(21, vVideoConfig);
        }
        ostream.writeInt32(22, iTopicId);
        ostream.writeInt64(23, iAutoID);
        if (null != vLabel) {
            ostream.writeList(24, vLabel);
        }
    }

    static java.util.ArrayList<BEC.TagInfo> VAR_TYPE_4_VTTAGINFO = new java.util.ArrayList<BEC.TagInfo>();
    static {
        VAR_TYPE_4_VTTAGINFO.add(new BEC.TagInfo());
    }

    static java.util.ArrayList<BEC.SecInfo> VAR_TYPE_4_VTRELASTOCK = new java.util.ArrayList<BEC.SecInfo>();
    static {
        VAR_TYPE_4_VTRELASTOCK.add(new BEC.SecInfo());
    }

    static java.util.ArrayList<BEC.VideoConfig> VAR_TYPE_4_VVIDEOCONFIG = new java.util.ArrayList<BEC.VideoConfig>();
    static {
        VAR_TYPE_4_VVIDEOCONFIG.add(new BEC.VideoConfig());
    }

    static java.util.ArrayList<BEC.Label> VAR_TYPE_4_VLABEL = new java.util.ArrayList<BEC.Label>();
    static {
        VAR_TYPE_4_VLABEL.add(new BEC.Label());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sNewsID = (String)istream.readString(0, false, this.sNewsID);
        this.eNewsType = (int)istream.readInt32(1, false, this.eNewsType);
        this.sTitle = (String)istream.readString(2, false, this.sTitle);
        this.sDescription = (String)istream.readString(3, false, this.sDescription);
        this.sFrom = (String)istream.readString(4, false, this.sFrom);
        this.iTime = (int)istream.readInt32(5, false, this.iTime);
        this.sDtSecCode = (String)istream.readString(6, false, this.sDtSecCode);
        this.vtTagInfo = (java.util.ArrayList<BEC.TagInfo>)istream.readList(7, false, VAR_TYPE_4_VTTAGINFO);
        this.sContent = (String)istream.readString(8, false, this.sContent);
        this.sDtInfoUrl = (String)istream.readString(9, false, this.sDtInfoUrl);
        this.iStyleType = (int)istream.readInt32(10, false, this.iStyleType);
        this.vtRelaStock = (java.util.ArrayList<BEC.SecInfo>)istream.readList(11, false, VAR_TYPE_4_VTRELASTOCK);
        this.iStatus = (int)istream.readInt32(12, false, this.iStatus);
        this.sThirdUrl = (String)istream.readString(13, false, this.sThirdUrl);
        this.iCreateTime = (int)istream.readInt32(14, false, this.iCreateTime);
        this.sSubTypeName = (String)istream.readString(16, false, this.sSubTypeName);
        this.iPublish = (int)istream.readInt32(17, false, this.iPublish);
        this.sImgUrl = (String)istream.readString(18, false, this.sImgUrl);
        this.eNewsFlag = (int)istream.readInt32(19, false, this.eNewsFlag);
        this.iShow = (int)istream.readInt32(20, false, this.iShow);
        this.vVideoConfig = (java.util.ArrayList<BEC.VideoConfig>)istream.readList(21, false, VAR_TYPE_4_VVIDEOCONFIG);
        this.iTopicId = (int)istream.readInt32(22, false, this.iTopicId);
        this.iAutoID = (long)istream.readInt64(23, false, this.iAutoID);
        this.vLabel = (java.util.ArrayList<BEC.Label>)istream.readList(24, false, VAR_TYPE_4_VLABEL);
    }

}

