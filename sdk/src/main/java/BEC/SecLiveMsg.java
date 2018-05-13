package BEC;

public final class SecLiveMsg extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sId = "";

    public int iTime = 0;

    public String sMsg = "";

    public int eSecLiveMsgType = 0;

    public String getSId()
    {
        return sId;
    }

    public void  setSId(String sId)
    {
        this.sId = sId;
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

    public int getESecLiveMsgType()
    {
        return eSecLiveMsgType;
    }

    public void  setESecLiveMsgType(int eSecLiveMsgType)
    {
        this.eSecLiveMsgType = eSecLiveMsgType;
    }

    public SecLiveMsg()
    {
    }

    public SecLiveMsg(String sId, int iTime, String sMsg, int eSecLiveMsgType)
    {
        this.sId = sId;
        this.iTime = iTime;
        this.sMsg = sMsg;
        this.eSecLiveMsgType = eSecLiveMsgType;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sId) {
            ostream.writeString(0, sId);
        }
        ostream.writeInt32(1, iTime);
        if (null != sMsg) {
            ostream.writeString(2, sMsg);
        }
        ostream.writeInt32(3, eSecLiveMsgType);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sId = (String)istream.readString(0, false, this.sId);
        this.iTime = (int)istream.readInt32(1, false, this.iTime);
        this.sMsg = (String)istream.readString(2, false, this.sMsg);
        this.eSecLiveMsgType = (int)istream.readInt32(3, false, this.eSecLiveMsgType);
    }

}

