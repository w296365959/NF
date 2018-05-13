package BEC;

public final class GetLHBSaleDepDetailReq extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public UserInfo stUserInfo = null;

    public String sDay = "";

    public String sSaleDepName = "";

    public UserInfo getStUserInfo()
    {
        return stUserInfo;
    }

    public void  setStUserInfo(UserInfo stUserInfo)
    {
        this.stUserInfo = stUserInfo;
    }

    public String getSDay()
    {
        return sDay;
    }

    public void  setSDay(String sDay)
    {
        this.sDay = sDay;
    }

    public String getSSaleDepName()
    {
        return sSaleDepName;
    }

    public void  setSSaleDepName(String sSaleDepName)
    {
        this.sSaleDepName = sSaleDepName;
    }

    public GetLHBSaleDepDetailReq()
    {
    }

    public GetLHBSaleDepDetailReq(UserInfo stUserInfo, String sDay, String sSaleDepName)
    {
        this.stUserInfo = stUserInfo;
        this.sDay = sDay;
        this.sSaleDepName = sSaleDepName;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stUserInfo) {
            ostream.writeMessage(0, stUserInfo);
        }
        if (null != sDay) {
            ostream.writeString(1, sDay);
        }
        if (null != sSaleDepName) {
            ostream.writeString(2, sSaleDepName);
        }
    }

    static UserInfo VAR_TYPE_4_STUSERINFO = new UserInfo();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.sDay = (String)istream.readString(1, false, this.sDay);
        this.sSaleDepName = (String)istream.readString(2, false, this.sSaleDepName);
    }

}

