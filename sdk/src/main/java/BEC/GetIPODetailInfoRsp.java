package BEC;

public final class GetIPODetailInfoRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public SecIPODetailInfo stInfo = null;

    public SecIPODetailInfo getStInfo()
    {
        return stInfo;
    }

    public void  setStInfo(SecIPODetailInfo stInfo)
    {
        this.stInfo = stInfo;
    }

    public GetIPODetailInfoRsp()
    {
    }

    public GetIPODetailInfoRsp(SecIPODetailInfo stInfo)
    {
        this.stInfo = stInfo;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stInfo) {
            ostream.writeMessage(0, stInfo);
        }
    }

    static SecIPODetailInfo VAR_TYPE_4_STINFO = new SecIPODetailInfo();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stInfo = (SecIPODetailInfo)istream.readMessage(0, false, VAR_TYPE_4_STINFO);
    }

}

