package BEC;

public final class AdditionListReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sStartId = "";

    public String sEndId = "";

    public BEC.UserInfo stUserInfo = null;

    public java.util.ArrayList<BEC.SearchCondition> vSearchCondition = null;

    public int eSortType = BEC.E_ADDITIN_LIST_SORT_TYPE.ALST_PUB_DATE;

    public int iStart = 0;

    public boolean bCountOnly = false;

    public String getSStartId()
    {
        return sStartId;
    }

    public void  setSStartId(String sStartId)
    {
        this.sStartId = sStartId;
    }

    public String getSEndId()
    {
        return sEndId;
    }

    public void  setSEndId(String sEndId)
    {
        this.sEndId = sEndId;
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

    public int getIStart()
    {
        return iStart;
    }

    public void  setIStart(int iStart)
    {
        this.iStart = iStart;
    }

    public boolean getBCountOnly()
    {
        return bCountOnly;
    }

    public void  setBCountOnly(boolean bCountOnly)
    {
        this.bCountOnly = bCountOnly;
    }

    public AdditionListReq()
    {
    }

    public AdditionListReq(String sStartId, String sEndId, BEC.UserInfo stUserInfo, java.util.ArrayList<BEC.SearchCondition> vSearchCondition, int eSortType, int iStart, boolean bCountOnly)
    {
        this.sStartId = sStartId;
        this.sEndId = sEndId;
        this.stUserInfo = stUserInfo;
        this.vSearchCondition = vSearchCondition;
        this.eSortType = eSortType;
        this.iStart = iStart;
        this.bCountOnly = bCountOnly;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sStartId) {
            ostream.writeString(0, sStartId);
        }
        if (null != sEndId) {
            ostream.writeString(1, sEndId);
        }
        if (null != stUserInfo) {
            ostream.writeMessage(2, stUserInfo);
        }
        if (null != vSearchCondition) {
            ostream.writeList(3, vSearchCondition);
        }
        ostream.writeInt32(4, eSortType);
        ostream.writeInt32(6, iStart);
        ostream.writeBoolean(7, bCountOnly);
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

        this.sStartId = (String)istream.readString(0, false, this.sStartId);
        this.sEndId = (String)istream.readString(1, false, this.sEndId);
        this.stUserInfo = (BEC.UserInfo)istream.readMessage(2, false, VAR_TYPE_4_STUSERINFO);
        this.vSearchCondition = (java.util.ArrayList<BEC.SearchCondition>)istream.readList(3, false, VAR_TYPE_4_VSEARCHCONDITION);
        this.eSortType = (int)istream.readInt32(4, false, this.eSortType);
        this.iStart = (int)istream.readInt32(6, false, this.iStart);
        this.bCountOnly = (boolean)istream.readBoolean(7, false, this.bCountOnly);
    }

}

