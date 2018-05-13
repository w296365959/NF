package BEC;

public final class ShapeChooseStockReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.UserInfo stUserInfo = null;

    public String sDtSecCode = "";

    public java.util.ArrayList<Float> vtClose = null;

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

    public java.util.ArrayList<Float> getVtClose()
    {
        return vtClose;
    }

    public void  setVtClose(java.util.ArrayList<Float> vtClose)
    {
        this.vtClose = vtClose;
    }

    public ShapeChooseStockReq()
    {
    }

    public ShapeChooseStockReq(BEC.UserInfo stUserInfo, String sDtSecCode, java.util.ArrayList<Float> vtClose)
    {
        this.stUserInfo = stUserInfo;
        this.sDtSecCode = sDtSecCode;
        this.vtClose = vtClose;
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
        if (null != vtClose) {
            ostream.writeList(2, vtClose);
        }
    }

    static BEC.UserInfo VAR_TYPE_4_STUSERINFO = new BEC.UserInfo();

    static java.util.ArrayList<Float> VAR_TYPE_4_VTCLOSE = new java.util.ArrayList<Float>();
    static {
        VAR_TYPE_4_VTCLOSE.add(0.0f);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (BEC.UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.sDtSecCode = (String)istream.readString(1, false, this.sDtSecCode);
        this.vtClose = (java.util.ArrayList<Float>)istream.readList(2, false, VAR_TYPE_4_VTCLOSE);
    }

}

