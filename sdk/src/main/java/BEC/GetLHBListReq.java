package BEC;

public final class GetLHBListReq extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public UserInfo stUserInfo = null;

    public String sDay = "";

    public int iSubList = 0;

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

    public int getISubList()
    {
        return iSubList;
    }

    public void  setISubList(int iSubList)
    {
        this.iSubList = iSubList;
    }

    public GetLHBListReq()
    {
    }

    public GetLHBListReq(UserInfo stUserInfo, String sDay, int iSubList)
    {
        this.stUserInfo = stUserInfo;
        this.sDay = sDay;
        this.iSubList = iSubList;
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
        ostream.writeInt32(2, iSubList);
    }

    static UserInfo VAR_TYPE_4_STUSERINFO = new UserInfo();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.sDay = (String)istream.readString(1, false, this.sDay);
        this.iSubList = (int)istream.readInt32(2, false, this.iSubList);
    }

}

