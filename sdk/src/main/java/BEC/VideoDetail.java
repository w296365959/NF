package BEC;

public final class VideoDetail extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sVideoKey = "";

    public int iOrder = 0;

    public int eType = 0;

    public int eChannel = 0;

    public String sVideoId = "";

    public int iBeginTime = 0;

    public int iEndTime = 0;

    public int ePlayType = 0;

    public String sPlayId = "";

    public String sPassWord = "";

    public String sUrl = "";

    public String sTitle = "";

    public String sDescription = "";

    public int eStatus = 0;

    public String sImgUrl = "";

    public java.util.ArrayList<String> vTags = null;

    public java.util.ArrayList<BEC.Professor> vtProfessor = null;

    public int iWatchNum = 0;

    public String sGroupKey = "";

    public String sBuyDate = "";

    public String sExpiryDate = "";

    public float fPrice = 0;

    public int iBuyDays = 0;

    public String sSourceData = "";

    public int iFaverateStatus = 0;

    public int iFaverateTime = 0;

    public int iExpiryDays = 0;

    public BEC.VideoChannelDesc stChannel = null;

    public String getSVideoKey()
    {
        return sVideoKey;
    }

    public void  setSVideoKey(String sVideoKey)
    {
        this.sVideoKey = sVideoKey;
    }

    public int getIOrder()
    {
        return iOrder;
    }

    public void  setIOrder(int iOrder)
    {
        this.iOrder = iOrder;
    }

    public int getEType()
    {
        return eType;
    }

    public void  setEType(int eType)
    {
        this.eType = eType;
    }

    public int getEChannel()
    {
        return eChannel;
    }

    public void  setEChannel(int eChannel)
    {
        this.eChannel = eChannel;
    }

    public String getSVideoId()
    {
        return sVideoId;
    }

    public void  setSVideoId(String sVideoId)
    {
        this.sVideoId = sVideoId;
    }

    public int getIBeginTime()
    {
        return iBeginTime;
    }

    public void  setIBeginTime(int iBeginTime)
    {
        this.iBeginTime = iBeginTime;
    }

    public int getIEndTime()
    {
        return iEndTime;
    }

    public void  setIEndTime(int iEndTime)
    {
        this.iEndTime = iEndTime;
    }

    public int getEPlayType()
    {
        return ePlayType;
    }

    public void  setEPlayType(int ePlayType)
    {
        this.ePlayType = ePlayType;
    }

    public String getSPlayId()
    {
        return sPlayId;
    }

    public void  setSPlayId(String sPlayId)
    {
        this.sPlayId = sPlayId;
    }

    public String getSPassWord()
    {
        return sPassWord;
    }

    public void  setSPassWord(String sPassWord)
    {
        this.sPassWord = sPassWord;
    }

    public String getSUrl()
    {
        return sUrl;
    }

    public void  setSUrl(String sUrl)
    {
        this.sUrl = sUrl;
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

    public int getEStatus()
    {
        return eStatus;
    }

    public void  setEStatus(int eStatus)
    {
        this.eStatus = eStatus;
    }

    public String getSImgUrl()
    {
        return sImgUrl;
    }

    public void  setSImgUrl(String sImgUrl)
    {
        this.sImgUrl = sImgUrl;
    }

    public java.util.ArrayList<String> getVTags()
    {
        return vTags;
    }

    public void  setVTags(java.util.ArrayList<String> vTags)
    {
        this.vTags = vTags;
    }

    public java.util.ArrayList<BEC.Professor> getVtProfessor()
    {
        return vtProfessor;
    }

    public void  setVtProfessor(java.util.ArrayList<BEC.Professor> vtProfessor)
    {
        this.vtProfessor = vtProfessor;
    }

    public int getIWatchNum()
    {
        return iWatchNum;
    }

    public void  setIWatchNum(int iWatchNum)
    {
        this.iWatchNum = iWatchNum;
    }

    public String getSGroupKey()
    {
        return sGroupKey;
    }

    public void  setSGroupKey(String sGroupKey)
    {
        this.sGroupKey = sGroupKey;
    }

    public String getSBuyDate()
    {
        return sBuyDate;
    }

    public void  setSBuyDate(String sBuyDate)
    {
        this.sBuyDate = sBuyDate;
    }

    public String getSExpiryDate()
    {
        return sExpiryDate;
    }

    public void  setSExpiryDate(String sExpiryDate)
    {
        this.sExpiryDate = sExpiryDate;
    }

    public float getFPrice()
    {
        return fPrice;
    }

    public void  setFPrice(float fPrice)
    {
        this.fPrice = fPrice;
    }

    public int getIBuyDays()
    {
        return iBuyDays;
    }

    public void  setIBuyDays(int iBuyDays)
    {
        this.iBuyDays = iBuyDays;
    }

    public String getSSourceData()
    {
        return sSourceData;
    }

    public void  setSSourceData(String sSourceData)
    {
        this.sSourceData = sSourceData;
    }

    public int getIFaverateStatus()
    {
        return iFaverateStatus;
    }

    public void  setIFaverateStatus(int iFaverateStatus)
    {
        this.iFaverateStatus = iFaverateStatus;
    }

    public int getIFaverateTime()
    {
        return iFaverateTime;
    }

    public void  setIFaverateTime(int iFaverateTime)
    {
        this.iFaverateTime = iFaverateTime;
    }

    public int getIExpiryDays()
    {
        return iExpiryDays;
    }

    public void  setIExpiryDays(int iExpiryDays)
    {
        this.iExpiryDays = iExpiryDays;
    }

    public BEC.VideoChannelDesc getStChannel()
    {
        return stChannel;
    }

    public void  setStChannel(BEC.VideoChannelDesc stChannel)
    {
        this.stChannel = stChannel;
    }

    public VideoDetail()
    {
    }

    public VideoDetail(String sVideoKey, int iOrder, int eType, int eChannel, String sVideoId, int iBeginTime, int iEndTime, int ePlayType, String sPlayId, String sPassWord, String sUrl, String sTitle, String sDescription, int eStatus, String sImgUrl, java.util.ArrayList<String> vTags, java.util.ArrayList<BEC.Professor> vtProfessor, int iWatchNum, String sGroupKey, String sBuyDate, String sExpiryDate, float fPrice, int iBuyDays, String sSourceData, int iFaverateStatus, int iFaverateTime, int iExpiryDays, BEC.VideoChannelDesc stChannel)
    {
        this.sVideoKey = sVideoKey;
        this.iOrder = iOrder;
        this.eType = eType;
        this.eChannel = eChannel;
        this.sVideoId = sVideoId;
        this.iBeginTime = iBeginTime;
        this.iEndTime = iEndTime;
        this.ePlayType = ePlayType;
        this.sPlayId = sPlayId;
        this.sPassWord = sPassWord;
        this.sUrl = sUrl;
        this.sTitle = sTitle;
        this.sDescription = sDescription;
        this.eStatus = eStatus;
        this.sImgUrl = sImgUrl;
        this.vTags = vTags;
        this.vtProfessor = vtProfessor;
        this.iWatchNum = iWatchNum;
        this.sGroupKey = sGroupKey;
        this.sBuyDate = sBuyDate;
        this.sExpiryDate = sExpiryDate;
        this.fPrice = fPrice;
        this.iBuyDays = iBuyDays;
        this.sSourceData = sSourceData;
        this.iFaverateStatus = iFaverateStatus;
        this.iFaverateTime = iFaverateTime;
        this.iExpiryDays = iExpiryDays;
        this.stChannel = stChannel;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sVideoKey) {
            ostream.writeString(0, sVideoKey);
        }
        ostream.writeInt32(1, iOrder);
        ostream.writeInt32(2, eType);
        ostream.writeInt32(3, eChannel);
        if (null != sVideoId) {
            ostream.writeString(4, sVideoId);
        }
        ostream.writeInt32(5, iBeginTime);
        ostream.writeInt32(6, iEndTime);
        ostream.writeInt32(7, ePlayType);
        if (null != sPlayId) {
            ostream.writeString(8, sPlayId);
        }
        if (null != sPassWord) {
            ostream.writeString(9, sPassWord);
        }
        if (null != sUrl) {
            ostream.writeString(10, sUrl);
        }
        if (null != sTitle) {
            ostream.writeString(11, sTitle);
        }
        if (null != sDescription) {
            ostream.writeString(12, sDescription);
        }
        ostream.writeInt32(13, eStatus);
        if (null != sImgUrl) {
            ostream.writeString(14, sImgUrl);
        }
        if (null != vTags) {
            ostream.writeList(15, vTags);
        }
        if (null != vtProfessor) {
            ostream.writeList(16, vtProfessor);
        }
        ostream.writeInt32(17, iWatchNum);
        if (null != sGroupKey) {
            ostream.writeString(18, sGroupKey);
        }
        if (null != sBuyDate) {
            ostream.writeString(19, sBuyDate);
        }
        if (null != sExpiryDate) {
            ostream.writeString(20, sExpiryDate);
        }
        ostream.writeFloat(21, fPrice);
        ostream.writeInt32(22, iBuyDays);
        if (null != sSourceData) {
            ostream.writeString(23, sSourceData);
        }
        ostream.writeInt32(24, iFaverateStatus);
        ostream.writeInt32(25, iFaverateTime);
        ostream.writeInt32(26, iExpiryDays);
        if (null != stChannel) {
            ostream.writeMessage(27, stChannel);
        }
    }

    static java.util.ArrayList<String> VAR_TYPE_4_VTAGS = new java.util.ArrayList<String>();
    static {
        VAR_TYPE_4_VTAGS.add("");
    }

    static java.util.ArrayList<BEC.Professor> VAR_TYPE_4_VTPROFESSOR = new java.util.ArrayList<BEC.Professor>();
    static {
        VAR_TYPE_4_VTPROFESSOR.add(new BEC.Professor());
    }

    static BEC.VideoChannelDesc VAR_TYPE_4_STCHANNEL = new BEC.VideoChannelDesc();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sVideoKey = (String)istream.readString(0, false, this.sVideoKey);
        this.iOrder = (int)istream.readInt32(1, false, this.iOrder);
        this.eType = (int)istream.readInt32(2, false, this.eType);
        this.eChannel = (int)istream.readInt32(3, false, this.eChannel);
        this.sVideoId = (String)istream.readString(4, false, this.sVideoId);
        this.iBeginTime = (int)istream.readInt32(5, false, this.iBeginTime);
        this.iEndTime = (int)istream.readInt32(6, false, this.iEndTime);
        this.ePlayType = (int)istream.readInt32(7, false, this.ePlayType);
        this.sPlayId = (String)istream.readString(8, false, this.sPlayId);
        this.sPassWord = (String)istream.readString(9, false, this.sPassWord);
        this.sUrl = (String)istream.readString(10, false, this.sUrl);
        this.sTitle = (String)istream.readString(11, false, this.sTitle);
        this.sDescription = (String)istream.readString(12, false, this.sDescription);
        this.eStatus = (int)istream.readInt32(13, false, this.eStatus);
        this.sImgUrl = (String)istream.readString(14, false, this.sImgUrl);
        this.vTags = (java.util.ArrayList<String>)istream.readList(15, false, VAR_TYPE_4_VTAGS);
        this.vtProfessor = (java.util.ArrayList<BEC.Professor>)istream.readList(16, false, VAR_TYPE_4_VTPROFESSOR);
        this.iWatchNum = (int)istream.readInt32(17, false, this.iWatchNum);
        this.sGroupKey = (String)istream.readString(18, false, this.sGroupKey);
        this.sBuyDate = (String)istream.readString(19, false, this.sBuyDate);
        this.sExpiryDate = (String)istream.readString(20, false, this.sExpiryDate);
        this.fPrice = (float)istream.readFloat(21, false, this.fPrice);
        this.iBuyDays = (int)istream.readInt32(22, false, this.iBuyDays);
        this.sSourceData = (String)istream.readString(23, false, this.sSourceData);
        this.iFaverateStatus = (int)istream.readInt32(24, false, this.iFaverateStatus);
        this.iFaverateTime = (int)istream.readInt32(25, false, this.iFaverateTime);
        this.iExpiryDays = (int)istream.readInt32(26, false, this.iExpiryDays);
        this.stChannel = (BEC.VideoChannelDesc)istream.readMessage(27, false, VAR_TYPE_4_STCHANNEL);
    }

}

