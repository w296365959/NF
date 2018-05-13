package BEC;

public final class KLineDesc extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public long lYmd = 0;

    public long lMinute = 0;

    public float fOpen = 0;

    public float fHigh = 0;

    public float fLow = 0;

    public float fClose = 0;

    public long lAmout = 0;

    public long lVolume = 0;

    public long lUp = 0;

    public long lDown = 0;

    public float fZjs = 0;

    public long lTotalvol = 0;

    public long fTotalAmount = 0;

    public long getLYmd()
    {
        return lYmd;
    }

    public void  setLYmd(long lYmd)
    {
        this.lYmd = lYmd;
    }

    public long getLMinute()
    {
        return lMinute;
    }

    public void  setLMinute(long lMinute)
    {
        this.lMinute = lMinute;
    }

    public float getFOpen()
    {
        return fOpen;
    }

    public void  setFOpen(float fOpen)
    {
        this.fOpen = fOpen;
    }

    public float getFHigh()
    {
        return fHigh;
    }

    public void  setFHigh(float fHigh)
    {
        this.fHigh = fHigh;
    }

    public float getFLow()
    {
        return fLow;
    }

    public void  setFLow(float fLow)
    {
        this.fLow = fLow;
    }

    public float getFClose()
    {
        return fClose;
    }

    public void  setFClose(float fClose)
    {
        this.fClose = fClose;
    }

    public long getLAmout()
    {
        return lAmout;
    }

    public void  setLAmout(long lAmout)
    {
        this.lAmout = lAmout;
    }

    public long getLVolume()
    {
        return lVolume;
    }

    public void  setLVolume(long lVolume)
    {
        this.lVolume = lVolume;
    }

    public long getLUp()
    {
        return lUp;
    }

    public void  setLUp(long lUp)
    {
        this.lUp = lUp;
    }

    public long getLDown()
    {
        return lDown;
    }

    public void  setLDown(long lDown)
    {
        this.lDown = lDown;
    }

    public float getFZjs()
    {
        return fZjs;
    }

    public void  setFZjs(float fZjs)
    {
        this.fZjs = fZjs;
    }

    public long getLTotalvol()
    {
        return lTotalvol;
    }

    public void  setLTotalvol(long lTotalvol)
    {
        this.lTotalvol = lTotalvol;
    }

    public long getFTotalAmount()
    {
        return fTotalAmount;
    }

    public void  setFTotalAmount(long fTotalAmount)
    {
        this.fTotalAmount = fTotalAmount;
    }

    public KLineDesc()
    {
    }

    public KLineDesc(long lYmd, long lMinute, float fOpen, float fHigh, float fLow, float fClose, long lAmout, long lVolume, long lUp, long lDown, float fZjs, long lTotalvol, long fTotalAmount)
    {
        this.lYmd = lYmd;
        this.lMinute = lMinute;
        this.fOpen = fOpen;
        this.fHigh = fHigh;
        this.fLow = fLow;
        this.fClose = fClose;
        this.lAmout = lAmout;
        this.lVolume = lVolume;
        this.lUp = lUp;
        this.lDown = lDown;
        this.fZjs = fZjs;
        this.lTotalvol = lTotalvol;
        this.fTotalAmount = fTotalAmount;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt64(0, lYmd);
        ostream.writeInt64(1, lMinute);
        ostream.writeFloat(2, fOpen);
        ostream.writeFloat(3, fHigh);
        ostream.writeFloat(4, fLow);
        ostream.writeFloat(5, fClose);
        ostream.writeInt64(6, lAmout);
        ostream.writeInt64(7, lVolume);
        ostream.writeInt64(8, lUp);
        ostream.writeInt64(9, lDown);
        ostream.writeFloat(10, fZjs);
        ostream.writeInt64(11, lTotalvol);
        ostream.writeInt64(12, fTotalAmount);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.lYmd = (long)istream.readInt64(0, false, this.lYmd);
        this.lMinute = (long)istream.readInt64(1, false, this.lMinute);
        this.fOpen = (float)istream.readFloat(2, false, this.fOpen);
        this.fHigh = (float)istream.readFloat(3, false, this.fHigh);
        this.fLow = (float)istream.readFloat(4, false, this.fLow);
        this.fClose = (float)istream.readFloat(5, false, this.fClose);
        this.lAmout = (long)istream.readInt64(6, false, this.lAmout);
        this.lVolume = (long)istream.readInt64(7, false, this.lVolume);
        this.lUp = (long)istream.readInt64(8, false, this.lUp);
        this.lDown = (long)istream.readInt64(9, false, this.lDown);
        this.fZjs = (float)istream.readFloat(10, false, this.fZjs);
        this.lTotalvol = (long)istream.readInt64(11, false, this.lTotalvol);
        this.fTotalAmount = (long)istream.readInt64(12, false, this.fTotalAmount);
    }

}

