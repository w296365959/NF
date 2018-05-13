package BEC;

public final class LargeUnitReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.UserInfo stUserInfo = null;

    public String sDtSecCode = "";

    public int iNum = 0;

    public int iTimeStamp = 0;

    public int iSort = 0;

    public int iDirection = 0;

    public BEC.UserInfo getStUserInfo()
    {
        return stUserInfo;
    }

    public void  setStUserInfo(BEC.UserInfo stUserInfo)
    {
        this.stUserInfo = stUserInfo;
    }

    public String getSDtSecCode()
    {
        return sDtSecCode;
    }

    public void  setSDtSecCode(String sDtSecCode)
    {
        this.sDtSecCode = sDtSecCode;
    }

    public int getINum()
    {
        return iNum;
    }

    public void  setINum(int iNum)
    {
        this.iNum = iNum;
    }

    public int getITimeStamp()
    {
        return iTimeStamp;
    }

    public void  setITimeStamp(int iTimeStamp)
    {
        this.iTimeStamp = iTimeStamp;
    }

    public int getISort()
    {
        return iSort;
    }

    public void  setISort(int iSort)
    {
        this.iSort = iSort;
    }

    public int getIDirection()
    {
        return iDirection;
    }

    public void  setIDirection(int iDirection)
    {
        this.iDirection = iDirection;
    }

    public LargeUnitReq()
    {
    }

    public LargeUnitReq(BEC.UserInfo stUserInfo, String sDtSecCode, int iNum, int iTimeStamp, int iSort, int iDirection)
    {
        this.stUserInfo = stUserInfo;
        this.sDtSecCode = sDtSecCode;
        this.iNum = iNum;
        this.iTimeStamp = iTimeStamp;
        this.iSort = iSort;
        this.iDirection = iDirection;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stUserInfo) {
            ostream.writeMessage(0, stUserInfo);
        }
        if (null != sDtSecCode) {
            ostream.writeString(1, sDtSecCode);
        }
        ostream.writeInt32(2, iNum);
        ostream.writeInt32(3, iTimeStamp);
        ostream.writeInt32(4, iSort);
        ostream.writeInt32(5, iDirection);
    }

    static BEC.UserInfo VAR_TYPE_4_STUSERINFO = new BEC.UserInfo();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (BEC.UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.sDtSecCode = (String)istream.readString(1, false, this.sDtSecCode);
        this.iNum = (int)istream.readInt32(2, false, this.iNum);
        this.iTimeStamp = (int)istream.readInt32(3, false, this.iTimeStamp);
        this.iSort = (int)istream.readInt32(4, false, this.iSort);
        this.iDirection = (int)istream.readInt32(5, false, this.iDirection);
    }

}

