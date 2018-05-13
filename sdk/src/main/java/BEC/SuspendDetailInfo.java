package BEC;

public final class SuspendDetailInfo extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sDtSecCode = "";

    public String sSecName = "";

    public long lSuspendTime = 0;

    public long lResumeTime = 0;

    public String sDuration = "";

    public String sReason = "";

    public String sProgress = "";

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

    public long getLSuspendTime()
    {
        return lSuspendTime;
    }

    public void  setLSuspendTime(long lSuspendTime)
    {
        this.lSuspendTime = lSuspendTime;
    }

    public long getLResumeTime()
    {
        return lResumeTime;
    }

    public void  setLResumeTime(long lResumeTime)
    {
        this.lResumeTime = lResumeTime;
    }

    public String getSDuration()
    {
        return sDuration;
    }

    public void  setSDuration(String sDuration)
    {
        this.sDuration = sDuration;
    }

    public String getSReason()
    {
        return sReason;
    }

    public void  setSReason(String sReason)
    {
        this.sReason = sReason;
    }

    public String getSProgress()
    {
        return sProgress;
    }

    public void  setSProgress(String sProgress)
    {
        this.sProgress = sProgress;
    }

    public SuspendDetailInfo()
    {
    }

    public SuspendDetailInfo(String sDtSecCode, String sSecName, long lSuspendTime, long lResumeTime, String sDuration, String sReason, String sProgress)
    {
        this.sDtSecCode = sDtSecCode;
        this.sSecName = sSecName;
        this.lSuspendTime = lSuspendTime;
        this.lResumeTime = lResumeTime;
        this.sDuration = sDuration;
        this.sReason = sReason;
        this.sProgress = sProgress;
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
        ostream.writeInt64(2, lSuspendTime);
        ostream.writeInt64(3, lResumeTime);
        if (null != sDuration) {
            ostream.writeString(4, sDuration);
        }
        if (null != sReason) {
            ostream.writeString(5, sReason);
        }
        if (null != sProgress) {
            ostream.writeString(6, sProgress);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sDtSecCode = (String)istream.readString(0, false, this.sDtSecCode);
        this.sSecName = (String)istream.readString(1, false, this.sSecName);
        this.lSuspendTime = (long)istream.readInt64(2, false, this.lSuspendTime);
        this.lResumeTime = (long)istream.readInt64(3, false, this.lResumeTime);
        this.sDuration = (String)istream.readString(4, false, this.sDuration);
        this.sReason = (String)istream.readString(5, false, this.sReason);
        this.sProgress = (String)istream.readString(6, false, this.sProgress);
    }

}

