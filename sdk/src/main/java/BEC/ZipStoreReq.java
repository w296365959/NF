package BEC;

public final class ZipStoreReq extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public UserInfo stUserInfo = null;

    public java.util.ArrayList<ZipRequest> vZipRequest = null;

    public UserInfo getStUserInfo()
    {
        return stUserInfo;
    }

    public void  setStUserInfo(UserInfo stUserInfo)
    {
        this.stUserInfo = stUserInfo;
    }

    public java.util.ArrayList<ZipRequest> getVZipRequest()
    {
        return vZipRequest;
    }

    public void  setVZipRequest(java.util.ArrayList<ZipRequest> vZipRequest)
    {
        this.vZipRequest = vZipRequest;
    }

    public ZipStoreReq()
    {
    }

    public ZipStoreReq(UserInfo stUserInfo, java.util.ArrayList<ZipRequest> vZipRequest)
    {
        this.stUserInfo = stUserInfo;
        this.vZipRequest = vZipRequest;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stUserInfo) {
            ostream.writeMessage(0, stUserInfo);
        }
        if (null != vZipRequest) {
            ostream.writeList(1, vZipRequest);
        }
    }

    static UserInfo VAR_TYPE_4_STUSERINFO = new UserInfo();

    static java.util.ArrayList<ZipRequest> VAR_TYPE_4_VZIPREQUEST = new java.util.ArrayList<ZipRequest>();
    static {
        VAR_TYPE_4_VZIPREQUEST.add(new ZipRequest());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.vZipRequest = (java.util.ArrayList<ZipRequest>)istream.readList(1, false, VAR_TYPE_4_VZIPREQUEST);
    }

}

