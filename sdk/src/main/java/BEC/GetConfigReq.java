package BEC;

public final class GetConfigReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.UserInfo stUserInfo = null;

    public java.util.ArrayList<Integer> vType = null;

    public BEC.UserInfo getStUserInfo()
    {
        return stUserInfo;
    }

    public void  setStUserInfo(BEC.UserInfo stUserInfo)
    {
        this.stUserInfo = stUserInfo;
    }

    public java.util.ArrayList<Integer> getVType()
    {
        return vType;
    }

    public void  setVType(java.util.ArrayList<Integer> vType)
    {
        this.vType = vType;
    }

    public GetConfigReq()
    {
    }

    public GetConfigReq(BEC.UserInfo stUserInfo, java.util.ArrayList<Integer> vType)
    {
        this.stUserInfo = stUserInfo;
        this.vType = vType;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stUserInfo) {
            ostream.writeMessage(0, stUserInfo);
        }
        if (null != vType) {
            ostream.writeList(1, vType);
        }
    }

    static BEC.UserInfo VAR_TYPE_4_STUSERINFO = new BEC.UserInfo();

    static java.util.ArrayList<Integer> VAR_TYPE_4_VTYPE = new java.util.ArrayList<Integer>();
    static {
        VAR_TYPE_4_VTYPE.add(0);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (BEC.UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.vType = (java.util.ArrayList<Integer>)istream.readList(1, false, VAR_TYPE_4_VTYPE);
    }

}

