package BEC;

public final class GetSecDailyRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sTradingDay = "";

    public int iType = 0;

    public java.util.ArrayList<SecDailyInfo> vInfo = null;

    public DefaultDailyInfo stDefault = null;

    public DtMorNewsItem stMorNews = null;

    public String getSTradingDay()
    {
        return sTradingDay;
    }

    public void  setSTradingDay(String sTradingDay)
    {
        this.sTradingDay = sTradingDay;
    }

    public int getIType()
    {
        return iType;
    }

    public void  setIType(int iType)
    {
        this.iType = iType;
    }

    public java.util.ArrayList<SecDailyInfo> getVInfo()
    {
        return vInfo;
    }

    public void  setVInfo(java.util.ArrayList<SecDailyInfo> vInfo)
    {
        this.vInfo = vInfo;
    }

    public DefaultDailyInfo getStDefault()
    {
        return stDefault;
    }

    public void  setStDefault(DefaultDailyInfo stDefault)
    {
        this.stDefault = stDefault;
    }

    public DtMorNewsItem getStMorNews()
    {
        return stMorNews;
    }

    public void  setStMorNews(DtMorNewsItem stMorNews)
    {
        this.stMorNews = stMorNews;
    }

    public GetSecDailyRsp()
    {
    }

    public GetSecDailyRsp(String sTradingDay, int iType, java.util.ArrayList<SecDailyInfo> vInfo, DefaultDailyInfo stDefault, DtMorNewsItem stMorNews)
    {
        this.sTradingDay = sTradingDay;
        this.iType = iType;
        this.vInfo = vInfo;
        this.stDefault = stDefault;
        this.stMorNews = stMorNews;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sTradingDay) {
            ostream.writeString(0, sTradingDay);
        }
        ostream.writeInt32(1, iType);
        if (null != vInfo) {
            ostream.writeList(2, vInfo);
        }
        if (null != stDefault) {
            ostream.writeMessage(3, stDefault);
        }
        if (null != stMorNews) {
            ostream.writeMessage(4, stMorNews);
        }
    }

    static java.util.ArrayList<SecDailyInfo> VAR_TYPE_4_VINFO = new java.util.ArrayList<SecDailyInfo>();
    static {
        VAR_TYPE_4_VINFO.add(new SecDailyInfo());
    }

    static DefaultDailyInfo VAR_TYPE_4_STDEFAULT = new DefaultDailyInfo();

    static DtMorNewsItem VAR_TYPE_4_STMORNEWS = new DtMorNewsItem();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sTradingDay = (String)istream.readString(0, false, this.sTradingDay);
        this.iType = (int)istream.readInt32(1, false, this.iType);
        this.vInfo = (java.util.ArrayList<SecDailyInfo>)istream.readList(2, false, VAR_TYPE_4_VINFO);
        this.stDefault = (DefaultDailyInfo)istream.readMessage(3, false, VAR_TYPE_4_STDEFAULT);
        this.stMorNews = (DtMorNewsItem)istream.readMessage(4, false, VAR_TYPE_4_STMORNEWS);
    }

}

