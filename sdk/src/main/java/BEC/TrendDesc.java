package BEC;

public final class TrendDesc extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iMinute = 0;

    public float fNow = 0;

    public float fAverage = 0;

    public long lNowvol = 0;

    public long lBuyvol = 0;

    public long lSellvol = 0;

    public float fLead = 0;

    public float fAmount = 0;

    public long lTotalvol = 0;

    public long fTotalAmount = 0;

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

    public float getFAverage()
    {
        return fAverage;
    }

    public void  setFAverage(float fAverage)
    {
        this.fAverage = fAverage;
    }

    public long getLNowvol()
    {
        return lNowvol;
    }

    public void  setLNowvol(long lNowvol)
    {
        this.lNowvol = lNowvol;
    }

    public long getLBuyvol()
    {
        return lBuyvol;
    }

    public void  setLBuyvol(long lBuyvol)
    {
        this.lBuyvol = lBuyvol;
    }

    public long getLSellvol()
    {
        return lSellvol;
    }

    public void  setLSellvol(long lSellvol)
    {
        this.lSellvol = lSellvol;
    }

    public float getFLead()
    {
        return fLead;
    }

    public void  setFLead(float fLead)
    {
        this.fLead = fLead;
    }

    public float getFAmount()
    {
        return fAmount;
    }

    public void  setFAmount(float fAmount)
    {
        this.fAmount = fAmount;
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

    public TrendDesc()
    {
    }

    public TrendDesc(int iMinute, float fNow, float fAverage, long lNowvol, long lBuyvol, long lSellvol, float fLead, float fAmount, long lTotalvol, long fTotalAmount)
    {
        this.iMinute = iMinute;
        this.fNow = fNow;
        this.fAverage = fAverage;
        this.lNowvol = lNowvol;
        this.lBuyvol = lBuyvol;
        this.lSellvol = lSellvol;
        this.fLead = fLead;
        this.fAmount = fAmount;
        this.lTotalvol = lTotalvol;
        this.fTotalAmount = fTotalAmount;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iMinute);
        ostream.writeFloat(1, fNow);
        ostream.writeFloat(2, fAverage);
        ostream.writeInt64(3, lNowvol);
        ostream.writeInt64(4, lBuyvol);
        ostream.writeInt64(5, lSellvol);
        ostream.writeFloat(6, fLead);
        ostream.writeFloat(7, fAmount);
        ostream.writeInt64(8, lTotalvol);
        ostream.writeInt64(9, fTotalAmount);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iMinute = (int)istream.readInt32(0, false, this.iMinute);
        this.fNow = (float)istream.readFloat(1, false, this.fNow);
        this.fAverage = (float)istream.readFloat(2, false, this.fAverage);
        this.lNowvol = (long)istream.readInt64(3, false, this.lNowvol);
        this.lBuyvol = (long)istream.readInt64(4, false, this.lBuyvol);
        this.lSellvol = (long)istream.readInt64(5, false, this.lSellvol);
        this.fLead = (float)istream.readFloat(6, false, this.fLead);
        this.fAmount = (float)istream.readFloat(7, false, this.fAmount);
        this.lTotalvol = (long)istream.readInt64(8, false, this.lTotalvol);
        this.fTotalAmount = (long)istream.readInt64(9, false, this.fTotalAmount);
    }

}

