package BEC;

public final class ReportReadReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.UserInfo stUserInfo = null;

    public BEC.RecomItem stRecomItem = null;

    public BEC.UserInfo getStUserInfo()
    {
        return stUserInfo;
    }

    public void  setStUserInfo(BEC.UserInfo stUserInfo)
    {
        this.stUserInfo = stUserInfo;
    }

    public BEC.RecomItem getStRecomItem()
    {
        return stRecomItem;
    }

    public void  setStRecomItem(BEC.RecomItem stRecomItem)
    {
        this.stRecomItem = stRecomItem;
    }

    public ReportReadReq()
    {
    }

    public ReportReadReq(BEC.UserInfo stUserInfo, BEC.RecomItem stRecomItem)
    {
        this.stUserInfo = stUserInfo;
        this.stRecomItem = stRecomItem;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stUserInfo) {
            ostream.writeMessage(0, stUserInfo);
        }
        if (null != stRecomItem) {
            ostream.writeMessage(1, stRecomItem);
        }
    }

    static BEC.UserInfo VAR_TYPE_4_STUSERINFO = new BEC.UserInfo();

    static BEC.RecomItem VAR_TYPE_4_STRECOMITEM = new BEC.RecomItem();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (BEC.UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.stRecomItem = (BEC.RecomItem)istream.readMessage(1, false, VAR_TYPE_4_STRECOMITEM);
    }

}

