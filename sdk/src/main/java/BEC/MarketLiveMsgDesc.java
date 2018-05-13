package BEC;

public final class MarketLiveMsgDesc extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iTime = 0;

    public int iType = 0;

    public String sMsg = "";

    public int iMsgType = 0;

    public String sSimpleMsg = "";

    public int getITime()
    {
        return iTime;
    }

    public void  setITime(int iTime)
    {
        this.iTime = iTime;
    }

    public int getIType()
    {
        return iType;
    }

    public void  setIType(int iType)
    {
        this.iType = iType;
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

    public String getSSimpleMsg()
    {
        return sSimpleMsg;
    }

    public void  setSSimpleMsg(String sSimpleMsg)
    {
        this.sSimpleMsg = sSimpleMsg;
    }

    public MarketLiveMsgDesc()
    {
    }

    public MarketLiveMsgDesc(int iTime, int iType, String sMsg, int iMsgType, String sSimpleMsg)
    {
        this.iTime = iTime;
        this.iType = iType;
        this.sMsg = sMsg;
        this.iMsgType = iMsgType;
        this.sSimpleMsg = sSimpleMsg;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iTime);
        ostream.writeInt32(1, iType);
        if (null != sMsg) {
            ostream.writeString(2, sMsg);
        }
        ostream.writeInt32(3, iMsgType);
        if (null != sSimpleMsg) {
            ostream.writeString(4, sSimpleMsg);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iTime = (int)istream.readInt32(0, false, this.iTime);
        this.iType = (int)istream.readInt32(1, false, this.iType);
        this.sMsg = (String)istream.readString(2, false, this.sMsg);
        this.iMsgType = (int)istream.readInt32(3, false, this.iMsgType);
        this.sSimpleMsg = (String)istream.readString(4, false, this.sSimpleMsg);
    }

}

