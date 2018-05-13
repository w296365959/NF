package BEC;

public final class IndustryPlateDtMarginReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.UserInfo stUserInfo = null;

    public String sIndustryPlate = "";

    public int iStartPos = 0;

    public int iWantNum = 0;

    public BEC.UserInfo getStUserInfo()
    {
        return stUserInfo;
    }

    public void  setStUserInfo(BEC.UserInfo stUserInfo)
    {
        this.stUserInfo = stUserInfo;
    }

    public String getSIndustryPlate()
    {
        return sIndustryPlate;
    }

    public void  setSIndustryPlate(String sIndustryPlate)
    {
        this.sIndustryPlate = sIndustryPlate;
    }

    public int getIStartPos()
    {
        return iStartPos;
    }

    public void  setIStartPos(int iStartPos)
    {
        this.iStartPos = iStartPos;
    }

    public int getIWantNum()
    {
        return iWantNum;
    }

    public void  setIWantNum(int iWantNum)
    {
        this.iWantNum = iWantNum;
    }

    public IndustryPlateDtMarginReq()
    {
    }

    public IndustryPlateDtMarginReq(BEC.UserInfo stUserInfo, String sIndustryPlate, int iStartPos, int iWantNum)
    {
        this.stUserInfo = stUserInfo;
        this.sIndustryPlate = sIndustryPlate;
        this.iStartPos = iStartPos;
        this.iWantNum = iWantNum;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stUserInfo) {
            ostream.writeMessage(0, stUserInfo);
        }
        if (null != sIndustryPlate) {
            ostream.writeString(1, sIndustryPlate);
        }
        ostream.writeInt32(2, iStartPos);
        ostream.writeInt32(3, iWantNum);
    }

    static BEC.UserInfo VAR_TYPE_4_STUSERINFO = new BEC.UserInfo();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (BEC.UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.sIndustryPlate = (String)istream.readString(1, false, this.sIndustryPlate);
        this.iStartPos = (int)istream.readInt32(2, false, this.iStartPos);
        this.iWantNum = (int)istream.readInt32(3, false, this.iWantNum);
    }

}

