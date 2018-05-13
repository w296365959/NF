package BEC;

public final class AdditionLiftedListReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iStart = 0;

    public BEC.UserInfo stUserInfo = null;

    public java.util.ArrayList<BEC.SearchCondition> vSearchCondition = null;

    public int eSortType = BEC.E_LIFTED_LIST_SORT_TYPE.LLST_DATE_DESC;

    public int getIStart()
    {
        return iStart;
    }

    public void  setIStart(int iStart)
    {
        this.iStart = iStart;
    }

    public BEC.UserInfo getStUserInfo()
    {
        return stUserInfo;
    }

    public void  setStUserInfo(BEC.UserInfo stUserInfo)
    {
        this.stUserInfo = stUserInfo;
    }

    public java.util.ArrayList<BEC.SearchCondition> getVSearchCondition()
    {
        return vSearchCondition;
    }

    public void  setVSearchCondition(java.util.ArrayList<BEC.SearchCondition> vSearchCondition)
    {
        this.vSearchCondition = vSearchCondition;
    }

    public int getESortType()
    {
        return eSortType;
    }

    public void  setESortType(int eSortType)
    {
        this.eSortType = eSortType;
    }

    public AdditionLiftedListReq()
    {
    }

    public AdditionLiftedListReq(int iStart, BEC.UserInfo stUserInfo, java.util.ArrayList<BEC.SearchCondition> vSearchCondition, int eSortType)
    {
        this.iStart = iStart;
        this.stUserInfo = stUserInfo;
        this.vSearchCondition = vSearchCondition;
        this.eSortType = eSortType;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iStart);
        if (null != stUserInfo) {
            ostream.writeMessage(1, stUserInfo);
        }
        if (null != vSearchCondition) {
            ostream.writeList(2, vSearchCondition);
        }
        ostream.writeInt32(3, eSortType);
    }

    static BEC.UserInfo VAR_TYPE_4_STUSERINFO = new BEC.UserInfo();

    static java.util.ArrayList<BEC.SearchCondition> VAR_TYPE_4_VSEARCHCONDITION = new java.util.ArrayList<BEC.SearchCondition>();
    static {
        VAR_TYPE_4_VSEARCHCONDITION.add(new BEC.SearchCondition());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iStart = (int)istream.readInt32(0, false, this.iStart);
        this.stUserInfo = (BEC.UserInfo)istream.readMessage(1, false, VAR_TYPE_4_STUSERINFO);
        this.vSearchCondition = (java.util.ArrayList<BEC.SearchCondition>)istream.readList(2, false, VAR_TYPE_4_VSEARCHCONDITION);
        this.eSortType = (int)istream.readInt32(3, false, this.eSortType);
    }

}

