package BEC;

public final class InteractionNotify extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sMsgId = "";

    public String sMsg = "";

    public long iAccountId = 0;

    public String sIcon = "";

    public String sUserName = "";

    public String sFeedId = "";

    public boolean bVerify = true;

    public int iFeedType = 0;

    public String getSMsgId()
    {
        return sMsgId;
    }

    public void  setSMsgId(String sMsgId)
    {
        this.sMsgId = sMsgId;
    }

    public String getSMsg()
    {
        return sMsg;
    }

    public void  setSMsg(String sMsg)
    {
        this.sMsg = sMsg;
    }

    public long getIAccountId()
    {
        return iAccountId;
    }

    public void  setIAccountId(long iAccountId)
    {
        this.iAccountId = iAccountId;
    }

    public String getSIcon()
    {
        return sIcon;
    }

    public void  setSIcon(String sIcon)
    {
        this.sIcon = sIcon;
    }

    public String getSUserName()
    {
        return sUserName;
    }

    public void  setSUserName(String sUserName)
    {
        this.sUserName = sUserName;
    }

    public String getSFeedId()
    {
        return sFeedId;
    }

    public void  setSFeedId(String sFeedId)
    {
        this.sFeedId = sFeedId;
    }

    public boolean getBVerify()
    {
        return bVerify;
    }

    public void  setBVerify(boolean bVerify)
    {
        this.bVerify = bVerify;
    }

    public int getIFeedType()
    {
        return iFeedType;
    }

    public void  setIFeedType(int iFeedType)
    {
        this.iFeedType = iFeedType;
    }

    public InteractionNotify()
    {
    }

    public InteractionNotify(String sMsgId, String sMsg, long iAccountId, String sIcon, String sUserName, String sFeedId, boolean bVerify, int iFeedType)
    {
        this.sMsgId = sMsgId;
        this.sMsg = sMsg;
        this.iAccountId = iAccountId;
        this.sIcon = sIcon;
        this.sUserName = sUserName;
        this.sFeedId = sFeedId;
        this.bVerify = bVerify;
        this.iFeedType = iFeedType;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sMsgId) {
            ostream.writeString(0, sMsgId);
        }
        if (null != sMsg) {
            ostream.writeString(1, sMsg);
        }
        ostream.writeUInt32(2, iAccountId);
        if (null != sIcon) {
            ostream.writeString(3, sIcon);
        }
        if (null != sUserName) {
            ostream.writeString(4, sUserName);
        }
        if (null != sFeedId) {
            ostream.writeString(5, sFeedId);
        }
        ostream.writeBoolean(6, bVerify);
        ostream.writeInt32(7, iFeedType);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sMsgId = (String)istream.readString(0, false, this.sMsgId);
        this.sMsg = (String)istream.readString(1, false, this.sMsg);
        this.iAccountId = (long)istream.readUInt32(2, false, this.iAccountId);
        this.sIcon = (String)istream.readString(3, false, this.sIcon);
        this.sUserName = (String)istream.readString(4, false, this.sUserName);
        this.sFeedId = (String)istream.readString(5, false, this.sFeedId);
        this.bVerify = (boolean)istream.readBoolean(6, false, this.bVerify);
        this.iFeedType = (int)istream.readInt32(7, false, this.iFeedType);
    }

}

