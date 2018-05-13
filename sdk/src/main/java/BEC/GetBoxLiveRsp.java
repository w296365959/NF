package BEC;

public final class GetBoxLiveRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sMsg = "";

    public String sSkipUrl = "";

    public int iMsgType = 0;

    public int iTime = 0;

    public int iLiveType = 0;

    public int iLiveMsgStatus = 0;

    public String getSMsg()
    {
        return sMsg;
    }

    public void  setSMsg(String sMsg)
    {
        this.sMsg = sMsg;
    }

    public String getSSkipUrl()
    {
        return sSkipUrl;
    }

    public void  setSSkipUrl(String sSkipUrl)
    {
        this.sSkipUrl = sSkipUrl;
    }

    public int getIMsgType()
    {
        return iMsgType;
    }

    public void  setIMsgType(int iMsgType)
    {
        this.iMsgType = iMsgType;
    }

    public int getITime()
    {
        return iTime;
    }

    public void  setITime(int iTime)
    {
        this.iTime = iTime;
    }

    public int getILiveType()
    {
        return iLiveType;
    }

    public void  setILiveType(int iLiveType)
    {
        this.iLiveType = iLiveType;
    }

    public int getILiveMsgStatus()
    {
        return iLiveMsgStatus;
    }

    public void  setILiveMsgStatus(int iLiveMsgStatus)
    {
        this.iLiveMsgStatus = iLiveMsgStatus;
    }

    public GetBoxLiveRsp()
    {
    }

    public GetBoxLiveRsp(String sMsg, String sSkipUrl, int iMsgType, int iTime, int iLiveType, int iLiveMsgStatus)
    {
        this.sMsg = sMsg;
        this.sSkipUrl = sSkipUrl;
        this.iMsgType = iMsgType;
        this.iTime = iTime;
        this.iLiveType = iLiveType;
        this.iLiveMsgStatus = iLiveMsgStatus;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sMsg) {
            ostream.writeString(0, sMsg);
        }
        if (null != sSkipUrl) {
            ostream.writeString(1, sSkipUrl);
        }
        ostream.writeInt32(2, iMsgType);
        ostream.writeInt32(3, iTime);
        ostream.writeInt32(4, iLiveType);
        ostream.writeInt32(5, iLiveMsgStatus);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sMsg = (String)istream.readString(0, false, this.sMsg);
        this.sSkipUrl = (String)istream.readString(1, false, this.sSkipUrl);
        this.iMsgType = (int)istream.readInt32(2, false, this.iMsgType);
        this.iTime = (int)istream.readInt32(3, false, this.iTime);
        this.iLiveType = (int)istream.readInt32(4, false, this.iLiveType);
        this.iLiveMsgStatus = (int)istream.readInt32(5, false, this.iLiveMsgStatus);
    }

}

