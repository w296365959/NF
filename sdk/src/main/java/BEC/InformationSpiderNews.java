package BEC;

public final class InformationSpiderNews extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iID = 0;

    public int iTID = 0;

    public int iSourceID = 0;

    public int iStatus = 0;

    public int iTopFlag = 0;

    public String sTopTime = "";

    public String sEndToptime = "";

    public String sTitle = "";

    public String sAbstracts = "";

    public String sContent = "";

    public String sAuthor = "";

    public String sSource = "";

    public String sInformationTime = "";

    public int iKind = 0;

    public int itype = 0;

    public String sUrl = "";

    public String sImgUrl = "";

    public String sShares = "";

    public String sReadCount = "";

    public int getIID()
    {
        return iID;
    }

    public void  setIID(int iID)
    {
        this.iID = iID;
    }

    public int getITID()
    {
        return iTID;
    }

    public void  setITID(int iTID)
    {
        this.iTID = iTID;
    }

    public int getISourceID()
    {
        return iSourceID;
    }

    public void  setISourceID(int iSourceID)
    {
        this.iSourceID = iSourceID;
    }

    public int getIStatus()
    {
        return iStatus;
    }

    public void  setIStatus(int iStatus)
    {
        this.iStatus = iStatus;
    }

    public int getITopFlag()
    {
        return iTopFlag;
    }

    public void  setITopFlag(int iTopFlag)
    {
        this.iTopFlag = iTopFlag;
    }

    public String getSTopTime()
    {
        return sTopTime;
    }

    public void  setSTopTime(String sTopTime)
    {
        this.sTopTime = sTopTime;
    }

    public String getSEndToptime()
    {
        return sEndToptime;
    }

    public void  setSEndToptime(String sEndToptime)
    {
        this.sEndToptime = sEndToptime;
    }

    public String getSTitle()
    {
        return sTitle;
    }

    public void  setSTitle(String sTitle)
    {
        this.sTitle = sTitle;
    }

    public String getSAbstracts()
    {
        return sAbstracts;
    }

    public void  setSAbstracts(String sAbstracts)
    {
        this.sAbstracts = sAbstracts;
    }

    public String getSContent()
    {
        return sContent;
    }

    public void  setSContent(String sContent)
    {
        this.sContent = sContent;
    }

    public String getSAuthor()
    {
        return sAuthor;
    }

    public void  setSAuthor(String sAuthor)
    {
        this.sAuthor = sAuthor;
    }

    public String getSSource()
    {
        return sSource;
    }

    public void  setSSource(String sSource)
    {
        this.sSource = sSource;
    }

    public String getSInformationTime()
    {
        return sInformationTime;
    }

    public void  setSInformationTime(String sInformationTime)
    {
        this.sInformationTime = sInformationTime;
    }

    public int getIKind()
    {
        return iKind;
    }

    public void  setIKind(int iKind)
    {
        this.iKind = iKind;
    }

    public int getItype()
    {
        return itype;
    }

    public void  setItype(int itype)
    {
        this.itype = itype;
    }

    public String getSUrl()
    {
        return sUrl;
    }

    public void  setSUrl(String sUrl)
    {
        this.sUrl = sUrl;
    }

    public String getSImgUrl()
    {
        return sImgUrl;
    }

    public void  setSImgUrl(String sImgUrl)
    {
        this.sImgUrl = sImgUrl;
    }

    public String getSShares()
    {
        return sShares;
    }

    public void  setSShares(String sShares)
    {
        this.sShares = sShares;
    }

    public String getSReadCount()
    {
        return sReadCount;
    }

    public void  setSReadCount(String sReadCount)
    {
        this.sReadCount = sReadCount;
    }

    public InformationSpiderNews()
    {
    }

    public InformationSpiderNews(int iID, int iTID, int iSourceID, int iStatus, int iTopFlag, String sTopTime, String sEndToptime, String sTitle, String sAbstracts, String sContent, String sAuthor, String sSource, String sInformationTime, int iKind, int itype, String sUrl, String sImgUrl, String sShares, String sReadCount)
    {
        this.iID = iID;
        this.iTID = iTID;
        this.iSourceID = iSourceID;
        this.iStatus = iStatus;
        this.iTopFlag = iTopFlag;
        this.sTopTime = sTopTime;
        this.sEndToptime = sEndToptime;
        this.sTitle = sTitle;
        this.sAbstracts = sAbstracts;
        this.sContent = sContent;
        this.sAuthor = sAuthor;
        this.sSource = sSource;
        this.sInformationTime = sInformationTime;
        this.iKind = iKind;
        this.itype = itype;
        this.sUrl = sUrl;
        this.sImgUrl = sImgUrl;
        this.sShares = sShares;
        this.sReadCount = sReadCount;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iID);
        ostream.writeInt32(1, iTID);
        ostream.writeInt32(2, iSourceID);
        ostream.writeInt32(3, iStatus);
        ostream.writeInt32(4, iTopFlag);
        if (null != sTopTime) {
            ostream.writeString(5, sTopTime);
        }
        if (null != sEndToptime) {
            ostream.writeString(6, sEndToptime);
        }
        if (null != sTitle) {
            ostream.writeString(7, sTitle);
        }
        if (null != sAbstracts) {
            ostream.writeString(8, sAbstracts);
        }
        if (null != sContent) {
            ostream.writeString(9, sContent);
        }
        if (null != sAuthor) {
            ostream.writeString(10, sAuthor);
        }
        if (null != sSource) {
            ostream.writeString(11, sSource);
        }
        if (null != sInformationTime) {
            ostream.writeString(12, sInformationTime);
        }
        ostream.writeInt32(13, iKind);
        ostream.writeInt32(14, itype);
        if (null != sUrl) {
            ostream.writeString(15, sUrl);
        }
        if (null != sImgUrl) {
            ostream.writeString(16, sImgUrl);
        }
        if (null != sShares) {
            ostream.writeString(17, sShares);
        }
        if (null != sReadCount) {
            ostream.writeString(18, sReadCount);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iID = (int)istream.readInt32(0, false, this.iID);
        this.iTID = (int)istream.readInt32(1, false, this.iTID);
        this.iSourceID = (int)istream.readInt32(2, false, this.iSourceID);
        this.iStatus = (int)istream.readInt32(3, false, this.iStatus);
        this.iTopFlag = (int)istream.readInt32(4, false, this.iTopFlag);
        this.sTopTime = (String)istream.readString(5, false, this.sTopTime);
        this.sEndToptime = (String)istream.readString(6, false, this.sEndToptime);
        this.sTitle = (String)istream.readString(7, false, this.sTitle);
        this.sAbstracts = (String)istream.readString(8, false, this.sAbstracts);
        this.sContent = (String)istream.readString(9, false, this.sContent);
        this.sAuthor = (String)istream.readString(10, false, this.sAuthor);
        this.sSource = (String)istream.readString(11, false, this.sSource);
        this.sInformationTime = (String)istream.readString(12, false, this.sInformationTime);
        this.iKind = (int)istream.readInt32(13, false, this.iKind);
        this.itype = (int)istream.readInt32(14, false, this.itype);
        this.sUrl = (String)istream.readString(15, false, this.sUrl);
        this.sImgUrl = (String)istream.readString(16, false, this.sImgUrl);
        this.sShares = (String)istream.readString(17, false, this.sShares);
        this.sReadCount = (String)istream.readString(18, false, this.sReadCount);
    }

}

