package BEC;

public final class TickDesc extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iMinute = 0;

    public float fNow = 0;

    public long lNowVolume = 0;

    public int iInOut = 0;

    public int iTradeIndex = 0;

    public int iTime = 0;

    public int getIMinute()
    {
        return iMinute;
    }

    public void  setIMinute(int iMinute)
    {
        this.iMinute = iMinute;
    }

    public float getFNow()
    {
        return fNow;
    }

    public void  setFNow(float fNow)
    {
        this.fNow = fNow;
    }

    public long getLNowVolume()
    {
        return lNowVolume;
    }

    public void  setLNowVolume(long lNowVolume)
    {
        this.lNowVolume = lNowVolume;
    }

    public int getIInOut()
    {
        return iInOut;
    }

    public void  setIInOut(int iInOut)
    {
        this.iInOut = iInOut;
    }

    public int getITradeIndex()
    {
        return iTradeIndex;
    }

    public void  setITradeIndex(int iTradeIndex)
    {
        this.iTradeIndex = iTradeIndex;
    }

    public int getITime()
    {
        return iTime;
    }

    public void  setITime(int iTime)
    {
        this.iTime = iTime;
    }

    public TickDesc()
    {
    }

    public TickDesc(int iMinute, float fNow, long lNowVolume, int iInOut, int iTradeIndex, int iTime)
    {
        this.iMinute = iMinute;
        this.fNow = fNow;
        this.lNowVolume = lNowVolume;
        this.iInOut = iInOut;
        this.iTradeIndex = iTradeIndex;
        this.iTime = iTime;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iMinute);
        ostream.writeFloat(1, fNow);
        ostream.writeInt64(2, lNowVolume);
        ostream.writeInt32(3, iInOut);
        ostream.writeInt32(4, iTradeIndex);
        ostream.writeInt32(5, iTime);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iMinute = (int)istream.readInt32(0, false, this.iMinute);
        this.fNow = (float)istream.readFloat(1, false, this.fNow);
        this.lNowVolume = (long)istream.readInt64(2, false, this.lNowVolume);
        this.iInOut = (int)istream.readInt32(3, false, this.iInOut);
        this.iTradeIndex = (int)istream.readInt32(4, false, this.iTradeIndex);
        this.iTime = (int)istream.readInt32(5, false, this.iTime);
    }

}

