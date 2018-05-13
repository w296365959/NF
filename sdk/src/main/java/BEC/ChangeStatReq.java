package BEC;

public final class ChangeStatReq extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public UserInfo stUserInfo = null;

    public long iStart = 0;

    public long iWantNum = 0;

    public boolean isNeedAll = false;

    public UserInfo getStUserInfo()
    {
        return stUserInfo;
    }

    public void  setStUserInfo(UserInfo stUserInfo)
    {
        this.stUserInfo = stUserInfo;
    }

    public long getIStart()
    {
        return iStart;
    }

    public void  setIStart(long iStart)
    {
        this.iStart = iStart;
    }

    public long getIWantNum()
    {
        return iWantNum;
    }

    public void  setIWantNum(long iWantNum)
    {
        this.iWantNum = iWantNum;
    }

    public boolean getIsNeedAll()
    {
        return isNeedAll;
    }

    public void  setIsNeedAll(boolean isNeedAll)
    {
        this.isNeedAll = isNeedAll;
    }

    public ChangeStatReq()
    {
    }

    public ChangeStatReq(UserInfo stUserInfo, long iStart, long iWantNum, boolean isNeedAll)
    {
        this.stUserInfo = stUserInfo;
        this.iStart = iStart;
        this.iWantNum = iWantNum;
        this.isNeedAll = isNeedAll;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stUserInfo) {
            ostream.writeMessage(0, stUserInfo);
        }
        ostream.writeUInt32(1, iStart);
        ostream.writeUInt32(2, iWantNum);
        ostream.writeBoolean(3, isNeedAll);
    }

    static UserInfo VAR_TYPE_4_STUSERINFO = new UserInfo();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.iStart = (long)istream.readUInt32(1, false, this.iStart);
        this.iWantNum = (long)istream.readUInt32(2, false, this.iWantNum);
        this.isNeedAll = (boolean)istream.readBoolean(3, false, this.isNeedAll);
    }

}

