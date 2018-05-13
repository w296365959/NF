package BEC;

public final class PortfolioLiveMsgDesc extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sDtSecCode = "";

    public String sSecName = "";

    public int iTime = 0;

    public String sMsg = "";

    public int iMsgType = 0;

    public String getSDtSecCode()
    {
        return sDtSecCode;
    }

    public void  setSDtSecCode(String sDtSecCode)
    {
        this.sDtSecCode = sDtSecCode;
    }

    public String getSSecName()
    {
        return sSecName;
    }

    public void  setSSecName(String sSecName)
    {
        this.sSecName = sSecName;
    }

    public int getITime()
    {
        return iTime;
    }

    public void  setITime(int iTime)
    {
        this.iTime = iTime;
    }

    public String getSMsg()
    {
        return sMsg;
    }

    public void  setSMsg(String sMsg)
    {
        this.sMsg = sMsg;
    }

    public int getIMsgType()
    {
        return iMsgType;
    }

    public void  setIMsgType(int iMsgType)
    {
        this.iMsgType = iMsgType;
    }

    public PortfolioLiveMsgDesc()
    {
    }

    public PortfolioLiveMsgDesc(String sDtSecCode, String sSecName, int iTime, String sMsg, int iMsgType)
    {
        this.sDtSecCode = sDtSecCode;
        this.sSecName = sSecName;
        this.iTime = iTime;
        this.sMsg = sMsg;
        this.iMsgType = iMsgType;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sDtSecCode) {
            ostream.writeString(0, sDtSecCode);
        }
        if (null != sSecName) {
            ostream.writeString(1, sSecName);
        }
        ostream.writeInt32(2, iTime);
        if (null != sMsg) {
            ostream.writeString(3, sMsg);
        }
        ostream.writeInt32(4, iMsgType);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sDtSecCode = (String)istream.readString(0, false, this.sDtSecCode);
        this.sSecName = (String)istream.readString(1, false, this.sSecName);
        this.iTime = (int)istream.readInt32(2, false, this.iTime);
        this.sMsg = (String)istream.readString(3, false, this.sMsg);
        this.iMsgType = (int)istream.readInt32(4, false, this.iMsgType);
    }

}

