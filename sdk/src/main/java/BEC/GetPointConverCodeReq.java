package BEC;

public final class GetPointConverCodeReq extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public UserInfo stUserInfo = null;

    public int iGetNum = 10;

    public boolean bGetNew = false;

    public UserInfo getStUserInfo()
    {
        return stUserInfo;
    }

    public void  setStUserInfo(UserInfo stUserInfo)
    {
        this.stUserInfo = stUserInfo;
    }

    public int getIGetNum()
    {
        return iGetNum;
    }

    public void  setIGetNum(int iGetNum)
    {
        this.iGetNum = iGetNum;
    }

    public boolean getBGetNew()
    {
        return bGetNew;
    }

    public void  setBGetNew(boolean bGetNew)
    {
        this.bGetNew = bGetNew;
    }

    public GetPointConverCodeReq()
    {
    }

    public GetPointConverCodeReq(UserInfo stUserInfo, int iGetNum, boolean bGetNew)
    {
        this.stUserInfo = stUserInfo;
        this.iGetNum = iGetNum;
        this.bGetNew = bGetNew;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stUserInfo) {
            ostream.writeMessage(0, stUserInfo);
        }
        ostream.writeInt32(1, iGetNum);
        ostream.writeBoolean(2, bGetNew);
    }

    static UserInfo VAR_TYPE_4_STUSERINFO = new UserInfo();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.iGetNum = (int)istream.readInt32(1, false, this.iGetNum);
        this.bGetNew = (boolean)istream.readBoolean(2, false, this.bGetNew);
    }

}

