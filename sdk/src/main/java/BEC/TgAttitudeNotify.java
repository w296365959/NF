package BEC;

public final class TgAttitudeNotify extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sFeedId = "";

    public long iAccountId = 0;

    public String sUserName = "";

    public String sIcon = "";

    public String sNotifyMsg = "";

    public boolean bVerify = true;

    public int iFeedType = 0;

    public String getSFeedId()
    {
        return sFeedId;
    }

    public void  setSFeedId(String sFeedId)
    {
        this.sFeedId = sFeedId;
    }

    public long getIAccountId()
    {
        return iAccountId;
    }

    public void  setIAccountId(long iAccountId)
    {
        this.iAccountId = iAccountId;
    }

    public String getSUserName()
    {
        return sUserName;
    }

    public void  setSUserName(String sUserName)
    {
        this.sUserName = sUserName;
    }

    public String getSIcon()
    {
        return sIcon;
    }

    public void  setSIcon(String sIcon)
    {
        this.sIcon = sIcon;
    }

    public String getSNotifyMsg()
    {
        return sNotifyMsg;
    }

    public void  setSNotifyMsg(String sNotifyMsg)
    {
        this.sNotifyMsg = sNotifyMsg;
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

    public TgAttitudeNotify()
    {
    }

    public TgAttitudeNotify(String sFeedId, long iAccountId, String sUserName, String sIcon, String sNotifyMsg, boolean bVerify, int iFeedType)
    {
        this.sFeedId = sFeedId;
        this.iAccountId = iAccountId;
        this.sUserName = sUserName;
        this.sIcon = sIcon;
        this.sNotifyMsg = sNotifyMsg;
        this.bVerify = bVerify;
        this.iFeedType = iFeedType;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sFeedId) {
            ostream.writeString(0, sFeedId);
        }
        ostream.writeUInt32(1, iAccountId);
        if (null != sUserName) {
            ostream.writeString(2, sUserName);
        }
        if (null != sIcon) {
            ostream.writeString(3, sIcon);
        }
        if (null != sNotifyMsg) {
            ostream.writeString(4, sNotifyMsg);
        }
        ostream.writeBoolean(5, bVerify);
        ostream.writeInt32(6, iFeedType);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sFeedId = (String)istream.readString(0, false, this.sFeedId);
        this.iAccountId = (long)istream.readUInt32(1, false, this.iAccountId);
        this.sUserName = (String)istream.readString(2, false, this.sUserName);
        this.sIcon = (String)istream.readString(3, false, this.sIcon);
        this.sNotifyMsg = (String)istream.readString(4, false, this.sNotifyMsg);
        this.bVerify = (boolean)istream.readBoolean(5, false, this.bVerify);
        this.iFeedType = (int)istream.readInt32(6, false, this.iFeedType);
    }

}

