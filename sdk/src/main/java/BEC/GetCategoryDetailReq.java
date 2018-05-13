package BEC;

public final class GetCategoryDetailReq extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public UserInfo stUserInfo = null;

    public String sCategoryId = "";

    public UserInfo getStUserInfo()
    {
        return stUserInfo;
    }

    public void  setStUserInfo(UserInfo stUserInfo)
    {
        this.stUserInfo = stUserInfo;
    }

    public String getSCategoryId()
    {
        return sCategoryId;
    }

    public void  setSCategoryId(String sCategoryId)
    {
        this.sCategoryId = sCategoryId;
    }

    public GetCategoryDetailReq()
    {
    }

    public GetCategoryDetailReq(UserInfo stUserInfo, String sCategoryId)
    {
        this.stUserInfo = stUserInfo;
        this.sCategoryId = sCategoryId;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stUserInfo) {
            ostream.writeMessage(0, stUserInfo);
        }
        if (null != sCategoryId) {
            ostream.writeString(1, sCategoryId);
        }
    }

    static UserInfo VAR_TYPE_4_STUSERINFO = new UserInfo();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.sCategoryId = (String)istream.readString(1, false, this.sCategoryId);
    }

}

