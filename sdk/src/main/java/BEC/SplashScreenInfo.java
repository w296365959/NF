package BEC;

public final class SplashScreenInfo extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sUrl = "";

    public int iEffectiveStartTime = 0;

    public int iEffectiveEndTime = 0;

    public int iAttr = 0;

    public int iPriority = 0;

    public String sID = "";

    public String sSkipUrl = "";

    public int iSkip = 0;

    public int iStaySecond = 0;

    public String getSUrl()
    {
        return sUrl;
    }

    public void  setSUrl(String sUrl)
    {
        this.sUrl = sUrl;
    }

    public int getIEffectiveStartTime()
    {
        return iEffectiveStartTime;
    }

    public void  setIEffectiveStartTime(int iEffectiveStartTime)
    {
        this.iEffectiveStartTime = iEffectiveStartTime;
    }

    public int getIEffectiveEndTime()
    {
        return iEffectiveEndTime;
    }

    public void  setIEffectiveEndTime(int iEffectiveEndTime)
    {
        this.iEffectiveEndTime = iEffectiveEndTime;
    }

    public int getIAttr()
    {
        return iAttr;
    }

    public void  setIAttr(int iAttr)
    {
        this.iAttr = iAttr;
    }

    public int getIPriority()
    {
        return iPriority;
    }

    public void  setIPriority(int iPriority)
    {
        this.iPriority = iPriority;
    }

    public String getSID()
    {
        return sID;
    }

    public void  setSID(String sID)
    {
        this.sID = sID;
    }

    public String getSSkipUrl()
    {
        return sSkipUrl;
    }

    public void  setSSkipUrl(String sSkipUrl)
    {
        this.sSkipUrl = sSkipUrl;
    }

    public int getISkip()
    {
        return iSkip;
    }

    public void  setISkip(int iSkip)
    {
        this.iSkip = iSkip;
    }

    public int getIStaySecond()
    {
        return iStaySecond;
    }

    public void  setIStaySecond(int iStaySecond)
    {
        this.iStaySecond = iStaySecond;
    }

    public SplashScreenInfo()
    {
    }

    public SplashScreenInfo(String sUrl, int iEffectiveStartTime, int iEffectiveEndTime, int iAttr, int iPriority, String sID, String sSkipUrl, int iSkip, int iStaySecond)
    {
        this.sUrl = sUrl;
        this.iEffectiveStartTime = iEffectiveStartTime;
        this.iEffectiveEndTime = iEffectiveEndTime;
        this.iAttr = iAttr;
        this.iPriority = iPriority;
        this.sID = sID;
        this.sSkipUrl = sSkipUrl;
        this.iSkip = iSkip;
        this.iStaySecond = iStaySecond;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sUrl) {
            ostream.writeString(0, sUrl);
        }
        ostream.writeInt32(1, iEffectiveStartTime);
        ostream.writeInt32(2, iEffectiveEndTime);
        ostream.writeInt32(3, iAttr);
        ostream.writeInt32(4, iPriority);
        if (null != sID) {
            ostream.writeString(5, sID);
        }
        if (null != sSkipUrl) {
            ostream.writeString(6, sSkipUrl);
        }
        ostream.writeInt32(7, iSkip);
        ostream.writeInt32(8, iStaySecond);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sUrl = (String)istream.readString(0, false, this.sUrl);
        this.iEffectiveStartTime = (int)istream.readInt32(1, false, this.iEffectiveStartTime);
        this.iEffectiveEndTime = (int)istream.readInt32(2, false, this.iEffectiveEndTime);
        this.iAttr = (int)istream.readInt32(3, false, this.iAttr);
        this.iPriority = (int)istream.readInt32(4, false, this.iPriority);
        this.sID = (String)istream.readString(5, false, this.sID);
        this.sSkipUrl = (String)istream.readString(6, false, this.sSkipUrl);
        this.iSkip = (int)istream.readInt32(7, false, this.iSkip);
        this.iStaySecond = (int)istream.readInt32(8, false, this.iStaySecond);
    }

}

