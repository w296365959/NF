package BEC;

public final class VideoDesc extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sVideoId = "";

    public int iType = 0;

    public String sTitle = "";

    public String sDescription = "";

    public int iBeginTime = 0;

    public int iEndTime = 0;

    public int iLiveState = 0;

    public String sType = "";

    public java.util.ArrayList<BEC.VideoImg> vtVideoImg = null;

    public java.util.ArrayList<String> vTags = null;

    public BEC.VideoTeacher stVideoTeacher = null;

    public String sVodId = "";

    public String sVodPassWord = "";

    public String sWebVodId = "";

    public String sWebVodPassWord = "";

    public String sLiveId = "";

    public String sLivePassWord = "";

    public String sWebLiveId = "";

    public String sWebLivePassWord = "";

    public int iWatchNum = 0;

    public int iTotalJoinedNum = 0;

    public String getSVideoId()
    {
        return sVideoId;
    }

    public void  setSVideoId(String sVideoId)
    {
        this.sVideoId = sVideoId;
    }

    public int getIType()
    {
        return iType;
    }

    public void  setIType(int iType)
    {
        this.iType = iType;
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

    public int getILiveState()
    {
        return iLiveState;
    }

    public void  setILiveState(int iLiveState)
    {
        this.iLiveState = iLiveState;
    }

    public String getSType()
    {
        return sType;
    }

    public void  setSType(String sType)
    {
        this.sType = sType;
    }

    public java.util.ArrayList<BEC.VideoImg> getVtVideoImg()
    {
        return vtVideoImg;
    }

    public void  setVtVideoImg(java.util.ArrayList<BEC.VideoImg> vtVideoImg)
    {
        this.vtVideoImg = vtVideoImg;
    }

    public java.util.ArrayList<String> getVTags()
    {
        return vTags;
    }

    public void  setVTags(java.util.ArrayList<String> vTags)
    {
        this.vTags = vTags;
    }

    public BEC.VideoTeacher getStVideoTeacher()
    {
        return stVideoTeacher;
    }

    public void  setStVideoTeacher(BEC.VideoTeacher stVideoTeacher)
    {
        this.stVideoTeacher = stVideoTeacher;
    }

    public String getSVodId()
    {
        return sVodId;
    }

    public void  setSVodId(String sVodId)
    {
        this.sVodId = sVodId;
    }

    public String getSVodPassWord()
    {
        return sVodPassWord;
    }

    public void  setSVodPassWord(String sVodPassWord)
    {
        this.sVodPassWord = sVodPassWord;
    }

    public String getSWebVodId()
    {
        return sWebVodId;
    }

    public void  setSWebVodId(String sWebVodId)
    {
        this.sWebVodId = sWebVodId;
    }

    public String getSWebVodPassWord()
    {
        return sWebVodPassWord;
    }

    public void  setSWebVodPassWord(String sWebVodPassWord)
    {
        this.sWebVodPassWord = sWebVodPassWord;
    }

    public String getSLiveId()
    {
        return sLiveId;
    }

    public void  setSLiveId(String sLiveId)
    {
        this.sLiveId = sLiveId;
    }

    public String getSLivePassWord()
    {
        return sLivePassWord;
    }

    public void  setSLivePassWord(String sLivePassWord)
    {
        this.sLivePassWord = sLivePassWord;
    }

    public String getSWebLiveId()
    {
        return sWebLiveId;
    }

    public void  setSWebLiveId(String sWebLiveId)
    {
        this.sWebLiveId = sWebLiveId;
    }

    public String getSWebLivePassWord()
    {
        return sWebLivePassWord;
    }

    public void  setSWebLivePassWord(String sWebLivePassWord)
    {
        this.sWebLivePassWord = sWebLivePassWord;
    }

    public int getIWatchNum()
    {
        return iWatchNum;
    }

    public void  setIWatchNum(int iWatchNum)
    {
        this.iWatchNum = iWatchNum;
    }

    public int getITotalJoinedNum()
    {
        return iTotalJoinedNum;
    }

    public void  setITotalJoinedNum(int iTotalJoinedNum)
    {
        this.iTotalJoinedNum = iTotalJoinedNum;
    }

    public VideoDesc()
    {
    }

    public VideoDesc(String sVideoId, int iType, String sTitle, String sDescription, int iBeginTime, int iEndTime, int iLiveState, String sType, java.util.ArrayList<BEC.VideoImg> vtVideoImg, java.util.ArrayList<String> vTags, BEC.VideoTeacher stVideoTeacher, String sVodId, String sVodPassWord, String sWebVodId, String sWebVodPassWord, String sLiveId, String sLivePassWord, String sWebLiveId, String sWebLivePassWord, int iWatchNum, int iTotalJoinedNum)
    {
        this.sVideoId = sVideoId;
        this.iType = iType;
        this.sTitle = sTitle;
        this.sDescription = sDescription;
        this.iBeginTime = iBeginTime;
        this.iEndTime = iEndTime;
        this.iLiveState = iLiveState;
        this.sType = sType;
        this.vtVideoImg = vtVideoImg;
        this.vTags = vTags;
        this.stVideoTeacher = stVideoTeacher;
        this.sVodId = sVodId;
        this.sVodPassWord = sVodPassWord;
        this.sWebVodId = sWebVodId;
        this.sWebVodPassWord = sWebVodPassWord;
        this.sLiveId = sLiveId;
        this.sLivePassWord = sLivePassWord;
        this.sWebLiveId = sWebLiveId;
        this.sWebLivePassWord = sWebLivePassWord;
        this.iWatchNum = iWatchNum;
        this.iTotalJoinedNum = iTotalJoinedNum;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sVideoId) {
            ostream.writeString(0, sVideoId);
        }
        ostream.writeInt32(1, iType);
        if (null != sTitle) {
            ostream.writeString(2, sTitle);
        }
        if (null != sDescription) {
            ostream.writeString(3, sDescription);
        }
        ostream.writeInt32(4, iBeginTime);
        ostream.writeInt32(5, iEndTime);
        ostream.writeInt32(6, iLiveState);
        if (null != sType) {
            ostream.writeString(7, sType);
        }
        if (null != vtVideoImg) {
            ostream.writeList(8, vtVideoImg);
        }
        if (null != vTags) {
            ostream.writeList(9, vTags);
        }
        if (null != stVideoTeacher) {
            ostream.writeMessage(10, stVideoTeacher);
        }
        if (null != sVodId) {
            ostream.writeString(11, sVodId);
        }
        if (null != sVodPassWord) {
            ostream.writeString(12, sVodPassWord);
        }
        if (null != sWebVodId) {
            ostream.writeString(13, sWebVodId);
        }
        if (null != sWebVodPassWord) {
            ostream.writeString(14, sWebVodPassWord);
        }
        if (null != sLiveId) {
            ostream.writeString(15, sLiveId);
        }
        if (null != sLivePassWord) {
            ostream.writeString(16, sLivePassWord);
        }
        if (null != sWebLiveId) {
            ostream.writeString(17, sWebLiveId);
        }
        if (null != sWebLivePassWord) {
            ostream.writeString(18, sWebLivePassWord);
        }
        ostream.writeInt32(19, iWatchNum);
        ostream.writeInt32(20, iTotalJoinedNum);
    }

    static java.util.ArrayList<BEC.VideoImg> VAR_TYPE_4_VTVIDEOIMG = new java.util.ArrayList<BEC.VideoImg>();
    static {
        VAR_TYPE_4_VTVIDEOIMG.add(new BEC.VideoImg());
    }

    static java.util.ArrayList<String> VAR_TYPE_4_VTAGS = new java.util.ArrayList<String>();
    static {
        VAR_TYPE_4_VTAGS.add("");
    }

    static BEC.VideoTeacher VAR_TYPE_4_STVIDEOTEACHER = new BEC.VideoTeacher();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sVideoId = (String)istream.readString(0, false, this.sVideoId);
        this.iType = (int)istream.readInt32(1, false, this.iType);
        this.sTitle = (String)istream.readString(2, false, this.sTitle);
        this.sDescription = (String)istream.readString(3, false, this.sDescription);
        this.iBeginTime = (int)istream.readInt32(4, false, this.iBeginTime);
        this.iEndTime = (int)istream.readInt32(5, false, this.iEndTime);
        this.iLiveState = (int)istream.readInt32(6, false, this.iLiveState);
        this.sType = (String)istream.readString(7, false, this.sType);
        this.vtVideoImg = (java.util.ArrayList<BEC.VideoImg>)istream.readList(8, false, VAR_TYPE_4_VTVIDEOIMG);
        this.vTags = (java.util.ArrayList<String>)istream.readList(9, false, VAR_TYPE_4_VTAGS);
        this.stVideoTeacher = (BEC.VideoTeacher)istream.readMessage(10, false, VAR_TYPE_4_STVIDEOTEACHER);
        this.sVodId = (String)istream.readString(11, false, this.sVodId);
        this.sVodPassWord = (String)istream.readString(12, false, this.sVodPassWord);
        this.sWebVodId = (String)istream.readString(13, false, this.sWebVodId);
        this.sWebVodPassWord = (String)istream.readString(14, false, this.sWebVodPassWord);
        this.sLiveId = (String)istream.readString(15, false, this.sLiveId);
        this.sLivePassWord = (String)istream.readString(16, false, this.sLivePassWord);
        this.sWebLiveId = (String)istream.readString(17, false, this.sWebLiveId);
        this.sWebLivePassWord = (String)istream.readString(18, false, this.sWebLivePassWord);
        this.iWatchNum = (int)istream.readInt32(19, false, this.iWatchNum);
        this.iTotalJoinedNum = (int)istream.readInt32(20, false, this.iTotalJoinedNum);
    }

}

