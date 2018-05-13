package BEC;

public final class DtActivityInfo extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iVersion = 0;

    public String sMsg = "";

    public int iForceRedDot = 0;

    public int getIVersion()
    {
        return iVersion;
    }

    public void  setIVersion(int iVersion)
    {
        this.iVersion = iVersion;
    }

    public String getSMsg()
    {
        return sMsg;
    }

    public void  setSMsg(String sMsg)
    {
        this.sMsg = sMsg;
    }

    public int getIForceRedDot()
    {
        return iForceRedDot;
    }

    public void  setIForceRedDot(int iForceRedDot)
    {
        this.iForceRedDot = iForceRedDot;
    }

    public DtActivityInfo()
    {
    }

    public DtActivityInfo(int iVersion, String sMsg, int iForceRedDot)
    {
        this.iVersion = iVersion;
        this.sMsg = sMsg;
        this.iForceRedDot = iForceRedDot;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iVersion);
        if (null != sMsg) {
            ostream.writeString(1, sMsg);
        }
        ostream.writeInt32(2, iForceRedDot);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iVersion = (int)istream.readInt32(0, false, this.iVersion);
        this.sMsg = (String)istream.readString(1, false, this.sMsg);
        this.iForceRedDot = (int)istream.readInt32(2, false, this.iForceRedDot);
    }

}

