package BEC;

public final class SearchIntelliPickStockListReq extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public UserInfo stUserInfo = null;

    public int eSearchCondition = 0;

    public String sSearchStr = "";

    public UserInfo getStUserInfo()
    {
        return stUserInfo;
    }

    public void  setStUserInfo(UserInfo stUserInfo)
    {
        this.stUserInfo = stUserInfo;
    }

    public int getESearchCondition()
    {
        return eSearchCondition;
    }

    public void  setESearchCondition(int eSearchCondition)
    {
        this.eSearchCondition = eSearchCondition;
    }

    public String getSSearchStr()
    {
        return sSearchStr;
    }

    public void  setSSearchStr(String sSearchStr)
    {
        this.sSearchStr = sSearchStr;
    }

    public SearchIntelliPickStockListReq()
    {
    }

    public SearchIntelliPickStockListReq(UserInfo stUserInfo, int eSearchCondition, String sSearchStr)
    {
        this.stUserInfo = stUserInfo;
        this.eSearchCondition = eSearchCondition;
        this.sSearchStr = sSearchStr;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stUserInfo) {
            ostream.writeMessage(0, stUserInfo);
        }
        ostream.writeInt32(1, eSearchCondition);
        if (null != sSearchStr) {
            ostream.writeString(2, sSearchStr);
        }
    }

    static UserInfo VAR_TYPE_4_STUSERINFO = new UserInfo();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.eSearchCondition = (int)istream.readInt32(1, false, this.eSearchCondition);
        this.sSearchStr = (String)istream.readString(2, false, this.sSearchStr);
    }

}

