package BEC;

public final class GetIndexStocksReq extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public UserInfo stUserInfo = null;

    public String sIndexDtCode = "";

    public int iReqType = 0;

    public int iNum = 0;

    public UserInfo getStUserInfo()
    {
        return stUserInfo;
    }

    public void  setStUserInfo(UserInfo stUserInfo)
    {
        this.stUserInfo = stUserInfo;
    }

    public String getSIndexDtCode()
    {
        return sIndexDtCode;
    }

    public void  setSIndexDtCode(String sIndexDtCode)
    {
        this.sIndexDtCode = sIndexDtCode;
    }

    public int getIReqType()
    {
        return iReqType;
    }

    public void  setIReqType(int iReqType)
    {
        this.iReqType = iReqType;
    }

    public int getINum()
    {
        return iNum;
    }

    public void  setINum(int iNum)
    {
        this.iNum = iNum;
    }

    public GetIndexStocksReq()
    {
    }

    public GetIndexStocksReq(UserInfo stUserInfo, String sIndexDtCode, int iReqType, int iNum)
    {
        this.stUserInfo = stUserInfo;
        this.sIndexDtCode = sIndexDtCode;
        this.iReqType = iReqType;
        this.iNum = iNum;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stUserInfo) {
            ostream.writeMessage(0, stUserInfo);
        }
        if (null != sIndexDtCode) {
            ostream.writeString(1, sIndexDtCode);
        }
        ostream.writeInt32(2, iReqType);
        ostream.writeInt32(3, iNum);
    }

    static UserInfo VAR_TYPE_4_STUSERINFO = new UserInfo();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.sIndexDtCode = (String)istream.readString(1, false, this.sIndexDtCode);
        this.iReqType = (int)istream.readInt32(2, false, this.iReqType);
        this.iNum = (int)istream.readInt32(3, false, this.iNum);
    }

}

