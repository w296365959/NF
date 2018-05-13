package BEC;

public final class CommonSearchReq extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public UserInfo stUserInfo = null;

    public String sUserInputStr = "";

    public int eSearchMatchType = E_SEARCH_MATCH_TYPE.E_SMT_FUZZY_MATCH;

    public int eSearchResultType = E_SEARCH_RESULT_TYPE.E_SEARCH_RESULT_ALL;

    public int eSearchTargetType = BEC.E_SEARCH_TARGET_TYPE.E_SEARCH_TARGET_SEC;

    public UserInfo getStUserInfo()
    {
        return stUserInfo;
    }

    public void  setStUserInfo(UserInfo stUserInfo)
    {
        this.stUserInfo = stUserInfo;
    }

    public String getSUserInputStr()
    {
        return sUserInputStr;
    }

    public void  setSUserInputStr(String sUserInputStr)
    {
        this.sUserInputStr = sUserInputStr;
    }

    public int getESearchMatchType()
    {
        return eSearchMatchType;
    }

    public void  setESearchMatchType(int eSearchMatchType)
    {
        this.eSearchMatchType = eSearchMatchType;
    }

    public int getESearchResultType()
    {
        return eSearchResultType;
    }

    public void  setESearchResultType(int eSearchResultType)
    {
        this.eSearchResultType = eSearchResultType;
    }

    public int getESearchTargetType()
    {
        return eSearchTargetType;
    }

    public void  setESearchTargetType(int eSearchTargetType)
    {
        this.eSearchTargetType = eSearchTargetType;
    }

    public CommonSearchReq()
    {
    }

    public CommonSearchReq(UserInfo stUserInfo, String sUserInputStr, int eSearchMatchType, int eSearchResultType, int eSearchTargetType)
    {
        this.stUserInfo = stUserInfo;
        this.sUserInputStr = sUserInputStr;
        this.eSearchMatchType = eSearchMatchType;
        this.eSearchResultType = eSearchResultType;
        this.eSearchTargetType = eSearchTargetType;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stUserInfo) {
            ostream.writeMessage(0, stUserInfo);
        }
        ostream.writeString(1, sUserInputStr);
        ostream.writeInt32(2, eSearchMatchType);
        ostream.writeInt32(3, eSearchResultType);
        ostream.writeInt32(4, eSearchTargetType);
    }

    static UserInfo VAR_TYPE_4_STUSERINFO = new UserInfo();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.sUserInputStr = (String)istream.readString(1, true, this.sUserInputStr);
        this.eSearchMatchType = (int)istream.readInt32(2, false, this.eSearchMatchType);
        this.eSearchResultType = (int)istream.readInt32(3, false, this.eSearchResultType);
        this.eSearchTargetType = (int)istream.readInt32(4, false, this.eSearchTargetType);
    }

}

