package BEC;

public final class PoFeedReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.UserInfo stUserInfo = null;

    public String sTitle = "";

    public String sContent = "";

    public BEC.AccountTicket stAccountTicket = null;

    public java.util.ArrayList<BEC.SecCodeName> vRelateSec = null;

    public int eType = BEC.E_FEED_TYPE.E_FT_STOCK_REVIEW;

    public int eAttiType = BEC.E_FEED_INVEST_ATTI_TYPE.E_FIAT_NEUTRAL;

    public String sDescription = "";

    public String sClientFeedId = "";

    public String sFeedId = "";

    public int iPubTime = -1;

    public int eFeedSourceType = BEC.E_FEED_SOURCE_TYPE.E_FEED_SOURCE_USER;

    public BEC.UserInfo getStUserInfo()
    {
        return stUserInfo;
    }

    public void  setStUserInfo(BEC.UserInfo stUserInfo)
    {
        this.stUserInfo = stUserInfo;
    }

    public String getSTitle()
    {
        return sTitle;
    }

    public void  setSTitle(String sTitle)
    {
        this.sTitle = sTitle;
    }

    public String getSContent()
    {
        return sContent;
    }

    public void  setSContent(String sContent)
    {
        this.sContent = sContent;
    }

    public BEC.AccountTicket getStAccountTicket()
    {
        return stAccountTicket;
    }

    public void  setStAccountTicket(BEC.AccountTicket stAccountTicket)
    {
        this.stAccountTicket = stAccountTicket;
    }

    public java.util.ArrayList<BEC.SecCodeName> getVRelateSec()
    {
        return vRelateSec;
    }

    public void  setVRelateSec(java.util.ArrayList<BEC.SecCodeName> vRelateSec)
    {
        this.vRelateSec = vRelateSec;
    }

    public int getEType()
    {
        return eType;
    }

    public void  setEType(int eType)
    {
        this.eType = eType;
    }

    public int getEAttiType()
    {
        return eAttiType;
    }

    public void  setEAttiType(int eAttiType)
    {
        this.eAttiType = eAttiType;
    }

    public String getSDescription()
    {
        return sDescription;
    }

    public void  setSDescription(String sDescription)
    {
        this.sDescription = sDescription;
    }

    public String getSClientFeedId()
    {
        return sClientFeedId;
    }

    public void  setSClientFeedId(String sClientFeedId)
    {
        this.sClientFeedId = sClientFeedId;
    }

    public String getSFeedId()
    {
        return sFeedId;
    }

    public void  setSFeedId(String sFeedId)
    {
        this.sFeedId = sFeedId;
    }

    public int getIPubTime()
    {
        return iPubTime;
    }

    public void  setIPubTime(int iPubTime)
    {
        this.iPubTime = iPubTime;
    }

    public int getEFeedSourceType()
    {
        return eFeedSourceType;
    }

    public void  setEFeedSourceType(int eFeedSourceType)
    {
        this.eFeedSourceType = eFeedSourceType;
    }

    public PoFeedReq()
    {
    }

    public PoFeedReq(BEC.UserInfo stUserInfo, String sTitle, String sContent, BEC.AccountTicket stAccountTicket, java.util.ArrayList<BEC.SecCodeName> vRelateSec, int eType, int eAttiType, String sDescription, String sClientFeedId, String sFeedId, int iPubTime, int eFeedSourceType)
    {
        this.stUserInfo = stUserInfo;
        this.sTitle = sTitle;
        this.sContent = sContent;
        this.stAccountTicket = stAccountTicket;
        this.vRelateSec = vRelateSec;
        this.eType = eType;
        this.eAttiType = eAttiType;
        this.sDescription = sDescription;
        this.sClientFeedId = sClientFeedId;
        this.sFeedId = sFeedId;
        this.iPubTime = iPubTime;
        this.eFeedSourceType = eFeedSourceType;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stUserInfo) {
            ostream.writeMessage(0, stUserInfo);
        }
        if (null != sTitle) {
            ostream.writeString(1, sTitle);
        }
        if (null != sContent) {
            ostream.writeString(2, sContent);
        }
        if (null != stAccountTicket) {
            ostream.writeMessage(3, stAccountTicket);
        }
        if (null != vRelateSec) {
            ostream.writeList(4, vRelateSec);
        }
        ostream.writeInt32(5, eType);
        ostream.writeInt32(6, eAttiType);
        if (null != sDescription) {
            ostream.writeString(7, sDescription);
        }
        if (null != sClientFeedId) {
            ostream.writeString(8, sClientFeedId);
        }
        if (null != sFeedId) {
            ostream.writeString(9, sFeedId);
        }
        ostream.writeInt32(10, iPubTime);
        ostream.writeInt32(11, eFeedSourceType);
    }

    static BEC.UserInfo VAR_TYPE_4_STUSERINFO = new BEC.UserInfo();

    static BEC.AccountTicket VAR_TYPE_4_STACCOUNTTICKET = new BEC.AccountTicket();

    static java.util.ArrayList<BEC.SecCodeName> VAR_TYPE_4_VRELATESEC = new java.util.ArrayList<BEC.SecCodeName>();
    static {
        VAR_TYPE_4_VRELATESEC.add(new BEC.SecCodeName());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (BEC.UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.sTitle = (String)istream.readString(1, false, this.sTitle);
        this.sContent = (String)istream.readString(2, false, this.sContent);
        this.stAccountTicket = (BEC.AccountTicket)istream.readMessage(3, false, VAR_TYPE_4_STACCOUNTTICKET);
        this.vRelateSec = (java.util.ArrayList<BEC.SecCodeName>)istream.readList(4, false, VAR_TYPE_4_VRELATESEC);
        this.eType = (int)istream.readInt32(5, false, this.eType);
        this.eAttiType = (int)istream.readInt32(6, false, this.eAttiType);
        this.sDescription = (String)istream.readString(7, false, this.sDescription);
        this.sClientFeedId = (String)istream.readString(8, false, this.sClientFeedId);
        this.sFeedId = (String)istream.readString(9, false, this.sFeedId);
        this.iPubTime = (int)istream.readInt32(10, false, this.iPubTime);
        this.eFeedSourceType = (int)istream.readInt32(11, false, this.eFeedSourceType);
    }

}

