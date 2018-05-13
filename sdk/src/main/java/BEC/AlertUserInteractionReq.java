package BEC;

public final class AlertUserInteractionReq extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public long iAccountId = 0;

    public long iFromAccountId = 0;

    public String sFromUserIcon = "";

    public String sFromUserName = "";

    public String sMsgId = "";

    public String sMsg = "";

    public String sFeedId = "";

    public boolean bVerify = true;

    public int eFeedType = 0;

    public long getIAccountId()
    {
        return iAccountId;
    }

    public void  setIAccountId(long iAccountId)
    {
        this.iAccountId = iAccountId;
    }

    public long getIFromAccountId()
    {
        return iFromAccountId;
    }

    public void  setIFromAccountId(long iFromAccountId)
    {
        this.iFromAccountId = iFromAccountId;
    }

    public String getSFromUserIcon()
    {
        return sFromUserIcon;
    }

    public void  setSFromUserIcon(String sFromUserIcon)
    {
        this.sFromUserIcon = sFromUserIcon;
    }

    public String getSFromUserName()
    {
        return sFromUserName;
    }

    public void  setSFromUserName(String sFromUserName)
    {
        this.sFromUserName = sFromUserName;
    }

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

    public int getEFeedType()
    {
        return eFeedType;
    }

    public void  setEFeedType(int eFeedType)
    {
        this.eFeedType = eFeedType;
    }

    public AlertUserInteractionReq()
    {
    }

    public AlertUserInteractionReq(long iAccountId, long iFromAccountId, String sFromUserIcon, String sFromUserName, String sMsgId, String sMsg, String sFeedId, boolean bVerify, int eFeedType)
    {
        this.iAccountId = iAccountId;
        this.iFromAccountId = iFromAccountId;
        this.sFromUserIcon = sFromUserIcon;
        this.sFromUserName = sFromUserName;
        this.sMsgId = sMsgId;
        this.sMsg = sMsg;
        this.sFeedId = sFeedId;
        this.bVerify = bVerify;
        this.eFeedType = eFeedType;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeUInt32(0, iAccountId);
        ostream.writeUInt32(1, iFromAccountId);
        if (null != sFromUserIcon) {
            ostream.writeString(2, sFromUserIcon);
        }
        if (null != sFromUserName) {
            ostream.writeString(3, sFromUserName);
        }
        if (null != sMsgId) {
            ostream.writeString(4, sMsgId);
        }
        if (null != sMsg) {
            ostream.writeString(5, sMsg);
        }
        if (null != sFeedId) {
            ostream.writeString(7, sFeedId);
        }
        ostream.writeBoolean(8, bVerify);
        ostream.writeInt32(9, eFeedType);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iAccountId = (long)istream.readUInt32(0, false, this.iAccountId);
        this.iFromAccountId = (long)istream.readUInt32(1, false, this.iFromAccountId);
        this.sFromUserIcon = (String)istream.readString(2, false, this.sFromUserIcon);
        this.sFromUserName = (String)istream.readString(3, false, this.sFromUserName);
        this.sMsgId = (String)istream.readString(4, false, this.sMsgId);
        this.sMsg = (String)istream.readString(5, false, this.sMsg);
        this.sFeedId = (String)istream.readString(7, false, this.sFeedId);
        this.bVerify = (boolean)istream.readBoolean(8, false, this.bVerify);
        this.eFeedType = (int)istream.readInt32(9, false, this.eFeedType);
    }

}

