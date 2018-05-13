package BEC;

public final class AccuPointTaskItem extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iType = 0;

    public String sName = "";

    public String sDesc = "";

    public String sSkipUrl = "";

    public int iGetPoints = 0;

    public int iTimesLimit = 0;

    public String sIcon = "";

    public int getIType()
    {
        return iType;
    }

    public void  setIType(int iType)
    {
        this.iType = iType;
    }

    public String getSName()
    {
        return sName;
    }

    public void  setSName(String sName)
    {
        this.sName = sName;
    }

    public String getSDesc()
    {
        return sDesc;
    }

    public void  setSDesc(String sDesc)
    {
        this.sDesc = sDesc;
    }

    public String getSSkipUrl()
    {
        return sSkipUrl;
    }

    public void  setSSkipUrl(String sSkipUrl)
    {
        this.sSkipUrl = sSkipUrl;
    }

    public int getIGetPoints()
    {
        return iGetPoints;
    }

    public void  setIGetPoints(int iGetPoints)
    {
        this.iGetPoints = iGetPoints;
    }

    public int getITimesLimit()
    {
        return iTimesLimit;
    }

    public void  setITimesLimit(int iTimesLimit)
    {
        this.iTimesLimit = iTimesLimit;
    }

    public String getSIcon()
    {
        return sIcon;
    }

    public void  setSIcon(String sIcon)
    {
        this.sIcon = sIcon;
    }

    public AccuPointTaskItem()
    {
    }

    public AccuPointTaskItem(int iType, String sName, String sDesc, String sSkipUrl, int iGetPoints, int iTimesLimit, String sIcon)
    {
        this.iType = iType;
        this.sName = sName;
        this.sDesc = sDesc;
        this.sSkipUrl = sSkipUrl;
        this.iGetPoints = iGetPoints;
        this.iTimesLimit = iTimesLimit;
        this.sIcon = sIcon;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iType);
        if (null != sName) {
            ostream.writeString(1, sName);
        }
        if (null != sDesc) {
            ostream.writeString(2, sDesc);
        }
        if (null != sSkipUrl) {
            ostream.writeString(3, sSkipUrl);
        }
        ostream.writeInt32(4, iGetPoints);
        ostream.writeInt32(5, iTimesLimit);
        if (null != sIcon) {
            ostream.writeString(6, sIcon);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iType = (int)istream.readInt32(0, false, this.iType);
        this.sName = (String)istream.readString(1, false, this.sName);
        this.sDesc = (String)istream.readString(2, false, this.sDesc);
        this.sSkipUrl = (String)istream.readString(3, false, this.sSkipUrl);
        this.iGetPoints = (int)istream.readInt32(4, false, this.iGetPoints);
        this.iTimesLimit = (int)istream.readInt32(5, false, this.iTimesLimit);
        this.sIcon = (String)istream.readString(6, false, this.sIcon);
    }

}

