package BEC;

public final class SecNotify extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public SecInfo stSecInfo = null;

    public String sNotifyMsg = "";

    public SecInfo getStSecInfo()
    {
        return stSecInfo;
    }

    public void  setStSecInfo(SecInfo stSecInfo)
    {
        this.stSecInfo = stSecInfo;
    }

    public String getSNotifyMsg()
    {
        return sNotifyMsg;
    }

    public void  setSNotifyMsg(String sNotifyMsg)
    {
        this.sNotifyMsg = sNotifyMsg;
    }

    public SecNotify()
    {
    }

    public SecNotify(SecInfo stSecInfo, String sNotifyMsg)
    {
        this.stSecInfo = stSecInfo;
        this.sNotifyMsg = sNotifyMsg;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stSecInfo) {
            ostream.writeMessage(0, stSecInfo);
        }
        if (null != sNotifyMsg) {
            ostream.writeString(1, sNotifyMsg);
        }
    }

    static SecInfo VAR_TYPE_4_STSECINFO = new SecInfo();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stSecInfo = (SecInfo)istream.readMessage(0, false, VAR_TYPE_4_STSECINFO);
        this.sNotifyMsg = (String)istream.readString(1, false, this.sNotifyMsg);
    }

}

