package BEC;

public final class AlertMsgClassDesc extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iClassID = 0;

    public String sClassName = "";

    public String sAndroidIconUrl = "";

    public String sIosIconUrl = "";

    public String sMsg = "";

    public int iPushTime = 0;

    public int iNewsPushCount = 0;

    public int getIClassID()
    {
        return iClassID;
    }

    public void  setIClassID(int iClassID)
    {
        this.iClassID = iClassID;
    }

    public String getSClassName()
    {
        return sClassName;
    }

    public void  setSClassName(String sClassName)
    {
        this.sClassName = sClassName;
    }

    public String getSAndroidIconUrl()
    {
        return sAndroidIconUrl;
    }

    public void  setSAndroidIconUrl(String sAndroidIconUrl)
    {
        this.sAndroidIconUrl = sAndroidIconUrl;
    }

    public String getSIosIconUrl()
    {
        return sIosIconUrl;
    }

    public void  setSIosIconUrl(String sIosIconUrl)
    {
        this.sIosIconUrl = sIosIconUrl;
    }

    public String getSMsg()
    {
        return sMsg;
    }

    public void  setSMsg(String sMsg)
    {
        this.sMsg = sMsg;
    }

    public int getIPushTime()
    {
        return iPushTime;
    }

    public void  setIPushTime(int iPushTime)
    {
        this.iPushTime = iPushTime;
    }

    public int getINewsPushCount()
    {
        return iNewsPushCount;
    }

    public void  setINewsPushCount(int iNewsPushCount)
    {
        this.iNewsPushCount = iNewsPushCount;
    }

    public AlertMsgClassDesc()
    {
    }

    public AlertMsgClassDesc(int iClassID, String sClassName, String sAndroidIconUrl, String sIosIconUrl, String sMsg, int iPushTime, int iNewsPushCount)
    {
        this.iClassID = iClassID;
        this.sClassName = sClassName;
        this.sAndroidIconUrl = sAndroidIconUrl;
        this.sIosIconUrl = sIosIconUrl;
        this.sMsg = sMsg;
        this.iPushTime = iPushTime;
        this.iNewsPushCount = iNewsPushCount;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iClassID);
        if (null != sClassName) {
            ostream.writeString(1, sClassName);
        }
        if (null != sAndroidIconUrl) {
            ostream.writeString(2, sAndroidIconUrl);
        }
        if (null != sIosIconUrl) {
            ostream.writeString(3, sIosIconUrl);
        }
        if (null != sMsg) {
            ostream.writeString(4, sMsg);
        }
        ostream.writeInt32(5, iPushTime);
        ostream.writeInt32(6, iNewsPushCount);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iClassID = (int)istream.readInt32(0, false, this.iClassID);
        this.sClassName = (String)istream.readString(1, false, this.sClassName);
        this.sAndroidIconUrl = (String)istream.readString(2, false, this.sAndroidIconUrl);
        this.sIosIconUrl = (String)istream.readString(3, false, this.sIosIconUrl);
        this.sMsg = (String)istream.readString(4, false, this.sMsg);
        this.iPushTime = (int)istream.readInt32(5, false, this.iPushTime);
        this.iNewsPushCount = (int)istream.readInt32(6, false, this.iNewsPushCount);
    }

}

