package BEC;

public final class AHExtendRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.AHExtendInfo stAHExtendInfo = null;

    public BEC.AHExtendInfo getStAHExtendInfo()
    {
        return stAHExtendInfo;
    }

    public void  setStAHExtendInfo(BEC.AHExtendInfo stAHExtendInfo)
    {
        this.stAHExtendInfo = stAHExtendInfo;
    }

    public AHExtendRsp()
    {
    }

    public AHExtendRsp(BEC.AHExtendInfo stAHExtendInfo)
    {
        this.stAHExtendInfo = stAHExtendInfo;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stAHExtendInfo) {
            ostream.writeMessage(0, stAHExtendInfo);
        }
    }

    static BEC.AHExtendInfo VAR_TYPE_4_STAHEXTENDINFO = new BEC.AHExtendInfo();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stAHExtendInfo = (BEC.AHExtendInfo)istream.readMessage(0, false, VAR_TYPE_4_STAHEXTENDINFO);
    }

}

