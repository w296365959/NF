package BEC;

public final class SecStatusInfo extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iStatus = 0;

    public String sDesc = "";

    public String sKey = "";

    public int getIStatus()
    {
        return iStatus;
    }

    public void  setIStatus(int iStatus)
    {
        this.iStatus = iStatus;
    }

    public String getSDesc()
    {
        return sDesc;
    }

    public void  setSDesc(String sDesc)
    {
        this.sDesc = sDesc;
    }

    public String getSKey()
    {
        return sKey;
    }

    public void  setSKey(String sKey)
    {
        this.sKey = sKey;
    }

    public SecStatusInfo()
    {
    }

    public SecStatusInfo(int iStatus, String sDesc, String sKey)
    {
        this.iStatus = iStatus;
        this.sDesc = sDesc;
        this.sKey = sKey;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iStatus);
        if (null != sDesc) {
            ostream.writeString(1, sDesc);
        }
        if (null != sKey) {
            ostream.writeString(2, sKey);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iStatus = (int)istream.readInt32(0, false, this.iStatus);
        this.sDesc = (String)istream.readString(1, false, this.sDesc);
        this.sKey = (String)istream.readString(2, false, this.sKey);
    }

}

